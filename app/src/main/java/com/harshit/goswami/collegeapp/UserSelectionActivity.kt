package com.harshit.goswami.collegeapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.harshit.goswami.collegeapp.databinding.ActivityUserSelectionBinding
import com.harshit.goswami.collegeapp.student.MainActivity

class UserSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSelectionBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
//                Toast.makeText(this,"uploaded Successfully!",Toast.LENGTH_SHORT).show()
//                FCMnotificationSender(
//                    "/topics/all", "Notification title", "this is notice notification",
//                    "BIGPIC",
//                    downloadUrl.toString(),//applicationContext,
//                    this@UserSelectionActivity
//                )
//                    .sendNotifications()
//            }
//            else{
            FCMnotificationSender(
                "/topics/all", "Notification title", "this is notice notification body",
                "BIGTEXT",
                "", //applicationContext,
                this@UserSelectionActivity
            )
                .sendNotifications()

        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic("all")//all subscribed
//        FirebaseMessaging.getInstance().unsubscribeFromTopic("notice")
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // You can use the API that requires the permission.
                Log.e("permission Granted", "onCreate: PERMISSION GRANTED")
                FCMnotificationSender(
                    "/topics/all", "Notification title", "this is notice notification body",
                    "BIGTEXT",
                    "", //applicationContext,
                    this@UserSelectionActivity
                )
                    .sendNotifications()
            }
            else -> {
                // The registered ActivityResultCallback gets the result of this request
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }

        binding.cardUSAdmin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "admin")
            startActivity(intent)
        }
        binding.cardUSTeacher.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "teacher")
            startActivity(intent)
        }
        binding.cardUSStudent.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "student")
            startActivity(intent)
        }
        binding.cardUSOther.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", "other")
            startActivity(intent)
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("token", token.toString())
            Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}