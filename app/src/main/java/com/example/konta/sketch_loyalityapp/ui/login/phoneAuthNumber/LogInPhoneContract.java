package com.example.konta.sketch_loyalityapp.ui.login.phoneAuthNumber;

public interface LogInPhoneContract {

    interface View {

        boolean getTextInputEditText();
        void displayErrorInputMessage();
        void dismissError(android.view.View v);
    }
}
