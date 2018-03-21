package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.location.Geocoder;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Coordinates;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Jansen on 3/19/2018.
 */

public class YelpFragmentUtil {
    public static String apiKey = "uLpgcL9A3hILUX7a4lzLe_JT63aHhO8Y2IDQ1uoJ0MdYc_o_1sWQnblPfHyqamS0-e2kXCY97dj0n_mTJZ3bpGVM6LTq3uOyzLDSGLCVVY7WxS8KXgZrMji4gZapWnYx";
    public static ArrayList<Business> generateYelpSearch(Map<String,String> termMap){
        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
        YelpFusionApi yelpFusionApi = null;
        try {
            yelpFusionApi = apiFactory.createAPI(apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(termMap);
        SearchResponse searchResponse = null;
        try {
            searchResponse = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalNumberOfResult = searchResponse.getTotal();  // 3

        ArrayList<Business> businesses = searchResponse.getBusinesses();
        return businesses;
    }
    public static String generateDisplayString(String intent, Business business){
        String businessName = business.getName();
        String locationAddress = business.getLocation().getAddress1();
        String city = business.getLocation().getCity();
        String state = business.getLocation().getState();
        String zipCode = business.getLocation().getZipCode();
        String country = business.getLocation().getCountry();
        String placeID = getGooglePlaceID(business);
        String displayString = intent + "\n" + businessName + "\n" + locationAddress + ", " + city + " " + state + " " + zipCode + ", " + country;
        return displayString;
    }
    public static String getGooglePlaceID(Business business){
        Coordinates coordinates = business.getCoordinates();
        double businessLatitude = coordinates.getLatitude();
        double businessLongitude = coordinates.getLongitude();
        return "";

    }

}
