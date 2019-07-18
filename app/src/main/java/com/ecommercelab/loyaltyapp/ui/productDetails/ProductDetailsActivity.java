package com.ecommercelab.loyaltyapp.ui.productDetails;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommercelab.loyaltyapp.base.activity.BaseActivity;
import com.ecommercelab.loyaltyapp.R;
import com.ecommercelab.loyaltyapp.data.Injection;
import com.ecommercelab.loyaltyapp.data.entity.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static com.ecommercelab.loyaltyapp.Constants.DEFAULT_STRING;
import static com.ecommercelab.loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private ProductDetailsPresenter presenter;

    private ImageView mProductImage;
    private TextView mProductTitle, mProductPrice, mProductDescription;
    private ProgressBar mProgressBar;
    private View mLayoutContainer;

    private int productId;

    @Override
    protected int getLayout() {
        return R.layout.activity_product_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Produkt");

        // TODO:
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getInt(EXTRAS_ELEMENT_ID);
        }

        // Init views
        initViews();

        // Setting up presenter
        presenter = new ProductDetailsPresenter(this, Injection.provideLoyaltyRepository(getApplicationContext()));
        presenter.requestDataFromServer(productId);
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mLayoutContainer = findViewById(R.id.layout_container);

        mProgressBar = findViewById(R.id.progress_bar);
        mProductImage = findViewById(R.id.imageView);
        mProductTitle = findViewById(R.id.product_title_text_view);
        mProductPrice = findViewById(R.id.price_amount_text_view);
        mProductDescription = findViewById(R.id.product_description_text_view);

        // Setting up views
        mLayoutContainer.setVisibility(View.GONE);
    }

    /**
     * Called from {@link ProductDetailsPresenter#passDataToView(Product)} to populate view with {@link Product} details.
     *
     * @param product item containing all details, refer {@link Product}
     */
    @Override
    public void setUpViewWithData(Product product) {
        if (!TextUtils.isEmpty(product.getImage())) {
            Picasso.get()
                    .load(product.getImage())
                    .error(R.drawable.no_image_available)
                    .into(mProductImage);
        } else {
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .into(mProductImage);
        }

        if (!TextUtils.isEmpty(product.getTitle())) {
            mProductTitle.setText(product.getTitle());
        } else {
            mProductTitle.setText(DEFAULT_STRING);
        }

        if (product.getPrice() != null && !product.getPrice().toString().trim().isEmpty()) {
            mProductPrice.setText(String.valueOf(formatPrice(product.getPrice())).concat(" zł"));
        } else {
            mProductPrice.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(product.getDescription())) {
            mProductDescription.setText(Html.fromHtml(product.getDescription()));
        } else {
            mProductDescription.setText(DEFAULT_STRING);
        }
    }

    /**
     * Called from {@link #setUpViewWithData(Product)} to edit price format.
     *
     * @param price get from {@link Product} item
     * @return formatted price form
     */
    private String formatPrice(float price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMaximumFractionDigits(2);

        return decimalFormat.format(price);
    }

    /**
     * This hook is called whenever an item in options menu is selected.
     *
     * @param item is a selected item
     * @return false to allow normal menu processing to proceed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called from {@link ProductDetailsPresenter#hideProgressBar()} to hide progress bar when data is fetched or not.
     */
    @Override
    public void hideProgressBar() {
        mLayoutContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Called from {@link ProductDetailsPresenter#requestDataFromServer(int)} whenever data is
     * unavailable to get.
     *
     * @param message is a string with type of toast that should be displayed
     */
    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}