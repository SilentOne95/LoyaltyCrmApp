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
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

public class ContactInfoFragment extends BaseFragment implements BottomSheetContract.ContactInfoView,
        View.OnClickListener {

    ContactInfoPresenter presenter;

    private TextView phoneTextView, emailTextView;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_contact_info; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ContactInfoPresenter(this);
        presenter.setUpObservable();
    }

    @Override
    public void setUpViewsWithData(List<Marker> markerList) {
        phoneTextView = rootView.findViewById(R.id.contact_info_phone_view);
        phoneTextView.setOnClickListener(this);

        emailTextView = rootView.findViewById(R.id.contact_info_email_view);
        emailTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String stringWithData;

        switch(view.getId()) {
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
        }
    }
}