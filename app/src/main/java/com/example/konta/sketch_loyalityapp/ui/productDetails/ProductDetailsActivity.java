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

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View{

    ProductDetailsPresenter presenter;

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

        presenter = new ProductDetailsPresenter(this, new ProductDetailsModel());
        presenter.requestDataFromServer(productId);

    }

    @Override
    public void setUpViewWithData(Product product) {
        ImageView productImage = findViewById(R.id.imageView);
        if (!(product.getImage() == null || product.getImage().isEmpty())) {
            Picasso.get().load(BASE_URL_IMAGES + product.getImage()).into(productImage);
        }

        TextView productTitle = findViewById(R.id.product_title_text_view);
        productTitle.setText(product.getTitle());

        TextView productPrice = findViewById(R.id.price_amount_text_view);
        productPrice.setText(String.valueOf(product.getPrice()).concat(" ").concat("zł"));

        TextView productDescription = findViewById(R.id.product_description_text_view);
        productDescription.setText(Html.fromHtml(product.getDescription()));
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