package com.example.bus_tracker.api;

import java.util.HashMap;
import java.util.Map;

public class RetrofitRequestBody {
    private String api_key = "7EgGmA";

    public RetrofitRequestBody() {

    }

    Map<String, Object> getBusCurrentPositionByBusId(int busId) {
        Map<String, Object> map = new HashMap<>();
        map.put("bus_id", busId);
        map.put("api_key", this.api_key);
        return map;
    }

    Map<String, Object> getApi_key() {
        Map<String, Object> map = new HashMap<>();
        map.put("api_key", this.api_key);
        return map;
    }
}
