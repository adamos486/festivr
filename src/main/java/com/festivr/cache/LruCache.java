package com.festivr.cache;

import android.graphics.Bitmap;
import com.festivr.url.UrlKeyCombo;
import java.util.LinkedHashMap;
import java.util.Map;
import timber.log.Timber;

public class LruCache implements BaseCache {
  public static final float LOW_MEMORY = 0.5f;
  public static final float MEDIUM_MEMORY = 1f;
  public static final float HIGH_MEMORY = 1.5f;
  private final LinkedHashMap<String, Bitmap> cache = new LinkedHashMap<>(0, 0.75f, true);
  private int initialMemMaxSize;
  private float memoryMode = MEDIUM_MEMORY;
  private int maxMemSize;
  private int currentMemSize = 0;
  private int maxPoolSize;
  private int currentPoolSize;

  public LruCache(int size, boolean isPool) {
    if (isPool) {
      this.maxPoolSize = size;
    } else {
      this.initialMemMaxSize = size;
      this.maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    }
  }

  public LruCache(int memSize, int poolSize) {
    this.initialMemMaxSize = memSize;
    this.maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    this.maxPoolSize = poolSize;
  }

  public LruCache(int memSize, float memoryMode) {
    this.memoryMode = memoryMode;
    this.initialMemMaxSize = memSize;
    this.maxMemSize = Math.round(memSize * memoryMode);
  }

  public LruCache(int memSize, int poolSize, float memoryMode) {
    this.memoryMode = memoryMode;
    this.initialMemMaxSize = memSize;
    this.maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    this.maxPoolSize = poolSize;
  }

  @Override public synchronized void setMemoryMode(float mode) {
    if (mode < 0) {
      throw new IllegalArgumentException("Memory Mode must be > 0");
    }
    this.memoryMode = mode;
    maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    evict();
  }

  private void evict() {
    trimToSize(maxMemSize, maxPoolSize);
  }

  @Override public void clearCache() {
    trimToSize(0, 0);
  }

  @Override public synchronized Bitmap remove(String key) {
    final Bitmap value = cache.remove(key);
    if (value != null) {
      currentMemSize = currentMemSize - getSize(value);
      --currentPoolSize;
    }
    return value;
  }

  @Override public synchronized Bitmap put(String key, Bitmap bitmap) {
    final int inputSize = getSize(bitmap);
    if (inputSize >= maxMemSize) {
      //We can evict this right away because we know it won't fit.
      onEviction(key, bitmap);
      return null;
    }

    final Bitmap result = cache.put(key, bitmap);
    ++currentPoolSize;
    if (bitmap != null) {
      currentMemSize = currentMemSize + getSize(bitmap);
    }
    if (result != null) {
      --currentPoolSize;
      currentMemSize = currentMemSize - getSize(bitmap);
    }
    evict();

    return result;
  }

  @Override public synchronized Bitmap get(String key) {
    return cache.get(key);
  }

  private void trimToSize(int memSize, int poolSize) {
    Map.Entry last;
    if (currentMemSize > memSize) {
      Timber.d("Current memory size is larger than max size.");
      Timber.d("Current mem: " + currentMemSize + " and max: " + memSize);
    }
    if (currentPoolSize > poolSize) {
      Timber.d("Current pool size is greater than max pool size.");
      Timber.d("Current pool: " + currentPoolSize + " and max: " + poolSize);
    }
    while (currentMemSize > memSize || currentPoolSize > poolSize) {
      last = cache.entrySet().iterator().next();
      final Bitmap evicted = (Bitmap) last.getValue();

      currentMemSize = currentMemSize - getSize(evicted);
      --currentPoolSize;

      final String key = (String) last.getKey();
      cache.remove(key);
      onEviction(key, evicted);
    }
    Timber.d(
        "Trim finished --> Current mem: " + currentMemSize + ", current pool: " + currentPoolSize);
  }

  public void onEviction(String key, Bitmap bitmap) {
    Timber.d("Evicting: " + key);
  }

  public synchronized int getMaxMemSize() {
    return maxMemSize;
  }

  @Override public synchronized void setMaxMemSize(int size) {
    maxMemSize = Math.round(size * memoryMode);
    evict();
  }

  @Override public void setMaxPoolSize(int size) {
    this.maxPoolSize = size;
    evict();
  }

  public synchronized int getCurrentMemSize() {
    return currentMemSize;
  }

  public synchronized boolean contains(UrlKeyCombo key) {
    return cache.containsKey(key);
  }

  private int getSize(Bitmap bitmap) {
    return bitmap.getRowBytes() * bitmap.getHeight();
  }
}
