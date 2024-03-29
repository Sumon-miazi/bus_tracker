package com.example.bus_tracker.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Bus implements Serializable {
    public int id;
    public String name;
    public String license;
    public String routes;
    public double lat;
    public double lon;
    public ArrayList<Stoppage> stoppages;
}
