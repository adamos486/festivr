package com.festivr.cache;

import android.graphics.Bitmap;
import android.support.annotation.VisibleForTesting;
import com.festivr.url.UrlKeyCombo;
import java.util.LinkedHashMap;
import java.util.Map;
import timber.log.Timber;

public class LruCache implements BaseCache {
  public static final float LOW_MEMORY = 0.5f;
  public static final float MEDIUM_MEMORY = 1f;
  public static final float HIGH_MEMORY = 1.5f;
  private final LinkedHashMap<String, Bitmap> cache = new LinkedHashMap<>(25, 0.75f, true);
  private int initialMemMaxSize;
  private float memoryMode = MEDIUM_MEMORY;
  private int maxMemSize;
  private int currentMemSize = 0;
  private int maxPoolSize;
  private int currentPoolSize;

  /**
   * Constructor that allows definition of either maxMemSize or maxPoolSize.
   *
   * @param size The size with which to set either maxMemSize or maxPoolSize.
   * @param isPool A flag to tell the constructor which mode to construct.z
   */
  public LruCache(int size, boolean isPool) {
    if (isPool) {
      this.maxPoolSize = size;
    } else {
      this.initialMemMaxSize = size;
      this.maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    }
  }

  /**
   * Constructor that allows definition of both maxMemSize and maxPoolSize.
   *
   * @param memSize The maximum memory allocation size.
   * @param poolSize The maximum pool size (max # of objects).
   */
  public LruCache(int memSize, int poolSize) {
    this.initialMemMaxSize = memSize;
    this.maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    this.maxPoolSize = poolSize;
  }

  /**
   * Constructor that allows definition of maxSize and defines a custom memory mode.
   *
   * @param memSize The size in bytes of the maxMemSize.
   * @param memoryMode The float Constant memoryMode which will affect the maxMemSize.
   */
  public LruCache(int memSize, float memoryMode) {
    this.memoryMode = memoryMode;
    this.initialMemMaxSize = memSize;
    this.maxMemSize = Math.round(memSize * memoryMode);
  }

  /**
   * Constructor that allows definition of maxSize, maxPool, and a custom memory mode.
   * @param memSize The size in bytes of the maxMemSize.
   * @param poolSize The size of the pool (max # of objects).
   * @param memoryMode The float Constant memory mode which will affect the maxMemSize.
   */
  public LruCache(int memSize, int poolSize, float memoryMode) {
    this.memoryMode = memoryMode;
    this.initialMemMaxSize = memSize;
    this.maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    this.maxPoolSize = poolSize;
  }

  /**
   * Set a float memory mode, which acts as a max memory multiplier.
   * Setting this mode, will set the field, calculate a new maxMemSize, and invoke
   * eviction.
   *
   * @param mode The float constant on which to multiply initialMaxSize.
   */
  @Override public synchronized void setMemoryMode(float mode) {
    if (mode < 0f) {
      throw new IllegalArgumentException("Memory Mode must be > 0");
    }
    this.memoryMode = mode;
    maxMemSize = Math.round(initialMemMaxSize * memoryMode);
    evict();
  }

  @Override public void evict() {
    trimToSize(maxMemSize, maxPoolSize);
  }

  /**
   * Clear the memory cache all the way down to zeroes.
   */
  @Override public void clearCache() {
    trimToSize(0, 0);
  }

  /**
   * Standard cache remove method, which updates currentMemSize and currentPoolSize.
   *
   * @param key The key of the entry to remove.
   * @return The value of the entry that was removed.
   */
  @Override public synchronized Bitmap remove(String key) {
    final Bitmap value = cache.remove(key);
    if (value != null) {
      currentMemSize = currentMemSize - getSize(value);
      --currentPoolSize;
    }
    return value;
  }

  /**
   * Since this is an LruCache, the put method must invoke eviction.
   *
   * @param key The key of the new entry.
   * @param bitmap The value of the new entry.
   * @return The bitmap that's being replaced, or null if new.
   */
  @Override public synchronized Bitmap put(String key, Bitmap bitmap) {
    final int inputSize = getSize(bitmap);
    //Check if incoming bitmap is LARGER than the entire maxMemSize.
    if (inputSize >= maxMemSize) {
      //We can evict this right away because we know it won't fit.
      onEviction(key, bitmap);
      return null;
    }

    //Place the new bitmap in cache, and see if it replaced another key-value.
    final Bitmap result = cache.put(key, bitmap);
    if (bitmap != null) {
      //The input bitmap is not null, therefore add it's memory to the currentMemSize.
      currentMemSize = currentMemSize + getSize(bitmap);
    }
    if (result != null) {
      //Don't increment the poolSize and subtract the size of the evicted entry since it's
      //a replacement not an addition.
      currentMemSize = currentMemSize - getSize(result);
    } else {
      //Result was null, therefore increment the poolSize;
      ++currentPoolSize;
    }
    //Invoke eviction.
    evict();

    return result;
  }

  @Override public synchronized Bitmap get(String key) {
    return cache.get(key);
  }

  /**
   * The main LRU eviction method. Accepting both a maxMemory and a maxPool size.
   *
   * @param memSize The max memory size in bytes.
   * @param poolSize The max pool size or # of objects allowed.
   */
  private void trimToSize(int memSize, int poolSize) {
    Map.Entry last;
    //Check if currentMemSize is greater than maxMem.
    if (currentMemSize > memSize) {
      Timber.d("Current memory size is larger than max size.");
      Timber.d("Current mem: " + currentMemSize + " and max: " + memSize);
    }
    //Check if currentPoolSize is greater than maxPoolSize.
    if (currentPoolSize > poolSize) {
      Timber.d("Current pool size is greater than max pool size.");
      Timber.d("Current pool: " + currentPoolSize + " and max: " + poolSize);
    }

    //iterate through the eviction loop while memory or poolSize is greater than
    //max.
    while (currentMemSize > memSize || currentPoolSize > poolSize) {
      //Grab the last entry (which is least recently used.)
      //Top will always be the most recently used.
      last = cache.entrySet().iterator().next();
      //Retrieve the bitmap of this element.
      final Bitmap evicted = (Bitmap) last.getValue();

      //Remove the size bytes of the bitmap from currentMemSize.
      currentMemSize = currentMemSize - getSize(evicted);
      //Decrement the currentPoolSize.
      --currentPoolSize;

      //Get the string of the last entry.
      final String key = (String) last.getKey();
      //Actually remove it from the cache.
      cache.remove(key);
      //Call the eviction listener for cleanup or notification.
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

  protected int getMaxPoolSize() {
    return this.maxPoolSize;
  }

  protected synchronized int getCurrentMemSize() {
    return currentMemSize;
  }

  public synchronized boolean contains(String key) {
    return cache.containsKey(key);
  }

  private int getSize(Bitmap bitmap) {
    return bitmap.getRowBytes() * bitmap.getHeight();
  }

  @VisibleForTesting protected int getCurrentPoolSize() {
    return this.currentPoolSize;
  }

  @VisibleForTesting protected int getCacheSize() {
    return cache.size();
  }
}
