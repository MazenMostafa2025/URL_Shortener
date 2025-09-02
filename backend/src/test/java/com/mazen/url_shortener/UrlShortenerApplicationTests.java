package com.mazen.url_shortener;

import com.mazen.url_shortener.entity.Url;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.mazen.url_shortener.dto.UrlShortenerRequest;
import com.mazen.url_shortener.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UrlService urlService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testShortenUrl() throws Exception {
		UrlShortenerRequest request = new UrlShortenerRequest("https://www.example.com");

		mockMvc.perform(post("/api/v1/shorten")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.originalUrl").value("https://www.example.com"))
				.andExpect(jsonPath("$.shortCode").exists());
	}

	@Test
	void testInvalidUrl() throws Exception {
		UrlShortenerRequest request = new UrlShortenerRequest("invalid-url");

		mockMvc.perform(post("/api/v1/shorten")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testStats() throws Exception {
		Url url = urlService.shortenUrl("https://www.example.com");

		mockMvc.perform(get("/api/v1/stats/" + url.getShortCode()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.originalUrl").value("https://www.example.com"))
				.andExpect(jsonPath("$.clickCount").value(0));
	}

}
