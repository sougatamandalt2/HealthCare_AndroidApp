package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

public class HomepageActivity extends AppCompatActivity {

    private RelativeLayout rl_bp, rl_sugar;
    private String req_type;
    private TextView tv_name;
    private CircularImageView iv_profile;
    FloatingActionButton floating_action_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        isOnline();

        rl_bp=findViewById(R.id.rl_bp);
        rl_sugar=findViewById(R.id.rl_sugar);
        tv_name=findViewById(R.id.tv_name);
        iv_profile=findViewById(R.id.iv_profile);
        floating_action_button=findViewById(R.id.floating_action_button);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()){
                            String yourname="Hi, "+ds.child("name").getValue();

                            tv_name.setText(yourname);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,OrdersListActivity.class));
            }
        });

        rl_bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type="BP Measure";
                Intent i=new Intent(HomepageActivity.this,LocationActivity.class);
                i.putExtra("req_type",req_type);
                startActivity(i);
            }
        });

        rl_sugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type="Blood Sugar Level";
                Intent i=new Intent(HomepageActivity.this,LocationActivity.class);
                i.putExtra("req_type",req_type);
                startActivity(i);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this, ProfileActivity.class));
            }
        });

    }

    public void onBackPressed() {
        finishAffinity();
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(HomepageActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}