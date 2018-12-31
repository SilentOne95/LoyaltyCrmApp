package com.example.konta.sketch_loyalityapp.additionalViews;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class ContactInfoFragment extends BaseFragment implements View.OnClickListener {

    private TextView phoneTextView, emailTextView;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_contact_info; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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