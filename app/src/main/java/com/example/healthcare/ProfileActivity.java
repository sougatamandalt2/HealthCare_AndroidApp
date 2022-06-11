package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iv_logout,iv_edit,iv_back;
    private TextView tv_orders,tv_username,tv_useredetails,tv_userphone,tv_settings,tv_contacts,tv_privacy,tv_aboutUs;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        isOnline();

        iv_logout=findViewById(R.id.iv_logout);
        tv_orders=findViewById(R.id.tv_orders);
        tv_username=findViewById(R.id.tv_username);
        tv_useredetails=findViewById(R.id.tv_useredetails);
        tv_userphone=findViewById(R.id.tv_userphone);
        iv_back=findViewById(R.id.iv_back);
        tv_settings=findViewById(R.id.tv_settings);
        tv_contacts=findViewById(R.id.tv_contacts);
        tv_aboutUs=findViewById(R.id.tv_aboutUs);
        tv_privacy=findViewById(R.id.tv_privacy);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        String useruid=firebaseAuth.getCurrentUser().getUid();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            String name=""+ds.child("name").getValue();
                            String yob=""+ds.child("yob").getValue();
                            String gender=""+ds.child("gender").getValue();
                            String phone=""+ds.child("mob_no").getValue();
                            int year=Calendar.getInstance().get(Calendar.YEAR);
                            int age=year-Integer.parseInt(yob);
                            tv_username.setText(" "+name);
                            tv_useredetails.setText(" "+gender+" , Age : "+age+"Y");
                            tv_userphone.setText(" "+phone);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,HomepageActivity.class));
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Logging Out....");

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customer");

                firebaseAuth.signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });

        tv_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Showing your orders....");
                startActivity(new Intent(ProfileActivity.this, OrdersListActivity.class));
            }
        });


        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,SettingsActivity.class));
            }
        });

        tv_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://yourhealthcare.w3spaces.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://t2-4-u.blogspot.com/2022/06/privacy-policy-healthcare.html");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        tv_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://sougata.tech");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(ProfileActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}