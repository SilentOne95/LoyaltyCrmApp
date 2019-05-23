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
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ContactInfoFragment extends BaseFragment implements BottomSheetContract.ContactInfoView,
        View.OnClickListener {

    private static final String TAG = ContactInfoFragment.class.getSimpleName();

    private ContactInfoPresenter presenter;

    private TextView mPhoneTextView, mEmailTextView, mWebsiteTextView;

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

    @Override
    public void initViews() {
        mPhoneTextView = rootView.findViewById(R.id.contact_info_phone_view);
        mEmailTextView = rootView.findViewById(R.id.contact_info_email_view);
        mWebsiteTextView = rootView.findViewById(R.id.contact_info_website_view);
    }

    @Override
    public void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress) {
        mPhoneTextView.setText(phoneNumber);
        if (!phoneNumber.equals(DEFAULT_STRING)) {
            mPhoneTextView.setOnClickListener(this);
        }

        mEmailTextView.setText(emailAddress);
        if (!emailAddress.equals(DEFAULT_STRING)) {
            mEmailTextView.setOnClickListener(this);
        }

        mWebsiteTextView.setText(websiteAddress);
        if (!websiteAddress.equals(DEFAULT_STRING)) {
            mWebsiteTextView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        String stringWithData;

        switch (view.getId()) {
            case R.id.contact_info_phone_view:
                stringWithData = mPhoneTextView.getText().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", stringWithData, null));

                try {
                    startActivity(phoneIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_info_email_view:
                stringWithData = mEmailTextView.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + stringWithData));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.contact_info_website_view:

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

    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}