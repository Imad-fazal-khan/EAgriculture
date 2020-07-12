package orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.User;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.Users;


public class Sign_Up extends AppCompatActivity {

    EditText et_uname, et_phoneNo, et_password, et_verify_code,et_email;
    Button btn_signup, btn_verify_code;
    //Button saveData_toFirebase;
    FirebaseAuth mAuth;
    DatabaseReference table_user;
    String codeSent;
    String userName,userPassword, userPhone,useremail;
    private DatabaseReference mdatabase;
    private StorageReference mStorageRef;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userType=intent.getStringExtra("UserType");
        if(userType.equals("Customer"))
        {

            table_user = database.getReference().child("CustomerSide");
            mdatabase = FirebaseDatabase.getInstance().getReference().child("CustomerSide");
        }
       else if(userType.equals("ShopKeeper"))
        {

            table_user = database.getReference().child("ShopKeeperSide");
            mdatabase = FirebaseDatabase.getInstance().getReference().child("ShopKeeperSide");
        }
       else if(userType.equals("DeliveryMan")){

            table_user = database.getReference().child("DeliveryManSide");
            mdatabase = FirebaseDatabase.getInstance().getReference().child("DeliveryManSide");

        } else {
            Toast.makeText(this, "The System is Wrong", Toast.LENGTH_SHORT).show();
        }



        et_uname = (EditText) findViewById(R.id.ret_username);
        et_phoneNo = (EditText) findViewById(R.id.ret_phone);
        et_password = (EditText) findViewById(R.id.ret_password);
        et_verify_code = (EditText) findViewById(R.id.ret_verify_code);
        et_email=(EditText)findViewById(R.id.ret_useremail);
        btn_verify_code = (Button) findViewById(R.id.btn_verify_code_registered);
        btn_signup = (Button) findViewById(R.id.btn_register);






        mStorageRef = FirebaseStorage.getInstance().getReference();
        //click on sign Up Now
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeredNow();
            }
        });

        btn_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();

            }
        });


    }


    // on click on verify code call here
    private void verifyCode() {

        String code = et_verify_code.getText().toString();

           if(TextUtils.isEmpty(code)){
                et_verify_code.setError("Code is not entered");
                et_verify_code.requestFocus();
                Toast.makeText(this, "Please Enter Code", Toast.LENGTH_SHORT).show();
                return;
            }
            if(code.equals(codeSent))
            {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
                signInWithPhoneAuthCredential(credential);
            }
            String s=code;
            String y=codeSent;
            if(code==codeSent);
                et_verify_code.setError("Code is not correct");
                et_verify_code.requestFocus();
                Toast.makeText(this, "Please Enter Code", Toast.LENGTH_SHORT).show();
                return;

     // PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
      //  signInWithPhoneAuthCredential(credential);


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          //  String userName = et_uname.getText().toString();
                       // String userPassword = et_password.getText().toString();
                        String verificationCode = et_verify_code.getText().toString();
                           // Log.d("Code Sent", codeSent);

                         Users user=new Users(userName,userType, userPhone, userPassword);
                            table_user.child(et_phoneNo.getText().toString()).child("Shop_Info").setValue(user);
                            Toast.makeText(getApplicationContext(), "Registed your Number", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);


                        }
                        else {
                            // Sign in failed, display a message and update the UI
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                et_verify_code.setError("OOPS you Enter Wrong Conde");
                                et_verify_code.requestFocus();

                                return ;
                            }
                        }
                    }
                });
    }

// on cick registered this code is running

    private void registeredNow() {
   userName = et_uname.getText().toString();
      userPassword = et_password.getText().toString();
     userPhone = et_phoneNo.getText().toString().trim();
     useremail=et_email.getText().toString().trim();

        //final String verificationCode = et_verify_code.getText().toString();


        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                userName = et_uname.getText().toString();
                userPassword = et_password.getText().toString();
                userPhone = et_phoneNo.getText().toString().trim();
                useremail=et_email.getText().toString().trim();
 Log.d("Phone No=",userPhone);
                Log.d("Email",useremail);

                //get user in formation from fibase


                // Check user exist in database  or not
                if (dataSnapshot.child(userPhone).exists()) {

                    Toast.makeText(getApplicationContext(), "User is Exist with this phone number!!", Toast.LENGTH_SHORT).show();
                    et_phoneNo.setError("No is already registered");
                    et_phoneNo.requestFocus();
                    et_phoneNo.setText("");
                    return;
                }
                if (userPhone.length() < 11) {
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Phone No", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(getApplicationContext(), "Please Fill all required Field", Toast.LENGTH_SHORT).show();

                } else {

                    // Verify the phone number for registere
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.rll_signUp_page1);
                    linearLayout.setVisibility(View.INVISIBLE);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rrl_signUp_page1);
                    relativeLayout.setVisibility(View.VISIBLE);
                    TextView tv_logo_vc=(TextView)findViewById(R.id.tv_vc_logo_signup);
                    tv_logo_vc.setText(tv_logo_vc.getText().toString()+userPhone);



                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            userPhone,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Sign_Up.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks

                    //      }


                    //  });



                    User user = new User(userName, userType, userPhone, userPassword);
                    table_user.child(et_phoneNo.getText().toString()).setValue(user);
                    //Toast.makeText(Registered.this, "Registed your Number", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            DatabaseReference newPost = mdatabase.child(userPhone).child("User_Info");
            newPost.child("Name").setValue(userName);
            //newPost.child("Password").setValue(userPassword);
            newPost.child("Description").setValue(userPhone);
            newPost.child("Email").setValue(useremail);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

    public void goto_SignIn(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Log_In.class);
                intent.putExtra("UserType",userType);
                startActivity(intent);
            }
        });
    }
}


