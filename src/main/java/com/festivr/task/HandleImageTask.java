package com.festivr.task;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;
import com.festivr.downloader.ImageDownloader;
import com.festivr.image.Configuration;
import com.festivr.image.ImageManager;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;

public class HandleImageTask implements Runnable {
  private final UrlKeyCombo key;
  private final ImageManager manager;
  private final Configuration config;
  private final ImageLoadingListener listener;
  private final ImageDownloader downloader;
  private final ImageViewWrapper view;

  public HandleImageTask(UrlKeyCombo key, ImageManager manager,
      ImageLoadingListener loadingListener, ImageViewWrapper viewToLoad) {
    this.key = key;
    this.manager = manager;
    this.config = manager.getConfiguration();
    this.listener = loadingListener;
    this.downloader = config.getDownloader();
    this.view = viewToLoad;
  }

  @Override public void run() {
    Bitmap bitmap;
    bitmap = config.getMemoryCache().get(key.getMD5HashKey());
    //Bitmap doesn't exist in cache, load from network.
    if (bitmap == null || bitmap.isRecycled()) {
      downloader.downloadImage(key, listener, view);
    } else {
      if (listener != null) {
        listener.loadingComplete(key, view, bitmap);
      }
    }
  }

  public UrlKeyCombo getKey() {
    return key;
  }
}
