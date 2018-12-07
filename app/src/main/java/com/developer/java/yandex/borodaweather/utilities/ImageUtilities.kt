package com.developer.java.yandex.borodaweather.utilities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * Created by Shiplayer on 06.12.18.
 */

object ImageUtilities{

    private lateinit var dir : File

    fun setCacheDir(cacheDir: File){
        dir = cacheDir
    }

    fun loadDrawable(url:String, nameDrawable:String) : Deferred<Drawable>{
        if(!checkInCacheFolder(nameDrawable)) {
            return GlobalScope.async(Dispatchers.IO) {
                val drawable = Drawable.createFromStream(URL(url).openStream(), nameDrawable)
                saveDrawable(drawable, nameDrawable)
                return@async drawable
            }
        } else {
            return GlobalScope.async( Dispatchers.IO){
                Log.i("ImageUtilities", "image is loaded from ${dir.path + "/" + nameDrawable}")
                Drawable.createFromPath(dir.path + "/" + nameDrawable)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun saveDrawable(drawable: Drawable, nameDrawable:String) {
        Single.just(drawable).subscribeOn(Schedulers.io())
            .subscribe { image ->
                FileOutputStream(File(dir, nameDrawable)).let {
                    //drawable
                    (image as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    Log.i("ImageUtilities", "image is saved path = ${File(dir, nameDrawable).path}")
                }
            }
    }

    private fun checkInCacheFolder(nameDrawable: String): Boolean {
        return if(dir.exists()){
            dir.listFiles { dir, name -> name == nameDrawable }.isNotEmpty()
        } else
            false
    }

}