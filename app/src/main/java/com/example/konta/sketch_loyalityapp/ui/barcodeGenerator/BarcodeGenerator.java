package com.example.konta.sketch_loyalityapp.ui.barcodeGenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import static com.example.konta.sketch_loyalityapp.Constants.BARCODE_HEIGHT;
import static com.example.konta.sketch_loyalityapp.Constants.BARCODE_WIDTH;

public class BarcodeGenerator extends BaseFragment {

    static final String BARCODE_DATA = "12345678901";

    private Bitmap bitmap = null;
    private ImageView outputImage;
    private TextView textView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_barcode_generator;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Moje konto");

        setHasOptionsMenu(true);

        textView = rootView.findViewById(R.id.textView);
        textView.setVisibility(View.GONE);
        textView.setText(BARCODE_DATA);

        outputImage = rootView.findViewById(R.id.imageView);

        try {
            bitmap = encodeAsBitmap(BARCODE_DATA);
            outputImage.setImageBitmap(bitmap);
            textView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        searchItem.setVisible(false);

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
