package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.contactInfo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ContactInfoFragment extends BaseFragment implements BottomSheetContract.ContactInfoView,
        View.OnClickListener {

    private static final String TAG = ContactInfoFragment.class.getSimpleName();

    private ContactInfoPresenter presenter;

    private TextView mPhoneTextView, mEmailTextView, mWebsiteTextView;
    private View mPhoneContainer, mEmailContainer, mWebsiteContainer;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_contact_info;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init views
        initViews();

        // Setting up presenter
        presenter = new ContactInfoPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.setUpObservable();
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mPhoneTextView = rootView.findViewById(R.id.contact_info_phone_view);
        mEmailTextView = rootView.findViewById(R.id.contact_info_email_view);
        mWebsiteTextView = rootView.findViewById(R.id.contact_info_website_view);
        mPhoneContainer = rootView.findViewById(R.id.contact_phone_container);
        mEmailContainer = rootView.findViewById(R.id.contact_email_container);
        mWebsiteContainer = rootView.findViewById(R.id.contact_website_container);
    }

    /**
     * Called from {@link ContactInfoPresenter#passDataToView(String, String, String)} to populate view.
     *
     * @param phoneNumber string of selected marker on map
     * @param emailAddress string of selected marker on map
     * @param websiteAddress string of selected marker on map
     */
    @Override
    public void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress) {
        mPhoneTextView.setText(phoneNumber);
        if (!phoneNumber.equals(DEFAULT_STRING)) {
            mPhoneContainer.setOnClickListener(this);
        }

        mEmailTextView.setText(emailAddress);
        if (!emailAddress.equals(DEFAULT_STRING)) {
            mEmailContainer.setOnClickListener(this);
        }

        mWebsiteTextView.setText(websiteAddress);
        if (!websiteAddress.equals(DEFAULT_STRING)) {
            mWebsiteContainer.setOnClickListener(this);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v is view which was clicked
     */
    @Override
    public void onClick(View v) {
        String stringWithData;

        // Open phone, email or website with marker's data based on chosen option
        switch (v.getId()) {
            case R.id.contact_phone_container:
                stringWithData = mPhoneTextView.getText().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", stringWithData, null));

                try {
                    startActivity(phoneIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_email_container:
                stringWithData = mEmailTextView.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + stringWithData));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.contact_website_container:

                if (mWebsiteTextView.getText().toString().contains("http://")) {
                    stringWithData = mWebsiteTextView.getText().toString();
                } else if (mWebsiteTextView.getText().toString().contains("https://")) {
                    stringWithData = mWebsiteTextView.getText().toString();
                } else {
                    stringWithData = "http://" + mWebsiteTextView.getText().toString();
                }

                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(stringWithData));

                try {
                    startActivity(webIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Called from {@link ContactInfoPresenter#getSelectedMarker(int)} whenever data is
     * unavailable to get.
     *
     * @param message is a string with type of toast that should be displayed
     */
    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}