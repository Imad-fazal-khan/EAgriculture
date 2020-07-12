package orederondoor.com.projectorder.Customer.CustomerSecondPage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Map;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.Customer.ShoppingCart.ShoppingCart;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Log_In;


public class item_detail extends AppCompatActivity {

    String item_name, item_price, item_detail, item_image, userType, shop_phoneNo;
    TextView tv_item_name, tv_item_price, tv_item_detail;
    ImageView imageView;
    private DatabaseReference mdatabase;
    private StorageReference mStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userPhoneNo;
    ProgressDialog mProgrssBar;
    Spinner spinner_quantity;
    // The No of shop saved with the item in shopping cart
    String shop_phone,item_quantity;
    private DatabaseReference newPost;
    Button btn_updateItem;
    LinearLayout ll_addToCart,ll_updateItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        //Offline Data Access
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        tv_item_name = (TextView) findViewById(R.id.tv_itemDetail_item_name);
        tv_item_price = (TextView) findViewById(R.id.tv_itemDetail_item_price);
        tv_item_detail = (TextView) findViewById(R.id.tv_itemDetail_detail1);
        imageView = (ImageView) findViewById(R.id.image1);
        btn_updateItem=(Button) findViewById(R.id.btn_saveUpdated_itemQuantity);
        spinner_quantity = (Spinner) findViewById(R.id.spinner_quantity_customer);

        ll_addToCart=(LinearLayout) findViewById(R.id.ll_order_itemDetail);
        ll_updateItem=(LinearLayout)findViewById(R.id.ll_item_detail_updateItem);
        // Prgress Bar Intialization
        mProgrssBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        item_name = intent.getStringExtra("ItemName");
        item_price = intent.getStringExtra("ItemPrice");
        item_detail = intent.getStringExtra("ItemDetail");
        item_image = intent.getStringExtra("ItemImage");
        userType = intent.getStringExtra("ÜserType");
        shop_phoneNo = intent.getStringExtra("ShopPhone");
        Log.d("userType=", userType);


        mdatabase = FirebaseDatabase.getInstance().getReference().child("Customer_Side");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        tv_item_name.setText(item_name);
        tv_item_price.setText(item_price);
        tv_item_detail.setText(item_detail);
        Picasso.with(this).load(item_image).into(imageView);
        //TODO Spinner of Item Quantity

        TextView tv_buyNow, tv_addToCart;
        tv_addToCart = (TextView) findViewById(R.id.tv_action_AddToCart);
        tv_buyNow = (TextView) findViewById(R.id.tv_action_BuyNow);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.item_quantity, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner_quantity.setAdapter(adapter);


        // This If Else statement are for the view of Item detail to Customer if Shopkeeper want to view item then his order section is disable

        if (userType.equals("Shopkeeper")) {
           ll_addToCart.setVisibility(View.GONE);
        }
        else if(userType.equals("UpdateItem"))
        {
            ll_addToCart.setVisibility(View.GONE);
            ll_updateItem.setVisibility(View.VISIBLE);
        }
            else
        {
           ll_addToCart.setVisibility(View.VISIBLE);
        }

