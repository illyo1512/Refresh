package com.refresh.refresh.repository;

import com.refresh.refresh.entity.RouteBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 경로 게시판 데이터 접근을 위한 Repository 인터페이스
 */
@Repository
public interface RouteBoardRepository extends JpaRepository<RouteBoard, Integer> {
    
    /**
     * 게시글 ID로 상세 조회 (JpaRepository에서 기본 제공)
     * @param routeBoardId 게시글 ID
     * @return 게시글 정보 (Optional로 감싸져 있음)
     */
    // Optional<RouteBoard> findById(Integer routeBoardId); // 기본 제공되므로 생략 가능
    
    /**
     * 게시글 조회 시 조회수 증가
     * @param routeBoardId 게시글 ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE RouteBoard r SET r.viewCount = r.viewCount + 1 WHERE r.routeBoardId = :routeBoardId")
    void incrementViewCount(@Param("routeBoardId") Integer routeBoardId);
    
    /**
     * 게시글 상세 조회 (작성자 정보 포함)
     * @param routeBoardId 게시글 ID
     * @return 게시글 정보와 작성자 정보
     */
    @Query("SELECT r FROM RouteBoard r WHERE r.routeBoardId = :routeBoardId")
    Optional<RouteBoard> findByIdWithDetails(@Param("routeBoardId") Integer routeBoardId);

    /**
     * 특정 게시글 상태의 게시글들을 동적 정렬로 조회합니다.
     * @param boardStatus 게시글 상태 (0: 일반, 1: 공지, 2: 인기글)
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 해당 상태의 게시글 목록
     */
    Page<RouteBoard> findByBoardStatus(Integer boardStatus, Pageable pageable);
    
    // ================================
    // 카테고리별 게시글 조회 기능
    // ================================
    
    /**
     * 특정 카테고리의 게시글들을 동적 정렬로 조회합니다.
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 해당 카테고리의 게시글 목록
     */
    Page<RouteBoard> findByCategoryId(Integer categoryId, Pageable pageable);
    
    /**
     * 특정 카테고리와 게시글 상태 조건으로 게시글들을 조회합니다.
     * @param categoryId 카테고리 ID
     * @param boardStatus 게시글 상태
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 조건에 맞는 게시글 목록
     */
    Page<RouteBoard> findByCategoryIdAndBoardStatus(Integer categoryId, Integer boardStatus, Pageable pageable);
    
    // ================================
    // 소량 데이터 조회용 편의 메소드 (페이징 없음)
    // 관리자 페이지나 특별한 경우에만 사용
    // ================================
    
    /**
     * 특정 게시글 상태의 게시글들을 최신순으로 조회합니다 (소량 데이터용).
     * @param boardStatus 게시글 상태
     * @return 해당 상태의 게시글 목록 (최신순, 전체)
     */
    List<RouteBoard> findByBoardStatusOrderByCreatedAtDesc(Integer boardStatus);
    
    /**
     * 특정 카테고리의 게시글들을 최신순으로 조회합니다 (소량 데이터용).
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 게시글 목록 (최신순, 전체)
     */
    List<RouteBoard> findByCategoryIdOrderByCreatedAtDesc(Integer categoryId);
    
    // ================================
    // 검색 기능
    // ================================
    
    /**
     * 제목에 특정 키워드가 포함된 게시글을 검색합니다.
     * @param title 검색할 제목 키워드
     * @param pageable 페이징 및 정렬 정보
     * @return 제목에 키워드가 포함된 게시글 목록
     */
    Page<RouteBoard> findByTitleContaining(String title, Pageable pageable);
    
    /**
     * 내용에 특정 키워드가 포함된 게시글을 검색합니다.
     * @param content 검색할 내용 키워드
     * @param pageable 페이징 및 정렬 정보
     * @return 내용에 키워드가 포함된 게시글 목록
     */
    Page<RouteBoard> findByContentContaining(String content, Pageable pageable);
    
    /**
     * 제목 또는 내용에 특정 키워드가 포함된 게시글을 검색합니다.
     * @param title 검색할 제목 키워드
     * @param content 검색할 내용 키워드
     * @param pageable 페이징 및 정렬 정보
     * @return 제목 또는 내용에 키워드가 포함된 게시글 목록
     */
    Page<RouteBoard> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    
    /**
     * 작성자 닉네임으로 게시글을 검색합니다.
     * @param nickname 검색할 작성자 닉네임
     * @param pageable 페이징 및 정렬 정보
     * @return 해당 작성자의 게시글 목록
     */
    @Query("SELECT r FROM RouteBoard r JOIN User u ON r.userId = u.userId WHERE u.nickname LIKE %:nickname%")
    Page<RouteBoard> findByAuthorNickname(@Param("nickname") String nickname, Pageable pageable);
    
    /**
     * 댓글 내용에 특정 키워드가 포함된 게시글을 검색합니다.
     * @param keyword 검색할 댓글 키워드
     * @param pageable 페이징 및 정렬 정보
     * @return 댓글에 키워드가 포함된 게시글 목록
     */
    @Query("SELECT DISTINCT r FROM RouteBoard r JOIN Comment c ON r.routeBoardId = c.routeBoardId WHERE c.content LIKE %:keyword%")
    Page<RouteBoard> findByCommentContent(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 전체 필드(제목, 내용, 작성자, 댓글)에서 키워드를 검색합니다.
     * @param keyword 검색할 키워드
     * @param pageable 페이징 및 정렬 정보
     * @return 모든 필드에서 키워드가 포함된 게시글 목록
     */
    @Query("SELECT DISTINCT r FROM RouteBoard r JOIN User u ON r.userId = u.userId LEFT JOIN Comment c ON r.routeBoardId = c.routeBoardId " +
           "WHERE r.title LIKE %:keyword% OR r.content LIKE %:keyword% OR u.nickname LIKE %:keyword% OR c.content LIKE %:keyword%")
    Page<RouteBoard> findByAllFields(@Param("keyword") String keyword, Pageable pageable);
}