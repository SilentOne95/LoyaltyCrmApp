package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.ui.map.MapModel;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class ContactInfoFragment extends BaseFragment implements BottomSheetContract.ContactInfoView,
        View.OnClickListener {

    private static final String TAG = ContactInfoFragment.class.getSimpleName();

    ContactInfoPresenter presenter;

    private TextView phoneTextView, emailTextView, websiteTextView;

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

        phoneTextView = rootView.findViewById(R.id.contact_info_phone_view);
        emailTextView = rootView.findViewById(R.id.contact_info_email_view);
        websiteTextView = rootView.findViewById(R.id.contact_info_website_view);

        presenter = new ContactInfoPresenter(this, new MapModel());
        presenter.setUpObservable();
    }

    @Override
    public void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress) {
        phoneTextView.setText(phoneNumber);
        if (!phoneNumber.equals(DEFAULT_STRING)) {
            phoneTextView.setOnClickListener(this);
        }

        emailTextView.setText(emailAddress);
        if (!emailAddress.equals(DEFAULT_STRING)) {
            emailTextView.setOnClickListener(this);
        }

        websiteTextView.setText(websiteAddress);
        if (!websiteAddress.equals(DEFAULT_STRING)) {
            websiteTextView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        String stringWithData;

        switch (view.getId()) {
            case R.id.contact_info_phone_view:
                stringWithData = phoneTextView.getText().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", stringWithData, null));

                try {
                    startActivity(phoneIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact_info_email_view:
                stringWithData = emailTextView.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + stringWithData));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.contact_info_website_view:

                if (websiteTextView.getText().toString().contains("http://")) {
                    stringWithData = websiteTextView.getText().toString();
                } else if (websiteTextView.getText().toString().contains("https://")) {
                    stringWithData = websiteTextView.getText().toString();
                } else {
                    stringWithData = "http://" + websiteTextView.getText().toString();
                }

                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(stringWithData));

                try {
                    startActivity(webIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }
}