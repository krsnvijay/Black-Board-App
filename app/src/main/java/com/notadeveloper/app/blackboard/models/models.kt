package com.notadeveloper.app.blackboard.models

import com.squareup.moshi.Json

/**
 * Created by krsnv on 10/10/2017.
 */
data class Schedule(
    @Json(name = "faculty_id")
    val facultyId: String,
    @Json(name = "class_id")
    val classId: String,
    @Json(name = "class_location")
    val classLocation: String,
    @Json(name = "subj_code")
    val subjCode: String,
    @Json(name = "subj_name")
    val subjName: String,
    @Json(name = "day")
    val day: String,
    @Json(name = "hour")
    val hour: String,
    @Json(name = "detail")
    val detail: String?
)

data class Faculty(
    @Json(name = "faculty_id")
    val facultyId: String,
    @Json(name = "dept")
    val dept: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String?,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "faculty_type")
    val facultyType: String,
    @Json(name = "incharge_of")
    val inchargeOf: String?,
    @Json(name = "responsibilities")
    val responsibilities: List<String>,
    @Json(name = "detail")
    val detail: String?

)

data class ClassTimetable(

    @Json(name = "class_id")
    val classId: String,
    @Json(name = "dept")
    val dept: String,
    @Json(name = "year")
    val year: String,
    @Json(name = "section")
    val section: String,
    @Json(name = "class_location")
    val location: String,
    @Json(name = "class_timetable")
    val classTimetable: List<Schedule>,
    @Json(name = "detail")
    val detail: String?
)


