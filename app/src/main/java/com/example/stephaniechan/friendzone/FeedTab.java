package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/5/2018.
 */

import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;

public class FeedTab extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerViewAdapter recyclerAdapter;

    private List<MicroReportItem> listMReport = new ArrayList<>();

    private DatabaseReference myRef;
    private Bitmap placeImg;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

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

        myRef = FirebaseDatabase.getInstance().getReference("/Microreports");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this
                // location is updated.

                listMReport = new ArrayList<>();

                for (DataSnapshot firebaseUser : dataSnapshot.getChildren()) {
                    for (DataSnapshot microReport : firebaseUser.getChildren()) {
                        MicroReportItem value = microReport.getValue(MicroReportItem.class);

                        MicroReportItem mReport = new MicroReportItem();

                        getPhotos();
                        mReport.setLocationPhoto(placeImg);
                        mReport.setEventName(value.getEventName());
                        mReport.setEventLocationName(value.getEventLocationName());
                        mReport.setEventLocationAddress(value.getEventLocationAddress());
                        mReport.setEventUsername(value.getEventUsername());
                        mReport.setEventStartTime(value.getEventStartTime());
                        mReport.setID(microReport.getKey());

                        listMReport.add(mReport);
                        //TODO: sort microreports by event time
                        recyclerAdapter = new RecyclerViewAdapter(getContext(), listMReport);
                        myRecyclerView.setAdapter(recyclerAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FEED_TAB", "Failed to read value.", error.toException());
            }
        });
    }

    // Request photos and metadata for the specified place.
    private void getPhotos() {
        //TODO: change placeID to actual place ID
        final String placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4";
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
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
                        placeImg = photo.getBitmap();
                    }
                });
            }
        });
    }
}