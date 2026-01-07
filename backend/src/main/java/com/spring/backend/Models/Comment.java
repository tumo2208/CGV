package com.spring.backend.Models;

import com.spring.backend.Enums.Movie.CommentType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentId implements Serializable {
        private Long userId;
        private Long targetId;
        private final LocalDateTime createdAt = LocalDateTime.now();
    }

    @EmbeddedId
    private CommentId id;
    @Enumerated(value = EnumType.STRING)
    private CommentType type;
    private String content;
    private double rating;
    private LocalDateTime updatedAt;
}
