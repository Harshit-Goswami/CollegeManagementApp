package com.harshit.goswami.collegeapp.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminDeleteNoticeBinding

open class DeleteNotice : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    companion object {
        lateinit var binding: ActivityAdminDeleteNoticeBinding
        var noticeList = ArrayList<NoticeData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDeleteNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveNotices()
        binding.rsvNotices.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvNotices.adapter = DeleteNoticeAdapter(noticeList, this, "DeleteNotice")
        binding.rsvNotices.setHasFixedSize(true)
    }

    fun retrieveNotices() {
        binding.deleteNoticeProgbar.visibility = View.VISIBLE
        noticeList = ArrayList()
        firestore.collection("Notices").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this@DeleteNotice, "Error found is $error", Toast.LENGTH_SHORT)
                    .show()
            }
            value?.documents?.forEach{
                noticeList.add(it.toObject(NoticeData::class.java)!!)
            }
            binding.rsvNotices.adapter?.notifyDataSetChanged()
            binding.deleteNoticeProgbar.visibility = View.GONE
            noticeList.sortWith(compareByDescending { "${it.date}${it.time}"})

        }

    }
}