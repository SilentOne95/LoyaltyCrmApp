package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.contactInfo;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter {

    private static final String TAG = ContactInfoPresenter.class.getSimpleName();

    @NonNull
    private BottomSheetContract.ContactInfoView view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    ContactInfoPresenter(@NonNull BottomSheetContract.ContactInfoView view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void setUpObservable() {
        Observable<Integer> observable = MapPresenter.getObservable();
        Observer<Integer> onMarkerClickObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer markerId) {
                getSelectedMarker(markerId);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        observable.subscribe(onMarkerClickObserver);
    }

    @Override
    public void getSelectedMarker(int markerId) {
        loyaltyRepository.getSingleMarker(markerId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                formatMarkerData((Marker) object);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    @Override
    public void formatMarkerData(Marker marker) {
        String phoneNumber, emailAddress, websiteAddress;

        phoneNumber = formatPhoneNumber(marker);
        emailAddress = formatEmailAddress(marker);
        websiteAddress = formatWebsiteAddress(marker);

        passDataToView(phoneNumber, emailAddress, websiteAddress);
    }

    @Override
    public String formatPhoneNumber(Marker marker) {
        String phoneNumber = DEFAULT_STRING;
        String[] phonePrefix = {"0048", "48", "0", "22"};

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
                    phoneNumber = phoneNumber.substring(4).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "+48 $1-$2-$3");
                    break;
                case 1:
                    phoneNumber = phoneNumber.substring(2).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "+48 $1-$2-$3");
                    break;
                case 2:
                    phoneNumber = phoneNumber.substring(1).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "+48 $1-$2-$3");
                    break;
                case 3:
                    phoneNumber = phoneNumber.substring(2).replaceFirst("(\\d{3})(\\d{2})(\\d+)", "+48 22 $1-$2-$3");
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

        if (!TextUtils.isEmpty(marker.getMail())) {
            emailAddress = marker.getMail();
        }

        return emailAddress;
    }

    @Override
    public String formatWebsiteAddress(Marker marker) {
        String websiteAddress = DEFAULT_STRING;

        if (!TextUtils.isEmpty(marker.getWebsite())) {
            websiteAddress = marker.getWebsite();
        }

        return websiteAddress;
    }

    @Override
    public void passDataToView(String phoneNumber, String emailAddress, String websiteAddress) {
        view.setUpViewsWithData(phoneNumber, emailAddress, websiteAddress);
    }
}
