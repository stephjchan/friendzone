package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/5/2018.
 */

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

// Jansen Yan: Firebase reference
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoFire.CompletionListener;
import com.firebase.geofire.GeoLocation;

import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;

// Jansen Yan: Causes conflict with google api listener
//import com.firebase.geofire.LocationCallback;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;

// Jansen Yan: Joda Time API for time calculations
import org.joda.time.*;
import org.joda.time.format.*;
import java.util.Date;

import android.widget.TextView;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;


import android.text.Spanned;
public class DashboardTab extends Fragment {

    private Button mLogOutButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FusedLocationProviderClient mFusedLocationClient;

    //Jansen Yan: added firebase ref
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private GeoFire geo;

    private GoogleSignInClient mGoogleSignInClient;

    // Jansen Yan: currentUser that's logged into firebase
    private FirebaseUser user;

    private static final String TAG = "DASHBOARD_FRAGMENT";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    public TextView mPlaceDetailsText;
    // Moved lat and long into main activity
//    private static double latitude;
//    private static double longitude;
    private TextView mPlaceAttribution;
    private String name;
    private String location;

    //Commented out by Jansen: private String
    // Jansen Yan: made the rootView and vpPager private variables
    private View rootView;
    private ViewPager vpPager;

    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;

    // Jansen Yan: LocationRequest is defined in enableLocationSettings Function
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;

    private final int REQUEST_CHECK_SETTINGS = 0x1;

