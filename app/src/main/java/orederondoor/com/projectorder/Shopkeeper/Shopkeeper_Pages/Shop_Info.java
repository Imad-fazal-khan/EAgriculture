package orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.DeliveryMan.DeliveryMan_Map;
import orederondoor.com.projectorder.R;

public class Shop_Info extends AppCompatActivity {
    EditText et_shopName, et_shopLocation;
    Spinner et_shopType;
    DatabaseReference mdatabase;
    String shopName, shpLoaction, shopType;
    ImageButton imageButton;
    String phonNo;
    private Uri imageUri = null;
    private ProgressDialog mprogressBar;
    private static final int Gallery_Request = 1;
    FirebaseAuth mAuth;
    String lat, longt;
    LatLng map;
    public static final int IntentId = 12; //Intent Request Code
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop__info);

        final Button btn_saved;
        mdatabase = FirebaseDatabase.getInstance().getReference().child("ShopKeeperSide");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mprogressBar = new ProgressDialog(this);


        et_shopName = (EditText) findViewById(R.id.et_shopInfo_shopName);
        et_shopType = (Spinner) findViewById(R.id.et_shopinfo_shopType);
        et_shopLocation = (EditText) findViewById(R.id.et_shopInfo_shopLocation);
        imageButton = (ImageButton) findViewById(R.id.ib_select_shopPic);

        // OnClick on Button Open Galary to Pick image for Shop
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getgallery = new Intent(Intent.ACTION_GET_CONTENT);
                getgallery.setType("image/*");
                startActivityForResult(getgallery, Gallery_Request);


            }
        });
        // Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shop_Type, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        et_shopType.setAdapter(adapter);

        et_shopLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(Shop_Info.this, DeliveryMan_Map.class);

                startActivityForResult(intent, IntentId);
                return true;

            }
        });


        et_shopLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Shop_Info.this, DeliveryMan_Map.class);

                startActivityForResult(intent, IntentId);


            }
        });


        btn_saved = (Button) findViewById(R.id.btn_shopInfo_save);
        btn_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userId = mAuth.getCurrentUser().getUid();
                phonNo = mAuth.getCurrentUser().getPhoneNumber();


                startPosting();


            }
        });


    }


    // send data to firebase
    private void startPosting() {

        mprogressBar.setMessage("Uploading .....");
        mprogressBar.show();
        shopName = et_shopName.getText().toString().trim();
        shopType = et_shopType.getSelectedItem().toString().trim();



        /*longt = DeliveryMan_Map.longitude;
        lat = DeliveryMan_Map.latitude;*/
  /*      Log.d("Longitude ", longt);
        Log.d("Latitude=", lat);*/


        if (!TextUtils.isEmpty(shopName) && !TextUtils.isEmpty(shopType) /*&& !TextUtils.isEmpty(shpLoaction)*/ && imageUri != null) {

            mprogressBar.dismiss();
            mprogressBar.show();
            //final DatabaseReference newPost = mdatabase.child(phonNo).child("Shop_Items");

            final StorageReference childRef = mStorageRef.child("ShopImages").child(imageUri.getLastPathSegment());

            //uploading the image
            //UploadTask uploadTask = childRef.putFile(imageUri);

            childRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            mdatabase.child(phonNo).child("Shop_Info").child("shop_image").setValue(downloadUrl.toString());
                            mdatabase.child(phonNo).child("Shop_Info").child("shop_Name").setValue(shopName);
                            mdatabase.child(phonNo).child("Shop_Info").child("shop_Type ").setValue(shopType);
                            mdatabase.child(phonNo).child("Shop_Info").child("shop_Location ").child("latitude").setValue(lat);
                            mdatabase.child(phonNo).child("Shop_Info").child("shop_Location ").child("longitude").setValue(longt);
                            Toast.makeText(Shop_Info.this, "Successfully Doone", Toast.LENGTH_LONG).show();
                        }

                        //Do what you want with the url}
                        // Task<Uri> u = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        //  Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();

                    });
                    mprogressBar.dismiss();
                }
            })

                    /*    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                newPost.child(name_val).child("Image").setValue(downloadUrl.toString());


                                Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
                            }
                        })*/.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mprogressBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else

        {
            Toast.makeText(getApplicationContext(), "Select an image", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Request && resultCode == RESULT_OK) {
            imageUri = data.getData();


            imageButton.setImageURI(imageUri);

        }
        if (requestCode == IntentId) {
            if (resultCode == RESULT_OK) {

               // Intent intent=getIntent();
                lat=data.getStringExtra("Latitude");
                Log.d("Latitude=",lat);
                longt=data.getStringExtra("Longitude");
                Log.d("Lngitude=",longt);

                //If result code is OK then get String extra and set message
                //  String message = data.getStringExtra("message");
                // resultMessage.setText(message);
            }

            if (resultCode == RESULT_CANCELED)

                //When result is cancelled display toast
                Toast.makeText(getApplicationContext(), "Activity cancelled.", Toast.LENGTH_SHORT).show();


        }


    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is IntentId
        if (requestCode == IntentId) {
            if (resultCode == RESULT_OK) {

                //If result code is OK then get String extra and set message
              //  String message = data.getStringExtra("message");
               // resultMessage.setText(message);
            }

            if (resultCode == RESULT_CANCELED)

                //When result is cancelled display toast
                Toast.makeText(getApplicationContext(), "Activity cancelled.", Toast.LENGTH_SHORT).show();


        }
    }*/

}
