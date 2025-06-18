package com.refresh.refresh.repository;

import com.refresh.refresh.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    /**
     * 특정 게시글의 댓글 목록을 조회합니다.
     * @param routeBoardId 게시글 ID
     * @param pageable 페이징 정보
     * @return 해당 게시글의 댓글 목록
     */
    Page<Comment> findByRouteBoardId(Integer routeBoardId, Pageable pageable);
    
    /**
     * 특정 게시글의 댓글 개수를 조회합니다.
     * @param routeBoardId 게시글 ID
     * @return 댓글 개수
     */
    long countByRouteBoardId(Integer routeBoardId);
    
    /**
     * 특정 사용자가 작성한 댓글 목록을 조회합니다.
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 해당 사용자의 댓글 목록
     */
    Page<Comment> findByUserId(Integer userId, Pageable pageable);
    
    /**
     * 댓글 내용에 키워드가 포함된 댓글을 검색합니다.
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 키워드가 포함된 댓글 목록
     */
    Page<Comment> findByContentContaining(String keyword, Pageable pageable);
    
    /**
     * 특정 게시글의 댓글을 최신순으로 조회합니다.
     * @param routeBoardId 게시글 ID
     * @return 해당 게시글의 댓글 목록 (최신순)
     */
    List<Comment> findByRouteBoardIdOrderByCreatedAtDesc(Integer routeBoardId);
}