package com.joanlica.ecommerce.common.util.pages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Standard paginated response wrapper (0-based page index).")
public record PageResponse<T>(
        @Schema(description = "Page content.")
        List<T> content,

        @Schema(description = "0-based page index returned by Spring Data.", example = "0")
        int page,

        @Schema(description = "Page size requested.", example = "20")
        int size,

        @Schema(description = "Total number of elements across all pages.", example = "125")
        long totalElements,

        @Schema(description = "Total number of pages.", example = "7")
        int totalPages,

        @Schema(description = "Whether this is the first page.")
        boolean first,

        @Schema(description = "Whether this is the last page.")
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}

