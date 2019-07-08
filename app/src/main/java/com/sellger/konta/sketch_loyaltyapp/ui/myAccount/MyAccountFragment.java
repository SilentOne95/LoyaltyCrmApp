package com.sellger.konta.sketch_loyaltyapp.ui.myAccount;

import android.graphics.Bitmap;

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
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.google.zxing.WriterException;

public class MyAccountFragment extends BaseFragment implements MyAccountContract.View {

    private MyAccountPresenter presenter;

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

        // Setting up presenter
        presenter = new MyAccountPresenter(this);

        try {
            bitmap = presenter.encodeAsBitmap(BARCODE_DATA);
            outputImage.setImageBitmap(bitmap);
            textView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        textView = rootView.findViewById(R.id.my_account_code_text);
        outputImage = rootView.findViewById(R.id.my_account_barcode_image);

        textView.setVisibility(View.GONE);
        textView.setText(BARCODE_DATA);
    }

    /**
     * Initialize the contents of the Activity's standard options menu and sets up items visibility.
     *
     * @param menu     in which you place items
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem accountItem = menu.findItem(R.id.main_menu_my_account);
        searchItem.setVisible(false);
        accountItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
