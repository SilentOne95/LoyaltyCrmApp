package com.example.konta.sketch_loyalityapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.modelClasses.Item;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter<Item> {

    private boolean mShowDescription;

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file.
     * The list is the data we want to populate into the list.
     * @param context of the app
     * @param gridItem A list of objects to display in a list
     */
    public HomeAdapter(@NonNull Activity context, @NonNull ArrayList<Item> gridItem, boolean showDescription) {
        // Initialize the ArrayAdapter's internal storage for the context
        super(context, 0, gridItem);
        mShowDescription = showDescription;
    }

    /**
     * Provides a view for an AdapterView (GridView)
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        View listItemView = convertView;

        class ViewHolder {
            private ImageView imageView;
            private TextView titleView, descriptionText;
            private Button button;
        }

        ViewHolder holder;

        // Check if there is any existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_list_item_home, parent, false);

            holder = new ViewHolder();
            holder.imageView = listItemView.findViewById(R.id.grid_item_image);
            holder.titleView = listItemView.findViewById(R.id.grid_item_title_text);
            holder.descriptionText = listItemView.findViewById(R.id.grid_item_content_description);
            holder.button = listItemView.findViewById(R.id.grid_item_view_details_button);

            // Depending on view, decide if those views should be shown or not
            if (!mShowDescription) {
                holder.descriptionText.setVisibility(View.GONE);
            }

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link GridViewItem} object located at this position in the list
        Item currentItem = getItem(position);

        holder.imageView.setImageDrawable(currentItem.getBitmapDrawable());
        holder.titleView.setText(currentItem.getItemTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return listItemView;
    }
}