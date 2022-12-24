package com.harshit.goswami.collegeapp.student

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.admin.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.admin.dataClasses.NoticeData
import com.harshit.goswami.collegeapp.databinding.FragmentNoticeBinding

class NoticeFragment : Fragment(), Backpressedlistener {
    companion object {
        var backpressedlistener: Backpressedlistener? = null
    }

    var noticeList = ArrayList<NoticeData>()
    private lateinit var binding: FragmentNoticeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)
        binding.FNBtnBack.setOnClickListener {
            val homeFrag = activity?.supportFragmentManager?.beginTransaction()
            homeFrag?.replace(R.id.fragment_container, HomeFragment())?.commit()
            MainActivity.binding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
            MainActivity.binding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))

            MainActivity.binding.textViewMyclass.setTextColor(Color.BLACK)
            MainActivity.binding.textViewNotice.setTextColor(Color.BLACK)
            MainActivity.binding.imageViewNotice.setColorFilter(Color.BLACK)
            MainActivity.binding.textViewEvent.setTextColor(Color.BLACK)
            MainActivity.binding.imageViewEvent.setColorFilter(Color.BLACK)
            MainActivity.binding.textViewAbout.setTextColor(Color.BLACK)
            MainActivity.binding.imageViewAbout.setColorFilter(Color.BLACK)

        }
        retrieveNotices()
        binding.FNRsvNotices.layoutManager =
            LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL, false)
        binding.FNRsvNotices.adapter =
            container?.context?.let {
                DeleteNoticeAdapter(
                    noticeList,
                    it,
                    "NoticeFragment"
                )
            }
        binding.FNRsvNotices.setHasFixedSize(true)
        binding.FNRsvNotices.adapter?.notifyDataSetChanged()



        return binding.root
    }

    fun retrieveNotices() {
        noticeList = ArrayList()
        FirebaseDatabase.getInstance().reference.child("Notices")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        noticeList.add(snapshot.getValue(NoticeData::class.java)!!)
                        Log.d(snapshot.value.toString(), "teacher data")
                        binding.FNRsvNotices.adapter?.notifyDataSetChanged()

                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    override fun onPause() {
        backpressedlistener = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        backpressedlistener = this
    }

    override fun onBackPressed() {
        val homeFrag = activity?.supportFragmentManager?.beginTransaction()
        homeFrag?.replace(R.id.fragment_container, HomeFragment())?.commit()
        MainActivity.binding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
        MainActivity.binding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))

        MainActivity.binding.textViewMyclass.setTextColor(Color.BLACK)
        MainActivity.binding.textViewNotice.setTextColor(Color.BLACK)
        MainActivity.binding.imageViewNotice.setColorFilter(Color.BLACK)
        MainActivity.binding.textViewEvent.setTextColor(Color.BLACK)
        MainActivity.binding.imageViewEvent.setColorFilter(Color.BLACK)
        MainActivity.binding.textViewAbout.setTextColor(Color.BLACK)
        MainActivity.binding.imageViewAbout.setColorFilter(Color.BLACK)
    }


}