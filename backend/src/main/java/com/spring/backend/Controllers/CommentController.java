package com.spring.backend.Controllers;

import com.spring.backend.DTOs.Comment.CommentRequest;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Services.Comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity createComment(@RequestBody CommentRequest req) {
        try {
            service.createComment(req);
            return ResponseEntity.ok().body("Comment created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{commentId}/content")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity changeComment(@RequestBody String content, @PathVariable Long commentId) {
        try {
            service.changeComment(content, commentId);
            return ResponseEntity.ok().body("Comment updated successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity deleteComment(@PathVariable Long commentId) {
        try {
            service.deleteComment(commentId);
            return ResponseEntity.ok().body("Comment deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getCommentsByTarget(
            @RequestParam String targetType,
            @RequestParam Long targetId) {
        try {
            return ResponseEntity.ok(service.getCommentsByTarget(targetId, targetType));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
