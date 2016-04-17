package com.festivr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.festivr.R;
import com.festivr.adapter.ChooseExperienceAdapter;
import com.festivr.image.Configuration;
import com.festivr.image.Images;
import com.festivr.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

  private ListView artistOptions;
  private ChooseExperienceAdapter adapter;
  private Handler handler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.deckard);

    if (getActionBar() != null) {
      getActionBar().setTitle(R.string.artists_title);
    }

    artistOptions = (ListView) findViewById(R.id.artist_options);
    artistOptions.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    adapter = new ChooseExperienceAdapter(
        Arrays.asList(getResources().getStringArray(R.array.artist_names)));
    artistOptions.setAdapter(adapter);
    artistOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> imageUrls = new ArrayList<String>();
        String title = "";
        switch(position) {
          case ChooseExperienceAdapter.PORTER_ROBINSON_INDEX:
            imageUrls = Constants.getPorterRobinson();
            title = getString(R.string.porter_robinson);
            break;
          case ChooseExperienceAdapter.BEYONCE_INDEX:
            imageUrls = Constants.getBeyonce();
            title = getString(R.string.beyonce);
            break;
          case ChooseExperienceAdapter.BEATLES_INDEX:
            imageUrls = Constants.getTheBeatles();
            title = getString(R.string.the_beatles);
            break;
          case ChooseExperienceAdapter.BLACK_KEYS_INDEX:
            imageUrls = Constants.getTheBlackKeys();
            title = getString(R.string.the_black_keys);
            break;
          case ChooseExperienceAdapter.COACHELLA_INDEX:
            imageUrls = Constants.getCoachellaDance();
            title = getString(R.string.coachella);
            break;
          case ChooseExperienceAdapter.EVERYTHING_INDEX:
            imageUrls = Constants.getHugeList();
            title = getString(R.string.everything);
            break;

        }

        Intent experienceIntent = new Intent(MainActivity.this, ExperienceImagesActivity.class);
        experienceIntent.putExtra("title", title);
        experienceIntent.putStringArrayListExtra("imageUrls", imageUrls);
        startActivity(experienceIntent);
        overridePendingTransition(R.anim.activity_slide_right_in,
            R.anim.activity_slide_left_out);
      }
    });

    Images.getSingleton().setup(new Configuration.Builder(this).build());
  }

  //private void runThroughImages() {
  //  List<String> list = Constants.getHugeList();
  //  for (int i = 0; i < list.size(); i++) {
  //    final int index = i;
  //    final String s = list.get(i);
  //    handler.postDelayed(new Runnable() {
  //      @Override public void run() {
  //        Timber.d("Index: " + index + "Trying to load " + s);
  //        Images.getSingleton().load(new UrlKeyCombo(s), imageView, new ImageLoadingListener() {
  //          @Override public void loadingStarted(UrlKeyCombo combo, ImageViewWrapper view) {
  //
  //          }
  //
  //          @Override
  //          public void loadingFailed(UrlKeyCombo combo, ImageViewWrapper view, String error) {
  //
  //          }
  //
  //          @Override public void loadingComplete(UrlKeyCombo combo, final ImageViewWrapper view,
  //              final Bitmap bitmap) {
  //            runOnUiThread(new Runnable() {
  //              @Override public void run() {
  //                Timber.d("setting " + index + " in view");
  //                view.setImageBitmap(bitmap);
  //              }
  //            });
  //          }
  //
  //          @Override public void loadingCancelled(UrlKeyCombo combo, ImageViewWrapper view) {
  //
  //          }
  //        });
  //      }
  //    }, (50 * i));
  //  }
  //}
}