package orederondoor.com.projectorder.Customer.ShoppingCart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.Customer.CustomerSecondPage.SecondActivity;
import orederondoor.com.projectorder.Customer.ExtrasClases.EmptyCart;
import orederondoor.com.projectorder.DeliveryMan.CustomerMapActivity;
import orederondoor.com.projectorder.DeliveryMan.DriversMapActivity;
import orederondoor.com.projectorder.OnRefresh;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Log_In;


public class ShoppingCart extends AppCompatActivity {
   Context mContext=this;
    RecyclerView mrecyclerView;
   public CartAdapter mAdapter;
   TextView tv_price;
    ArrayList<ItemsData> dataList=new ArrayList<>();
    double price=0.0;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userPhoneNo;
    TextView tv_order;
   // TextView tv_
//Context mcontext=this.getApplicationContext();
   //ArrayList < ItemsData> data;
   DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        mAuth=FirebaseAuth.getInstance();
        tv_order=(TextView)findViewById(R.id.tv_order_cartItem);
        currentUser=mAuth.getCurrentUser();
        if(currentUser!=null) {
            userPhoneNo=currentUser.getPhoneNumber();

            init();
        }
        else
        {
            Intent intent=new Intent(getApplicationContext(),Log_In.class);
            intent.putExtra("UserType","Customer");
            startActivity(intent);
        }

    }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
       finish();

    }
public  void delete(final String s) {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mdata= database.getReference().child("Customer_Side").child("ShoppingCart");

    mdata.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                for(DataSnapshot remove: appleSnapshot.getChildren()) {
                    if ((remove.getKey().trim()).equals(s.trim())) {
                        remove.getRef().removeValue();
                    }
                }

            }

        }



        @Override
        public void onCancelled(DatabaseError databaseError) {
           Log.d("onCancelled", databaseError.toException().toString());
        }
    });


}


private void init() {


    tv_price = (TextView) findViewById(R.id.tv_total_cardItem);
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    myRef = database.getReference().child("Customer_Side").child("ShoppingCart");
    //TODO Shopping Cart Activity

    // Retrive data from firebase and set to recyclerView

    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            // This method is called once with the initial value and again
            if (dataSnapshot.getValue() == null) {
                finish();
                Intent intent = new Intent(getApplicationContext(), EmptyCart.class);
                startActivity(intent);
            }
            dataList.clear();
            price = 0.0;
            //  mAdapter.notifyDataSetChanged();
            // whenever data at this location is updated.
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                String phoneNo = childSnapshot.getKey();
                double itemPrice = 0.0;

                for (DataSnapshot shop_items : childSnapshot.getChildren()) {
                    String item_key = shop_items.getKey();
                    Map<String, String> map = (Map<String, String>) shop_items.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    //String item_name = map.get("item_name");
                    String item_image = map.get("Image");
                    String item_detail = map.get("Description");
                    String item_price = map.get("Price");
                    String item_quantity=map.get("Quantity");
                    String shop_no=map.get("ShopNo");
                    dataList.add(new ItemsData(item_key, item_price, item_image, item_detail,item_quantity,shop_no,4));

                    itemPrice = Double.parseDouble(item_price.trim());
                    price = price + itemPrice;


                }

                tv_price.setText("Total= " + ((Double) price).toString());


                if(mAdapter!=null) {
                    mAdapter.notifyDataSetChanged();
                }
                //   progressBar.setVisibility(View.GONE);


            }
            if(mAdapter!=null) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("OnCancelled", "Failed to read value.", error.toException());
        }
    });


    RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_shoppingCart);

    if(dataList.size()==0){

    }else {
        mAdapter = new CartAdapter(this, dataList, new OnRefresh() {
            @Override
            public void refresh(boolean refresh) {
                if (refresh)
                    init();
            }
        });
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(mAdapter);
    }
    /* return v; */
    // Call to  Number
    tv_order.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //  String phoneNo = mPhoneNoEt.getText().toString();

            //TODO Order Confirm Dialog
            final Dialog dialog = new Dialog(ShoppingCart.this);
            // Include dialog.xml file
            dialog.setContentView(R.layout.dialog_payment);
            // Set dialog title
            dialog.setTitle("Payment");

            // set values for custom dialog components - text, image and button

            TextView tv_itemPrice,tv_driverRent,tv_totalPrice;
            Button btn_cancel,btn_confirm;
            tv_itemPrice=(TextView)dialog.findViewById(R.id.tv_total_of_item_price);
            tv_driverRent=(TextView)dialog.findViewById(R.id.tv_rent_ofDriver);
            tv_totalPrice=(TextView)dialog.findViewById(R.id.tv_totla_amount_pay);
            btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel_payment);
            btn_confirm=(Button)dialog.findViewById(R.id.btn_confirm_payment);

            tv_itemPrice.setText(tv_itemPrice.getText().toString() + "        "+ ((Double) price).toString());
            double driverRent=123.0;
            tv_driverRent.setText(tv_driverRent.getText().toString()+"      "+ ((Double) driverRent).toString());
            //double itemPrice= Double.valueOf(tv_price.getText().toString().trim());

            double total=driverRent+price;


            tv_totalPrice.setText(tv_totalPrice.getText().toString()+"      "+ ((Double) total).toString());
                    dialog.show();


            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),CustomerMapActivity.class);
                    startActivity(intent);
                    finish();

                }
            });


        }
    });


}


}