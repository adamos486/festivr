package com.festivr.image;

import android.graphics.Bitmap;
import com.festivr.factory.ConfigFactory;
import com.festivr.task.HandleImageTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import timber.log.Timber;

public class ImageManager {
  final Configuration config;

  private Executor networkExecutor;
  private Executor cachedExecutor;
  private Executor taskDistributor;

  public ImageManager(Configuration config) {
    this.config = config;
    this.networkExecutor = config.networkExecutor;
    this.cachedExecutor = config.cachedExecutor;
    this.taskDistributor = ConfigFactory.initDefaultTaskDistributor();
  }

  public Configuration getConfiguration() {
    return this.config;
  }

  void addTask(final HandleImageTask task) {
    Timber.d("Is taskDistributor shutdown? " +((ExecutorService) taskDistributor).isShutdown());
    Timber.d("Is taskDistributor terminated? " +((ExecutorService) taskDistributor).isTerminated());

    taskDistributor.execute(new Runnable() {
      @Override public void run() {
        Bitmap bitmap = config.getMemoryCache().get(task.getKey().getMD5HashKey());
        checkForExecutorShutdown();
        if (bitmap == null || bitmap.isRecycled()) {
          Timber.d("Adding a network Task!!!");
          networkExecutor.execute(task);
        } else {
          Timber.d("Adding a cached Task!!!");
          cachedExecutor.execute(task);
        }
      }
    });
  }

  private void checkForExecutorShutdown() {
    if (((ExecutorService) networkExecutor).isShutdown()) {
      networkExecutor =
          ConfigFactory.initDefaultExecutor(config.threadPoolSize, config.threadPriority);
    }

    if (((ExecutorService) cachedExecutor).isShutdown()) {
      cachedExecutor =
          ConfigFactory.initDefaultExecutor(config.threadPoolSize, config.threadPriority);
    }
  }
}
