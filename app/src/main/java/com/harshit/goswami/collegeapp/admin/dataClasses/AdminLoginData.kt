package com.harshit.goswami.collegeapp.admin.dataClasses

data class AdminLoginData(
    val userId: String = "",
    val fullName: String = "",
    val profilePicUrl: String = "",
    val department: String = "",
    val password: String = "")
