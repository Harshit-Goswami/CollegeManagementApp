package com.harshit.goswami.collegeapp.admin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminDeleteNoticeBinding

open class DeleteNotice : AppCompatActivity() {
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
        FirebaseDatabase.getInstance().reference.child("Notices")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            noticeList.add(it.getValue(NoticeData::class.java)!!)
                        }
                        binding.rsvNotices.adapter?.notifyDataSetChanged()
                        binding.deleteNoticeProgbar.visibility = View.GONE
                        noticeList.sortWith(compareByDescending { it.dateTime })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}