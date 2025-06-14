package com.refresh.refresh.controller;

import com.refresh.refresh.entity.RouteBoard;
import com.refresh.refresh.service.RouteBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 경로 게시판 컨트롤러
 * 경로 게시글의 CRUD 작업 및 검색 기능을 제공합니다.
 * 
 * @author 개발팀
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/boards")
public class RouteBoardController {
    
    @Autowired
    private RouteBoardService routeBoardService;
    
    /**
     * 모든 게시글을 페이징하여 조회합니다.
     * 
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 필드 (기본값: createdAt)
     * @param sortDir 정렬 방향 (asc/desc, 기본값: desc)
     * @return 페이징된 게시글 목록
     * 
     * @example GET /api/boards?page=0&size=20&sortBy=boardTitle&sortDir=asc
     */
    @GetMapping
    public ResponseEntity<Page<RouteBoard>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        // 정렬 방향에 따라 Sort 객체 생성
        Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(routeBoardService.getAllPosts(pageable));
    }
    
    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * 
     * @param routeBoardId 조회할 게시글 ID
     * @return 게시글 상세 정보 (게시글, 댓글, 좋아요 수 등 포함)
     * 
     * @example GET /api/boards/1
     */
    @GetMapping("/{routeBoardId}")
    public ResponseEntity<Map<String, Object>> getPostDetail(@PathVariable Integer routeBoardId) {
        return ResponseEntity.ok(routeBoardService.getPostDetail(routeBoardId));
    }
    
    /**
     * 특정 상태의 게시글들을 페이징하여 조회합니다.
     * 
     * @param boardStatus 게시글 상태 (0: 비활성, 1: 활성, 2: 임시저장 등)
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 필드 (기본값: createdAt)
     * @param sortDir 정렬 방향 (기본값: desc)
     * @return 해당 상태의 페이징된 게시글 목록
     * 
     * @example GET /api/boards/status/1?page=0&size=10
     */
    @GetMapping("/status/{boardStatus}")
    public ResponseEntity<Page<RouteBoard>> getPostsByStatus(
            @PathVariable Integer boardStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(routeBoardService.getPostsByStatus(boardStatus, pageable));
    }
    
    /**
     * 특정 카테고리의 게시글들을 페이징하여 조회합니다.
     * 
     * @param categoryId 카테고리 ID (1: 서울, 2: 부산, 8: 경기도 등)
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 필드 (기본값: createdAt)
     * @param sortDir 정렬 방향 (기본값: desc)
     * @return 해당 카테고리의 페이징된 게시글 목록
     * 
     * @example GET /api/boards/category/1 (서울 지역 게시글)
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<RouteBoard>> getPostsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(routeBoardService.getPostsByCategory(categoryId, pageable));
    }
    
    /**
     * 키워드와 검색 타입에 따라 게시글을 검색합니다.
     * 
     * @param keyword 검색할 키워드
     * @param searchType 검색 타입:
     *                   - title: 제목에서 검색
     *                   - content: 내용에서 검색
     *                   - titleOrContent: 제목 또는 내용에서 검색
     *                   - author: 작성자에서 검색
     *                   - comment: 댓글에서 검색
     *                   - all: 모든 필드에서 검색 (기본값)
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 필드 (기본값: createdAt)
     * @param sortDir 정렬 방향 (기본값: desc)
     * @return 검색된 페이징된 게시글 목록
     * 
     * @example GET /api/boards/search?keyword=부산&searchType=title
     * @example GET /api/boards/search?keyword=여행&searchType=all
     */
    @GetMapping("/search")
    public ResponseEntity<Page<RouteBoard>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "all") String searchType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RouteBoard> result;
        
        // 검색 타입에 따른 분기 처리
        switch (searchType) {
            case "title":
                result = routeBoardService.searchByTitle(keyword, pageable);
                break;
            case "content":
                result = routeBoardService.searchByContent(keyword, pageable);
                break;
            case "titleOrContent":
                result = routeBoardService.searchByTitleOrContent(keyword, pageable);
                break;
            case "author":
                result = routeBoardService.searchByAuthor(keyword, pageable);
                break;
            case "comment":
                result = routeBoardService.searchByComment(keyword, pageable);
                break;
            default: // "all" 또는 기타 값
                result = routeBoardService.searchAllFields(keyword, pageable);
                break;
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 새로운 게시글을 작성합니다.
     * 
     * @param routeBoard 작성할 게시글 정보
     *                   - boardTitle: 게시글 제목 (필수)
     *                   - boardContent: 게시글 내용 (필수)
     *                   - categoryId: 카테고리 ID (필수)
     *                   - userId: 작성자 ID (필수)
     *                   - boardStatus: 게시글 상태 (선택, 기본값: 1)
     * @return 생성된 게시글 정보
     * 
     * @example POST /api/boards
     * Body: {
     *   "boardTitle": "부산 여행 코스 추천",
     *   "boardContent": "부산의 멋진 여행 코스를 소개합니다.",
     *   "categoryId": 2,
     *   "userId": 1
     * }
     */
    @PostMapping
    public ResponseEntity<RouteBoard> createPost(@RequestBody RouteBoard routeBoard) {
        RouteBoard createdPost = routeBoardService.createPost(routeBoard);
        return ResponseEntity.ok(createdPost);
    }
    
    /**
     * 기존 게시글을 수정합니다.
     * 
     * @param routeBoardId 수정할 게시글 ID
     * @param routeBoard 수정할 게시글 정보
     *                   - boardTitle: 수정할 제목
     *                   - boardContent: 수정할 내용
     *                   - categoryId: 수정할 카테고리 ID
     *                   - boardStatus: 수정할 상태
     * @return 수정된 게시글 정보
     * 
     * @example PUT /api/boards/1
     * Body: {
     *   "boardTitle": "수정된 제목",
     *   "boardContent": "수정된 내용"
     * }
     */
    @PutMapping("/{routeBoardId}")
    public ResponseEntity<RouteBoard> updatePost(
            @PathVariable Integer routeBoardId, 
            @RequestBody RouteBoard routeBoard) {
        RouteBoard updatedPost = routeBoardService.updatePost(routeBoardId, routeBoard);
        return ResponseEntity.ok(updatedPost);
    }
    
    /**
     * 게시글을 삭제합니다.
     * 
     * @param routeBoardId 삭제할 게시글 ID
     * @return 204 No Content (삭제 성공)
     * 
     * @example DELETE /api/boards/1
     */
    @DeleteMapping("/{routeBoardId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer routeBoardId) {
        routeBoardService.deletePost(routeBoardId);
        return ResponseEntity.noContent().build();
    }
}
