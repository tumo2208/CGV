package com.spring.backend.DTOs.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userAvatar;
    private String content;
    private Double rating;
    private Integer likes;
    private LocalDateTime createdAt;
}
