package com.harshit.goswami.collegeapp.data

data class ViewAttendanceDataModal(
    var rollNo: String = "",
var attendancePer:String = ""
)

data class MonthData(
    var month: String = "",
    var dateData: ArrayList<DateData> = ArrayList()
)

data class DateData(var date: String = "", var subData: ArrayList<SubjectData> = ArrayList())

data class SubjectData(
    var subject: String = "",
    var status: String = ""
)