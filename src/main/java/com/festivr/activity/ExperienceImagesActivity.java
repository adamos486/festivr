package com.festivr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.festivr.R;
import com.festivr.adapter.ExperienceImagesAdapter;
import java.util.List;

public class ExperienceImagesActivity extends Activity {

  private Bundle extras;
  private List<String> imageUrls;
  private ListView imageList;
  private ExperienceImagesAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_experience_images);

    imageList = (ListView) findViewById(R.id.image_list);

    if (getIntent().getExtras() != null) {
      extras = getIntent().getExtras();
      if (extras.containsKey("title")) {
        if (getActionBar() != null) {
          getActionBar().setTitle(extras.getString("title"));
        }
      }
      if (extras.containsKey("imageUrls")) {
        imageUrls = extras.getStringArrayList("imageUrls");
        adapter = new ExperienceImagesAdapter(imageUrls, this);
        imageList.setAdapter(adapter);
      } else {
        throw new IllegalStateException(
            "Can't use an ExperienceImagesActivity without a imageUrls list.");
      }
    }
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
  }
}
