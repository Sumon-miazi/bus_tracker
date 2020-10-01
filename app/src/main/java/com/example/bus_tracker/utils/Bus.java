package com.example.bus_tracker.utils;

import java.io.Serializable;

public class Bus implements Serializable {
    public int id;
    public String name;
    public String license;
    public String routes;
    public double lat;
    public double lon;
}
