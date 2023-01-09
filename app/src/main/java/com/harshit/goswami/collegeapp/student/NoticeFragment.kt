package com.harshit.goswami.collegeapp.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.FragmentNoticeBinding

class NoticeFragment : Fragment() {

    var noticeList = ArrayList<NoticeData>()
    private lateinit var binding: FragmentNoticeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)
        binding.FNBtnBack.setOnClickListener {
            activity?.onBackPressed()
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

        binding.FNScrollView.viewTreeObserver.addOnScrollChangedListener {
            val y = binding.FNScrollView.scrollY
            if (y>200) {
                MainActivity.binding.cordinatorNavBar.visibility = View.GONE
            } else MainActivity.binding.cordinatorNavBar.visibility = View.VISIBLE
        }

        return binding.root
    }

    fun retrieveNotices() {
        FirebaseDatabase.getInstance().reference.child("Notices")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noticeList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            noticeList.add(i.getValue(NoticeData::class.java)!!)
                        }
                        binding.FNRsvNotices.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

}