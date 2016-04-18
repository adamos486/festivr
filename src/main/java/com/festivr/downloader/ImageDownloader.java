package com.festivr.downloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.festivr.cache.BaseCache;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * The ImageDownloader class should actually take care of the Bitmap downloading and decoding.
 * Bitmaps that are downloaded are placed inside the memory cache.
 */
public class ImageDownloader {
  private OkHttpClient client;
  private BaseCache memoryCache;

  /**
   * ImageDownloader must be initialized with a memoryCache.
   *
   * @param memoryCache The memoryCache that will be added to from this downloader.
   */
  public ImageDownloader(BaseCache memoryCache) {
    //Create the default OkHttpClient.
    this.client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS)
        .readTimeout(20000, TimeUnit.MILLISECONDS)
        .build();
    this.memoryCache = memoryCache;
  }

  /**
   * The central image downloading method. Should always take place on a background thread.
   *
   * @param key The key which contains the md5 cache key, as well as the url on which to build the
   * request.
   * @param listener The listener which allows a callback to the UI thread.
   * @param view The {@link ImageViewWrapper} which contains a weak reference to to the original
   * ImageView, and gets sent back in the listener.
   */
  public void downloadImage(final UrlKeyCombo key, final ImageLoadingListener listener,
      final ImageViewWrapper view) {
    //Build the get request with a safely encoded url.
    final Request request = new Request.Builder().get().url(key.getSafelyEncodedUrl()).build();
    //Asynchronously call the request.
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        Timber.e("We failed!", e);
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          InputStream stream = response.body().byteStream();
          BitmapFactory.Options options = new BitmapFactory.Options();
          if (response.body().contentLength() >= 250000
              && response.body().contentLength() <= 999999) {
            //Images between 250kB and 999kB are downsampled by 2.
            options.inSampleSize = 2;
          } else if (response.body().contentLength() >= 1000000) {
            //Images 1MB or greater are downsampled by 4.
            options.inSampleSize = 4;
          } else {
            //Images below 250kb are not downsampled.
            options.inSampleSize = 1;
          }

          Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
          if (bitmap != null) {
            Timber.d("Calling back with bitmap.");
            //Listener callback.
            listener.loadingComplete(key, view, bitmap);
            //Grab the md5 cache key from the UrlKeyCombo, and put into memory cache.
            memoryCache.put(key.getMD5HashKey(), bitmap);
          } else {
            Timber.e("Uh oh! Couldn't load the bitmap.");
          }
        }
      }
    });
  }
}
