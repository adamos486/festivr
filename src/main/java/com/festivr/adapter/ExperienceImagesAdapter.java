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

/**
 * Main ImageLoading adapter, should load big beautiful images into the ListView items.
 * Uses {@link Images} - a custom image loader, to handle all image loading.
 */
public class ExperienceImagesAdapter extends BaseAdapter {
  private List<String> urls;
  private WeakReference<Activity> activityRef;

  /**
   * Standard Adapter Constructor
   *
   * @param urls A list of URLs for images which will be loaded.
   * @param activity An Activity context used to update UI elements asynchronously.
   */
  public ExperienceImagesAdapter(List<String> urls, Activity activity) {
    this.urls = urls;
    this.activityRef = new WeakReference<>(activity);
  }

  /**
   * @return the size of url list.
   */
  @Override public int getCount() {
    return urls.size();
  }

  /**
   * Gets a url from the url list.
   *
   * @param position of data set.
   * @return The String object of the url to be loaded.
   */
  @Override public String getItem(int position) {
    return urls.get(position);
  }

  /**
   * Simple position returning the position.
   */
  @Override public long getItemId(int position) {
    return position;
  }

  /**
   * Populate viewholder and kick off image loading for each cell.
   *
   * @param position Position in ListView and Data set.
   * @param convertView The view to be populated.
   * @param parent The parent view to be loaded into, i.e. ListView
   * @return Typically returns the updated or initial convertView.
   */
  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    LayoutInflater layoutInflater =
        (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    convertView = layoutInflater.inflate(R.layout.experience_image_item, parent, false);
    //Init a ViewHolder.
    holder = new ViewHolder();
    holder.imageView = (ImageView) convertView.findViewById(R.id.image);
    //Without a placeholder image being supported by this image loader,
    //programmatically set this background color instead.
    holder.imageView.setBackgroundColor(
        parent.getContext().getResources().getColor(android.R.color.black));
    //Set the viewHolder on the view, even though we aren't using recycling to it's
    //fullest yet.
    convertView.setTag(holder);

    String url = getItem(position);
    //RefreshDrawableState may just be superstition, but this forces imageView to refresh
    //and display the placeholder color (black).
    holder.imageView.refreshDrawableState();

    //Invoke the custom image loader.
    Images.getSingleton().load(new UrlKeyCombo(url), holder.imageView, new ImageLoadingListener() {
      //The loadingStarted callback could be used to display a spinner.
      @Override public void loadingStarted(UrlKeyCombo combo, ImageViewWrapper view) {

      }

      //The loadingFailed callback could be used to display a failed placeholder.
      @Override public void loadingFailed(UrlKeyCombo combo, ImageViewWrapper view, String error) {

      }

      //The loadingComplete callback is where all the work happens, grabbing the Activity
      //reference, load the bitmap into the specific ImageView on the UI Thread.
      @Override public void loadingComplete(UrlKeyCombo combo, final ImageViewWrapper view,
          final Bitmap bitmap) {
        //Make sure reference hasn't been cleared
        if (activityRef.get() != null) {
          activityRef.get().runOnUiThread(new Runnable() {
            @Override public void run() {
              view.setImageBitmap(bitmap);
            }
          });
        } else {
          //TODO: Callback to activity through EventBus which will access the activity context directly.
        }
      }

      //The loadingCancelled callback would be good in the future if detecting a scrolled past imageview.
      //i.e. if ImageView is no longer on screen, clear the request and cancel the load.
      @Override public void loadingCancelled(UrlKeyCombo combo, ImageViewWrapper view) {

      }
    });

    return convertView;
  }

  /**
   * ViewHolder class, uses ViewHolder paradigm to more intelligently store refs to views.
   * This class just stores the {@link ImageView} used to load an image into.
   */
  private static class ViewHolder {
    ImageView imageView;
  }
}
