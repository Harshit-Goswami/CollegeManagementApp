package com.harshit.goswami.collegeapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class NetworkSecurity: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}