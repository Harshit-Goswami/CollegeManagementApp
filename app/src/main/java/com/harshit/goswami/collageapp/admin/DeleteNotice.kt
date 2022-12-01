package com.harshit.goswami.collageapp.admin

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collageapp.admin.DeleteNotice.Companion.noticeList
import com.harshit.goswami.collageapp.databinding.ActivityDeleteNoticeBinding

class DeleteNotice : AppCompatActivity() {
        companion object{
             lateinit var binding: ActivityDeleteNoticeBinding
             var noticeList = ArrayList<NoticeData>()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveNotices()
        binding.rsvNotices.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvNotices.adapter = DeleteNoticeAdapter(noticeList, this)
        binding.rsvNotices.setHasFixedSize(true)
    }

    private fun retrieveNotices() {
        binding.deleteNoticeProgbar.visibility = View.VISIBLE
        noticeList = ArrayList()
        FirebaseDatabase.getInstance().reference.child("Notices")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        noticeList.add(snapshot.getValue(NoticeData::class.java)!!)
                        Log.d( snapshot.value.toString(), "teacher data")
                        binding.rsvNotices.adapter?.notifyDataSetChanged()
                        binding.deleteNoticeProgbar.visibility = View.GONE
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DeleteNotice, error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

}