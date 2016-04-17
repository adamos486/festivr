package com.festivr.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.festivr.R;
import com.festivr.image.Images;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.url.UrlKeyCombo;
import com.festivr.view.ImageViewWrapper;
import java.lang.ref.WeakReference;
import java.util.List;

public class ExperienceImagesAdapter extends BaseAdapter {
  private List<String> urls;
  private WeakReference<Activity> activityRef;

  public ExperienceImagesAdapter(List<String> urls, Activity activity) {
    this.urls = urls;
    this.activityRef = new WeakReference<Activity>(activity);
  }

  @Override public int getCount() {
    return urls.size();
  }

  @Override public String getItem(int position) {
    return urls.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    LayoutInflater layoutInflater =
        (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //if (convertView == null) {
    convertView = layoutInflater.inflate(R.layout.experience_image_item, parent, false);
    holder = new ViewHolder();
    holder.imageView = (ImageView) convertView.findViewById(R.id.image);
    holder.imageView.setBackgroundColor(
        parent.getContext().getResources().getColor(android.R.color.black));

    convertView.setTag(holder);
    //} else {
    //  holder = (ViewHolder) convertView.getTag();
    //}

    String url = getItem(position);
    holder.imageView.refreshDrawableState();
    Images.getSingleton().load(new UrlKeyCombo(url), holder.imageView, new ImageLoadingListener() {
      @Override public void loadingStarted(UrlKeyCombo combo, ImageViewWrapper view) {

      }

      @Override public void loadingFailed(UrlKeyCombo combo, ImageViewWrapper view, String error) {

      }

      @Override public void loadingComplete(UrlKeyCombo combo, final ImageViewWrapper view,
          final Bitmap bitmap) {
        if (activityRef.get() != null) {
          activityRef.get().runOnUiThread(new Runnable() {
            @Override public void run() {
              view.setImageBitmap(bitmap);
            }
          });
        }
      }

      @Override public void loadingCancelled(UrlKeyCombo combo, ImageViewWrapper view) {

      }
    });

    return convertView;
  }

  private static class ViewHolder {
    ImageView imageView;
  }
}
