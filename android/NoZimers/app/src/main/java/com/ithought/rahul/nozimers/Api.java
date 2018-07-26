package com.ithought.rahul.nozimers;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {



    @POST("recognize")
    Call<ResponseBody> postRequest(
            @HeaderMap Map<String, String> headers,
            @Field("image") String image,
            @Field("gallery_name") String gallery_name
    );
}
