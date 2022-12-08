package com.harshit.goswami.collegeapp

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.harshit.goswami.collegeapp.admin.AdminDashboard
import com.harshit.goswami.collegeapp.admin.DeleteNotice.Companion.binding
import com.harshit.goswami.collegeapp.cr.CRdashboard
import com.harshit.goswami.collegeapp.databinding.ActivityLoginBinding
import com.harshit.goswami.collegeapp.student.MainActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.getStringExtra("SelectedUser") == "student") binding.txtRegToLogIn.visibility = View.VISIBLE
        /* val pref: SharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
       val isUserLogin = pref.getBoolean("isUserLogin",false)
       if (isUserLogin) {
           val intent = Intent(this, AdminDashboard::class.java)
           startActivity(intent)
           finish()
       }*/
//        val sppinerItems = arrayOf("Student", "Admin", "CR")
//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sppinerItems)
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // binding.spinner.adapter = adapter


        binding.txtRegToLogIn.setOnClickListener {
            val intent = Intent(this, ResisterAsStutent::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener { updateUI() }//loginUser()

    }
    /* val pref: SharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
        val isUserLogin = pref.getBoolean("
        isUserLogin", false)
        if (isUserLogin) {
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish()*/

//    fun CharSequence?.isValidEmail() =
//        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    // private fun loginUser() {
//        val auth = FirebaseAuth.getInstance()
//        val email = binding.username.text.toString()
//        val password = binding.password.text.toString()
//        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
//        if (!email.isValidEmail()) {
//            binding.username.error = "Invalid Email"
//        } else if (password.isEmpty()) {
//            binding.password.error = "Password should not be empty"
//        } else if (password.length < 4) {
//            binding.password.error = "Password should be greater than 4"
//        } else {
//            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
//                Log.d(TAG, "signInWithEmail:success")
//                val user = auth.currentUser
//                updateUI()
//            }.addOnFailureListener {
//                Log.d(TAG, "some this went wrong...${it.message}")
//                Toast.makeText(this, "Error.=${it.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun updateUI() {
        val user = intent.getStringExtra("SelectedUser")

        when (user) {
            "admin" -> {
                val intent = Intent(this, AdminDashboard::class.java)
//                val pref: SharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
//                val editor = pref.edit()
//                editor.putBoolean("isUserLogin", true).commit()
                startActivity(intent)
                // finish()
            }
            "teacher" -> {
                val intent = Intent(this, CRdashboard::class.java)
                startActivity(intent)
                //finish()
            }
            "student" -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // finish()
            }
            "other" -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
