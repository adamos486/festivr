package com.festivr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.festivr.R;
import com.festivr.adapter.ExperienceImagesAdapter;
import java.util.List;

public class ExperienceImagesActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_experience_images);

    ListView imageList = (ListView) findViewById(R.id.image_list);

    if (getIntent().getExtras() != null) {
      Bundle extras = getIntent().getExtras();
      //Make sure a title is contained
      if (extras.containsKey("title")) {
        //Make sure an actionBar is added to the hierarchy.
        if (getActionBar() != null) {
          //Set the screen title.
          getActionBar().setTitle(extras.getString("title"));
        }
      }
      //Make sure a list of imageUrls is provided.
      if (extras.containsKey("imageUrls")) {
        List<String> imageUrls = extras.getStringArrayList("imageUrls");
        ExperienceImagesAdapter adapter = new ExperienceImagesAdapter(imageUrls, this);
        imageList.setAdapter(adapter);
      } else {
        //If a list of imageUrls isn't provided, offensively throw an
        //IllegalStateException.
        throw new IllegalStateException(
            "Can't use an ExperienceImagesActivity without a imageUrls list.");
      }
    } else {
      throw new IllegalStateException(
          "Can't start a decent ExperienceImagesActivity without extras");
    }
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    //Override the transition with the reverse animations of entry.
    overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
  }
}
