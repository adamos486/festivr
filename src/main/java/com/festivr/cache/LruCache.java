package com.festivr.cache;

import android.graphics.Bitmap;
import com.festivr.url.UrlKeyCombo;
import java.util.LinkedHashMap;
import java.util.Map;
import timber.log.Timber;

public class LruCache implements BaseCache {
  private float memoryMode = LOW_MEMORY;
  public static final float LOW_MEMORY = 0.5f;
  public static final float MEDIUM_MEMORY = 1f;
  public static final float HIGH_MEMORY = 1.5f;

  private final LinkedHashMap<UrlKeyCombo, Bitmap> cache = new LinkedHashMap<>(0, 0.75f, true);
  private final int initialMaxSize;
  private int maxSize;
  private int currentSize = 0;

  public LruCache(int size) {
    this.initialMaxSize = size;
    this.maxSize = Math.round(initialMaxSize * memoryMode);
  }

  public LruCache(int size, float memoryMode) {
    this.memoryMode = memoryMode;
    this.initialMaxSize = size;
    this.maxSize = Math.round(size * memoryMode);
  }

  public synchronized void setMemoryMode(float mode) {
    if (mode < 0) {
      throw new IllegalArgumentException("Memory Mode must be > 0");
    }
    this.memoryMode = mode;
    maxSize = Math.round(initialMaxSize * memoryMode);
    evict();
  }

  private void evict() {
    trimToSize(maxSize);
  }

  @Override public void clearCache() {
    trimToSize(-1);
  }

  @Override public synchronized Bitmap remove(UrlKeyCombo key) {
    final Bitmap value = cache.remove(key);
    if (value != null) {
      currentSize = currentSize - getSize(value);
    }
    return value;
  }

  @Override public synchronized Bitmap put(UrlKeyCombo key, Bitmap bitmap) {
    final int inputSize = getSize(bitmap);
    if (inputSize >= maxSize) {
      //We can evict this right away because we know it won't fit.
      onEviction(key, bitmap);
      return null;
    }

    final Bitmap result = cache.put(key, bitmap);
    if (bitmap != null) {
      currentSize = currentSize + getSize(bitmap);
    }
    if (result != null) {
      currentSize = currentSize - getSize(bitmap);
    }
    evict();

    return result;
  }

  @Override public synchronized Bitmap get(UrlKeyCombo key) {
    return cache.get(key);
  }

  @Override public synchronized void setMaxSize(int size) {
    maxSize = Math.round(size * memoryMode);
  }


  private void trimToSize(int size) {
    Map.Entry last;
    Timber.d("current size is " + currentSize);
    Timber.d("size is " + size);
    while (currentSize > size) {
      last = cache.entrySet().iterator().next();
      final Bitmap evicted = (Bitmap) last.getValue();

      currentSize = currentSize - getSize(evicted);

      final UrlKeyCombo key = (UrlKeyCombo) last.getKey();
      cache.remove(key);
      onEviction(key, evicted);
    }
  }

  public void onEviction(UrlKeyCombo key, Bitmap bitmap) {
    Timber.d("Evicting: " + key.getSafelyEncodedUrlString());
  }

  public synchronized int getMaxSize() {
    return maxSize;
  }

  public synchronized int getCurrentSize() {
    return currentSize;
  }

  public synchronized boolean contains(UrlKeyCombo key) {
    return cache.containsKey(key);
  }

  private int getSize(Bitmap bitmap) {
    return bitmap.getRowBytes() * bitmap.getHeight();
  }
}
