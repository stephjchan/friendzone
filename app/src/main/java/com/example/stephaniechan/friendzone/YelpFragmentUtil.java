package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.location.Geocoder;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.Projection;
import com.google.gson.Gson;
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
    public static String googlePlacesApiKey = "AIzaSyBMBjqGglKjnz_EtTC2JGFmZwhj2CcZBfI";

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
        String urlToImage = business.getImageUrl();
        String displayString = intent + "\n" + businessName + "\n" + locationAddress + ", " + city + " " + state + " " + zipCode + ", " + country + "\n" + urlToImage;
        return displayString;
    }
    // not used at the moment, TODO: trying to get the google place ID given latitude and longitude
    public static void getGooglePlaceID(Business business, final Context context) {
//        StringBuilder searchURL = new StringBuilder();
//        searchURL.append("https://maps.googleapis.com/maps/api/place/nearbysearch/");
//        searchURL.append("json?");
//        searchURL.append("location=" + latitude + "," + longitude);
//        searchURL.append("&key=" + googlePlacesApiKey);
        Coordinates coordinates = business.getCoordinates();
        final double latitude = coordinates.getLatitude();
        final double longitude = coordinates.getLongitude();
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, "https://maps.googleapis.com/maps/api/place/nearbysearch/json?", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // the response is already constructed as a JSONObject!


                        Toast toast = Toast.makeText(context, response, Toast.LENGTH_SHORT);
                        toast.show();
                        Gson gson = new Gson();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("location",latitude + "," + longitude);
                params.put("key", googlePlacesApiKey);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

}
