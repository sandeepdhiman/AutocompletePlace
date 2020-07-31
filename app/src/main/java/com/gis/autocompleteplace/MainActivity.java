package com.gis.autocompleteplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.gis.autocompleteplace.adapter.PlaceAdapter;
import com.gis.autocompleteplace.model.PlaceDataModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MainActivity extends AppCompatActivity  {
    private GoogleMap mMap;
    private PlaceAdapter placeAdapter;
    private  PlacesClient mPlacesClient;
    private AutoCompleteTextView autoCompleteEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteEditText =  findViewById(R.id.autoCompleteEditText);
        String apiKey = ""; // put your api key here
        Places.initialize(this, apiKey);
        mPlacesClient = Places.createClient(this);

        placeAdapter = new PlaceAdapter(this, R.layout.layout_item_places, mPlacesClient);
        autoCompleteEditText.setAdapter(placeAdapter);

       autoCompleteEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               PlaceDataModel placeDataModel = (PlaceDataModel) autoCompleteEditText.getAdapter().getItem(position);
               autoCompleteEditText.setText(placeDataModel.getFullText());
           }
       });

    }
}
