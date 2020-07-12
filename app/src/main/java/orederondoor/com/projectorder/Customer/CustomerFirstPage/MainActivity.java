package orederondoor.com.projectorder.Customer.CustomerFirstPage;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.support.v7.widget.SearchView;
import me.relex.circleindicator.CircleIndicator;
import orederondoor.com.projectorder.Customer.ExtrasClases.MyApplication;
import orederondoor.com.projectorder.Customer.ShoppingCart.ShoppingCart;
import orederondoor.com.projectorder.DeliveryMan.DeliveryMan_Map;
import orederondoor.com.projectorder.DeliveryMan.DriverLoginActivity;
import orederondoor.com.projectorder.DeliveryMan.DriverMapActivity;
import orederondoor.com.projectorder.DeliveryMan.DriversMapActivity;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Log_In;
import orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Shopkeeper_Page;
import retrofit2.http.Url;

import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,SearchView.OnQueryTextListener {
    Context context;
    public static int notificationCountCart = 0;
    Button btn_shopkeeper, btn_deleveryMan;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef;
    //TODO Customer First Page

    List<Shop> shopList=new ArrayList<>();
    private ArrayList<Shop> itemList = new ArrayList<>();
    private ImageView shoppingCart;
    Button btn_became_shopkeeper, btn_became_captain,btn_allCatagory;
    List<Shop> list;
FirebaseAuth mAuth;
    // image Slding
    private static ViewPager mPager;
    private static int currentPage = 0;
    private URL urllogo;
    private static final String url="https://shopkeepeside.firebaseio.com/ShopKeeperSide";
    private static final Integer[] XMEN = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private ShopAdapter myAdapter;
SearchView searchView;
Button btn_fashion,btn_kitchen,btn_toys;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageSliding();
        //database.setPersistenceEnabled(true);
        myRef = database.getReference().child("ShopKeeperSide");
        mAuth=FirebaseAuth.getInstance();


// Offline Data Access


        btn_became_shopkeeper = (Button) findViewById(R.id.btn_become_shopkeeper);
        btn_became_captain = (Button) findViewById(R.id.btn_became_deleveryMan);


        btn_allCatagory=(Button)findViewById(R.id.btn_mainActivity_allCatagory);
        btn_kitchen=(Button)findViewById(R.id.btn_mainActivity_kitchen);
        btn_fashion=(Button)findViewById(R.id.btn_mainActivity_fashiom);
        btn_toys=(Button)findViewById(R.id.btn_mainActivity_toys);

        btn_became_shopkeeper.setOnClickListener(this);
        btn_became_captain.setOnClickListener(this);

        btn_allCatagory.setOnClickListener(this);
        btn_kitchen.setOnClickListener(this);
        btn_toys.setOnClickListener(this);
        btn_fashion.setOnClickListener(this);

        init();
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_mainactivity);
        progressBar.setVisibility(ProgressBar.VISIBLE);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RecyclerView
        RecyclerView myrv = (RecyclerView) findViewById(R.id.main_recycler_view);
        myAdapter = new ShopAdapter(this, itemList);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);
        /* return v; */

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // SearchView searchView = (SearchView) searchItem.getActionView();
        android.support.v7.widget.SearchView searchView=(android.support.v7.widget.SearchView) searchItem.getActionView();
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


    // on Back Pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Shopclicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if(id==R.id.action_search)
        {
            return true;
        }
