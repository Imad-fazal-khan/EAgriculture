package orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.Customer.CustomerFirstPage.Shop;
import orederondoor.com.projectorder.Customer.CustomerSecondPage.ItemAdapter;
import orederondoor.com.projectorder.Customer.CustomerSecondPage.SecondActivity;
import orederondoor.com.projectorder.Customer.ShoppingCart.ShoppingCart;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.Item_Details;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.ShopItem_Adapter;


public class My_Shop extends AppCompatActivity {
    private ArrayList<Shop> itemList = new ArrayList<>();
    ItemAdapter myAdapter;
    String shop_phoneNo, shop_name, shop_location, shop_type, shop_image;
    TextView tv_shop_name, tv_shop_type, tv_shop_location;
    Button btn_contact;
    ImageView shopImage;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, currentUserdata;

    ProgressBar progressBar;
    Toolbar toolbar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__shop);
        toolbar = (Toolbar) findViewById(R.id.tb_myShop_shopkeeper);
        setSupportActionBar(toolbar);

        tv_shop_name = (TextView) findViewById(R.id.tv_activity2_shopName);
        tv_shop_location = (TextView) findViewById(R.id.tv_activity2_location);
        tv_shop_type = (TextView) findViewById(R.id.tv_activity2_shopType);
        shopImage = (ImageView) findViewById(R.id.iv_activity2_shopDet);
        btn_contact = (Button) findViewById(R.id.btn_activity2_contact);


        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference().child("ShopKeeperSide");

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_secondActivity);
        progressBar.setVisibility(View.VISIBLE);

        // getting phone number on which shop click
        final Intent intent = getIntent();

        shop_phoneNo = mAuth.getCurrentUser().getPhoneNumber();
        Log.d("Shop_Phine=", shop_phoneNo);


        // Call to  Number
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  String phoneNo = mPhoneNoEt.getText().toString();


                final Dialog dialog = new Dialog(My_Shop.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog_contact);
                // Set dialog title
                dialog.setTitle("Custom Dialog");

                // set values for custom dialog components - text, image and button
                FloatingActionButton btnCal = (FloatingActionButton) dialog.findViewById(R.id.fb_call);

                FloatingActionButton btn_msg = (FloatingActionButton) dialog.findViewById(R.id.fb_message);

                dialog.show();


                btnCal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(shop_phoneNo)) {
                            String dial = "tel:" + shop_phoneNo;
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid" + " phone number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btn_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendMessage = new Intent(Intent.ACTION_VIEW);
                        String message = "sms:" + shop_phoneNo;
                        sendMessage.setData(Uri.parse(message));
                        startActivity(sendMessage);
                    }
                });


            }
        });


        // Special Recycler View for Firebase in android Studio


      /*  mdatabase=FirebaseDatabase.getInstance();
        mRef=mdatabase.getReference();*/

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again


                // whenever data at this location is updated.
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String phoneNo = childSnapshot.getKey();
                    if (shop_phoneNo.equals(phoneNo)) {
                        for (DataSnapshot childrenofNo : childSnapshot.getChildren()) {

                            // list = new ArrayList<Shop>();
                            String key = childrenofNo.getKey();
                            if (key.equals("Shop_Info")) {
                                Map<String, String> maps = (Map<String, String>) childrenofNo.getValue();
                                shop_name = maps.get("shop_Name");
                                shop_type = maps.get("shop_Type ");
                                //    shop_location=maps.get("shop_Location ");
                                shop_image = maps.get("shop_image");


                                tv_shop_type.setText(shop_type);
                                tv_shop_name.setText(shop_name);
                                tv_shop_location.setText(shop_location);
                                if (shop_image.length() > 12) {

                                    Picasso.with(getApplicationContext()).load(shop_image).into(shopImage);
                                } else {
                                    shopImage.setImageResource(R.drawable.loading_logo);
                                }

                            }
                            if (key.equals("Shop_Items")) {
                                for (DataSnapshot shop_items : childrenofNo.getChildren()) {
                                    String item_key = shop_items.getKey();
                                    Log.d("Item_Name", item_key);
                                    Map<String, String> map = (Map<String, String>) shop_items.getValue();
                                    //Shop user = childSnapshot.getValue(Shop.class);
                                    //String item_name = map.get("item_name");
                                    String item_image = map.get("item_image");
                                    String item_detail = map.get("item_detail ");
                                    String item_price = map.get("item_price");

                                    itemList.add(new Shop(item_key, item_detail, item_price, item_image, "Shopkeeper", 1));
                                    Log.d("ShopType12", shop_items.getKey().toString());


                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("OnCancelled", "Failed to read value.", error.toException());
            }
        });


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerView_my_shop);
        myAdapter = new ItemAdapter(this, itemList);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);
        /* return v; */


    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {*/
        super.onBackPressed();
        // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myshop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Shopclicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
