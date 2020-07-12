package orederondoor.com.projectorder.DeliveryMan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Log_In;

public class DriversMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener

{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    FirebaseAuth mAuth;
    Button btn_logout,btn_setting;
    FirebaseUser currentUser;
    private Boolean currentLogOutDriverStatus=false;
   // String currentUser="+923123132285";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_map);
        btn_logout=(Button)findViewById(R.id.btn_drivermap_logOut);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent intent=new Intent(this,Log_In.class);
            intent.putExtra("UserType","DeliveryMan");
            startActivity(intent);
            finish();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLogOutDriverStatus=true;
                disconnectDriver();
                mAuth.signOut();
                logOutDriver();
            }
        });
    }

    private void logOutDriver()
    {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation=location;
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        // Check availability of driver
       // String userID=currentUser.getUid();
        String userID="+923123132285";
        DatabaseReference driverAvalabilityRef=FirebaseDatabase.getInstance().getReference().child("Driver Avaliable");
        GeoFire geoFire=new GeoFire(driverAvalabilityRef);
        geoFire.setLocation(userID,new GeoLocation(location.getLatitude(),location.getLongitude()));


    }
    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient=new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Show all Driver avalable in the range of customer
        // Check availability of drivers
        if(!currentLogOutDriverStatus) {
            disconnectDriver();

        }
    }

    private void disconnectDriver() {
        String userID = currentUser.getUid();
        //String userID="+923123132285";
        DatabaseReference driverAvalabilityRef = FirebaseDatabase.getInstance().getReference().child("Driver Avaliable");
        GeoFire geoFire = new GeoFire(driverAvalabilityRef);
        geoFire.removeLocation(userID);
    }
}
