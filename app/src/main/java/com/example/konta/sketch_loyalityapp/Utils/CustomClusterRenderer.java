package com.example.konta.sketch_loyalityapp.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.example.konta.sketch_loyalityapp.ModelClasses.ItemLocation;
import com.example.konta.sketch_loyalityapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<ItemLocation> {

     private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ItemLocation> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(ItemLocation item, MarkerOptions markerOptions) {
        // Set custom color of markers
        String string = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorAccent));
        final BitmapDescriptor markerDescriptor = CustomBitmapDescriptorFactory.fromColorString(string);

        markerOptions.icon(markerDescriptor).snippet(item.getTitle());
    }
}

// Custom helper class to get demanded color string and pass to bitmap
final class CustomBitmapDescriptorFactory {
    private static float[] getHsvFromColor(String colorString) {
        float[] hsv = new float[3];
        int _color = Color.parseColor(colorString);
        Color.colorToHSV(_color, hsv);
        return hsv;
    }

    static BitmapDescriptor fromColorString(String colorString) {
        return BitmapDescriptorFactory.defaultMarker(CustomBitmapDescriptorFactory.getHsvFromColor(colorString)[0]);
    }
}