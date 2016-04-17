package com.festivr.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.festivr.R;
import com.festivr.image.Configuration;
import com.festivr.image.Images;
import com.festivr.listener.ImageLoadingListener;
import com.festivr.url.UrlKeyCombo;
import com.festivr.utils.Constants;
import com.festivr.view.ImageViewWrapper;
import java.util.List;
import timber.log.Timber;

public class MainActivity extends Activity {

  private ImageView imageView;
  private Handler handler = new Handler();
  private long timeToDelay;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.deckard);

    imageView = (ImageView) findViewById(R.id.test_image);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        runThroughImages();
      }
    });

    Images.getSingleton().setup(new Configuration.Builder(this).build());
  }

  private void runThroughImages() {
    List<String> list = Constants.getHugeList();
    for (int i = 0; i < list.size(); i++) {
      final int index = i;
      final String s = list.get(i);
      handler.postDelayed(new Runnable() {
        @Override public void run() {
          Timber.d("Index: " + index + "Trying to load " + s);
          Images.getSingleton().load(new UrlKeyCombo(s), imageView, new ImageLoadingListener() {
            @Override public void loadingStarted(UrlKeyCombo combo, ImageViewWrapper view) {

            }

            @Override
            public void loadingFailed(UrlKeyCombo combo, ImageViewWrapper view, String error) {

            }

            @Override public void loadingComplete(UrlKeyCombo combo, final ImageViewWrapper view,
                final Bitmap bitmap) {
              runOnUiThread(new Runnable() {
                @Override public void run() {
                  Timber.d("setting " + index + " in view");
                  view.setImageBitmap(bitmap);
                }
              });
            }

            @Override public void loadingCancelled(UrlKeyCombo combo, ImageViewWrapper view) {

            }
          });
        }
      }, (50 * i));
    }
  }
}