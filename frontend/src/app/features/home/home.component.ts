import {
  afterNextRender,
  Component,
  DestroyRef,
  inject,
  OnInit,
  signal,
  viewChild,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { UrlService } from '../../core/services/url.service';
import { debounceTime } from 'rxjs';
import { UrlCardComponent } from '../../shared/url-card/url-card.component';
import { ShortUrl } from '../../core/models/ShortUrl.model';

@Component({
  selector: 'app-home',
  imports: [FormsModule, UrlCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  private form = viewChild.required<NgForm>('form');
  private destroyRef = inject(DestroyRef);
  private urlService = inject(UrlService);
  urls = signal<ShortUrl[]>([]);

  constructor() {
    afterNextRender(() => {
      const savedForm = window.localStorage.getItem('saved-url-form');

      if (savedForm) {
        const loadedFormData = JSON.parse(savedForm);
        const savedUrl = loadedFormData.url;
        setTimeout(() => {
          this.form().controls['url'].setValue(savedUrl);
        }, 1);
      }

      const subscription = this.form()
        .valueChanges?.pipe(debounceTime(500))
        .subscribe({
          next: (value) =>
            window.localStorage.setItem(
              'saved-url-form',
              JSON.stringify({ url: value.url })
            ),
        });

      this.destroyRef.onDestroy(() => subscription?.unsubscribe());
    });
  }
  ngOnInit() {
    this.getAllShortenedUrls();
  }
  getAllShortenedUrls() {
    this.urlService.loadAll().subscribe({
      next: (response) => {
        console.log(response);
        const modifiedArr = response.map((url) => ({
          originalUrl: url.originalUrl,
          shortUrl: `http://localhost:8080/api/v1/${url.shortCode}`,
          // shortUrl: `https://mzn-shortee.onrender.com/api/${url.shortCode}`,
          clickCount: url.clickCount || 0,
        }));
        this.urls.set(modifiedArr);
      },
    });
  }
  onSubmit(formData: NgForm) {
    if (formData.form.invalid) {
      return;
    }

    const enteredUrl = formData.form.value.url;
    console.log(enteredUrl);
    const subscription = this.urlService.shortenUrl(enteredUrl).subscribe({
      next: (response) => {
        console.log('URL shortened successfully', response);
        const { shortCode, originalUrl, clickCount = 0 } = response;
        const shortUrl = `http://localhost:8080/api/v1/${shortCode}`;
        // const shortUrl = `https://mzn-shortee.onrender.com/${shortCode}`;
        const exists = this.urls().some(
          (url) => url.originalUrl === originalUrl
        );
        if (!exists) {
          this.urls.set([
            ...this.urls(),
            { shortUrl, originalUrl, clickCount },
          ]);
        }
      },
      error: (error) => console.error('Error shortening URL', error),
    });
    this.destroyRef.onDestroy(() => subscription.unsubscribe());
    formData.form.reset();
  }
}
