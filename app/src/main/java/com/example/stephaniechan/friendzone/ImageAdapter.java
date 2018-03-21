package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by mosheyang on 3/12/18.
 */

// Jansen Yan: changed the ImageAdapter class to allow for thumb nail inputs
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIDs;
    private String[] termMap;
    private HashMap<Integer, String> imageMap = new HashMap<Integer, String>();
    public ImageAdapter(Context c, Integer[] thumbIDs, String[] termMap) {
        mContext = c;
        this.mThumbIDs = thumbIDs;
        this.termMap = termMap;
        if (mThumbIDs.length == termMap.length){
            // Jansen Yan: map the position index to the search term
            for (int i = 0; i < mThumbIDs.length; i++){
                imageMap.put(i, termMap[i]);
            }
        }
    }

    public int getCount() {
        return mThumbIDs.length;
    }

    public Object getItem(int position) {
        return imageMap.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(125, 125));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIDs[position]);
        return imageView;
    }
//
//    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.asian_noodles, R.drawable.beer_drinking,
//            R.drawable.burger, R.drawable.burrito,
//            R.drawable.chinese_food, R.drawable.donut,
//            R.drawable.hotdog, R.drawable.pizza,
//            R.drawable.sandwich, R.drawable.tacos,
//
//    };
}