package com.example.konta.sketch_loyalityapp.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.konta.sketch_loyalityapp.Utils.GridItemAdapter;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.Utils.Item;

import java.util.ArrayList;

public class CouponsFragment extends Fragment {

    static final ArrayList<Item> itemList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

        for (int i = 0; i < 10; i++) {
            itemList.add(new Item("Product Name", R.drawable.product_image));
        }
        GridItemAdapter adapter = new GridItemAdapter(getActivity(), itemList);
        final GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Coupons");
    }
}
