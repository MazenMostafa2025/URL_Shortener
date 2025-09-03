export interface ShortUrl {
  originalUrl: string;
  shortUrl: string;
  clickCount?: number;
}

export interface ShortUrlResponse {
  shortUrl: ShortUrl;
}
