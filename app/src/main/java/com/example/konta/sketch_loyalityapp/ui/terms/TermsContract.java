package com.example.konta.sketch_loyalityapp.ui.terms;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

public interface TermsContract {

    interface View {

        void setUpViewWithData(Page page);
        void onResponseFailure();
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
    }

    interface Model {

        void fetchDataFromServer(BaseCallbackListener.SingleItemOnFinishListener<Page> onFinishedListener,
                                 int pageId);
    }
}
