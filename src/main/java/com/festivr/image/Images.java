package com.festivr.image;

import android.graphics.Bitmap;
import android.view.View;
import com.festivr.listener.ImageLoadingListener;
import timber.log.Timber;

public class Images {

  private ImageManager manager;
  private Configuration configuration;
  private ImageLoadingListener loadingListener = new ImageLoadingListener() {
    @Override public void loadingStarted(String url, View view) {
      //TODO
    }

    @Override public void loadingFailed(String url, View view, String error) {
      //TODO
    }

    @Override public void loadingComplete(String url, View view, Bitmap bitmap) {
      //TODO
    }

    @Override public void loadingCancelled(String url, View view) {
      //TODO
    }
  };

  private volatile static Images instance;

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

  private Images() {}

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
}
