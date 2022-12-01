package com.harshit.goswami.collageapp.student

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.harshit.goswami.collageapp.admin.AdminDashboard
import com.harshit.goswami.collageapp.cr.CRdashboard
import com.harshit.goswami.collageapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /* val pref: SharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
       val isUserLogin = pref.getBoolean("isUserLogin",false)
       if (isUserLogin) {
           val intent = Intent(this, AdminDashboard::class.java)
           startActivity(intent)
           finish()
       }*/
        val sppinerItems = arrayOf("Student", "Admin", "CR")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sppinerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.loginBtn.setOnClickListener { updateUI() }//loginUser()

    }
    /* val pref: SharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
        val isUserLogin = pref.getBoolean("isUserLogin", false)
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
        when (binding.spinner.selectedItem.toString()) {
            "Admin" -> {
                val intent = Intent(this, AdminDashboard::class.java)
//                val pref: SharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
//                val editor = pref.edit()
//                editor.putBoolean("isUserLogin", true).commit()
                startActivity(intent)
                finish()
            }
            "CR" -> {
                val intent = Intent(this, CRdashboard::class.java)
                startActivity(intent)
                finish()
            }
            "Student" -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    }
