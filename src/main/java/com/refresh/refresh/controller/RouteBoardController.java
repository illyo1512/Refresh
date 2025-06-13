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

@RestController
@RequestMapping("/api/boards")
public class RouteBoardController {
    
    @Autowired
    private RouteBoardService routeBoardService;
    
    @GetMapping
    public ResponseEntity<Page<RouteBoard>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(routeBoardService.getAllPosts(pageable));
    }
    
    @GetMapping("/{routeBoardId}")
    public ResponseEntity<Map<String, Object>> getPostDetail(@PathVariable Integer routeBoardId) {
        return ResponseEntity.ok(routeBoardService.getPostDetail(routeBoardId));
    }
    
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
            default:
                result = routeBoardService.searchAllFields(keyword, pageable);
                break;
        }
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    public ResponseEntity<RouteBoard> createPost(@RequestBody RouteBoard routeBoard) {
        RouteBoard createdPost = routeBoardService.createPost(routeBoard);
        return ResponseEntity.ok(createdPost);
    }
    
    @PutMapping("/{routeBoardId}")
    public ResponseEntity<RouteBoard> updatePost(
            @PathVariable Integer routeBoardId, 
            @RequestBody RouteBoard routeBoard) {
        RouteBoard updatedPost = routeBoardService.updatePost(routeBoardId, routeBoard);
        return ResponseEntity.ok(updatedPost);
    }
    
    @DeleteMapping("/{routeBoardId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer routeBoardId) {
        routeBoardService.deletePost(routeBoardId);
        return ResponseEntity.noContent().build();
    }
}
