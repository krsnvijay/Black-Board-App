package com.notadeveloper.app.blackboard.util

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.notadeveloper.app.blackboard.models.ClassTimetable
import com.notadeveloper.app.blackboard.models.Faculty
import com.notadeveloper.app.blackboard.models.Responsibility
import com.notadeveloper.app.blackboard.models.Schedule
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

  @GET("faculties/")
  fun getFacultyList(@Query("dept") dept: String, @Query(
      "faculty_type") facultyType: String): Observable<List<Faculty>>

  @GET("schedules/")
  fun getFacultySchedule(@Query("faculty_id") faculty_id: String): Observable<List<Schedule>>

  @GET("classes/{class_id}/")
  fun getClass(@Path("class_id") class_id: String): Observable<ClassTimetable>

  @GET("availability/")
  fun getAvailability(@Query("dept") dept: String, @Query("day") day: String, @Query(
      "hour") hour: String): Observable<List<Faculty>>

  @GET("responsibilities/")
  fun getResponsibilities(): Observable<List<Responsibility>>

  companion object Factory {
    fun create(): RetrofitInterface {
      val retrofit = Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(MoshiConverterFactory.create().asLenient())
          .client(OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build())
          .baseUrl("https://blackboard-rest-api.herokuapp.com/")
          .build()
      return retrofit.create(RetrofitInterface::class.java)
      //        USAGE
      //        val apiService = RetrofitInterface.create()
      //        apiService.search(/* search params go in here */)

    }
  }

}