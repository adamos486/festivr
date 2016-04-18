package com.festivr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.festivr.R;
import java.util.List;

public class ChooseExperienceAdapter extends BaseAdapter {
  public static final int PORTER_ROBINSON_INDEX = 0;
  public static final int BEYONCE_INDEX = 1;
  public static final int BEATLES_INDEX = 2;
  public static final int BLACK_KEYS_INDEX = 3;
  public static final int COACHELLA_INDEX = 4;
  public static final int EVERYTHING_INDEX = 5;

  private List<String> options;

  public ChooseExperienceAdapter(List<String> options) {
    this.options = options;
  }

  @Override public int getCount() {
    return options.size();
  }

  @Override public String getItem(int position) {
    return options.get(position);
  }

  /**
   * Item id will just be the position since there are so few objects.
   * @param position A position in the adapter.
   * @return long of the position.
   */
  @Override public long getItemId(int position) {
    return position;
  }

  /**
   * Uses the {@link ViewHolder} paradigm and stores a label {@link TextView} and a {@link
   * ImageView}
   * in order to store them statically and better manage the view elements upon recycling.
   *
   * @param position The position in the data backing list.
   * @param convertView The view that will be populated.
   * @param parent The parent view object (in this case a {@link ListView})
   * @return Typically the convertView object is returned.
   */
  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    LayoutInflater layoutInflater =
        (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null) {
      //convertView is null so inflate a new one.
      convertView = layoutInflater.inflate(R.layout.choose_experience_item, parent, false);
      holder = new ViewHolder();
      holder.optionLabel = (TextView) convertView.findViewById(R.id.option_label);
      holder.cover = (ImageView) convertView.findViewById(R.id.cover_slot);

      convertView.setTag(holder);
    } else {
      //convertView was not null so grab the viewHolder from the view tag.
      holder = (ViewHolder) convertView.getTag();
    }

    //Get the label from the array.
    String label = getItem(position);
    //set the label on the viewHolder.textView
    holder.optionLabel.setText(label);
    //Again, there are a finite number of choices, so hardcoded indexes are a cheap
    //way to offer unique selections.
    switch (position) {
      case PORTER_ROBINSON_INDEX:
        holder.cover.setImageResource(R.drawable.porter_cover);
        break;
      case BEYONCE_INDEX:
        holder.cover.setImageResource(R.drawable.beyonce_cover);
        //Cosmetic padding applied to center her face in the cover.
        holder.cover.setPadding(0, 300, 0, 0);
        break;
      case BEATLES_INDEX:
        holder.cover.setImageResource(R.drawable.beatles_cover);
        break;
      case BLACK_KEYS_INDEX:
        holder.cover.setImageResource(R.drawable.black_keys_cover);
        //Cosmetic padding applied to center The Black Keys sign in the cover
        holder.cover.setPadding(0, 330, 0, 0);
        break;
      case COACHELLA_INDEX:
        holder.cover.setImageResource(R.drawable.coachella_cover);
        break;
      case EVERYTHING_INDEX:
        holder.cover.setImageResource(R.drawable.everything_cover);
        //Remove all padding to better center the cover.
        holder.cover.setPadding(0, 0, 0, 0);
        break;
    }
    return convertView;
  }

  private static class ViewHolder {
    TextView optionLabel;
    ImageView cover;
  }
}
