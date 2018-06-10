package com.eaapps.thebesacademy.Utils;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by eslamandroid on 4/22/18.
 */

public interface InterfaceRetrofit {

    @POST("fcm/send")
    @Headers({"Content-Type:application/json"
            , "Authorization:key=AAAA_hV35gs:APA91bGJyDJO_Gc1nDlkRe8RAq26iVefB-l4S5R_DcMP0SzE2qDxVFFE8U7mMdnUzwQQi6kS4xtYb-pbGtuI35BtJrHTEdu0DDyHC95tmGjTMR5aDp8T1m5kw-VyMDokqPha2IzrUcWK"})


    Call<FCMResponse> sendMessage(@Body Sender bodys);


    public interface MapCallBack<T> {
        public Call<T> workService(InterfaceRetrofit service);

        public void onDataMap(Response<T> response, Retrofit retrofit);

    }
}
