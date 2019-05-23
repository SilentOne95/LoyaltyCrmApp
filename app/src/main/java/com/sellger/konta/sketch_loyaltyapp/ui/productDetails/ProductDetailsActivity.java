package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BASE_URL_IMAGES;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View{

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private ProductDetailsPresenter presenter;

    private ImageView mProductImage;
    private TextView mProductTitle, mProductPrice, mProductDescription;
    private ProgressBar mProgressBar;
    private View mLayoutContainer;

    private int productId;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected int getLayout() { return R.layout.activity_product_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Produkt");

        // Temporary solution - setting up sample pojo
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getInt("EXTRA_ELEMENT_ID");
        }

        // Init views
        initViews();

        // Setting up views
        mLayoutContainer.setVisibility(View.GONE);

        // Setting up presenter
        presenter = new ProductDetailsPresenter(this, Injection.provideLoyaltyRepository(getApplicationContext()));
        presenter.requestDataFromServer(productId);
    }

    @Override
    public void initViews() {
        mLayoutContainer = findViewById(R.id.layout_container);

        mProgressBar = findViewById(R.id.progress_bar);
        mProductImage = findViewById(R.id.imageView);
        mProductTitle = findViewById(R.id.product_title_text_view);
        mProductPrice = findViewById(R.id.price_amount_text_view);
        mProductDescription = findViewById(R.id.product_description_text_view);
    }

    @Override
    public void hideProgressBar() {
        mLayoutContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUpViewWithData(Product product) {
        if (!TextUtils.isEmpty(product.getImage())) {
            // TODO: Upload images to server
            Picasso.get()
                    .load(BASE_URL_IMAGES + product.getImage())
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
            mProductPrice.setText(String.valueOf(decimalFormat.format(product.getPrice())).concat(" zł"));
        } else {
            mProductPrice.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(product.getDescription())) {
            mProductDescription.setText(Html.fromHtml(product.getDescription()));
        } else {
            mProductDescription.setText(DEFAULT_STRING);
        }
    }

    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(this, message , Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}