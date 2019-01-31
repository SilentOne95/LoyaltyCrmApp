package com.example.konta.sketch_loyalityapp.ui.productDetails;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;
import com.squareup.picasso.Picasso;

import static com.example.konta.sketch_loyalityapp.Constants.BASE_URL_IMAGES;
import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View{

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    ProductDetailsPresenter presenter;

    ImageView productImage;
    TextView productTitle, productPrice, productDescription;

    private int productId;

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

        productImage = findViewById(R.id.imageView);
        productTitle = findViewById(R.id.product_title_text_view);
        productPrice = findViewById(R.id.price_amount_text_view);
        productDescription = findViewById(R.id.product_description_text_view);

        presenter = new ProductDetailsPresenter(this, new ProductDetailsModel());
        presenter.requestDataFromServer(productId);
    }

    @Override
    public void setUpViewWithData(Product product) {
        if (product.getImage() != null && product.getImage().trim().isEmpty()) {
            Picasso.get().load(BASE_URL_IMAGES + product.getImage()).into(productImage);
        }

        if (product.getTitle() != null && !product.getTitle().trim().isEmpty()) {
            productTitle.setText(product.getTitle());
        } else {
            productTitle.setText(DEFAULT_STRING);
        }

        if (product.getPrice() != null && !product.getPrice().toString().trim().isEmpty()) {
            productPrice.setText(String.valueOf(product.getPrice()).concat(" ").concat("zł"));
        } else {
            productPrice.setText(DEFAULT_STRING);
        }

        if (product.getDescription() != null && !product.getDescription().trim().isEmpty()) {
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