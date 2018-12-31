package com.example.konta.sketch_loyalityapp.additionalViews;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsActivity extends BaseActivity {

    private String json, layoutTitle;

    // Temporary variables using to get json data from assets
    private int productPosition = 0;
    private static final String jsonFileData = "products.json";

    // Fields necessary to store values that will be displayed to user
    private int mProductImageResourceId;
    private String mProductTitle, mProductDescription;
    private double mProductPrice;

    @Override
    protected int getLayout() { return R.layout.activity_product_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Temporary solution - setting up sample data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productPosition = extras.getInt("EXTRA_ELEMENT_ID");
        }

        // Reading JSON file from assets
        json = ((MyApplication) getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data
        extractDataFromJson();

        setTitle(layoutTitle);
        setDataToRelatedViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            String image = object.getString("contentImage");
            mProductImageResourceId = resources
                    .getIdentifier(image, "drawable", this.getPackageName());

            mProductDescription = object.getString("contentFullDescription");

            JSONArray array = object.getJSONArray("products");
            JSONObject insideObj = array.getJSONObject(productPosition);

            mProductTitle = insideObj.getString("contentTitle");
            mProductPrice = insideObj.getDouble("contentPrice");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDataToRelatedViews() {
        ImageView productImage = findViewById(R.id.imageView);
        productImage.setImageResource(mProductImageResourceId);

        TextView productTitle = findViewById(R.id.product_title_text_view);
        productTitle.setText(mProductTitle);

        TextView productPrice = findViewById(R.id.price_amount_text_view);
        productPrice.setText(String.valueOf(mProductPrice).concat(" ").concat("zł"));

        TextView productDescription = findViewById(R.id.product_description_text_view);
        productDescription.setText(mProductDescription);
    }
}