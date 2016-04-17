package com.festivr.factory;

import android.content.Context;
import com.festivr.cache.BaseCache;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigFactory {

  public static Executor initDefaultExecutor(int poolSize, int priority) {
    BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    return new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, queue,
        createCustomThreadFactory(priority, "festivr-images-"));
  }

  public static CustomThreadFactory createCustomThreadFactory(int priority, String prefix) {
    return new CustomThreadFactory(priority, prefix);
  }

  public static BaseCache createDefaultLruCache(Context context, int maxSize) {
    return null;
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
