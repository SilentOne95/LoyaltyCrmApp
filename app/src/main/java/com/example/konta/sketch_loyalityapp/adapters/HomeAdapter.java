package com.example.konta.sketch_loyalityapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.modelClasses.Item;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<Item> listOfItems;

    public HomeAdapter(ArrayList<Item> items) { listOfItems = items; }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_item_home, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleView, descriptionText;
        private Button button;

        ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.grid_item_image);
            titleView = view.findViewById(R.id.grid_item_title_text);
            descriptionText = view.findViewById(R.id.grid_item_content_description);
            button = view.findViewById(R.id.grid_item_view_details_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Item currentItem = listOfItems.get(position);

        holder.imageView.setImageDrawable(currentItem.getBitmapDrawable());
        holder.titleView.setText(currentItem.getItemTitle());
        holder.descriptionText.setVisibility(View.GONE);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
