package com.spring.backend.DTOs.Comment;

import com.spring.backend.Enums.Movie.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private CommentType type;
    private Long targetId;
    private String content;
    private double rating;
}
