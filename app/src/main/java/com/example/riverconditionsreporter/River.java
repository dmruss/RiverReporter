package com.example.riverconditionsreporter;

import android.util.Log;

public class River {

    private String id;
    private String name;
    private String category;
    private String agency;
    private Double longitude;
    private Double latitude;
    private String url;
    private boolean favorite;
    private int index;


    public River () {
        id = "";
        name = "";
        category = "";
        agency = "";
        longitude = 1.1;
        latitude = 1.1;
        url = "";
        favorite = false;
    }
    public River (String id, String name, String category, String agency, Double longitude, Double latitude,
                   String url) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.agency = agency;
        this.longitude = longitude;
        this.latitude = latitude;
        this.url = url;
        favorite = false;
    }

    public River(String id, String name, String category, String agency, Double longitude, Double latitude,
                 String url, boolean favorite, int index) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.agency = agency;
        this.longitude = longitude;
        this.latitude = latitude;
        this.url = url;
        this.favorite = favorite;
        this.index = index;
    }

    public River (String[] line) {
        //read in to vars
        String id = line[0];
        String name = line[1];
        String category = line[2];
        String agency = line[3];
        double latitude = Double.parseDouble(line[5]);
        double longitude = Double.parseDouble(line[4]);
        String url = line[6];

        //assign to object
        this.id = id;
        this.name = name;
        this.category = category;
        this.agency = agency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        favorite = false;
    }



    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public String getAgency() {return agency;}

    public void setAgency(String agency) {this.agency = agency;}

    public Double getLongitude() {return longitude;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}

    public Double getLatitude() {return latitude;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public boolean getFavorite() {return favorite;}

    public void setFavorite(boolean favorite) {this.favorite = favorite;}

    public int getIndex() {return index;}

    public void setIndex(int index) {this.index = index;}


}
