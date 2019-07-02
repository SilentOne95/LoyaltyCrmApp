package com.sellger.konta.sketch_loyaltyapp.ui.website;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class WebsitePresenter implements WebsiteContract.Presenter {

    private static final String TAG = WebsitePresenter.class.getSimpleName();

    @NonNull
    private WebsiteContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    WebsitePresenter(@NonNull WebsiteContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link WebsiteFragment#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     *
     * @param pageId of the item which info is going to be fetched
     */
    @Override
    public void requestDataFromServer(int pageId) {
        loyaltyRepository.getSinglePage(pageId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                view.setUpViewWithData((Page) object);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }
}
