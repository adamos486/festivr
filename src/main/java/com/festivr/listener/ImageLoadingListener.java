package com.festivr.listener;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;

public interface ImageLoadingListener {
  void loadingStarted(UrlKeyCombo combo, ImageViewWrapper view);
  void loadingFailed(UrlKeyCombo combo, ImageViewWrapper view, String error);
  void loadingComplete(UrlKeyCombo combo, ImageViewWrapper view, Bitmap bitmap);
  void loadingCancelled(UrlKeyCombo combo, ImageViewWrapper view);
}
