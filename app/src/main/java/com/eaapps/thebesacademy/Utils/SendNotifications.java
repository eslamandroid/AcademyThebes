package com.eaapps.thebesacademy.Utils;

import android.content.Context;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;

public class SendNotifications<T> {

    Context context;

    public SendNotifications(Context context) {
        this.context = context;
    }

    public void sendNotify(InterfaceRetrofit.MapCallBack<T> callBack) {
        retrofit.Retrofit retrofit = new retrofit.Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceRetrofit service = retrofit.create(InterfaceRetrofit.class);
        Call<T> call = callBack.workService(service);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Response<T> response, retrofit.Retrofit retrofit) {
                callBack.onDataMap(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
