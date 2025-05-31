package com.refresh.refresh.service;

import com.refresh.refresh.entity.Comment;
import com.refresh.refresh.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 모든 댓글 정보를 가져오는 메서드
     * @return List<Comment>
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    /**
     * 특정 ID의 댓글 정보를 가져오는 메서드
     * @param id 댓글 ID
     * @return Comment
     */
    public Comment getCommentById(int id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    /**
     * 새로운 댓글 정보를 생성하는 메서드
     * @param comment 댓글 엔티티
     * @return Comment
     */
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * 기존 댓글 정보를 업데이트하는 메서드
     * @param comment 댓글 엔티티
     * @return Comment
     */
    public Comment updateComment(Comment comment) {
        if (!commentRepository.existsById(comment.getCommentId())) {
            throw new RuntimeException("Comment not found");
        }
        return commentRepository.save(comment);
    }

    /**
     * 특정 ID의 댓글 정보를 삭제하는 메서드
     * @param id 댓글 ID
     */
    public void deleteComment(int id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
}
