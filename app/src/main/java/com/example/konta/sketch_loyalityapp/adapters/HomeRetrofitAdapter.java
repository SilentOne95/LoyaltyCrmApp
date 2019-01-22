package com.example.konta.sketch_loyalityapp.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

import java.util.List;

public class HomeRetrofitAdapter extends RecyclerView.Adapter<HomeRetrofitAdapter.ViewHolder> {

    private List<MenuComponent> listOfItems;
    private RecyclerItemClickListener.HomeRetrofitClickListener homeClickListener;

    public HomeRetrofitAdapter(List<MenuComponent> items,
                               RecyclerItemClickListener.HomeRetrofitClickListener clickListener) {
        listOfItems = items;
        homeClickListener = clickListener;
    }

    @NonNull
    @Override
    public HomeRetrofitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MenuComponent currentItem = listOfItems.get(position);

        // Temporary solution - testing library
//        if (!(currentItem.getImage() == null || currentItem.getImage().isEmpty())) {
//            Picasso.get().load(BASE_URL_IMAGES + currentItem.getImage()).into(holder.imageView);
//        }

        holder.titleView.setText(currentItem.getComponentTitle());
        holder.descriptionText.setVisibility(View.GONE);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeClickListener.onItemHomeClick(listOfItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
