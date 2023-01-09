package com.harshit.goswami.collegeapp.student

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.data.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ActivityStudentResisterAsStutentBinding

class ResisterAsStutent : AppCompatActivity() {
    private var Sfullname = ""
    private var Sdepartment = ""
    private var Syear = ""
    private var Scontact = ""
    private var Spassword = ""
    private var Srollno = ""
    private val FireRef = FirebaseDatabase.getInstance()
    private lateinit var binding: ActivityStudentResisterAsStutentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentResisterAsStutentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemsDeparment = listOf("BScIT", "BMS", "BAF", "BMM")
        val adapterDepatment = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            itemsDeparment
        )
        binding.RASdepartment.setAdapter(adapterDepatment)
        val itemsYear = listOf("FY", "SY", "TY")
        val adapterYear = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            itemsYear
        )
        binding.RASyear.setAdapter(adapterYear)

        binding.RASBtnSubmit.setOnClickListener {
            if (binding.RASfullname.text.toString().isNotEmpty()) {
                Sfullname = binding.RASfullname.text.toString()
            } else {
                binding.RASfullname.error = "this field Should not be Empty"
            }
            if ((binding.RASrollNo.text.toString().isNotEmpty())) {
                Srollno = binding.RASrollNo.text.toString()
            } else {
                binding.RASrollNo.error = "this field Should not be Empty"
            }
            if (android.util.Patterns.PHONE.matcher(binding.RASContactNo.text.toString())
                    .matches()
            ) {
                Scontact = binding.RASContactNo.text.toString()
            } else {
                binding.RASContactNo.error = "Please enter Valid Number"
            }

            if (binding.RASdepartment.text.toString().isNotEmpty()) {
                Sdepartment = binding.RASdepartment.text.toString()

            } else {
                binding.RASdepartment.error = "please select your department"
            }

            if (binding.RASyear.text.toString().isNotEmpty()) {
                Syear = binding.RASyear.text.toString()
            } else {
                binding.RASyear.error = "please select your year"
            }

            if (binding.RASpasswd.text.toString().isNotEmpty()) {
                if (binding.RASpasswd.text.toString().length > 7) {
                    if (binding.RASpasswd.text.toString() == binding.RASrePasswd.text.toString()) {
                        Spassword = binding.RASpasswd.text.toString()
                    } else {
                        binding.RASrePasswd.error = "Password does not match"
                    }
                } else {
                    binding.RASpasswd.error = "Password Should contain at least 8 characters"
                }
            } else {
                binding.RASpasswd.error = "please Enter your password"
            }
            sendData()
        }
    }

    private fun sendData() {
        FireRef.reference.keepSynced(true)
        if (Sfullname != "" && Srollno != "" && Sdepartment != "" && Spassword != "" && Scontact != "" && Syear != "") {

            FireRef.reference.child("BScIT").child("Registered Student").child(Sfullname).setValue(
                RegisteredStudentData(
                    Sfullname, Srollno, Sdepartment, Syear, Scontact, Spassword
                )
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Toast.makeText(this@ResisterAsStutent, "Done!! ", Toast.LENGTH_SHORT).show()
                    val snackBar = Snackbar.make(
                        binding.ActivityRegisterAsStudent, "Data Added Successfully..",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null)
                    snackBar.setActionTextColor(Color.WHITE)
                    val snackBarView = snackBar.view
                    snackBarView.setBackgroundColor(Color.GREEN)
                    val textView =
                        snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.WHITE)
                    snackBar.show()
                    binding.RASfullname.setText("")
                    binding.RASdepartment.setText("")
                    binding.RASyear.setText("")
                    binding.RASContactNo.setText("")
                    binding.RASrollNo.setText("")
                    binding.RASpasswd.setText("")
                    binding.RASrePasswd.setText("")
                } else {
                    val snackBar = Snackbar.make(
                        binding.ActivityRegisterAsStudent, "Error..${it.exception?.message}",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null)
                    snackBar.setActionTextColor(Color.WHITE)
                    val snackBarView = snackBar.view
                    snackBarView.setBackgroundColor(Color.RED)
                    val textView =
                        snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.WHITE)
                    snackBar.show()
                    binding.RASfullname.setText("")
                    binding.RASdepartment.setText("")
                    binding.RASyear.setText("")
                    binding.RASContactNo.setText("")
                    binding.RASrollNo.setText("")
                    binding.RASpasswd.setText("")
                    binding.RASrePasswd.setText("")
                }
            }
        }
    }
}
