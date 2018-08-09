package com.rel3.lixoconsciente.model;

/**
 * Created by Felipe on 31/08/2016.
 */
public class Coordenadas {

    private static double latitude;

    private static double longitude;

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        Coordenadas.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        Coordenadas.latitude = latitude;
    }
}
