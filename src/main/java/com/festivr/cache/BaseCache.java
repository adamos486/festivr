package com.festivr.cache;

import android.graphics.Bitmap;
import com.festivr.url.UrlKeyCombo;

public interface BaseCache {
  Bitmap put(UrlKeyCombo key, Bitmap bitmap);
  Bitmap get(UrlKeyCombo key);
  Bitmap remove(UrlKeyCombo key);
  void clearCache();
  void setMaxSize(int size);
}
