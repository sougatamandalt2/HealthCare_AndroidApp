package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textField;
    private TextInputEditText edt_phone;
    private Button btn_otp;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_otp=findViewById(R.id.btn_otp);
        edt_phone=findViewById(R.id.edt_phone);

        isOnline();

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edt_phone.getText().toString()) || edt_phone.length()!=10) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(LoginActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    phone = "+91" + edt_phone.getText().toString();
                    Intent i= new Intent(LoginActivity.this,MobileNoActivity.class);
                    i.putExtra("phone",phone);
                    startActivity(i);
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(LoginActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



}