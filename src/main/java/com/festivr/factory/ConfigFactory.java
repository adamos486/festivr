package com.festivr.factory;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.festivr.cache.BaseCache;
import com.festivr.cache.LruCache;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigFactory {

  public static Executor initDefaultExecutor(int poolSize, int priority) {
    BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(poolSize);
    return new ThreadPoolExecutor(3, poolSize, 0L, TimeUnit.MILLISECONDS, queue);
  }

  public static CustomThreadFactory initCustomThreadFactory(int priority, String prefix) {
    return new CustomThreadFactory(priority, prefix);
  }

  public static BaseCache initDefaultLruCache(Context context, int maxSize) {
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
    return new LruCache(maxSize);
  }

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
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private static int getLargeMemoryClass(ActivityManager am) {
    return am.getLargeMemoryClass();
  }

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
