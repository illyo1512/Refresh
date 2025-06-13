package com.refresh.refresh.controller;

import com.refresh.refresh.entity.Comment;
import com.refresh.refresh.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    /**
     * 특정 ID의 댓글 정보를 가져오는 API
     * @param id 댓글 ID
     * @return Comment
     */
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable int id) {
        return commentService.getCommentById(id);
    }

    /**
     * 새로운 댓글 정보를 생성하는 API
     * @param comment 댓글
     * @return Comment
     */
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.ok(createdComment);
    }

    /**
     * 기존 댓글 정보를 업데이트하는 API
     * @param id 댓글 ID
     * @param content 댓글 내용
     * @return Comment
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Integer id,
            @RequestBody String content) {
        Comment updatedComment = commentService.updateComment(id, content);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 특정 ID의 댓글 정보를 삭제하는 API
     * @param id 댓글 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/board/{routeBoardId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Integer routeBoardId) {
        List<Comment> comments = commentService.getCommentsByPostId(routeBoardId);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/board/{routeBoardId}/paged")
    public ResponseEntity<Page<Comment>> getCommentsByPostIdPaged(
            @PathVariable Integer routeBoardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.getCommentsByPostId(routeBoardId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Comment>> getCommentsByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.getCommentsByUserId(userId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Comment>> searchComments(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.searchComments(keyword, pageable);
        return ResponseEntity.ok(comments);
    }
}
