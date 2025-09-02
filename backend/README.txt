# URL Shortener Service

A simple URL shortener service built with Spring Boot that provides REST APIs to shorten URLs and track click statistics.

## Features

- Shorten long URLs to compact codes
- Redirect short URLs to original URLs
- Track click statistics
- Prevent duplicate entries for the same URL
- Input validation
- H2 in-memory database for development

## API Endpoints

### 1. Shorten URL
**POST** `/api/v1/shorten`

Request Body:
```json
{
  "url": "https://www.example.com/very/long/url"
}
```

Response:
```json
{
  "originalUrl": "https://www.example.com/very/long/url",
  "shortUrl": "http://localhost:8080/abc123",
  "shortCode": "abc123"
}
```

### 2. Redirect to Original URL
**GET** `/{shortCode}`

Redirects to the original URL and increments click count.

### 3. Get URL Statistics
**GET** `/api/v1/stats/{shortCode}`

Response:
```json
{
  "originalUrl": "https://www.example.com/very/long/url",
  "shortCode": "abc123",
  "clickCount": 15,
  "createdAt": "2023-06-15T10:30:00"
}
```

## Running the Application

1. Clone the repository
2. Run: `mvn spring-boot:run`
3. Access the application at `http://localhost:8080`
4. H2 Console available at `http://localhost:8080/h2-console`

## Testing

Run tests with: `mvn test`

## Configuration

You can customize the base URL in `application.yml`:
```yaml
app:
  base-url: https://yourdomain.com
```