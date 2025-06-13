package com.refresh.refresh.service;

import com.refresh.refresh.entity.RouteBoard;
import com.refresh.refresh.repository.RouteBoardRepository;
import com.refresh.refresh.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class RouteBoardService {
    
    @Autowired
    private RouteBoardRepository routeBoardRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    /**
     * 모든 경로 게시판 정보를 가져오는 메서드
     * @return List<RouteBoard>
     */
    public List<RouteBoard> getAllRouteBoards() {
        return routeBoardRepository.findAll();
    }

    /**
     * 특정 ID의 경로 게시판 정보를 가져오는 메서드
     * @param id 게시판 ID
     * @return RouteBoard
     */
    public RouteBoard getRouteBoardById(int id) {
        return routeBoardRepository.findById(id).orElseThrow(() -> new RuntimeException("RouteBoard not found"));
    }

    /**
     * 새로운 경로 게시판 정보를 생성하는 메서드
     * @param routeBoard 게시판 엔티티
     * @return RouteBoard
     */
    public RouteBoard createPost(RouteBoard routeBoard) {
        routeBoard.setCreatedAt(LocalDateTime.now());
        routeBoard.setViewCount(0);
        routeBoard.setLikeCount(0);
        return routeBoardRepository.save(routeBoard);
    }
    
    /**
     * 기존 경로 게시판 정보를 업데이트하는 메서드
     * @param routeBoardId 게시판 ID
     * @param routeBoard 업데이트할 게시판 엔티티
     * @return RouteBoard
     */
    public RouteBoard updatePost(Integer routeBoardId, RouteBoard routeBoard) {
        RouteBoard existingPost = routeBoardRepository.findById(routeBoardId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        existingPost.setTitle(routeBoard.getTitle());
        existingPost.setContent(routeBoard.getContent());
        existingPost.setCategoryId(routeBoard.getCategoryId());
        
        return routeBoardRepository.save(existingPost);
    }
    
    /**
     * 특정 ID의 경로 게시판 정보를 삭제하는 메서드
     * @param id 게시판 ID
     */
    public void deletePost(Integer routeBoardId) {
        if (!routeBoardRepository.existsById(routeBoardId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        routeBoardRepository.deleteById(routeBoardId);
    }
    
    @Transactional
    public Map<String, Object> getPostDetail(Integer routeBoardId) {
        RouteBoard post = routeBoardRepository.findById(routeBoardId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        routeBoardRepository.incrementViewCount(routeBoardId);
        
        long commentCount = commentRepository.countByRouteBoardId(routeBoardId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("post", post);
        result.put("commentCount", commentCount);
        
        return result;
    }
    
    public Page<RouteBoard> getAllPosts(Pageable pageable) {
        return routeBoardRepository.findAll(pageable);
    }
    
    public Page<RouteBoard> getPostsByStatus(Integer boardStatus, Pageable pageable) {
        return routeBoardRepository.findByBoardStatus(boardStatus, pageable);
    }
    
    public Page<RouteBoard> getPostsByCategory(Integer categoryId, Pageable pageable) {
        return routeBoardRepository.findByCategoryId(categoryId, pageable);
    }
    
    public Page<RouteBoard> searchByTitle(String keyword, Pageable pageable) {
        return routeBoardRepository.findByTitleContaining(keyword, pageable);
    }
    
    public Page<RouteBoard> searchByContent(String keyword, Pageable pageable) {
        return routeBoardRepository.findByContentContaining(keyword, pageable);
    }
    
    public Page<RouteBoard> searchByTitleOrContent(String keyword, Pageable pageable) {
        return routeBoardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }
    
    public Page<RouteBoard> searchByAuthor(String nickname, Pageable pageable) {
        return routeBoardRepository.findByAuthorNickname(nickname, pageable);
    }
    
    public Page<RouteBoard> searchByComment(String keyword, Pageable pageable) {
        return routeBoardRepository.findByCommentContent(keyword, pageable);
    }
    
    public Page<RouteBoard> searchAllFields(String keyword, Pageable pageable) {
        return routeBoardRepository.findByAllFields(keyword, pageable);
    }
}
