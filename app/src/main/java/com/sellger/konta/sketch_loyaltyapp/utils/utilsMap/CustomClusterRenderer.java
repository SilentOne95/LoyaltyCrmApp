package com.sellger.konta.sketch_loyaltyapp.utils.utilsMap;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMarker;

public class CustomClusterRenderer extends DefaultClusterRenderer<HelperMarker> {

     private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<HelperMarker> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected int getColor(int clusterSize) {
        return ContextCompat.getColor(mContext, R.color.colorAccent);
    }

    @Override
    protected void onBeforeClusterItemRendered(HelperMarker itemLocation, MarkerOptions markerOptions) {
        // Set custom color of markers
        String string = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorAccentDark));
        final BitmapDescriptor markerDescriptorCustom = CustomBitmapDescriptorFactory.fromColorString(string);

        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        markerOptions.icon(markerDescriptorCustom).snippet(itemLocation.getTitle());
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