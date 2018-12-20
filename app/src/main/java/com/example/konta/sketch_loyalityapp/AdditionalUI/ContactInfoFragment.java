package com.example.konta.sketch_loyalityapp.AdditionalUI;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;

public class ContactInfoFragment extends Fragment implements View.OnClickListener {

    private TextView phoneTextView, emailTextView;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_info, container, false);

        phoneTextView = rootView.findViewById(R.id.contact_info_phone_view);
        phoneTextView.setOnClickListener(this);

        emailTextView = rootView.findViewById(R.id.contact_info_email_view);
        emailTextView.setOnClickListener(this);

        return rootView;
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