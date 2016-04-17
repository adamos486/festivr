package com.festivr.downloader;

import android.content.Context;
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

public class ImageDownloader {
  private Context context;
  private OkHttpClient client;
  private BaseCache memoryCache;

  public ImageDownloader(Context context, BaseCache memoryCache) {
    this.context = context;
    this.client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS)
        .readTimeout(20000, TimeUnit.MILLISECONDS)
        .build();
    this.memoryCache = memoryCache;
  }

  public void downloadImage(final UrlKeyCombo key, final ImageLoadingListener listener, final ImageViewWrapper view) {
    final Request request = new Request.Builder().get().url(key.getSafelyEncodedUrl()).build();
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        Timber.e("We failed!", e);
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          Timber.d("YAY!");
          InputStream stream = response.body().byteStream();
          Bitmap bitmap = BitmapFactory.decodeStream(stream);
          if (bitmap != null) {
            Timber.d("Calling back with bitmap.");
            listener.loadingComplete(key, view, bitmap);
            memoryCache.put(key, bitmap);
          } else {
            Timber.e("Uh oh! Couldn't load the bitmap.");
          }
        }
      }
    });
  }
}
