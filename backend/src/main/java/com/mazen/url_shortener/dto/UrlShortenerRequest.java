package com.mazen.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UrlShortenerRequest {
    @NotBlank(message = "URL cannot be blank")
    @Pattern(regexp = "^(https?://).*", message = "URL must start with http:// or https://")
    private String url;

    public UrlShortenerRequest() {}

    public UrlShortenerRequest(String url) {
        this.url = url;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
