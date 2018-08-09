package com.rel3.lixoconsciente.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class Util {

    public static double calculaDistancia(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000; //em metros
    }


    public static String toString(InputStream is) throws Exception{

        BufferedInputStream in = new BufferedInputStream(is);
        InputStreamReader r = new InputStreamReader(in, "UTF-8");
        StringWriter w = new StringWriter();
        int v = r.read();
        while (v != -1) {
            w.write(v);
            v = r.read();
        }

        return w.toString();

    }

}
