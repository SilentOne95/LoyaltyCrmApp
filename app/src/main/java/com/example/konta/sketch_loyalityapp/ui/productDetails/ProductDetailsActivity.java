package com.example.konta.sketch_loyalityapp.ui.productDetails;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View{

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    ProductDetailsPresenter presenter;

    private ImageView productImage;
    private TextView productTitle, productPrice, productDescription;
    private ProgressBar progressBar;
    private View layoutContainer;

    private int productId;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected int getLayout() { return R.layout.activity_product_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Product");

        // Temporary solution - setting up sample pojo
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getInt("EXTRA_ELEMENT_ID");
        }

        layoutContainer = findViewById(R.id.layout_container);
        layoutContainer.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_bar);

        productImage = findViewById(R.id.imageView);
        productTitle = findViewById(R.id.product_title_text_view);
        productPrice = findViewById(R.id.price_amount_text_view);
        productDescription = findViewById(R.id.product_description_text_view);

        presenter = new ProductDetailsPresenter(this, new ProductDetailsModel());
        presenter.requestDataFromServer(productId);
    }

    @Override
    public void hideProgressBar() {
        layoutContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUpViewWithData(Product product) {
        if (product.getImage() != null && product.getImage().trim().isEmpty()) {
            // TODO: Upload images to server
            Picasso.get().load(R.drawable.image_product).into(productImage);
        }

        if (!TextUtils.isEmpty(product.getTitle())) {
            productTitle.setText(product.getTitle());
        } else {
            productTitle.setText(DEFAULT_STRING);
        }

        if (product.getPrice() != null && !product.getPrice().toString().trim().isEmpty()) {
            productPrice.setText(String.valueOf(decimalFormat.format(product.getPrice())).concat(" zł"));
        } else {
            productPrice.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(product.getDescription())) {
            productDescription.setText(Html.fromHtml(product.getDescription()));
        } else {
            productDescription.setText(DEFAULT_STRING);
        }
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