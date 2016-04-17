package com.festivr.image;

import com.festivr.factory.ConfigFactory;
import java.util.concurrent.Executor;

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


}
