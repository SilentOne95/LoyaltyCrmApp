package com.sellger.konta.sketch_loyaltyapp.ui.myAccount;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_HEIGHT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_WIDTH;

public class MyAccountFragment extends BaseFragment {

    static final String BARCODE_DATA = "12345678901";

    private Bitmap bitmap = null;
    private ImageView outputImage;
    private TextView textView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_account;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Moje konto");

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up views
        textView.setVisibility(View.GONE);
        textView.setText(BARCODE_DATA);

        try {
            bitmap = encodeAsBitmap(BARCODE_DATA);
            outputImage.setImageBitmap(bitmap);
            textView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        textView = rootView.findViewById(R.id.my_account_code_text);
        outputImage = rootView.findViewById(R.id.my_account_barcode_image);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem accountItem = menu.findItem(R.id.main_menu_my_account);
        searchItem.setVisible(false);
        accountItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private Bitmap encodeAsBitmap(String contents) throws WriterException {
        if (contents == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;

        try {
            result = writer.encode(contents, BarcodeFormat.UPC_A, BARCODE_WIDTH, BARCODE_HEIGHT, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
