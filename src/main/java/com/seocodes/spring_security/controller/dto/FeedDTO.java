package com.seocodes.spring_security.controller.dto;

import java.util.List;

public record FeedDTO(List<FeedItemDTO> feedItems,
                      int page,
                      int pageSize,
                      int totalPages,
                      long totalElements) {
}
