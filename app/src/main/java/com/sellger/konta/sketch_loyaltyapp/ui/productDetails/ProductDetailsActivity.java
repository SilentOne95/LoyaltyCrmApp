package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.databinding.ActivityProductDetailsBinding;

import static com.sellger.konta.sketch_loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ProductDetailsActivity extends BaseActivity implements ProductDetailsContract.View {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();

    private ProductDetailsPresenter presenter;
    private ActivityProductDetailsBinding mBinding;

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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);

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
        mBinding.setProductDetail(product);
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