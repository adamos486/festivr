package com.festivr.image;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.task.HandleImageTask;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;
import timber.log.Timber;

public class Images {

  private volatile static Images instance;
  private ImageManager manager;
  private Configuration configuration;
  private ImageLoadingListener loadingListener = new ImageLoadingListener() {

    @Override public void loadingStarted(UrlKeyCombo combo, ImageViewWrapper view) {

    }

    @Override public void loadingFailed(UrlKeyCombo combo, ImageViewWrapper view, String error) {

    }

    @Override public void loadingComplete(UrlKeyCombo combo, ImageViewWrapper view, Bitmap bitmap) {

    }

    @Override public void loadingCancelled(UrlKeyCombo combo, ImageViewWrapper view) {

    }
  };

  private Images() {
  }

  public static Images getSingleton() {
    if (instance == null) {
      //Synchronize around a lock object.
      synchronized (Images.class) {
        if (instance == null) {
          instance = new Images();
        }
      }
    }
    return instance;
  }

  public synchronized void setup(Configuration config) {
    if (config == null) {
      throw new NullPointerException("Configuration should never be null when setting up Images.");
    }
    //We haven't set configuration up yet.
    if (this.configuration == null) {
      manager = new ImageManager(config);
      this.configuration = config;
    } else {
      Timber.d("We already have a valid configuration.");
    }
  }

  public void load(UrlKeyCombo key, ImageView view, ImageLoadingListener listener) {
    if (configuration == null) {
      throw new IllegalStateException("You haven't configured Images yet.");
    }

    if (view == null) {
      throw new IllegalArgumentException("You can't load an image into a null ImageView!!!");
    }

    if (listener == null) {
      listener = loadingListener;
    }

    if (key.getSafelyEncodedUrl() == null) {
      throw new IllegalArgumentException("Can't load without a url!!!");
    }

    manager.addTask(new HandleImageTask(key, manager, listener, new ImageViewWrapper(view)));
  }
}
