package com.festivr.downloader;

import android.content.Context;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;
import java.io.IOException;
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

  public ImageDownloader(Context context) {
    this.context = context;
    this.client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS)
        .readTimeout(20000, TimeUnit.MILLISECONDS)
        .build();
  }

  public void downloadImage(UrlKeyCombo key, ImageLoadingListener listener, ImageViewWrapper view) {
    Request request = new Request.Builder().get().url(key.getSafelyEncodedUrl()).build();
    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        Timber.e("We failed!", e);
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          Timber.d("YAY!");
        }
      }
    });
  }
}
