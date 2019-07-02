package com.sellger.konta.sketch_loyaltyapp.ui.myAccount;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;

public interface MyAccountContract {

    interface View {

    }

    interface Presenter {

        Bitmap encodeAsBitmap(String contents) throws WriterException;
    }
}
