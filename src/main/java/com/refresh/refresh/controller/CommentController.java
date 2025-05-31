package com.refresh.refresh.controller;

import com.refresh.refresh.dto.CommentDTO;
import com.refresh.refresh.entity.Comment;
import com.refresh.refresh.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 모든 댓글 정보를 가져오는 API
     * @return List<CommentDTO>
     */
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments().stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    commentDTO.setCommentId(comment.getCommentId());
                    commentDTO.setUserId(comment.getUserId());
                    commentDTO.setRouteBoardId(comment.getRouteBoardId());
                    commentDTO.setContent(comment.getContent());
                    commentDTO.setCreatedAt(comment.getCreatedAt());
                    return commentDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 댓글 정보를 가져오는 API
     * @param id 댓글 ID
     * @return CommentDTO
     */
    @GetMapping("/{id}")
    public CommentDTO getCommentById(@PathVariable int id) {
        Comment comment = commentService.getCommentById(id);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setRouteBoardId(comment.getRouteBoardId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        return commentDTO;
    }

    /**
     * 새로운 댓글 정보를 생성하는 API
     * @param commentDTO 댓글 DTO
     * @return CommentDTO
     */
    @PostMapping
    public CommentDTO createComment(@RequestBody CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setRouteBoardId(commentDTO.getRouteBoardId());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        Comment savedComment = commentService.createComment(comment);
        commentDTO.setCommentId(savedComment.getCommentId());
        return commentDTO;
    }

    /**
     * 기존 댓글 정보를 업데이트하는 API
     * @param id 댓글 ID
     * @param commentDTO 댓글 DTO
     * @return CommentDTO
     */
    @PutMapping("/{id}")
    public CommentDTO updateComment(@PathVariable int id, @RequestBody CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setCommentId(id);
        comment.setUserId(commentDTO.getUserId());
        comment.setRouteBoardId(commentDTO.getRouteBoardId());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        Comment updatedComment = commentService.updateComment(comment);
        commentDTO.setCommentId(updatedComment.getCommentId());
        return commentDTO;
    }

    /**
     * 특정 ID의 댓글 정보를 삭제하는 API
     * @param id 댓글 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
        return "Comment with ID " + id + " has been deleted.";
    }
}
