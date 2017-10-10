package com.notadeveloper.app.blackboard.util

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.notadeveloper.app.blackboard.models.Availabilty
import com.notadeveloper.app.blackboard.models.ClassTimetable
import com.notadeveloper.app.blackboard.models.Faculty
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by krsnv on 10/10/2017.
 */
interface RetrofitInterface {
  @GET("auth/")
  fun loginFaculty(@Query("faculty_id") faculty_id: String, @Query(
      "password") password: String): Observable<Faculty>

  @GET("faculties/{faculty_id}/")
  fun getFaculty(@Path("faculty_id") faculty_id: String): Observable<Faculty>

  @GET("classes/{class_id}/")
  fun getClass(@Path("class_id") class_id: String): Observable<ClassTimetable>

  @GET("availability/")
  fun getAvailability(@Query("dept") dept: String,@Query("day") day: String,@Query("hour") hour: String): Observable<Availabilty>

  companion object Factory {
    fun create(): RetrofitInterface {
      val retrofit = Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(MoshiConverterFactory.create().asLenient())
          .client(OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build())
          .baseUrl("http://192.168.1.2:8000/")
          .build()
      return retrofit.create(RetrofitInterface::class.java)
      //        USAGE
      //        val apiService = RetrofitInterface.create()
      //        apiService.search(/* search params go in here */)

    }
  }

}