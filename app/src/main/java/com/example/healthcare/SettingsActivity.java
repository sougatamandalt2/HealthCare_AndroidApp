package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private TextView tv_mobile,tv_updatename,tv_logout,tv_cancel;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        tv_updatename=findViewById(R.id.tv_updatename);
        tv_logout=findViewById(R.id.tv_logout);
//        tv_cancel=findViewById(R.id.tv_cancel);
        iv_back=findViewById(R.id.iv_back);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");

                firebaseAuth.signOut();
                startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
            }
        });

//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//                Query applesQuery = ref.child("Users").orderByChild("orders").equalTo(firebaseAuth.getUid());
//
//                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                            appleSnapshot.getRef().removeValue();
//                            Toast.makeText(SettingsActivity.this, "Last Order Cancelled Successfully.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//            }
//        });


        tv_updatename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,UpdateNameActivity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,ProfileActivity.class));
            }
        });

    }

//    private void deleteAccount() {
//        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
//        currentUser.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User account deleted.");
//                        }
//                    }
//                });
//    }
}