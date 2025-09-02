package com.mazen.url_shortener.controller;

import com.mazen.url_shortener.dto.UrlShortenerRequest;
import com.mazen.url_shortener.dto.UrlShortenerResponse;
import com.mazen.url_shortener.dto.UrlStatsResponse;
import com.mazen.url_shortener.entity.Url;
import com.mazen.url_shortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
    maxAge = 3600
)  
@RequestMapping("/api")
@Tag(name = "URL Shortener", description = "API for URL shortening and management")
public class UrlController {
    private final UrlService urlService;

    // @Value("${app.base-url:https://mzn-shortee.onrender.com}")
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }
    @Operation(summary = "Get URLs",
            description = "Retrieve original and shortened URLs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlStatsResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/")
    public List<Url> getAllUrls() throws IOException {
        return urlService.getUrls();
    }
    
    @PostMapping("/shorten")
    @Operation(summary = "Shorten a URL",
            description = "Create a shortened version of a long URL. Returns existing short code if URL was already shortened.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL successfully shortened",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UrlShortenerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid URL format",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<UrlShortenerResponse> shortenUrl(@Valid @RequestBody UrlShortenerRequest request) {
        try {
            Url url = urlService.shortenUrl(request.getUrl());
            String shortUrl = baseUrl + "/" + url.getShortCode();

            UrlShortenerResponse response = new UrlShortenerResponse(
                    url.getOriginalUrl(),
                    shortUrl,
                    url.getShortCode()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // @Operation(summary = "Get URL statistics",
    //         description = "Retrieve statistics for a shortened URL including click count and creation date")
    // @ApiResponses(value = {
    //         @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
    //                 content = @Content(mediaType = "application/json",
    //                         schema = @Schema(implementation = UrlStatsResponse.class))),
    //         @ApiResponse(responseCode = "404", description = "Short code not found",
    //                 content = @Content)
    // })
    // @GetMapping("/stats/{shortCode}")
    // public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
    //     Optional<Url> url = urlService.getUrlStats(shortCode);

    //     if (url.isPresent()) {
    //         Url urlEntity = url.get();
    //         UrlStatsResponse response = new UrlStatsResponse(
    //                 urlEntity.getOriginalUrl(),
    //                 urlEntity.getShortCode(),
    //                 urlEntity.getClickCount(),
    //                 urlEntity.getCreatedAt()
    //         );
    //         return ResponseEntity.ok(response);
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }
    @Operation(summary = "Redirect to original URL",
            description = "Redirect to the original URL using the short code. Increments click count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to original URL"),
            @ApiResponse(responseCode = "404", description = "Short code not found")
    })
    @GetMapping("/{shortCode}")
    public void redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response)
            throws IOException {
        Optional<Url> url = urlService.getOriginalUrl(shortCode);

        if (url.isPresent()) {
            response.sendRedirect(url.get().getOriginalUrl());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Short URL not found");
        }
    }

}
