package com.harshit.goswami.collegeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.harshit.goswami.collegeapp.databinding.ActivityUserSelectionBinding
import com.harshit.goswami.collegeapp.student.MainActivity

class UserSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic("all")//all subscribed
//        FirebaseMessaging.getInstance().unsubscribeFromTopic("notice")
        FCMnotificationSender("/topics/all","Test","body of the notification for testing",applicationContext,this@UserSelectionActivity)
            .sendNotifications()

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