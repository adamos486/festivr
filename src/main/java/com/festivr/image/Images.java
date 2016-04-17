package com.festivr.image;

public class Images {

  private ImageManager manager;

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

  public synchronized void setup(Configuration config) {

  }
}
