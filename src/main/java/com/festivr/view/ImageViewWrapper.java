package com.festivr.view;

import android.graphics.Bitmap;
import android.widget.ImageView;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class ImageViewWrapper {
  protected Reference<ImageView> reference;

  public ImageViewWrapper(ImageView view) {
    if (view == null) {
      throw new NullPointerException("Can't construct an ImageViewWrapper without an ImageView.");
    }

    this.reference = new WeakReference<ImageView>(view);
  }

  public ImageView getWrappedView() {
    return reference.get();
  }

  public boolean isCollected() {
    return reference.get() == null;
  }

  public void setImageBitmap(Bitmap bitmap) {
    if (!isCollected()) {
      ImageView view = reference.get();
      view.setImageBitmap(bitmap);
    }
  }
}
