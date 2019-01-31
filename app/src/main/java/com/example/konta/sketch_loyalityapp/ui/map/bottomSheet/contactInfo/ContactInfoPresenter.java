package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.MapContract;
import com.example.konta.sketch_loyalityapp.ui.map.MapPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter {

    @Nullable
    private BottomSheetContract.ContactInfoView view;
    private MapContract.Model model;

    private static final String TAG = "ContactInfoFragment";
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
    public void formatContactInfoData(List<Marker> markerList) {
        String phoneNumber, emailAddress;
        int markerPosition = 0;

        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getId().equals(selectedMarkerId)) {
                markerPosition = i;
                break;
            }
        }

        phoneNumber = formatPhoneNumber(markerList.get(markerPosition));
        emailAddress = formatEmailAddress(markerList.get(markerPosition));

        passDataToView(phoneNumber, emailAddress);
    }

    @Override
    public String formatPhoneNumber(Marker marker) {
        String phoneNumber = DEFAULT_STRING;
        String[] phonePrefix = {"0048", "48", "0"};

        if (marker.getPhoneNumber() != null && !marker.getPhoneNumber().trim().isEmpty()) {
            phoneNumber = phoneNumber
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
    public String formatEmailAddress(Marker marker) {
        String emailAddress = DEFAULT_STRING;

        if (marker.getMail() != null && !marker.getMail().trim().isEmpty()) {
            emailAddress = marker.getMail();
        }

        return emailAddress;
    }

    @Override
    public void passDataToView(String phoneNumber, String emailAddress) {
        if (view != null) {
            view.setUpViewsWithData(phoneNumber, emailAddress);
        }
    }
}
