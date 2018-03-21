
package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/5/2018.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FeedTab extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerViewAdapter recyclerAdapter;

    private List<MicroReportItem> listMReport = new ArrayList<>();

    private DatabaseReference myRef;
    private Bitmap placeImg;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    private HashMap<String, Integer> nearbyUsers = new HashMap<String,Integer>();

    private double searchRadius;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        myRecyclerView = rootView.findViewById(R.id.feed_recyclerview);
        recyclerAdapter = new RecyclerViewAdapter(getContext(), listMReport);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Construct a GeoDataClient
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        // hard code the search radius to be .5 km
        searchRadius = .5;

        myRef = FirebaseDatabase.getInstance().getReference("/Microreports");
        DatabaseReference fireRef = FirebaseDatabase.getInstance().getReference("user-location-geofire");
        final GeoFire geo = new GeoFire(fireRef);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this
                // location is updated.
                // clear the list of nearby users


                GeoQuery geoQuery = geo.queryAtLocation(new GeoLocation(MainActivity.latitude, MainActivity.longitude), searchRadius);

                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener(){
                    @Override
                    public void onKeyEntered(String key, GeoLocation location){
                        //System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                        // remove the dash, and put 1 as a dummy value

                        listMReport = new ArrayList<>();


                        System.out.println("Key: " + key.substring(1));
                        nearbyUsers.put(key, 1);

                        for (DataSnapshot firebaseUser : dataSnapshot.getChildren()) {
                            for (DataSnapshot microReport : firebaseUser.getChildren()) {

                                MicroReportItem value = microReport.getValue(MicroReportItem.class);
                                // Jansen Yan: if these users are nearby within the search radius, add them to the feed
                                System.out.println("Key from datasnap shot: " + microReport.getKey());
                                if (nearbyUsers.get( microReport.getKey()) != null){
                                    //System.out.println(value.getEventPhotoRetriever());
                                    MicroReportItem mReport = new MicroReportItem();
                                    // Jansen Yan: moved code
//                        getPhotos();
//                        mReport.setLocationPhoto(placeImg);
                        String photoRetriever = "";
//                        if (value.getEventPhotoRetriever() != null){
//                            photoRetriever = value.getEventPhotoRetriever();
//                            mReport.setEventPhotoRetriever(value.getEventPhotoRetriever());
//                        }

                                    mReport.setEventName(value.getEventName());
                                    mReport.setEventLocationName(value.getEventLocationName());
                                    mReport.setEventLocationAddress(value.getEventLocationAddress());
                                    mReport.setEventUsername(value.getEventUsername());
                                    mReport.setEventStartTime(value.getEventStartTime());
                                    mReport.setID(microReport.getKey());

                                    // Jansen Yan: set bitmap for image
//                        getPhotos(photoRetriever);
//                        mReport.setLocationPhoto(placeImg);

                                    listMReport.add(mReport);
                                    //TODO: sort microreports by event time
                                    recyclerAdapter = new RecyclerViewAdapter(getContext(), listMReport);
                                    myRecyclerView.setAdapter(recyclerAdapter);
                                }

                            }
                        }
                    }
                    @Override
                    public void onKeyExited(String key) {
                        System.out.println(String.format("Key %s is no longer in the search area", key));
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                    }

                    @Override
                    public void onGeoQueryReady() {
                        System.out.println("All initial data has been loaded and events have been fired!");
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {
                        System.err.println("There was an error with this query: " + error);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FEED_TAB", "Failed to read value.", error.toException());
            }
        });
    }

    // Request photos and metadata for the specified place.
    // Jansen Yan: changed the code to add parameter event_photo_retriever
    private void getPhotos(String photoRetriever) {
        //TODO: change placeID to actual place ID
        // if the photoRetriever is a URL
        System.out.println("photoretriever: " + photoRetriever);
        if (photoRetriever.equals("")){
            photoRetriever = "ChIJa147K9HX3IAR-lwiGIQv9i4";
        }
        else if (URLUtil.isValidUrl(photoRetriever)){
            System.out.println("Downloading Image Task");

            new DownloadImageTask()
                    .execute(photoRetriever);
//        }
        // if the photoRetriever is a google place ID
//        else{
            final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(photoRetriever);
            photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                    // Get the list of photos.
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    // Get the first photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                    // Get the attribution text.
                    CharSequence attribution = photoMetadata.getAttributions(); // may delete this
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            System.out.println("Found Image for google places api");
                            placeImg = photo.getBitmap();
                        }
                    });
                }
            });

        }
        // Jansen Yan: moved the following code above into the if/else
        //final String placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4";

//        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
//        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
//                // Get the list of photos.
//                PlacePhotoMetadataResponse photos = task.getResult();
//                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
//                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
//                // Get the first photo in the list.
//                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
//                // Get the attribution text.
//                CharSequence attribution = photoMetadata.getAttributions(); // may delete this
//                // Get a full-size bitmap for the photo.
//                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
//                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
//                        PlacePhotoResponse photo = task.getResult();
//                        placeImg = photo.getBitmap();
//                    }
//                });
//            }
//        });
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask() {
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            System.out.println("Set image bitmap in async");
            placeImg = result;
        }
    }
}
