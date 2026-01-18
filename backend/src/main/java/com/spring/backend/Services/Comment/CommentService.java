package com.spring.backend.Services.Comment;

import com.spring.backend.DTOs.Comment.CommentDTO;
import com.spring.backend.DTOs.Comment.CommentRequest;
import com.spring.backend.DTOs.User.UserPartDTO;
import com.spring.backend.Enums.User.Role;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Comment;
import com.spring.backend.Models.User;
import com.spring.backend.Repositories.Comment.CommentLikeRepository;
import com.spring.backend.Repositories.Comment.CommentRepository;
import com.spring.backend.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private UserService userService;

    public void createComment(CommentRequest req) {
        User user = userService.getCurrentUser();
        Comment comment = Comment.builder()
                .userId(user.getId())
                .targetId(req.getTargetId())
                .type(req.getType())
                .content(req.getContent())
                .rating(req.getRating())
                .build();

        repository.save(comment);
    }

    public void changeComment(String content, Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        User user = userService.getCurrentUser();
        if (!comment.getUserId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to change this comment");
        }
        comment.setContent(content);
        repository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        User user = userService.getCurrentUser();
        if (!comment.getUserId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("You are not authorized to delete this comment");
        }
        repository.delete(comment);
    }

    public List<CommentDTO> getCommentsByTarget(Long targetId, String targetType) {
        List<Comment> comments = repository.findByTypeAndTargetId(targetId, targetType);
        return comments.stream().map(comment -> {
            UserPartDTO user = userService.getUserProfile(comment.getUserId());
            return CommentDTO.builder()
                    .id(comment.getId())
                    .userId(user.getId())
                    .userAvatar(user.getImg())
                    .userName(user.getFullName())
                    .content(comment.getContent())
                    .rating(comment.getRating())
                    .likes(comment.getLikes())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }).toList();
    }
}
