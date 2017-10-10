package com.notadeveloper.app.blackboard.models

import com.squareup.moshi.Json
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by krsnv on 10/10/2017.
 */
data class FacultySchedule(
    @Json(name = "class_id")
    val classId: String,
    @Json(name = "subj_code")
    val subjCode: String,
    @Json(name = "day")
    val day: String,
    @Json(name = "hour")
    val hour: String,
    @Json(name = "class_id__location")
    val classIdLocation: String
)

data class FacultyList(
    @Json(name = "faculty_id")
    val facultyId: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "phone")
    val phone: String?,
    @Json(name = "dept")
    val dept: String?
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
    val password: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "faculty_type")
    val facultyType: String,
    @Json(name = "incharge_of")
    val inchargeOf: String?=null,
    @Json(name = "faculty_list")
    val facultyList: List<FacultyList>?=null,
    @Json(name = "faculty_schedule")
    val facultySchedule: List<FacultySchedule>?=null,
    @Json(name = "detail")
    val detail: String?=null

)

public class Availabilty(
    @Json(name = "dept")
    val dept: String,
    @Json(name = "day")
    val day: String,
    @Json(name = "hour")
    val hour: String,
    @Json(name = "availability")
    val availability: List<FacultyList>?
)

data class ClassSchedule(

    @Json(name = "faculty_id__name")
    val facultyIdName: String,
    @Json(name = "subj_code")
    val subjCode: String,
    @Json(name = "day")
    val day: String,
    @Json(name = "hour")
    val hour: String
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
    @Json(name = "location")
    val location: String,
    @Json(name = "class_timetable")
    val classTimetable: List<ClassSchedule>,
    @Json(name = "detail")
    val detail: String?
)

open class CurrentFaculty(
    // You can put properties in the constructor as long as all of them are initialized with
    // default values. This ensures that an empty constructor is generated.
    // All properties are by default persisted.
    // Properties can be annotated with PrimaryKey or Index.
    // If you use non-nullable types, properties must be initialized with non-null values.
    @PrimaryKey var faculty_id: String="",
    var password: String = "",
    var email: String="",
    var phone: String="",
    var dept: String = "",
    var name: String = "",
    var facultyType: String = "",
    var inchargeOf: String? = null
    ) : RealmObject()
open class CurrentFacultyList(
    var facultyId: String="",
    var name: String="",
    var phone: String?=null,
    var dept: String?=null
) : RealmObject()
open class CurrentFacultySchedule(
    var classId: String="",
    var subjCode: String="",
    var day: String="",
    var hour: String="",
    var classIdLocation: String=""
) : RealmObject()

