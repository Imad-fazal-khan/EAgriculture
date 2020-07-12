package orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.Customer.CustomerFirstPage.RecyclerViewShop;
import orederondoor.com.projectorder.Customer.CustomerFirstPage.Shop;
import orederondoor.com.projectorder.Customer.CustomerSecondPage.ItemAdapter;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.RecyclerViewListner;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.Shopkeeper_Data;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.Shopkeeper_dataAdapter;


public class Shopkeeper_Page extends AppCompatActivity implements View.OnClickListener, RecyclerViewShop, android.support.v7.widget.SearchView.OnQueryTextListener {
    // private List<Shopkeeper_Data> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Shopkeeper_dataAdapter mAdapter;
    private SearchView searchView;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogressBar;
    FirebaseStorage mstorageReference;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private TextView tv_item_name, tv_item_description, tv_item_price;
    private Button btn_add_item_to_shop;
    FirebaseUser currentUser;
    private String userPhoneNo;
    private ImageView iv_shop;
    private TextView tv_shop, tv_shop_notExist;
    Button addNewShop, addNewItem, goToShop;
    LinearLayout linearLayout_addItem;
    DatabaseReference mRef;
    private ArrayList<Shopkeeper_Data> itemList = new ArrayList<>();
    Shopkeeper_dataAdapter myAdapter;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_page);
        // set toolbar to app
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.tb_shopkeeperPage_shopkeeper);
        setSupportActionBar(mTopToolbar);
        searchView = (SearchView) findViewById(R.id.sv_shopkeeper_page);
        // searchView.setIconifiedByDefault(false);

        mAuth = FirebaseAuth.getInstance();
        mprogressBar = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_shopkeeper);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("ShopKeeperSide");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference().child("DefaultItems");

        goToShop = (Button) findViewById(R.id.btn_goTo_YourShop);
        // btn_add_item_to_shop = (Button) findViewById(R.id.btn_add_item_toShop);
        // btn_add_item_to_shop.setOnClickListener(this);


        // For Just For Check without Authentcation Fix phne Number
        //userPhoneNo="+9223123132285";


        // Check user is login or not before open the shop
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //FirebaseUser user2=mAuth.getCurrentUser();

            String userId = mAuth.getCurrentUser().getUid();
            userPhoneNo = mAuth.getCurrentUser().getPhoneNumber();
            mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(userPhoneNo).child("Shop_Info").child("shop_Name").exists()) {

                        return;
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Shop_Info.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //  final Shopkeeper_Data data = movieList.get(position);
            //final String name_val=data.getName();


        } else {
            Intent intent = new Intent(getApplicationContext(), Log_In.class);
            intent.putExtra("UserType", "ShopKeeper");
            startActivity(intent);
            finish();
        }


        addNewItem = (Button) findViewById(R.id.btn_addNew_item_toShop);


        // addNewShop.setOnClickListener(this);
        addNewItem.setOnClickListener(this);
        goToShop.setOnClickListener(this);


        // Get All Default Item data saved By App Owner  to set RecyclerView

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String item_key = childSnapshot.getKey();
                    Log.d("Item_Name", item_key);
                    Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    //String item_name = map.get("item_name");
                    String item_image = map.get("item_image");
                    String item_detail = map.get("item_detail");
                    String item_price = map.get("item_price");
                    itemList.add(new Shopkeeper_Data(item_key, item_detail, item_price, item_image));
                    //    Log.d("ShopType12", shop_items.getKey().toString());
                }
                myAdapter.notifyDataSetChanged();
                //   progressBar.setVisibility(View.GONE)
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("OnCancelled", "Failed to read value.", error.toException());
            }
        });

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_view_shopkeeper);
        myAdapter = new Shopkeeper_dataAdapter(this, itemList, this);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        myrv.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_shopkeeper, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search1);
        // SearchView searchView = (SearchView) searchItem.getActionView();
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener((android.support.v7.widget.SearchView.OnQueryTextListener) this);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //TODO SearchView
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recreate();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       /* FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);*/
    }

    // menu item in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signOut) {
            if (mAuth.getCurrentUser() != null)
                mAuth.signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_update) {
            Intent intent = new Intent(getApplicationContext(), Shop_Info.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // on click listener

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_goTo_YourShop) {
            Intent intent = new Intent(getApplicationContext(), My_Shop.class);
            startActivity(intent);

        }
        if (id == R.id.btn_addNew_item_toShop) {
            Intent intent = new Intent(getApplicationContext(), Post_Data_ToFirebase.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClickItem(View view, int position) {
        mprogressBar.setTitle("uploading....");
        mprogressBar.setMessage("uploading....");
        mprogressBar.show();
        final Shopkeeper_Data data = itemList.get(position);
        final String name_val = data.getName();
        final String price_val = data.getPrice();
        final String des_val = data.getDescription();
        String image = data.getImage();
        Uri imageUri = Uri.parse(image);

        DatabaseReference newPost = mdatabase.child(userPhoneNo).child("Shop_Items");
        newPost.child(name_val).child("item_image").setValue(imageUri.toString());
        newPost.child(name_val).child("item_title").setValue(name_val);
        newPost.child(name_val).child("item_price").setValue(price_val);
        newPost.child(name_val).child("item_detail ").setValue(des_val);
        mprogressBar.dismiss();
    }

    // Item Search Query

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();

        List<Shopkeeper_Data> shopList = new ArrayList<>();

        for (Shopkeeper_Data shop : itemList) {
            if (shop.getName().toLowerCase().contains(userInput)) {
                shopList.add(shop);

            }
        }
        myAdapter.updateList(shopList);

        return false;
    }


}
