package com.mazen.url_shortener.service;

import com.mazen.url_shortener.entity.Url;
import com.mazen.url_shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url shortenUrl(String originalUrl) {
        // Check if URL already exists
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        // Generate unique short code
        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));

        Url url = new Url(originalUrl, shortCode);
        return urlRepository.save(url);
    }
    
    public Optional<Url> getOriginalUrl(String shortCode) {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        if (url.isPresent()) {
            Url urlEntity = url.get();
            urlEntity.incrementClickCount();
            urlRepository.save(urlEntity);
        }
        return url;
    }

    public Optional<Url> getUrlStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }
    public List<Url> getUrls() {
        return urlRepository.findAll();
    }
    private String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
