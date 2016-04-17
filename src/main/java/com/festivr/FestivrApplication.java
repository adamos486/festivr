package com.festivr;

import android.app.Application;

public class FestivrApplication extends Application {
  private static FestivrApplication instance;

  public static FestivrApplication getSingleton() {
    return instance;
  }

  @Override public void onCreate() {
    super.onCreate();
    instance = this;
  }
}
