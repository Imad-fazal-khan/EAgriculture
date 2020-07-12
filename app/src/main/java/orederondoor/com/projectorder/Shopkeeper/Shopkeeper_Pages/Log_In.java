package orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.auth.User;

import java.util.concurrent.TimeUnit;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.DeliveryMan.DeliveryMan_Map;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.Users;


public class Log_In extends AppCompatActivity {


    private EditText et_phoneNo;
    private EditText et_verfy_code;
    private Button btn_signIn;
    private Button btn_verifyCode;
    FirebaseAuth mAuth;
    private DatabaseReference table_user;
    private LinearLayout ll_signIn, ll_verifyCode;
    private String codeSent;
    private ProgressDialog mprogressbar;
    TextView tv_logo;

    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);

        et_phoneNo = (EditText) findViewById(R.id.let_phone);
        et_verfy_code = (EditText) findViewById(R.id.let_verify_code);
        btn_signIn = (Button) findViewById(R.id.btn_login_page);
        btn_verifyCode = (Button) findViewById(R.id.btn_login_verfy_code);
        ll_signIn = (LinearLayout) findViewById(R.id.layout_login_main);
        ll_verifyCode = (LinearLayout) findViewById(R.id.layout_login_verfyCode);

        //TODO LoG In

        tv_logo=(TextView)findViewById(R.id.tv_vc_logo_login);
        mprogressbar=new ProgressDialog(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        // Check Which Type of User Trying To Login Now
        Intent intent=getIntent();
        userType=intent.getStringExtra("UserType");
        if(userType.equals("ShopKeeper"))
        {
            table_user = database.getReference().child("ShopKeeperSide");
        }
        if(userType.equals("DeliveryMan"))
        {
            table_user = database.getReference().child("DeliveryManSide");
        }

        if(userType.equals("Customer"))
        {
            table_user = database.getReference().child("CustomerSide");
        }

// on  click sign in button
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginNow();
            }
        });

        // call to the confirmation of verification code
        btn_verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_verfy_code.getText().toString();
                String codeVerify=codeSent.getBytes().toString();
                Log.d("Verification Code=",codeSent);
                mprogressbar.setMessage("Verifying Code....");
                mprogressbar.show();
                if(!TextUtils.isEmpty(code)&& !TextUtils.isEmpty(codeSent)) {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

                    signInWithPhoneAuthCredential(credential);
                }

                else
                {
                    et_verfy_code.setError("Please Enter Verification Code");
                    mprogressbar.dismiss();
                    return;
                }
            }
        });

    }

    // Log in method which send verification code to user phone


    private void LoginNow() {
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get user in formation from fibase
                mprogressbar.setMessage("Sending Code......");
                mprogressbar.show();


                if (et_phoneNo.getText().toString().isEmpty()) {
                    et_phoneNo.setError("Please Enter Phone No");
                    et_phoneNo.requestFocus();
                    mprogressbar.dismiss(); 
                    return;
                }
                if (et_phoneNo.getText().toString().length() < 11 || et_phoneNo.getText().toString().length() > 13) {
                    et_phoneNo.setError(" Enter Correct Phone  No");
                    et_phoneNo.requestFocus();
                    mprogressbar.dismiss();
                    return;
                }

                // Check user exist in database  or not
                String phoneNumbertemp=et_phoneNo.getText().toString();
                if (dataSnapshot.child(phoneNumbertemp).exists()) {
                    ll_signIn.setVisibility(View.INVISIBLE);
                    ll_verifyCode.setVisibility(View.VISIBLE);
                    tv_logo.setText(tv_logo.getText().toString()+et_phoneNo.getText().toString());


                    Users user = dataSnapshot.child(et_phoneNo.getText().toString()).getValue(Users.class);
                    String phoneNumber = et_phoneNo.getText().toString();
                    Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();


                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Log_In.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                    mprogressbar.dismiss();


                } else {
                    et_phoneNo.setError("Phone No is Not Exist ! Sign Up Now");
                    et_phoneNo.requestFocus();
                    mprogressbar.dismiss();
                    return;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // save  sent code  through mcallback function
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
         public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            et_verfy_code.setError("Please Enter Correct Code");

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign In Successfull", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            mprogressbar.dismiss();
                            //if()
                         LoginSuccessfull();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Sign In Error", "signInWithCredential:failure", task.getException());
                            et_verfy_code.setError("Please Enter Correct verification code ");
                            mprogressbar.dismiss();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void LoginSuccessfull() {
        if(userType.equals("Customer"))
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(userType.equals("ShopKeeper"))
        {
            Intent intent = new Intent(getApplicationContext(), Shopkeeper_Page.class);
            startActivity(intent);
            finish();
        }
        if(userType.equals("DeliveryMan"))
        {
            Intent intent=new Intent(getApplicationContext(),DeliveryMan_Map.class);
            startActivity(intent);
            finish();
        }
    }

    public void goto_signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), Sign_Up.class);
        intent.putExtra("UserType",userType);
        startActivity(intent);
    }

  /*  @Override
    public void onBackPressed() {
        finish();
    }*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
