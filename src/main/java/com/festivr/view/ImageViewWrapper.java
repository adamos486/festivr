package com.festivr.view;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.festivr.utils.Constants;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a wrapped ImageView, so that each task can have a weak reference to an ImageView,
 * and memory can be cleared as needed.
 */
public class ImageViewWrapper {
  protected Reference<ImageView> reference;

  public ImageViewWrapper(ImageView view) {
    Constants.checkForNonNull(view, "Can't construct an ImageViewWrapper without an ImageView.");
    this.reference = new WeakReference<ImageView>(view);
  }

  public boolean isCollected() {
    return reference.get() == null;
  }

  public void setImageBitmap(Bitmap bitmap) {
    if (!isCollected()) {
      ImageView view = reference.get();
      view.setImageBitmap(bitmap);
    } else {
      //TODO: Figure something else out in the event that this reference gets cleared.
      //TODO: Perhaps tag the ImageView with the url or md5 key, and then use findByTag to retrieve.
    }
  }
}
