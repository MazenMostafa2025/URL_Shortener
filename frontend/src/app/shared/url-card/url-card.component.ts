import { Component, Input, signal, Signal, computed } from '@angular/core';

@Component({
  selector: 'app-url-card',
  imports: [],
  templateUrl: './url-card.component.html',
  styleUrl: './url-card.component.css',
})
export class UrlCardComponent {
  private _urlData = signal<{
    originalUrl: string;
    shortUrl: string;
  }>({
    originalUrl: '',
    shortUrl: '',
  });
  @Input() set urlData(value: { originalUrl: string; shortUrl: string }) {
    this._urlData.set(value);
  }
  get urlData(): Signal<{ originalUrl: string; shortUrl: string }> {
    return this._urlData.asReadonly();
  }
}
