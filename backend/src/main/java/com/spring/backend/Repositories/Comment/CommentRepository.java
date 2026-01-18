package com.spring.backend.Repositories.Comment;

import com.spring.backend.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTypeAndTargetId(Long targetId, String targetType);
}
