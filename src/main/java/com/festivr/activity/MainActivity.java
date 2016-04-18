package com.festivr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.festivr.R;
import com.festivr.adapter.ChooseExperienceAdapter;
import com.festivr.cache.LruCache;
import com.festivr.image.Configuration;
import com.festivr.image.Images;
import com.festivr.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

  private ListView artistOptions;
  private ChooseExperienceAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.deckard);

    //Check if actionBar is defined.
    if (getActionBar() != null) {
      //Set the title for this screen.
      getActionBar().setTitle(R.string.artists_title);
    }

    artistOptions = (ListView) findViewById(R.id.artist_options);
    //Blocking descendant focusability to always allow clicks to get through
    //to an OnItemClickListener.
    artistOptions.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

    //Define the String array which provides the options to choose.
    adapter = new ChooseExperienceAdapter(
        Arrays.asList(getResources().getStringArray(R.array.artist_names)));

    artistOptions.setAdapter(adapter);
    artistOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Since there are a finite number of controlled options on this screen,
        //we can display and process them with Constants defined in the adapter.
        ArrayList<String> imageUrls = new ArrayList<>();
        String title = "";
        switch (position) {
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

        //ExperienceImagesActivity should be as generic as possible, therefore,
        //passing the title and the url list as extras, allows less code in the onCreate.
        Intent experienceIntent = new Intent(MainActivity.this, ExperienceImagesActivity.class);
        experienceIntent.putExtra("title", title);
        experienceIntent.putStringArrayListExtra("imageUrls", imageUrls);
        startActivity(experienceIntent);
        overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
      }
    });

    Images.getSingleton().setup(new Configuration.Builder(this).build());

    //ALTERNATIVE
    //Images.getSingleton()
    //    .setup(new Configuration.Builder(this).setMemoryCacheSize(52428800)
    //        .setMemoryCachePool(13)
    //        .setMemoryCacheMode(this, LruCache.MEDIUM_MEMORY)
    //        .build());

    //ANOTHER ALTERNATIVE
    //Images.getSingleton()
    //    .setup(new Configuration.Builder(this).setMemoryCache(new LruCache(52428800, 13)).build());
  }
}