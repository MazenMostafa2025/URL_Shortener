package com.mazen.url_shortener.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response object containing URL statistics")

public class UrlStatsResponse {
    @Schema(description = "The original URL",
            example = "https://www.example.com/very/long/url/path")
    private String originalUrl;
    @Schema(description = "The short code for the URL",
            example = "abc123")
    private String shortCode;
    @Schema(description = "Number of times the short URL has been accessed",
            example = "42")
    private Long clickCount;
    @Schema(description = "When the URL was shortened",
            example = "2023-06-15T10:30:00")
    private LocalDateTime createdAt;

    public UrlStatsResponse() {}

    public UrlStatsResponse(String originalUrl, String shortCode, Long clickCount, LocalDateTime createdAt) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public Long getClickCount() { return clickCount; }
    public void setClickCount(Long clickCount) { this.clickCount = clickCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}