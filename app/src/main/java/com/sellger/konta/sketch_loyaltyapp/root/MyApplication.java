package com.sellger.konta.sketch_loyaltyapp.root;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
        mContext = context;
    }
}
