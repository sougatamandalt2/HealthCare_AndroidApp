package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    Button btn_submit;
    ImageView iv_back;
    AutoCompleteTextView dropdown_menu,edt_gender;
    TextInputEditText tvname,tvAge;
    ProgressDialog progressDialog;
    RelativeLayout ll;
    FirebaseAuth mAuth;
    int age;
    String relation,gender,issue,pin,yob,location,city;
    String orderby;
    String phone;
    int old;
    String sex;
    String yobirth;
    TextView ordertype,delivery_price;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        isOnline();

        btn_submit=findViewById(R.id.btn_submit);
        dropdown_menu=findViewById(R.id.dropdown_menu);
        edt_gender=findViewById(R.id.edt_gender);
        tvname=findViewById(R.id.tvname);
        tvAge=findViewById(R.id.tvAge);
        iv_back=findViewById(R.id.iv_back);
        ll=findViewById(R.id.ll);
        issue=getIntent().getStringExtra("issue");
        location=getIntent().getStringExtra("location");
        pin=getIntent().getStringExtra("pin");
        city=getIntent().getStringExtra("city");
        delivery_price=findViewById((R.id.delivery_price));
        ordertype=findViewById((R.id.ordertype));

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth=FirebaseAuth.getInstance();
        relation="Himself";
        reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            orderby = "" + ds.child("name").getValue();
                            yobirth = "" + ds.child("yob").getValue();
                            sex = "" + ds.child("gender").getValue();
                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            old = year - Integer.parseInt(yobirth);
                            phone=""+ds.child("mob_no").getValue();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if(String.valueOf(issue).equals("Blood Sugar Level")){
            ordertype.setText("Blood Sugar Level");
            delivery_price.setText("100");
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_submit.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);

        String[] items={"Myself","Mother","Father","Wife","Family Member"};
        ArrayAdapter<String> itemAdapter=new ArrayAdapter<>(CheckoutActivity.this,R.layout.list_item,items);
        dropdown_menu.setAdapter((itemAdapter));

        dropdown_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    ll.setVisibility(View.VISIBLE);
                    tvname.setText(orderby);
                    tvAge.setText(Integer.toString(old));
                    edt_gender.setText(sex);
                    relation="Myself";
                }
                else if(i==1) {
                    ll.setVisibility(View.VISIBLE);
                    edt_gender.setText("Female");
                    relation="Mother";
                }
                else if (i==2){
                    ll.setVisibility(View.VISIBLE);
                    relation="Father";
                    edt_gender.setText("Male");
                }
                else if (i==3){
                    ll.setVisibility(View.VISIBLE);
                    relation="Wife";
                    edt_gender.setText("Female");
                }
                else {
                    ll.setVisibility(View.VISIBLE);
                    relation="Others Family Member";
                }

                btn_submit.setVisibility(View.VISIBLE);
            }
        });

        String[] sex={"Male","Female","Others"};
        ArrayAdapter<String> itemadapter=new ArrayAdapter<>(CheckoutActivity.this,R.layout.list_item,sex);
        edt_gender.setAdapter((itemadapter));
        edt_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    gender="Male";
                }
                else if(i==1){
                    gender="Female";
                }
                else{
                    gender="Others";
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(tvname.getText().toString())) {
                    Toast.makeText(CheckoutActivity.this, "Name Feild is Empty.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(edt_gender.getText().toString())) {
                    Toast.makeText(CheckoutActivity.this, "Gender Feild is Empty.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(tvAge.getText().toString()) || tvAge.length()!=2) {
                    Toast.makeText(CheckoutActivity.this, "Enter a valid Age.", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Saving Order Details");
                    progressDialog.show();

                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    String timestamp =""+System.currentTimeMillis();

                    final DatabaseReference orderref=FirebaseDatabase.getInstance().getReference().child("AllOrders");
                    reference.orderByChild("Order ID").equalTo(timestamp);

                    HashMap<String,Object> orderlist=new HashMap<>();
                    orderlist.put("relation",relation);
                    orderlist.put("gender",edt_gender.getText().toString());
                    orderlist.put("orderby",orderby);
                    orderlist.put("patientname",tvname.getText().toString());
                    orderlist.put("age",tvAge.getText().toString());
                    orderlist.put("issue",issue);
                    orderlist.put("price",delivery_price.getText().toString());
                    orderlist.put("date",date);
                    orderlist.put("Location",location);
                    orderlist.put("PIN",pin);
                    orderlist.put("City",city);
                    orderlist.put("Phone",phone);
                    orderlist.put("Order ID",timestamp);
                    orderlist.put("UserId",FirebaseAuth.getInstance().getUid());

                    orderref.child(timestamp).setValue(orderlist);

//                    orderref.updateChildren(orderlist).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
                                final DatabaseReference order=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Orders");

                                HashMap<String,Object> orderdetails =new HashMap<>();
                                orderdetails.put("relation",relation);
                                orderdetails.put("patientname",tvname.getText().toString());
                                orderdetails.put("issue",issue);
                                orderdetails.put("price",delivery_price.getText().toString());
                                orderdetails.put("date",date);
                                orderdetails.put("orderid",timestamp);
                                orderdetails.put("Status","Order Received");
                                orderdetails.put("orderby",FirebaseAuth.getInstance().getUid());

                                order.child(timestamp).setValue(orderdetails);
                                progressDialog.dismiss();

//                                order.updateChildren(orderdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful()){
                                            Toast.makeText(CheckoutActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(CheckoutActivity.this,OrdersListActivity.class));
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                    });

                }
            }

        });

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(CheckoutActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}