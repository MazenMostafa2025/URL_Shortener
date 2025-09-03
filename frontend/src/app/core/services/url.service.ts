import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UrlService {
  private apiUrl = 'http://localhost:8080/api/v1/';
  // private apiUrl = 'https://mzn-shortee.onrender.com/api/';
  constructor(private httpClient: HttpClient) {}
  shortenUrl(url: string): Observable<any> {
    // Simple hash function to generate a short code
    return this.httpClient.post(`${this.apiUrl}shorten`, {
      url,
    });
  }
  getShortenedUrl(shortCode: string): Observable<any> {
    // Simple hash function to generate a short code
    return this.httpClient.get(this.apiUrl + shortCode);
  }
  loadAll(): Observable<any[]> {
    // Simple hash function to generate a short code
    return this.httpClient.get<any[]>(this.apiUrl);
  }
}
