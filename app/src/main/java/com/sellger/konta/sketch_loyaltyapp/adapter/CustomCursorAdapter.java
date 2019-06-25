package com.sellger.konta.sketch_loyaltyapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.sellger.konta.sketch_loyaltyapp.R;

public class CustomCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    public CustomCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.searchbar_map_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.searchbar_map_item_text);
        textView.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
    }
}
