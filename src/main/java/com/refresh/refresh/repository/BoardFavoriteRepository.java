package com.refresh.refresh.repository;

import com.refresh.refresh.entity.BoardFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardFavoriteRepository extends JpaRepository<BoardFavorite, Integer> {
    
    /**
     * 특정 사용자의 즐겨찾기 게시글 목록을 조회합니다.
     */
    Page<BoardFavorite> findByUserId(Integer userId, Pageable pageable);
    
    /**
     * 특정 사용자가 특정 게시글을 즐겨찾기했는지 확인합니다.
     */
    Optional<BoardFavorite> findByUserIdAndRouteBoardId(Integer userId, Integer routeBoardId);
    
    /**
     * 특정 사용자가 특정 게시글을 즐겨찾기했는지 여부를 반환합니다.
     */
    boolean existsByUserIdAndRouteBoardId(Integer userId, Integer routeBoardId);
    
    /**
     * 특정 게시글의 즐겨찾기 수를 조회합니다.
     */
    long countByRouteBoardId(Integer routeBoardId);
    
    /**
     * 특정 사용자의 즐겨찾기 게시글을 게시글 정보와 함께 조회합니다.
     */
    @Query("SELECT bf FROM BoardFavorite bf JOIN FETCH bf.routeBoard rb WHERE bf.userId = :userId ORDER BY bf.createdAt DESC")
    List<BoardFavorite> findByUserIdWithRouteBoard(@Param("userId") Integer userId);
    
    /**
     * 특정 사용자의 즐겨찾기 게시글을 게시글 정보와 함께 페이징 조회합니다.
     */
    @Query("SELECT bf FROM BoardFavorite bf JOIN FETCH bf.routeBoard rb WHERE bf.userId = :userId ORDER BY bf.createdAt DESC")
    Page<BoardFavorite> findByUserIdWithRouteBoard(@Param("userId") Integer userId, Pageable pageable);
}