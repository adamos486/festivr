package com.festivr.task;

import android.graphics.Bitmap;
import com.festivr.downloader.ImageDownloader;
import com.festivr.image.Configuration;
import com.festivr.image.ImageManager;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.url.UrlKeyCombo;
import com.festivr.utils.Constants;
import com.festivr.view.ImageViewWrapper;

/**
 * Prime Runnable for downloading are retrieving images from the cache.
 */
public class HandleImageTask implements Runnable {
  private final UrlKeyCombo key;
  private final Configuration config;
  private final ImageLoadingListener listener;
  private final ImageDownloader downloader;
  private final ImageViewWrapper view;

  public HandleImageTask(UrlKeyCombo key, ImageManager manager,
      ImageLoadingListener loadingListener, ImageViewWrapper viewToLoad) {
    this.key = Constants.checkForNonNull(key, "Key can't be null in HandleImageTask!!!");
    Constants.checkForNonNull(manager, "ImageManager can't be null in HandleImageTask!!!");
    this.config = Constants.checkForNonNull(manager.getConfiguration(),
        "Manager must have a built configuration to Handle Images!!!");
    this.downloader = Constants.checkForNonNull(manager.getConfiguration().getDownloader(),
        "Configuration must have a valid downloader to handle images!!!");
    this.listener = Constants.checkForNonNull(loadingListener,
        "ImageLoadingListener can't be null in HandleImageTask!!!");
    this.view = Constants.checkForNonNull(viewToLoad,
        "A loadable view must be supplied in HandleImageTask!!!");
  }

  @Override public void run() {
    Bitmap bitmap;
    bitmap = config.getMemoryCache().get(key.getMD5HashKey());
    //Bitmap doesn't exist in cache, load from network.
    if (bitmap == null || bitmap.isRecycled()) {
      //Tell the ImageDownloader to download and cache an image.
      downloader.downloadImage(key, listener, view);
    } else {
      //We retrieved this bitmap from our cache and it's not recycled.
      if (listener != null) {
        //Calling back with a cached result.
        listener.loadingComplete(key, view, bitmap);
      }
    }
  }

  public UrlKeyCombo getKey() {
    return key;
  }
}
