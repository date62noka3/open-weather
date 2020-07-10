package com.example.openweather.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

/*
* URL画像を取得し、ImageViewに設定する
* */
class UrlImageGetTask(private val image: ImageView) :
    AsyncTask<String?, Void?, Bitmap?>() {

    /*
    * 取得した画像をImageViewに設定
    *
    * @param 画像のビットマップデータ
    * */
    override fun onPostExecute(result: Bitmap?) {
        image.setImageBitmap(result)
    }

    /*
    * 画像を取得する
    *
    * @param 画像URL
    * @return 画像のビットマップデータ
    * */
    override fun doInBackground(vararg p0: String?): Bitmap? {
        val image: Bitmap
        return try {
            val imageUrl = URL(p0[0])
            val imageIs: InputStream
            imageIs = imageUrl.openStream()
            image = BitmapFactory.decodeStream(imageIs)
            image
        } catch (e: MalformedURLException) {
            null
        } catch (e: IOException) {
            null
        }
    }

}