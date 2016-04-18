package com.festivr.image;

import android.content.Context;
import android.content.res.Resources;
import com.festivr.cache.BaseCache;
import com.festivr.cache.LruCache;
import com.festivr.downloader.ImageDownloader;
import com.festivr.factory.ConfigFactory;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Allows a Configuration to be built for {@link Images} object.
 */
public class Configuration {
  final Context context;
  final Resources resources;
  final int maxWidth;
  final int maxHeight;

  final Executor networkExecutor;
  final Executor cachedExecutor;

  final BaseCache memoryCache;
  final int threadPoolSize;
  final int threadPriority;

  final ImageDownloader downloader;

  /**
   * Private constructor so the only way to get a {@link Configuration} object is
   * to use the builder.
   *
   * @param builder A builder that supplied in the {@link Builder#build()} method.
   */
  private Configuration(Builder builder) {
    this.context = builder.context;
    this.resources = builder.context.getResources();
    this.maxWidth = builder.maxImageWidth;
    this.maxHeight = builder.maxImageHeight;
    this.networkExecutor = builder.networkExecutor;
    this.cachedExecutor = builder.cachedExecutor;
    this.memoryCache = builder.memoryCache;
    this.threadPoolSize = builder.threadPoolSize;
    this.threadPriority = builder.threadPriority;
    this.downloader = builder.downloader;
  }

  public BaseCache getMemoryCache() {
    return this.memoryCache;
  }

  public ImageDownloader getDownloader() {
    return this.downloader;
  }

  public static class Builder {
    public static final int THREAD_POOL_SIZE = 5;
    public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;
    private Context context;

    private int maxImageWidth = -1;
    private int maxImageHeight = -1;

    private Executor networkExecutor = null;
    private Executor cachedExecutor = null;
    private int threadPoolSize = THREAD_POOL_SIZE;
    private int threadPriority = DEFAULT_THREAD_PRIORITY;

    private int maxMemSize = -1;
    private int maxPoolSize = -1;

    private BaseCache memoryCache = null;
    private ImageDownloader downloader = null;

    public Builder(Context context) {
      this.context = context.getApplicationContext();
    }

    /**
     * Allows a customized thread executor for network calls.
     *
     * @param executor The user's custom network {@link Executor}
     * @return The current {@link Configuration.Builder}.
     */
    public Builder setNetworkExecutor(Executor executor) {
      this.networkExecutor = executor;
      return this;
    }

    /**
     * Allows a customized thread executor for purely cached responses,
     * allowing for fruther optimization.
     *
     * @param executor The user's custom cached {@link Executor}
     * @return The current {@link Configuration.Builder}
     */
    public Builder setCachedExecutor(Executor executor) {
      this.cachedExecutor = executor;
      return this;
    }

    /**
     * Allows customization of thread pool size. If not supplied,
     * default is 5
     *
     * @param size int value of maximum thread pool size desired.
     * @return The current {@Link Configuration.Builder}
     */
    public Builder setThreadPoolSize(int size) {
      this.threadPoolSize = size;
      return this;
    }

    /**
     * Set a thread priority with any number. If the priority is less than
     * the {@link Thread} minimum, or more than the {@link Thread} maximum,
     * dynamically substitute the constant min or max.
     *
     * @param priority int of the priority to define in {@link ThreadFactory}
     * @return The current {@link Configuration.Builder}
     */
    public Builder setThreadPriority(int priority) {
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

    /**
     * Set a max memory cache size in bytes. Currently only configured for
     * {@link LruCache} and not {@link BaseCache} but could be adapted in the future.
     *
     * @param size integer value of maximum bytes.
     * @return The current {@link Configuration.Builder}
     */
    public Builder setMemoryCacheSize(int size) {
      this.maxMemSize = size;
      //If the memoryCache object is null.
      if (memoryCache == null) {
        //Define a new memoryCache with the defined size, using the
        //max Mem size constructor.
        memoryCache = new LruCache(size, false);
      } else {
        //memoryCache is not null, this will set the max on the class and
        //call evict();
        memoryCache.setMaxMemSize(size);
      }
      return this;
    }

    /**
     * Set a max memory cache pool size. This will define the upper limit on the amount
     * of objects allowed in the memory cache. Currently only configured for {@link LruCache}.
     *
     * @param size integer value of maximum pool size (maximum # of objects).
     * @return The current {@link Configuration.Builder}
     */
    public Builder setMemoryCachePool(int size) {
      this.maxPoolSize = size;
      //If memoryCache object is null.
      if (memoryCache == null) {
        //Define a new memoryCache with the isPool flag set to true.
        memoryCache = new LruCache(size, true);
      } else {
        //memoryCache is not null, so set the poolSize on the class, which
        //calls evict();
        memoryCache.setMaxPoolSize(size);
      }
      return this;
    }

    /**
     * Set a memoryMode in a float value which will be a multiplier against the maxMemSize.
     * Currently only configured for {@link LruCache}.
     *
     * @param context {@link Context} object will be used to measure instance memory usage
     * during initialization.
     * @param mode the float mode using the constants defined in {@link LruCache}.
     * @return The current {@link Configuration.Builder}.
     */
    public Builder setMemoryCacheMode(Context context, float mode) {
      if (memoryCache == null) {
        //memoryCache was null, using ConfigFactory to define a Default LruCache, since
        //there is no maxSize or maxPool. If either of these had been defined before
        //this point then memoryCache would not be null.
        memoryCache = ConfigFactory.initDefaultLruCache(context, -1);
        memoryCache.setMemoryMode(mode);
      } else {
        //memoryCache has been predefined, setting memoryMode which resets maxMemSize
        //of the cache, and triggers evict();
        memoryCache.setMemoryMode(mode);
      }
      return this;
    }

    /**
     * Allows a custom {@link BaseCache} object which implements the BaseCache interface.
     *
     * @param cache a {@link BaseCache} object or any implementor of BaseCache.
     * @return The current {@link Configuration.Builder}.
     */
    public Builder setMemoryCache(BaseCache cache) {
      this.memoryCache = cache;
      return this;
    }

    /**
     * Supply a custom {@link ImageDownloader}, currently with no generic parent class/interface.
     *
     * @param downloader The ImageDownloader;
     * @return The current {@link Configuration.Builder}.
     */
    public Builder setImageDownloader(ImageDownloader downloader) {
      this.downloader = downloader;
      return this;
    }

    /**
     * Standard Builder.build() method. Invokes {@link #defineEmptyDefaults()}, which fills in
     * all missing data with sensible defaults.
     *
     * @return A new {@link Configuration} with all of the settings from the {@link
     * Configuration.Builder}
     */
    public Configuration build() {
      defineEmptyDefaults();
      return new Configuration(this);
    }

    /**
     * For all of the empty fields, uses {@link ConfigFactory} to define sensible,
     * pre-defined defaults.
     *
     * When network or cached Executor are null, uses {@link ConfigFactory#initDefaultExecutor(int, int)}.
     *
     * When memory cache is null, use {@link ConfigFactory#initDefaultLruCache(Context, int)}
     */
    private void defineEmptyDefaults() {
      if (networkExecutor == null) {
        networkExecutor = ConfigFactory.initDefaultExecutor(threadPoolSize, threadPriority);
      }

      if (cachedExecutor == null) {
        cachedExecutor = ConfigFactory.initDefaultExecutor(threadPoolSize, threadPriority);
      }

      if (memoryCache == null) {
        memoryCache = ConfigFactory.initDefaultLruCache(context, -1);
      }

      if (downloader == null) {
        downloader = new ImageDownloader(memoryCache);
      }
    }
  }
}
