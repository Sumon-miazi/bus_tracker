package com.example.bus_tracker.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitService {

    @POST(ApiUrls.ALL_BUSES)
    Call<ResponseBody> getAllBus(@Body Map<String, Object> body);

    @POST(ApiUrls.BUS_LOCATION)
    Call<ResponseBody> getBusCurrentPositionByBusId(@Body Map<String, Object> body);

}
