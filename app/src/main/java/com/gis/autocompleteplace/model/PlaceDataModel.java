package com.gis.autocompleteplace.model;

public class PlaceDataModel {
    private String placeId;
    private String fullText;

    public PlaceDataModel(String placeId, String fullText) {
        this.placeId = placeId;
        this.fullText = fullText;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
}
