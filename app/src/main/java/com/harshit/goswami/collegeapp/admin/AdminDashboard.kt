package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.admin.dataClasses.AdminLoginData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminDashboardBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminViewProfileBinding
import java.net.URL


class AdminDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    val fireDb = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cardClickListenrs()
        val imgUrl = "https://t3.ftcdn.net/jpg/05/15/63/82/240_F_515638234_Leo0UBEay0ozXWnObkkxLRNJXM9xhdWG.jpg"
        Glide.with(this)
            .load(imgUrl)
            .into(binding.ADAdminImage)

        binding.ADAdminImage.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.ADAdminImage)
            popupMenu.menuInflater.inflate(
                com.harshit.goswami.collegeapp.R.menu.logged_admin_menu,
                popupMenu.menu
            )
            popupMenu.setOnMenuItemClickListener {
                when (it.title) {
                    "View Profile" -> {
                        val viewProfileBinding =
                            DialogAdminViewProfileBinding.inflate(layoutInflater)
                        val builder = AlertDialog.Builder(
                            this,
                            com.harshit.goswami.collegeapp.R.style.CustomAlertDialog
                        ).create()
                        builder.window?.setGravity(Gravity.CENTER)
                        val window: Window? = builder.window
                        window!!.setGravity(Gravity.CENTER)
                        window.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
//                        builder.setCancelable(true)
                        builder.setView(viewProfileBinding.root)
                        builder.setCanceledOnTouchOutside(true)

                        fireDb.child("BScIT").child("Admin")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (data in snapshot.children) {
                                            Glide.with(this@AdminDashboard)
                                                .load(data.getValue(AdminLoginData::class.java)!!.profilePicUrl)
                                                .into(viewProfileBinding.DVPImgAdmin)
                                            viewProfileBinding.DVPAdminName.text =
                                                data.getValue(AdminLoginData::class.java)!!.fullName
                                            viewProfileBinding.DVPAdminDepartment.text =
                                                data.getValue(AdminLoginData::class.java)!!.department
                                        }
                                    } else Toast.makeText(
                                        this@AdminDashboard,
                                        "snapshot not exist!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@AdminDashboard,
                                        "Error!-${error.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })


                        builder.show()
                    }
                    "Edit Profile" -> {}
                    "Log Out" -> {}

                }
                true
            }

            popupMenu.show()
        }
    }

    private fun cardClickListenrs() {

        binding.cardUploadNotice.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadNotice::class.java
                )
            )
        }
        binding.uploadImage.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadImage::class.java
                )
            )
        }
        binding.uploadBook.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ManageStudent::class.java
                )
            )
        }
        binding.updateFaculty.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ManageFaculty::class.java
                )
            )
        }
        binding.deleteNotice.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    DeleteNotice::class.java
                )
            )
        }
        binding.AddEvent.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UploadedEvents::class.java
                )
            )
        }
        binding.AddClass.setOnClickListener { startActivity(Intent(this, UploadClass::class.java)) }

    }
}