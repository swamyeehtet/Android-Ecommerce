package com.zwe.myapplication.services

import com.zwe.myapplication.modals.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface WebService {

    @GET("cats")
    fun getAllCat(@Header("Authorization") token: String): Call<List<Category>>

    @FormUrlEncoded         // insert if contain field parameters [email,password]
    @POST("login")
    fun loginUser(
        @Field("email") email: String
        , @Field("password") passwoed: String
    ): Call<Token>

    @GET("product/cat/{id}")
    fun getProductOfACategory(
        @Header("Authorization") token: String,
        @Path("id") id: Int): Call<Products>

    @FormUrlEncoded
    @POST("previewCart")
    fun getCartPreviewItems(
        @Header("Authorization") token: String,
        @Field("items") items : String
    ) : Call<List<CartProduct>>

    @FormUrlEncoded
    @POST("order")
    fun billOutOrder(
        @Header("Authorization") token: String,
        @Field("orders") orders : String
    ) : Call<ErrorMessager>

    @Multipart
    @POST("imageUpload")
    fun imageUpload(
        @Header("Authorization") token: String,
        @Part file : MultipartBody.Part)
            : Call<FileInfo>

    @FormUrlEncoded
    @POST("newProduct")
    fun newProductUpload(
        @Header("Authorization") token: String,
        @Field("cat_id") cat_id : Int,
        @Field("name") name : String,
        @Field("price") price : Double,
        @Field("image") image : String,
        @Field("description") description : String
    ) : Call<ErrorMessager>
}