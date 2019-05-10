package com.zwe.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import com.zwe.myapplication.libby.H.Companion.USER_TOKEN
import com.zwe.myapplication.libby.H.Companion.l
import com.zwe.myapplication.modals.ErrorMessager
import com.zwe.myapplication.modals.FileInfo
import com.zwe.myapplication.services.ServiceBuilder
import com.zwe.myapplication.services.WebService
import kotlinx.android.synthetic.main.activity_product_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class ProductUpload : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_upload)

        upload_product_image.setOnClickListener {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {  // same switch
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("Permission Denided")
                } else {
                    fileUpload()
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Choose Carefully"), 103)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 103 && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri = data.data
            val inst: InputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inst)
            upload_product_image.imageBitmap = bitmap

            var imagePath = getImagePath(uri)
            l(imagePath)

            var file = File(imagePath)
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestBody)

            val services: WebService = ServiceBuilder.buildService(WebService::class.java)
            val responseUpload: Call<FileInfo> = services.imageUpload("Bearer $USER_TOKEN", body)

            responseUpload.enqueue(object : Callback<FileInfo> {
                override fun onFailure(call: Call<FileInfo>, t: Throwable) {
                    l(t.message!!)
                }

                override fun onResponse(call: Call<FileInfo>, response: Response<FileInfo>) {
                    if (response.isSuccessful) {
                        var info: FileInfo = response.body()!!
                        toast(info.name)
                        uploadProduct(info.name)
                    } else {
                        l("Something went wrong on Product Upload!!!")
                    }
                }

            })

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun uploadProduct(image : String){
        val cat_id : Int = upload_product_id.text.toString().toInt()
        val name : String = upload_product_name.text.toString()
        val price : Double = upload_product_price.text.toString().toDouble()
        val description : String = upload_product_description.text.toString()

        val uploadServices : WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseUploadProduct : Call<ErrorMessager> = uploadServices.newProductUpload("Bearer $USER_TOKEN",
            cat_id,
            name,
            price,
            image,
            description
            )
        responseUploadProduct.enqueue(object : Callback<ErrorMessager>{
            override fun onFailure(call: Call<ErrorMessager>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<ErrorMessager>, response: Response<ErrorMessager>) {
                if(response.isSuccessful){
                    val message : ErrorMessager = response.body()!!
                    toast(message.msg)
                    l(message.msg)
                }else{
                    l("Something went wrong!, Please Try again!!")
                }
            }

        })

    }

    private fun getImagePath(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)!!
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        var mediaPath = cursor.getString(columnIndex)
        cursor.close()
        return mediaPath

    }
}
