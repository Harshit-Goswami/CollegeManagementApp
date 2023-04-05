package com.harshit.goswami.collegeapp.data

data class ViewAttendanceDataModal(
    var rollNo: String = "",
var attendancePer:String = ""
)
data class AllAttendanceData(
    var rollNo: String = "",
    var name: String = "",
    val month:MonthData = MonthData()
):java.io.Serializable
data class MonthData(
    var month: String = "",
    var dateData: DateData = DateData()
):java.io.Serializable

data class DateData(var date: String = "", var subData: SubjectData= SubjectData()):java.io.Serializable

data class SubjectData(
    var subject: String = "",
    var status: String = ""
):java.io.Serializable
