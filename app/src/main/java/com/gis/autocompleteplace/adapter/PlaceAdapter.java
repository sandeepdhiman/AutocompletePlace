package com.gis.autocompleteplace.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gis.autocompleteplace.MapExtention;
import com.gis.autocompleteplace.R;
import com.gis.autocompleteplace.model.PlaceDataModel;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends ArrayAdapter<PlaceDataModel> {
    private Context context;
    private List<PlaceDataModel>resultList;
    private PlacesClient placesClient;
    public PlaceAdapter(@NonNull Context context, int resource, PlacesClient client) {
        super(context, resource);
        this.context = context;
        resultList =  new ArrayList<>();
        this.placesClient = client;
    }

    @Override
    public int getCount() {
        return resultList.size();

    }

    @Nullable
    @Override
    public PlaceDataModel getItem(int position) {
        if (resultList.size()>0){
            return resultList.get(position);
        }else {
            return null;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_places, parent, false);
            holder =  new ViewHolder();
            holder.description =  convertView.findViewById(R.id.searchFullText);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position<resultList.size()) {
            holder.description.setText(resultList.get(position).getFullText());

        }// Lookup view for data population
        // Populate the data into the template view using the data object
        // Return the completed view to render on screen
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults =  new FilterResults();
                if (constraint!=null){
                  resultList.clear();
                  List<AutocompletePrediction> addresses = MapExtention.getAutoComplete(context,placesClient,constraint);
                    if (addresses!=null){
                        for (AutocompletePrediction prediction:addresses){
                            resultList.add(new PlaceDataModel(prediction.getPlaceId(),prediction.getFullText(new StyleSpan(
                                    Typeface.BOLD)).toString()));
                        }
                    }
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                   notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
    private class ViewHolder{
        TextView description;
    }
}