        // Add Item to cart or Order Item by Customer
        tv_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    //FirebaseUser user2=mAuth.getCurrentUser();
                   /* mProgrssBar.setMessage("adding...");
                    mProgrssBar.show();*/
                    String userId = mAuth.getCurrentUser().getUid();
                    userPhoneNo = mAuth.getCurrentUser().getPhoneNumber();
                    if (!spinner_quantity.getSelectedItem().equals("0")) {
                        item_quantity=spinner_quantity.getSelectedItem().toString().trim();

                        startPosting("addToCart");
                    } else {
                        mProgrssBar.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(item_detail.this, R.style.AlertDialogStyle)
                                .setTitle("Alert!")
                                .setMessage("Please Select Item  Quantity First  \n You have need to atleast 1 quantity")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();

                                    }
                                }).show();
                        mProgrssBar.dismiss();


                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), Log_In.class);
                    intent.putExtra("UserType", "Customer");
                    startActivity(intent);
                    finish();
                }


            }
        });

        // Add Item To Cart and Open Shopping Cart for order Now
        tv_buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    String userId = mAuth.getCurrentUser().getUid();
                    userPhoneNo = mAuth.getCurrentUser().getPhoneNumber();
                    String quantity = spinner_quantity.toString();
                    if (!spinner_quantity.getSelectedItem().equals("0")) {
                        item_quantity=spinner_quantity.getSelectedItem().toString().trim();

                        startPosting("buyNow");
                    } else {
                        mProgrssBar.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(item_detail.this, R.style.AlertDialogStyle)
                                .setTitle("Alert!")
                                .setMessage("Please Select Item  Quantity First  \n You have need to atleast 1 quantity")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();

                                    }
                                }).show();
                        mProgrssBar.dismiss();


                    }


                } else {
                    Intent intent = new Intent(getApplicationContext(), Log_In.class);
                    intent.putExtra("UserType", "Customer");
                    startActivity(intent);
                    finish();
                }


            }
        });

        //To Update the Item Quantity
        btn_updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    String userId = mAuth.getCurrentUser().getUid();
                    userPhoneNo = mAuth.getCurrentUser().getPhoneNumber();
                    String quantity = spinner_quantity.toString();
                    if (!spinner_quantity.getSelectedItem().equals("0")) {
                        item_quantity=spinner_quantity.getSelectedItem().toString().trim();

                        updateItem();
                    } else {
                        mProgrssBar.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(item_detail.this, R.style.AlertDialogStyle)
                                .setTitle("Alert!")
                                .setMessage("Please Select Item  Quantity First  \n You have need to atleast 1 quantity")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();

                                    }
                                }).show();
                        mProgrssBar.dismiss();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Log_In.class);
                    intent.putExtra("UserType", "Customer");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // On Click Update The Quantity of Item in Customer Shopping Cart
    private void updateItem() {

        newPost = mdatabase.child("ShoppingCart").child(userPhoneNo);
        newPost.child(item_name).child("Title").setValue(item_name);
        newPost.child(item_name).child("Price").setValue(item_price);
        newPost.child(item_name).child("Description").setValue(item_detail);
        newPost.child(item_name).child("Image").setValue(item_image);
        newPost.child(item_name).child("ShopNo").setValue(shop_phoneNo);
        newPost.child(item_name).child("Quantity").setValue(item_quantity);
        mProgrssBar.dismiss();

        Toast.makeText(this, "Item is Added in Shopping Cart Successfuly", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
        startActivity(intent);
        finish();
    }


// This function is for Adding Customer item to Shopping Cart
    private void startPosting(final String type) {

        if (!TextUtils.isEmpty(item_name) && !TextUtils.isEmpty(item_price) && !TextUtils.isEmpty(item_detail)) {


            newPost = mdatabase.child("ShoppingCart").child(userPhoneNo);


            // The condtion for the item is existing in cart and now adding is same or not


            newPost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {


                        for (DataSnapshot shop_items : dataSnapshot.getChildren()) {
                            String item_key = shop_items.getKey();
                            Map<String, String> map = (Map<String, String>) shop_items.getValue();
                            //Shop user = childSnapshot.getValue(Shop.class);
                            //String item_name = map.get("item_name");
                            shop_phone = map.get("ShopNo");
                        }

                        checkItemNoSame(type);
                    }
                    else
                        checkItemNoSame(type);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(item_detail.this, "Database Error", Toast.LENGTH_SHORT).show();

                }
            });


        } else {
            Toast.makeText(this, "Sorry!\n   Your Data is not Ready for Upload .", Toast.LENGTH_LONG).show();
            mProgrssBar.dismiss();
            return;
        }
    }


    // Add Same Shop item in shopping cart
    public void checkItemNoSame(String type) {
        mProgrssBar.setMessage("adding...");
        mProgrssBar.show();

        if (shop_phone == null) {

            newPost.child(item_name).child("Title").setValue(item_name);
            newPost.child(item_name).child("Price").setValue(item_price);
            newPost.child(item_name).child("Description").setValue(item_detail);
            newPost.child(item_name).child("Image").setValue(item_image);
            newPost.child(item_name).child("ShopNo").setValue(shop_phoneNo);
            newPost.child(item_name).child("Quantity").setValue(item_quantity);
            mProgrssBar.dismiss();

            Toast.makeText(this, "Item is Added in Shopping Cart Successfuly", Toast.LENGTH_SHORT).show();
            if (type.equals("buyNow")) {
                Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);

                startActivity(intent);
                finish();
            }

            finish();


        } else if (shop_phone.equals(shop_phoneNo)) {

            newPost.child(item_name).child("Title").setValue(item_name);
            newPost.child(item_name).child("Price").setValue(item_price);
            newPost.child(item_name).child("Description").setValue(item_detail);
            newPost.child(item_name).child("Image").setValue(item_image);
            newPost.child(item_name).child("ShopNo").setValue(shop_phoneNo);
            newPost.child(item_name).child("Quantity").setValue(item_quantity);
            mProgrssBar.dismiss();

            Toast.makeText(this, "Item is Added in Shopping Cart Successfuly", Toast.LENGTH_SHORT).show();
            if (type.equals("buyNow")) {
                Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(intent);
                finish();
            }
            finish();

        } else {
            mProgrssBar.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(item_detail.this, R.style.AlertDialogStyle)
                    .setTitle("Alert!")
                    .setMessage("1 . You can not add diffrent shops item in shopping cart\n 2. You can only order one shop at a time\n" +
                            "3. so Add item with same shop\n" +
                            "4. Go to Shoppig cart and remove other items then add these items\n" +
                            "5. Thank You for Your concentration \n")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    }).show();
            mProgrssBar.dismiss();
            //finish();

        }
    }
}