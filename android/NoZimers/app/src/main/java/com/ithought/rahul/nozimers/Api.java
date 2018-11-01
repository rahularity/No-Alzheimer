package com.ithought.rahul.nozimers;

import com.ithought.rahul.nozimers.models.AddingPeopleStatusObject;
import com.ithought.rahul.nozimers.models.UserObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {


    @GET("getall/")
    Call<UserObject> getall();

    @FormUrlEncoded
    @POST("add/")
    Call<AddingPeopleStatusObject> add(

            @Field("image") String image,
            @Field("name") String name,
            @Field("lives_in") String lives_in,
            @Field("contact") String contact,
            @Field("age") String age,
            @Field("place_of_meeting") String place_of_meeting,
            @Field("relation") String relation,
            @Field("notes") String notes

    );

    @FormUrlEncoded
    @POST("compare/")
    Call<UserObject> compare(
            @Field("image") String image

    );


}
