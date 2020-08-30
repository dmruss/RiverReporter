package com.example.riverconditionsreporter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.valueOf;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private ArrayList<River> riverList;
    private ArrayList<River> favorites;
    private ArrayList<USGSRequest> favoritesUSGS;
    private ArrayList<WeatherRequest> favoritesWeather;
    //private RecyclerView recyclerView;
    //private RiverAdapter riverAdapter;
    private DataManager dataManager;
    private GoogleMap mMap;
    private EditText mSearchText;
    private USGSRequest usgsRequest;
    private WeatherRequest weatherRequest;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //recyclerView.findViewById(R.id.recyclerView);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        riverList = new ArrayList<River>();
        favorites = new ArrayList<River>();
        favoritesUSGS = new ArrayList<USGSRequest>();
        favoritesWeather = new ArrayList<WeatherRequest>();
        dataManager = new DataManager(this);
        usgsRequest = new USGSRequest();
        weatherRequest = new WeatherRequest();
        mSearchText = findViewById(R.id.input_search);


        //bug, this is not initializing recycler view
        //recyclerView.findViewById(R.id.recyclerView);



        init();

    }
    
    private void init() {
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                || i == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute search method
                    geoLocate();
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        MapsActivity mapsActivity = this;
        switch (item.getItemId()) {
            case R.id.favorites:
                //make api calls for each favorite
                Comparator<River> compareByName = new Comparator<River>() {
                    @Override
                    public int compare(River o1, River o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                };
                Collections.sort(favorites, compareByName);
                for (int i = 0; i < favorites.size(); i++) {
                    River river = favorites.get(i);
                    river = getRiverFromFav(river);
                    Log.i("Favorites api call", "ID: " + river.getLatitude() + " and " + river.getLongitude());
                    USGSRequest usgsRequest = new USGSRequest(getApplicationContext(), river);
                    favoritesUSGS.add(usgsRequest);
                    WeatherRequest weatherRequest = new WeatherRequest(getApplicationContext(), river);
                    favoritesWeather.add(weatherRequest);

                }

                //show dialogfragment of favorites
                Log.i("Menu", "Favorites button clicked");
                ViewFavoritesDialog viewFavoritesDialog = new ViewFavoritesDialog(this, favorites);
                viewFavoritesDialog.show(getSupportFragmentManager(), "");
                return true;
            case R.id.align:
                //align map
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(alignCamera()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void geoLocate() {
        Log.d(TAG, "geolocate: geolocating");

        String searchString = mSearchText.getText().toString() + ", co";

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geolocate: found a location: " +address.toString());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 10));

        }
        hideSoftKeyboard();
    }

    @Override
    protected void onResume() {

        super.onResume();
        loadData();
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

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);*/
        //Read in USGS stream gauge locations
        readLocationData(mMap);
        //Center camera on Colorado

        LatLng CO = new LatLng(39.5501, -105.7821);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CO, 8));        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        //marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                River river = getMarkerRiver(marker);
                //api calls
                Log.i("API", "Making api calls now");
                usgsRequest = new USGSRequest(getApplicationContext(), river);
                weatherRequest = new WeatherRequest(getApplicationContext(), river);


                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //get river object from marker
                River river = getMarkerRiver(marker);
                //api calls
                //Log.i("API", "Making api calls now");
                //usgsRequest = new USGSRequest(getApplicationContext(), river);
                //show dialog fragment with info
                ViewRiverDialog viewRiverDialog = new ViewRiverDialog(river, usgsRequest, weatherRequest);
                viewRiverDialog.show(getSupportFragmentManager(), " ");
            }
        });
        //loadData();
    };


        public void readLocationData(GoogleMap googleMap) {
            try {
                InputStream inputStream = getAssets().open("RiverLocations.csv");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, Charset.forName("UTF-8"))
                );

                CSVReader aReader = new CSVReader(reader);
                String[] nextLine;
                nextLine = aReader.readNext();
                while ((nextLine = aReader.readNext()) != null) {

                    double latitude = Double.parseDouble(nextLine[5]);
                    double longitude = Double.parseDouble(nextLine[4]);

                    LatLng aLocation = new LatLng(latitude, longitude);

                    River newRiver = new River(nextLine);
                    //add to array list of rivers

                    //set index
                    newRiver.setIndex(riverList.size());
                    //set favorite if saved in favorites list
                    for (int i = 0; i < favorites.size(); i++) {
                        if (favorites.get(i).getName().equals(newRiver.getName())){
                            newRiver.setFavorite(true);
                            Log.i("Setting to favorite", "" + newRiver.getName());
                        }
                    }
                    riverList.add(newRiver);
                    //Log.i("add", "added river " + riverList.get(riverList.size() - 1).getName() + riverList.get(riverList.size() - 1).getIndex());

                    //add to map
                    mMap.addMarker(new MarkerOptions()
                            .position(aLocation)
                            .title(nextLine[1])
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


                }

            } catch (IOException e) {
                Log.i("ReadData", "Error while reading location file", e);
            }
        }

    public void showRiver (int riverToShow) {
            //River river = favorites.get(riverToShow);
        Log.i("showRiver", "Making api calls now");
        //usgsRequest = new USGSRequest(getApplicationContext(), river);
        //weatherRequest = new WeatherRequest(getApplicationContext(), river);
        ViewRiverDialog viewRiverDialog = new ViewRiverDialog(favorites.get(riverToShow), favoritesUSGS.get(riverToShow), favoritesWeather.get(riverToShow));
        viewRiverDialog.sendSelectedRiver(favorites.get(riverToShow));
        viewRiverDialog.show(getSupportFragmentManager(), "");
    }

    public ArrayList<River> getFavorites() {return favorites;}



    public River getMarkerRiver (Marker marker) {

        River river = new River();
        String name = "";
        name = marker.getTitle();
        //Log.i("Marker Name", name);
        boolean found = false;
        int i = 0;

        while (!found) {
            if (riverList.get(i).getName() != null) {
                String currentName = riverList.get(i).getName();
                //Log.i("name", " " + currentName);
                if (currentName.equals(name)){
                    found = true;
                    river = riverList.get(i);

                }else {
                    i += 1;
                }
            }else {
                i += 1;
            }
        }
        return river;
    }

    public River getRiverFromFav (River river) {

        River newRiver = new River();
        String name = "";
        name = river.getName();
        //Log.i("Marker Name", name);
        boolean found = false;
        int i = 0;

        while (!found) {
            if (riverList.get(i).getName() != null) {
                String currentName = riverList.get(i).getName();
                //Log.i("name", " " + currentName);
                if (currentName.equals(name)){
                    found = true;
                    newRiver = riverList.get(i);

                }else {
                    i += 1;
                }
            }else {
                i += 1;
            }
        }
        return newRiver;
    }

    public void addNewFavorite (River river) {
        String id = river.getId();
        Log.i("New favorite", "Id: " + id);
        String name = river.getName();
        String category = river.getCategory();
        String agency = river.getAgency();
        Double longitude = river.getLongitude();
        Double latitude = river.getLatitude();
        String url = river.getUrl();
        int favorite = 1;
        int index = river.getIndex();
        riverList.get(index).setFavorite(true);
        //favorites.add(river);
        dataManager.insert(id, name, category, agency, longitude, latitude, url, favorite, index);
        loadData();
    }

    public void deleteFavorite (River river) {
            favorites.remove(river);
            dataManager.deleteFavorite(river);
            riverList.get(river.getIndex()).setFavorite(false);
            Log.i("Delete Favorite", " "+river.getName());
    }

    public boolean containsFavorite (River river) {
            if (favorites.contains(river)){
                return true;
            }else {
                return false;
            }
    }

    public void loadData() {
        Cursor cursor = dataManager.selectAll();
        int favoriteCount = cursor.getCount();
        Log.i("favorite count", " "+favoriteCount);
        if (favoriteCount > 0) {
            favorites.clear();
            while (cursor.moveToNext()) {

                String id = cursor.getString(0);

                String name = cursor.getString(1);
                Log.i("Favorite Name", " " + name);
                String category = cursor.getString(2);
                String agency = cursor.getString(3);
                Double longitude = cursor.getDouble(4);
                Double latitude = cursor.getDouble(5);
                String url = cursor.getString(6);
                boolean favorite = true;
                int index = cursor.getInt(8);


                River river = new River(id, name, category, agency, longitude, latitude, url, favorite, index);

                //get river index and change favorite in riverlist
                Log.i ("Favorites", "Is favorite " + river.getFavorite() + " ID: " + river.getId());
                //riverList.get(index).setFavorite(true);
                favorites.add(river);

            }
        }
        else {
            favorites.clear();
        }
        //Log.i("Favorites size", " " + favorites.size());
        //Log.i("Favorite name" , ""+ favorites.get(0).getName());
        //riverAdapter.notifyDataSetChanged();
    }

    public void hideSoftKeyboard() {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public CameraPosition alignCamera() {
        CameraPosition position = mMap.getCameraPosition();
        LatLng target = position.target;
        float zoom = position.zoom;
        float tilt = position.tilt;
        float bearing = 0;

       CameraPosition newPosition = new CameraPosition(target, zoom, tilt, bearing);
       return newPosition;

    }

};







