package com.festivr.image;

import android.content.Context;
import android.content.res.Resources;
import com.festivr.cache.BaseCache;
import com.festivr.cache.LruCache;
import com.festivr.downloader.ImageDownloader;
import com.festivr.factory.ConfigFactory;
import java.util.concurrent.Executor;

public class Configuration {
  final Resources resources;
  final int maxWidth;
  final int maxHeight;

  final Executor networkExecutor;
  final Executor cachedExecutor;

  private static boolean altNetworkExecutor;
  private static boolean altCachedExecutor;
  private static boolean altMemoryCache;

  final BaseCache memoryCache;
  final int threadPoolSize;
  final int threadPriority;

  final ImageDownloader downloader;

  public Configuration(ConfigBuilder configBuilder) {
    this.resources = configBuilder.context.getResources();
    this.maxWidth = configBuilder.maxImageWidth;
    this.maxHeight = configBuilder.maxImageHeight;
    this.networkExecutor = configBuilder.networkExecutor;
    this.cachedExecutor = configBuilder.cachedExecutor;
    this.memoryCache = configBuilder.memoryCache;
    this.threadPoolSize = configBuilder.threadPoolSize;
    this.threadPriority = configBuilder.threadPriority;
    this.downloader = configBuilder.downloader;
  }

  public static class ConfigBuilder {
    public static final int THREAD_POOL_SIZE = 3;
    public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;
    private Context context;

    private int maxImageWidth = -1;
    private int maxImageHeight = -1;

    private Executor networkExecutor = null;
    private Executor cachedExecutor = null;
    private int threadPoolSize = THREAD_POOL_SIZE;
    private int threadPriority = DEFAULT_THREAD_PRIORITY;

    private BaseCache memoryCache = null;
    private ImageDownloader downloader = null;

    public ConfigBuilder(Context context) {
      this.context = context.getApplicationContext();
    }

    public ConfigBuilder setCachedExecutor(Executor executor) {
      this.cachedExecutor = executor;
      return this;
    }

    public ConfigBuilder setThreadPoolSize(int size) {
      this.threadPoolSize = size;
      return this;
    }

    public ConfigBuilder setThreadPriority(int priority) {
      if (priority < Thread.MIN_PRIORITY) {
        this.threadPriority = Thread.MIN_PRIORITY;
      } else {
        if (priority > Thread.MAX_PRIORITY) {
          this.threadPriority = Thread.MAX_PRIORITY;
        } else {
          this.threadPriority = priority;
        }
      }
      return this;
    }

    public ConfigBuilder setMemoryCacheSize(int size) {
      if (memoryCache == null) {
        memoryCache = new LruCache(size);
      } else {
        memoryCache.setMaxSize(size);
      }
      return this;
    }

    public ConfigBuilder setMemoryCache(BaseCache cache) {
      this.memoryCache = cache;
      return this;
    }

    public ConfigBuilder setImageDownloader(ImageDownloader downloader) {
      this.downloader = downloader;
      return this;
    }

    public Configuration build() {
      defineEmptyDefaults();
      return new Configuration(this);
    }

    private void defineEmptyDefaults() {
      if (networkExecutor == null) {
        networkExecutor = ConfigFactory.initDefaultExecutor(threadPoolSize, threadPriority);
      } else {
        altNetworkExecutor = true;
      }

      if (cachedExecutor == null) {
        cachedExecutor = ConfigFactory.initDefaultExecutor(threadPoolSize, threadPriority);
      } else {
        altCachedExecutor = true;
      }

      if (memoryCache == null) {
        memoryCache = ConfigFactory.initDefaultLruCache(context, -1);
      } else {
        altMemoryCache = true;
      }

      if (downloader == null) {
        downloader = new ImageDownloader();
      }
    }
  }
}
