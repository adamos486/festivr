package com.festivr.cache;

import android.graphics.Bitmap;
import com.festivr.url.UrlKeyCombo;

public interface BaseCache {
  Bitmap put(String key, Bitmap bitmap);
  Bitmap get(String key);
  Bitmap remove(String key);
  void clearCache();
  void evict();
  void setMaxMemSize(int size);
  void setMaxPoolSize(int size);
  void setMemoryMode(float mode);
}
