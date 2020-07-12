package orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.developers.imagezipper.ImageZipper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import io.grpc.Compressor;
import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.R;

import static android.media.MediaRecorder.VideoSource.CAMERA;


public class Post_Data_ToFirebase extends AppCompatActivity {
    private ImageButton imageButton;
    EditText et_name, et_descrip, et_price, et_quantity;
    private Button btn_saved_data;
    private Uri imageUri = null;
    private ProgressDialog mprogressBar;
    private static final int Gallery_Request = 1;
    private DatabaseReference mdatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String userPhoneNo;
    Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__data__to_firebase);
        et_name = (EditText) findViewById(R.id.et_post_title);
        et_descrip = (EditText) findViewById(R.id.et_description_post);
        et_price = (EditText) findViewById(R.id.et_post_price);
        et_quantity = (EditText) findViewById(R.id.et_post_quantity);
        btn_saved_data = (Button) findViewById(R.id.btn_save_data_toDatabase);
        mAuth = FirebaseAuth.getInstance();

        //Offline data
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("ShopKeeperSide");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mprogressBar = new ProgressDialog(this);
        imageButton = (ImageButton) findViewById(R.id.ib_take_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getgallery = new Intent(Intent.ACTION_GET_CONTENT);
                getgallery.setType("image/*");
                startActivityForResult(getgallery, Gallery_Request);


            }
        });
        btn_saved_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
        FirebaseUser user2 = mAuth.getCurrentUser();
       /* String userId = mAuth.getCurrentUser().getUid();
        userPhoneNo = mAuth.getCurrentUser().getPhoneNumber();*/
       userPhoneNo="+923123132285";


    }


    // send data to firebase
    private void startPosting() {

        mprogressBar.setMessage("Uploading .....");
        mprogressBar.show();
        final String name_val = et_name.getText().toString().trim();
        final String des_val = et_descrip.getText().toString().trim();
        final String price_val = et_price.getText().toString().trim();
        final String quantity_val = et_quantity.getText().toString().trim();

        if (!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(des_val) && !TextUtils.isEmpty(price_val) && !TextUtils.isEmpty(quantity_val) && imageUri != null) {

            mprogressBar.dismiss();
            mprogressBar.show();
            final DatabaseReference newPost = mdatabase.child(userPhoneNo).child("Shop_Items");

            final StorageReference childRef = mStorageRef.child("Shopkeeper_Data").child(name_val + ".image").child(imageUri.getLastPathSegment());

            //uploading the image
            Log.d("ImageURI",imageUri.toString());
            //UploadTask uploadTask = childRef.putFile(imageUri);

            childRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            newPost.child(name_val).child("item_image").setValue(downloadUrl.toString());
                            newPost.child(name_val).child("item_title").setValue(name_val);
                            newPost.child(name_val).child("item_price").setValue(price_val.trim());
                            newPost.child(name_val).child("item_detail ").setValue(des_val);
                            newPost.child(name_val).child("item_quantity").setValue(quantity_val);
                            mprogressBar.dismiss();
                            AlertDialog dialog=new AlertDialog.Builder(Post_Data_ToFirebase.this)
                                    .setTitle("Uploaded")
                                    .setMessage("Did You Want to Add More Item \n\n\n")
                                    .setPositiveButton("Add More", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),Post_Data_ToFirebase.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    })
                                    .setNegativeButton("No Exit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(getApplicationContext(),Shopkeeper_Page.class);
                                            startActivity(intent);
                                            finish();


                                        }
                                    }).show();
                            //Do what you want with the url
                        }
                        // Task<Uri> u = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        //  Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();

                    });
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
        } else {
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


    }



}
