package com.festivr.image;

import android.graphics.Bitmap;
import com.festivr.factory.ConfigFactory;
import com.festivr.task.HandleImageTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import timber.log.Timber;

/**
 * This class manages the image loading and cache retrieving threads for every load request.
 */
public class ImageManager {
  final Configuration config;

  private Executor networkExecutor;
  private Executor cachedExecutor;
  private Executor taskDistributor;

  /**
   * Default constructor, and configuration must be provided.
   *
   * @param config A valid configuration that's been built.
   */
  public ImageManager(Configuration config) {
    if (config == null) {
      throw new NullPointerException("ImageManager can't be used without a valid Configuration!");
    }
    this.config = config;
    this.networkExecutor = config.networkExecutor;
    this.cachedExecutor = config.cachedExecutor;
    this.taskDistributor = ConfigFactory.initDefaultTaskDistributor();
  }

  public Configuration getConfiguration() {
    return this.config;
  }

  /**
   * Adds a new HandleImageTask which can either download or retrieve a cached image.
   *
   * @param task A new task.
   */
  void addTask(final HandleImageTask task) {
    Timber.d("Is taskDistributor shutdown? " + ((ExecutorService) taskDistributor).isShutdown());
    Timber.d(
        "Is taskDistributor terminated? " + ((ExecutorService) taskDistributor).isTerminated());

    taskDistributor.execute(new Runnable() {
      @Override public void run() {
        //First check for a cached bitmap.
        Bitmap bitmap = config.getMemoryCache().get(task.getKey().getMD5HashKey());
        //Check both executors for shutdown.
        checkForExecutorShutdown();
        if (bitmap == null || bitmap.isRecycled()) {
          //Add a network task when a bitmap is not found, or the found bitmap has been recycled.
          Timber.d("Adding a network Task!!!");
          networkExecutor.execute(task);
        } else {
          Timber.d("Adding a cached Task!!!");
          //If the bitmap was returned and is not recycled.
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
