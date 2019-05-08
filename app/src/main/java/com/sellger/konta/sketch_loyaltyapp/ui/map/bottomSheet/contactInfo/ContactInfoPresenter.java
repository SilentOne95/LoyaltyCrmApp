package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.contactInfo;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMarker;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapContract;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter {

    @Nullable
    private BottomSheetContract.ContactInfoView view;
    private MapContract.Model model;

    private static final String TAG = ContactInfoPresenter.class.getSimpleName();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int selectedMarkerId;

    ContactInfoPresenter(@Nullable BottomSheetContract.ContactInfoView view, MapContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void setUpObservable() {

        Observable<Integer> observable = MapPresenter.getObservable();
        Observer<Integer> onMarkerClickObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer markerId) {
                Log.d(TAG, "onNext");
                selectedMarkerId = markerId;
                getMarkerList();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        observable.subscribe(onMarkerClickObserver);
    }

    @Override
    public void getMarkerList() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void formatContactInfoData(List<HelperMarker> markerList) {
        String phoneNumber, emailAddress, websiteAddress;
        int markerPosition = 0;

        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getId().equals(selectedMarkerId)) {
                markerPosition = i;
                break;
            }
        }

        phoneNumber = formatPhoneNumber(markerList.get(markerPosition));
        emailAddress = formatEmailAddress(markerList.get(markerPosition));
        websiteAddress = formatWebsiteAddress(markerList.get(markerPosition));

        passDataToView(phoneNumber, emailAddress, websiteAddress);
    }

    @Override
    public String formatPhoneNumber(HelperMarker marker) {
        String phoneNumber = DEFAULT_STRING;
        String[] phonePrefix = {"0048", "48", "0"};

        if (!TextUtils.isEmpty(marker.getPhoneNumber())) {
            phoneNumber = marker.getPhoneNumber()
                    .replaceAll("\\s+", "")
                    .replaceAll("[\\D]", "");

            int caseCounter;
            for (caseCounter = 0; caseCounter < phonePrefix.length; caseCounter++) {
                if (phoneNumber.startsWith(phonePrefix[caseCounter]))
                    break;
            }

            switch (caseCounter) {
                case 0:
                    phoneNumber = phoneNumber.substring(4);
                    break;
                case 1:
                    phoneNumber = phoneNumber.substring(2);
                    break;
                case 2:
                    phoneNumber = phoneNumber.substring(1);
                    break;
                default:
                    break;
            }
        }

        return phoneNumber;
    }

    @Override
    public String formatEmailAddress(HelperMarker marker) {
        String emailAddress = DEFAULT_STRING;

        if (!TextUtils.isEmpty(marker.getMail())) {
            emailAddress = marker.getMail();
        }

        return emailAddress;
    }

    @Override
    public String formatWebsiteAddress(HelperMarker marker) {
        String websiteAddress = DEFAULT_STRING;

        if (!TextUtils.isEmpty(marker.getWebsite())) {
            websiteAddress = marker.getWebsite();
        }

        return websiteAddress;
    }

    @Override
    public void passDataToView(String phoneNumber, String emailAddress, String websiteAddress) {
        if (view != null) {
            view.setUpViewsWithData(phoneNumber, emailAddress, websiteAddress);
        }
    }
}
