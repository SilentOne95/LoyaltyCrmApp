package com.example.konta.sketch_loyalityapp.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ModelClasses.Item;

import java.util.ArrayList;

public class GridItemAdapter extends ArrayAdapter<Item> {

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file.
     * The list is the data we want to populate into the list.
     * @param context of the app
     * @param gridItem A list of objects to display in a list
     */
    public GridItemAdapter(@NonNull Activity context, @NonNull ArrayList<Item> gridItem) {
        // Initialize the ArrayAdapter's internal storage for the context
        super(context, 0, gridItem);
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
            private TextView titleView;
            private Button button;
        }

        ViewHolder holder;

        // Check if there is any existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_list_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = listItemView.findViewById(R.id.grid_item_image);
            holder.titleView = listItemView.findViewById(R.id.grid_item_text);
            holder.button = listItemView.findViewById(R.id.view_details_button);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link GridViewItem} object located at this position in the list
        Item currentItem = getItem(position);

        holder.imageView.setImageResource(currentItem.getResourceId());
        holder.titleView.setText(currentItem.getItemTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return listItemView;
    }
}
