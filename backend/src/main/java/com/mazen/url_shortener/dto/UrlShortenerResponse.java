package com.mazen.url_shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Response object containing shortened URL information")
public class UrlShortenerResponse {
    @Schema(description = "The original URL that was shortened",
            example = "https://www.example.com/very/long/url/path")
    private String originalUrl;
    @Schema(description = "The complete shortened URL",
            example = "http://localhost:8080/abc123")
    private String shortUrl;
    @Schema(description = "The short code used for the URL",
            example = "abc123")
    private String shortCode;

    public UrlShortenerResponse() {}

    public UrlShortenerResponse(String originalUrl, String shortUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
}
