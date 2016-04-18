package com.festivr.image;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.festivr.adapter.ExperienceImagesAdapter;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.task.HandleImageTask;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;
import timber.log.Timber;

/**
 * The entryway in the Image Loading component.
 */
public class Images {

  private volatile static Images instance;
  private ImageManager manager;
  private Configuration configuration;
  //Default loading listener currently not being used.
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

  /**
   * A private constructor ensures that the {@link #getSingleton()} is the only
   * way to get an Images object.
   */
  private Images() {
  }

  /**
   * A singleton call to get the synchronization locked Singleton instance of
   * the Images object. There can only be one, even on parallel accesses.
   *
   * @return The singleton instance of {@link Images}
   */
  public static Images getSingleton() {
    //Only do this work if instance is null.
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

  /**
   * A synchronized method for setting up all the necessary pre-requisites for image loading.
   *
   * @param config A configuration object that's been built by {@link
   * Configuration.Builder#build()}.
   */
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

  /**
   * The main UI entry-point into the image loading component. This will spin up a
   * background {@link HandleImageTask} and add it to the appropriate executor.
   *
   * @param key A combo object that contains the url for the image, the md5key, and a few other
   * tools.
   * @param view The {@link ImageView} the image will be loaded into in the callback.
   * @param listener The callback method that will alert the {@link ExperienceImagesAdapter} to set
   * the downloaded bitmap on screen.
   */
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
