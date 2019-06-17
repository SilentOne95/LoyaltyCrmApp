package com.sellger.konta.sketch_loyaltyapp.service.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateReceiver extends BroadcastReceiver {

    private ConnectivityReceiverListener mConnectivityReceiverListener;

    public NetworkStateReceiver(ConnectivityReceiverListener connectivityReceiverListener) {
        mConnectivityReceiverListener = connectivityReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mConnectivityReceiverListener.onNetworkConnectionChanged(isNetworkOnline(context));
    }

    private boolean isNetworkOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
