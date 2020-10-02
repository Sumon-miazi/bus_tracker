package com.example.bus_tracker.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.bus_tracker.utils.Bus;
import com.example.bus_tracker.utils.CustomLocation;
import com.example.bus_tracker.utils.GetAllBuses;
import com.example.bus_tracker.utils.Stoppage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCalls {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiUrls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RetrofitService service = retrofit.create(RetrofitService.class);

    public void getAllBuses(final Context context, GetAllBuses getAllBuses) {

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        System.out.println("getAllBuses>>>>>>>>>>> called ");
        final RetrofitRequestBody retrofitRequestBody = new RetrofitRequestBody();
        Call<ResponseBody> responseBodyCall = service.getAllBus(retrofitRequestBody.getApi_key());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                if (response.isSuccessful()) {
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        System.out.println("getAllBuses>>>>>>>>>>> " + jsonObject.toString());
                        if (jsonObject.optString("status").equals("true")) {

                            JSONArray busesData = jsonObject.getJSONArray("data");
                            ArrayList<Bus> buses = new ArrayList<>();
                            for (int i = 0; i < busesData.length(); i++) {
                                JSONObject object = busesData.getJSONObject(i);

                                Bus bus = new Bus();
                                bus.id = object.getInt("id");
                                bus.name = object.getString("name");
                                bus.license = object.getString("license");
                                bus.routes = object.getString("routes");
                                bus.lat = object.getDouble("lat");
                                bus.lon = object.getDouble("long");

                                JSONArray stoppageArray = object.getJSONArray("bus_stoppages");
                                ArrayList<Stoppage> stoppages = new ArrayList<>();

                                for (int j = 0; j < stoppageArray.length(); j++) {
                                    JSONObject stoppageArrayJSONObject = stoppageArray.getJSONObject(j);
                                    Stoppage stoppage = new Stoppage();
                                    stoppage.id = stoppageArrayJSONObject.getInt("id");
                                    stoppage.spot_name = stoppageArrayJSONObject.getString("spot_name");
                                    stoppage.lat = stoppageArrayJSONObject.getDouble("lat");
                                    stoppage.lon = stoppageArrayJSONObject.getDouble("long");

                                    stoppages.add(stoppage);
                                }

                                bus.stoppages = stoppages;
                                buses.add(bus);
                            }
                            getAllBuses.data(buses, jsonObject.optString("message"));
                        } else getAllBuses.data(null, jsonObject.optString("message"));

                    } catch (Exception ignore) {
                        System.out.println("getAllBuses>>>>>>>>>>> catch " + ignore.getMessage());
                        getAllBuses.data(null, ignore.getMessage());
                    }
                } else {
                    System.out.println("getAllBuses>>>>>>>>>>> response failed");
                    getAllBuses.data(null, response.message());
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("getAllBuses>>>>>>>>>>> failed " + t.getMessage());
                getAllBuses.data(null, t.getMessage());
                mProgressDialog.dismiss();
            }
        });
    }

    public void getBusCurrentPositionByBusId(int bus_id, CustomLocation customLocation) {

        Call<ResponseBody> data = service.getBusCurrentPositionByBusId(new RetrofitRequestBody().getBusCurrentPositionByBusId(bus_id));
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            System.out.println("getBusCurrentPositionByBusId>>>>>>>>>>> " + jsonObject.toString());
                            if (jsonObject.optString("status").equals("true")) {
                                JSONObject busLocation = jsonObject.getJSONObject("data");
                                customLocation.customLocation(busLocation.getDouble("lat"),
                                        busLocation.getDouble("long"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
