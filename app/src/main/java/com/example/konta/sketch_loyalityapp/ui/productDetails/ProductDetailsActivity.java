package com.example.konta.sketch_loyalityapp.ui.productDetails;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.data.product.Product;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View{

    ProductDetailsPresenter presenter;

    // Temporary variables using to get json data from assets
    private int productId;

    @Override
    protected int getLayout() { return R.layout.activity_product_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Product");

        // Temporary solution - setting up sample data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getInt("EXTRA_ELEMENT_ID");
        }

        presenter = new ProductDetailsPresenter(this, new ProductDetailsModel());
        presenter.requestDataFromServer(productId);

    }

    @Override
    public void setUpViewWithData(Product product) {
        ImageView productImage = findViewById(R.id.imageView);

        TextView productTitle = findViewById(R.id.product_title_text_view);
        productTitle.setText(product.getTitle());

        TextView productPrice = findViewById(R.id.price_amount_text_view);
        productPrice.setText(String.valueOf(product.getPrice()).concat(" ").concat("zł"));

        TextView productDescription = findViewById(R.id.product_description_text_view);
        productDescription.setText(product.getDescription());
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