package com.example.konta.sketch_loyalityapp.ui.terms;

import com.example.konta.sketch_loyalityapp.data.staticPage.Page;

public interface TermsContract {

    interface View {

        void setUpViewWithData(Page page);
        void onResponseFailure();
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
    }

    interface Model {
        interface OnFinishedListener {
            void onFinished(Page page);

            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener, int pageId);
    }
}
