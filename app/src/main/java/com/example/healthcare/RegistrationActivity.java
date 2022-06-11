package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    String name,sex,gender,yob,mob_no;
    private TextInputEditText edt_yob,edt_name;
    private AutoCompleteTextView edt_gender;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        edt_yob=findViewById(R.id.edt_yob);
        edt_name=findViewById(R.id.edt_name);
        edt_gender=findViewById(R.id.edt_gender);
        btn_submit=findViewById(R.id.btn_submit);
        mob_no=getIntent().getStringExtra("mob_no");

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        String[] items={"Male","Female","Others"};
        ArrayAdapter<String> itemadapter=new ArrayAdapter<>(RegistrationActivity.this,R.layout.list_item,items);
        edt_gender.setAdapter((itemadapter));
        edt_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sex="Male";
                }
                else if (i==1){
                    sex="Female";
                }
                else{
                    sex="Others";
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

    }

    private void inputData() {
        
        name=edt_name.getText().toString().trim();
        gender=edt_gender.getText().toString().trim();
        yob=edt_yob.getText().toString().trim();
        int year_of_birth= Integer.parseInt(yob);
        int curr_year=Calendar.getInstance().get(Calendar.YEAR);

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegistrationActivity.this, "Name field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(gender)){
            Toast.makeText(RegistrationActivity.this, "Gender field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(yob)){
            Toast.makeText(RegistrationActivity.this, "YOB field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(yob.length()!=4){
            Toast.makeText(RegistrationActivity.this, "Year of Birth should contain 4 characters Ex : 2000", Toast.LENGTH_LONG).show();
            return;
        }

        if (year_of_birth<1920 || year_of_birth>curr_year){
            Toast.makeText(RegistrationActivity.this,"Invalid Year of Birth",Toast.LENGTH_SHORT).show();
            return;
        }

        saveFirebaseData();
    }

    public void onBackPressed() {
        Toast.makeText(RegistrationActivity.this,"Please Enter the Details First.",Toast.LENGTH_SHORT).show();
    }


    private void saveFirebaseData() {

        progressDialog.setMessage("Saving info....");
        progressDialog.show();
        String timeStamp=""+System.currentTimeMillis();

        HashMap<String,Object> hashmap=new HashMap<>();

        hashmap.put("uid",firebaseAuth.getUid());
        hashmap.put("name",""+name);
        hashmap.put("mob_no",""+mob_no);
        hashmap.put("gender",""+sex);
        hashmap.put("yob",""+yob);
        hashmap.put("timeStamp",""+timeStamp);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Intent i=new Intent(RegistrationActivity.this,HomepageActivity.class);
                        i.putExtra("name",name);
                        Toast.makeText(RegistrationActivity.this,"Registration Successful",Toast.LENGTH_SHORT);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}