    String wantPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Jansen Yan: moved the rootView and vpPager code as a variable above
        // original code:
        //View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //ViewPager vpPager = (ViewPager) rootView.findViewById(R.id.Recommendations);
       rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
       vpPager = (ViewPager) rootView.findViewById(R.id.Recommendations);
       vpPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

       Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
       ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(getContext(), R.array.times_array, android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Jansen Yan: get the currently logged in firebase user
                user = firebaseAuth.getCurrentUser();
                //Jansen Yan: changed to user instead of calling the function
//                if (firebaseAuth.getCurrentUser() == null) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
                if (user == null){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };
        // Jansen Yan: added the firebase database reference
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        DatabaseReference fireRef = database.getReference("user-location-geofire");
        geo = new GeoFire(fireRef);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient((Activity) getContext(), gso);




//        LocationManager locationManager;
//        Context cont = this.getContext();
//        locationManager = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);
//        System.out.println("Is it running checkPermission?");
//        if (checkPermission(wantPermission)){
//            Location lastKnownLocationGps = locationManager.getLastKnownLocation(gpsLocationProvider);
//            Location lastKnownLocationNetwork = locationManager.getLastKnownLocation(networkLocationProvider);
//            if (lastKnownLocationGps == null){
//                System.out.println( "NO GPS");
//                if (lastKnownLocationNetwork == null){
//                    System.out.println("NO Network");
//                    System.out.println( "NO Location!");
//                }
//                else{
//                    System.out.println( "Network " + lastKnownLocationNetwork.toString());
//                    System.out.println( "Location (Network)" + lastKnownLocationNetwork.getLatitude() + ", " + lastKnownLocationNetwork.getLongitude());
//                }
//            }
//            else{
//                System.out.println( "GPS " + lastKnownLocationGps.toString());
//                if (lastKnownLocationNetwork == null){
//                    System.out.println( "NO Network");
//                    System.out.println( "Location (GPS) " + lastKnownLocationGps.getLatitude() + ", " + lastKnownLocationGps.getLongitude());
//                    System.out.println( "Time (GPS) " + lastKnownLocationGps.getTime());
//                    System.out.println( "Accuracy (GPS) " + lastKnownLocationGps.getAccuracy());
//                }
//                else{
//                    System.out.println( "Network " + lastKnownLocationNetwork.toString());
//                    if (lastKnownLocationGps.getAccuracy() >= lastKnownLocationNetwork.getAccuracy()){
//                        System.out.println( "Location (GPS)" + lastKnownLocationGps.getLatitude() + ", " + lastKnownLocationGps.getLongitude());
//                        latitude = lastKnownLocationGps.getLatitude();
//                        longitude = lastKnownLocationGps.getLongitude();
//                    }
//                    else{
//                        System.out.println( "Location (Network) " + lastKnownLocationNetwork.getLatitude() + ", " + lastKnownLocationNetwork.getLongitude());
//                        Date date = new Date(lastKnownLocationNetwork.getTime());
//                        latitude = lastKnownLocationNetwork.getLatitude();
//                        longitude = lastKnownLocationNetwork.getLongitude();
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//                        String myDate= sdf.format(date);
//                        System.out.println("Date: " + myDate);
//
//
//                    }
//                }
//            }
//        }


        Button openButton = (Button) rootView.findViewById(R.id.search);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }



        });
        mPlaceDetailsText = (TextView) rootView.findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) rootView.findViewById(R.id.place_attribution);
        Button createLobby = (Button) rootView.findViewById(R.id.create_lobby);
        createLobby.setOnClickListener(lobbyListener);
        mLogOutButton = rootView.findViewById(R.id.logOutButton);
        mLogOutButton.setTag(1);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        return rootView;
    }

    private View.OnClickListener lobbyListener = new View.OnClickListener() {
        public void onClick(View v) {

            // Jansen Yan: Submit the microreport here

            //get the user-id of the user who is logged in
            String uid = user.getUid();
            String username = user.getDisplayName();

            // get the location and event name
            TextView textview = rootView.findViewById(R.id.place_details);
            String eventLocationAndName[] = textview.getText().toString().split("\n");
//            for (int i = 0; i < eventLocationAndName.length; i++){
//                System.out.println(eventLocationAndName[i]);
//            }

            String eventName = "";
            String locationName = "";
            String locationAddress = "";
            //parse the info
            // if the textview comes from a recommendation, it should be 3 lines long
            if (eventLocationAndName.length == 3){
                eventName = eventLocationAndName[0];
                locationName = eventLocationAndName[1];
                locationAddress = eventLocationAndName[2];
            }
            // searches return 5 lines of text
            else if (eventLocationAndName.length == 5){
                eventName = eventLocationAndName[0];
                locationName = eventLocationAndName[0];
                // first split by :, so the Address: 3123 bla.dr becomes [Address, 3123 bla.dr]
                locationAddress = eventLocationAndName[2].split(":")[1];
            }
            System.out.println("eventName: " + eventName + " " + "locationName: " + locationName + " " + "locatinAddress: " + locationAddress);

            //get the user's available time duration
            Spinner timeDuration = rootView.findViewById(R.id.spinner);
            String time = timeDuration.getSelectedItem().toString();

            //calculate the startTime of the event
            // if in minutes...
            String endtime = "";
            DateTimeFormatter formatter = DateTimeFormat.forPattern("h:mm a");
            DateTime jodaTime = new DateTime();

            String timeArray[] = time.split(" ");
            // minHourAmount is either in minutes or hours
            int minHourAmount = Integer.parseInt(timeArray[0]);
            // if mins, add minutes, if hours, add hours
            if ("min".equals(timeArray[1])){
                jodaTime = jodaTime.plusMinutes(minHourAmount);
            }
            else{
                jodaTime = jodaTime.plusHours(minHourAmount);
            }
            endtime = formatter.print(jodaTime);

            MicroReport report = new MicroReport(eventName, endtime, locationName, locationAddress, username,  jodaTime.toString() );
            final String microReportKey;
            DatabaseReference microReportRef = mDatabase.child("Microreports").child(uid).push();
            String microReportID = microReportRef.getKey();

            microReportRef.setValue(report, new DatabaseReference.CompletionListener() {
                        // test to see if value is set
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            System.out.println("Added microreport successfully");

                        }
                    });

            // set geo fire location for the microreport
            // get latitude and longitude values from main activity

            geo.setLocation(microReportID, new GeoLocation(MainActivity.latitude, MainActivity.longitude), new CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }
            });
            // set lobby for other users to join
            DatabaseReference joinLobby = mDatabase.child("Joined").child(uid).child(microReportID);
            joinLobby.setValue("0", new DatabaseReference.CompletionListener() {
                // test to see if value is set
                @Override
                public void onComplete(DatabaseError databaseError,
                                       DatabaseReference databaseReference) {
                    System.out.println("Added microreport to lobby successfully");

                }
            });
            Toast.makeText(getContext(), "Submitted Successfully!",
                    Toast.LENGTH_SHORT).show();
        }


    };

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }




    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign out success, update UI with the signed-in user's information
                            Log.d(TAG, "signOut:success");
                        } else {
                            // If sign out fails, display a message to the user.
                            Log.w(TAG, "signOutWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Sign out failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve the TextViews that will display details about the selected place.

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    // Jansen Yan: changed to MainActivity.longitude and latitude
                    return FoodFragment.newInstance(0, "Food", MainActivity.longitude, MainActivity.latitude);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ActivitiesFragment.newInstance(1, "Activities");
                //case 2: // Fragment # 1 - This will show SecondFragment
                 //   return SecondFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "Foods";
            return "Activities";
        }

    }


    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }




}