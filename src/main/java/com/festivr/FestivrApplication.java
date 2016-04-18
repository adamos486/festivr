package com.festivr;

import android.app.Application;
import timber.log.Timber;

public class FestivrApplication extends Application {
  private static FestivrApplication instance;

  public static FestivrApplication getSingleton() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    instance = this;

    //Plant timber so that it can be used throughout the application.
    Timber.plant(new Timber.DebugTree());
  }
}
