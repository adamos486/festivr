package com.festivr.factory;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.festivr.cache.LruCache;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is responsible for defining and providing sensible defaults for the Configuration
 * of the Images loader.
 */
public class ConfigFactory {

  /**
   * Defines a custom Thread Executor.
   *
   * @param poolSize The maximum thread pool size.
   * @param priority The thread priority of each new thread.
   * @return The {@link Executor} created by this method.
   */
  public static Executor initDefaultExecutor(int poolSize, int priority) {
    BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(poolSize);
    //TODO: Figure out what's wrong with my ThreadFactory and apply that here.
    return new ThreadPoolExecutor(3, poolSize, 0L, TimeUnit.MILLISECONDS, queue);
  }

  public static CustomThreadFactory initCustomThreadFactory(int priority, String prefix) {
    return new CustomThreadFactory(priority, prefix);
  }

  /**
   * Defines a default LruCache using the Android context to determine an appropriate amount of
   * memory to set as the maxMemSize.
   *
   * @param context The Android context.
   * @param maxSize A provided maximum size or -1 in order to fetch it from the system.
   * @return An instance of {@link LruCache}
   */
  public static LruCache initDefaultLruCache(Context context, int maxSize) {
    if (maxSize == -1) {
      //This is from UniversalImageLoader.
      //Smart handling of 1/8th memory of active.
      ActivityManager activityManager =
          (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      int memoryClass = activityManager.getMemoryClass();
      if (greaterThanApiHoneycomb() && isLargeHeap(context)) {
        memoryClass = getLargeMemoryClass(activityManager);
      }
      maxSize = 1024 * 1024 * memoryClass / 8;
    }
    return new LruCache(maxSize, 13, LruCache.MEDIUM_MEMORY);
  }

  /**
   * This defines a default TaskDistributor which can used cached threads instead of always creating
   * new ones.
   *
   * @return The {@link Executor} created by this.
   */
  public static Executor initDefaultTaskDistributor() {
    return Executors.newCachedThreadPool();
  }

  private static boolean greaterThanApiHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }

  //From UniversalImageLoader
  @TargetApi(Build.VERSION_CODES.HONEYCOMB) private static boolean isLargeHeap(Context context) {
    return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
  }

  //From UniversalImageLoader
  @TargetApi(Build.VERSION_CODES.HONEYCOMB) private static int getLargeMemoryClass(
      ActivityManager am) {
    return am.getLargeMemoryClass();
  }

  /**
   * A Custom Thread Factory which would have allowed me to label and more closely monitor my threads.
   * TODO: Fix this implementation, since it currently makes my Executors reject all tasks.
   */
  private static class CustomThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNum = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNum = new AtomicInteger(1);
    private final String prefix;
    private final int priority;

    CustomThreadFactory(int priority, String prefix) {
      this.priority = priority;
      group = Thread.currentThread().getThreadGroup();
      this.prefix = prefix + poolNum.getAndIncrement() + "--image-thread--";
    }

    @Override public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(group, runnable, prefix + threadNum.getAndIncrement(), 0);
      if (thread.isDaemon()) {
        thread.setDaemon(false);
      }
      thread.setPriority(priority);
      return null;
    }
  }
}
