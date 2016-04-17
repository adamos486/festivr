package com.festivr.listener;

import android.graphics.Bitmap;
import android.view.View;

public interface ImageLoadingListener {
  void loadingStarted(String url, View view);
  void loadingFailed(String url, View view, String error);
  void loadingComplete(String url, View view, Bitmap bitmap);
  void loadingCancelled(String url, View view);
}
