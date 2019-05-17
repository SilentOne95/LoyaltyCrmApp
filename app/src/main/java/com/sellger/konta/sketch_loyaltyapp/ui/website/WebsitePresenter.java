package com.sellger.konta.sketch_loyaltyapp.ui.website;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_DATA_ERROR_MESSAGE;

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

    @Override
    public void requestDataFromServer(int pageId) {
        loyaltyRepository.getSinglePage(pageId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                view.setUpViewWithData((Page) object);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_DATA_ERROR_MESSAGE);
            }
        });
    }
}
