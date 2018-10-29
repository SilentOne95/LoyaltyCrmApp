package com.example.konta.sketch_loyalityapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridItemAdapter extends ArrayAdapter<Item> {

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file.
     * The list is the data we want to populate into the list.
     * @param context of the app
     * @param gridItem A list of objects to display in a list
     */
    public GridItemAdapter(Activity context, ArrayList<Item> gridItem) {
        // Initialize the ArrayAdapter's internal storage for the context
        super(context, 0, gridItem);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView)
     *
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_list_item, parent, false);
        }

        // Get the {@link GridViewItem} object located at this position in the list
        Item currentItem = getItem(position);

        // Find the TextView in the grid_list_item.xml layout with the ID version_name
        TextView itemTitle = listItemView.findViewById(R.id.grid_item_text);
        // Get the version name from the current Item object and set this
        // text on the name TextView
        if (currentItem != null) {
            itemTitle.setText(currentItem.getItemTitle());
        }

        // Find the ImageView in the grid_list_item.xml layout with the ID version_number
        ImageView imageResource = listItemView.findViewById(R.id.grid_item_image);
        // Get the version number from the current Item object and set this
        // resource on the ImageView
        if (currentItem != null) {
            imageResource.setImageResource(currentItem.getResourceId());
        }

        // Return the whole list item layout (containing TextView and ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