*/
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fashion) {
            // Handle the camera action
            shopTypeSelected("Fashion");

        } else if (id == R.id.nav_all_catagory) {
            init();

        } else if (id == R.id.nav_toys) {
            shopTypeSelected("Toys");

        } else if (id == R.id.nav_kitchen_items) {
            shopTypeSelected("Kitchen");

        } else if (id == R.id.nav_login) {
            Intent intent=new Intent(getApplicationContext(),Log_In.class);
            intent.putExtra("UserType","Customer");
            startActivity(intent);
            finish();

        }   if (id == R.id.nav_logout) {

            if (mAuth.getCurrentUser() != null)
                Toast.makeText(getApplicationContext(), "Log Out Successfully", Toast.LENGTH_LONG).show();
                mAuth.signOut();

            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Init Function for the sliding of images
    private void init() {

        // Onselected All Catagory
        if(itemList.size()>0) {
            itemList.removeAll(itemList);

            myAdapter.notifyDataSetChanged();
        }
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again


                // whenever data at this location is updated.
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String phoneNo = childSnapshot.getKey().toString();

                    for (DataSnapshot childrenofNo : childSnapshot.getChildren()) {

                        // list = new ArrayList<Shop>();
                        String key = childrenofNo.getKey();
                        if (key.equals("Shop_Info")) {
                            Map<String, String> map = (Map<String, String>) childrenofNo.getValue();
                            //Shop user = childSnapshot.getValue(Shop.class);
                            String shop_name = map.get("shop_Name");
                            String shop_image = map.get("shop_image");
//                            String shop_location_ = map.get("shop_Location ");
                            String shop_type = map.get("shop_Type ");

                            itemList.add(new Shop(shop_name, shop_type, "", shop_image, phoneNo));

                        }
                        //lurl=childrenofNo.child("Shop_Info").toString();
                    }
                    myAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("OnCancelled", "Failed to read value.", error.toException());
            }
        });


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btn_become_shopkeeper) {
            startActivity(new Intent(getApplicationContext(), Shopkeeper_Page.class));
        }
        if (id == R.id.btn_became_deleveryMan) {
            // Go to DeleveryMan Side
            Intent intent=new Intent(getApplicationContext(),DriverMapActivity.class);
            startActivity(intent);
        }
        if(id==R.id.btn_mainActivity_kitchen)
        {
shopTypeSelected("Kitchen");
        }
        if(id==R.id.btn_mainActivity_toys)
        {
            shopTypeSelected("Toys");
        }
        if(id==R.id.btn_mainActivity_fashiom)
        {
            shopTypeSelected("Fashion");
        }
        if(id==R.id.btn_mainActivity_allCatagory)
        {
            init();
        }
    }

    // if Kitchen Type selected then
    public void shopTypeSelected(final String s)
    {


        int size = itemList.size();

       // itemList.clear();
        itemList.removeAll(itemList);
      //  itemList.notifyAll();
        myAdapter.notifyDataSetChanged();

        if(shopList.size()>0)
        {
            shopList.removeAll(shopList);
            myAdapter.notifyDataSetChanged();
        }


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again

                // whenever data at this location is updated.
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String phoneNo = childSnapshot.getKey();

                    for (DataSnapshot childrenofNo : childSnapshot.getChildren()) {

                        // list = new ArrayList<Shop>();
                        String key = childrenofNo.getKey();
                        if (key.equals("Shop_Info")) {

                            Map<String, String> map = (Map<String, String>) childrenofNo.getValue();
                            String shop_type = map.get("shop_Type ");
                            if(shop_type.equals(s)) {
                                //Shop user = childSnapshot.getValue(Shop.class);
                                String shop_name = map.get("shop_Name");
                                String shop_image = map.get("shop_image");
//                                String shop_location_ = map.get("shop_Location ");


                                itemList.add(new Shop(shop_name, shop_type, "", shop_image, phoneNo));
                            }
                            else
                            {

                            }

                        }
                        //lurl=childrenofNo.child("Shop_Info").toString();
                    }
                    myAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("OnCancelled", "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput=newText.toLowerCase();

        List<Shop> shopList=new ArrayList<>();

        for(Shop shop : itemList)
        {
            if(shop.getShop_name().toLowerCase().contains(userInput))
            {
                shopList.add(shop);

            }
        }
        myAdapter.updateList(shopList);

        return false;
    }


    public void imageSliding()
    {
        // Image Sliding on the page



        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImageSlider_Adapter(MainActivity.this, XMENArray));
        final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        /*indicator.setScaleX(5);
        indicator.setScaleY(5);*/
        indicator.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                indicator.setAccessibilityLiveRegion(view.getId());
            }
        });

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }
}
