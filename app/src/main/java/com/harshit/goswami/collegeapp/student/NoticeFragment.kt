package com.harshit.goswami.collegeapp.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
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
                    "viewNotice"
                )
            }
        binding.FNRsvNotices.setHasFixedSize(true)
        binding.FNRsvNotices.adapter?.notifyDataSetChanged()
        binding.FNRsvNotices.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > 0) {
                    MainActivity.mainBinding.cordinatorNavBar.visibility = View.GONE
                } else MainActivity.mainBinding.cordinatorNavBar.visibility = View.VISIBLE
            }
        })
//        binding.FNScrollView.viewTreeObserver.addOnScrollChangedListener {
//            val y = binding.FNScrollView.scrollY
//            if (y>200) {
//                MainActivity.mainBinding.cordinatorNavBar.visibility = View.GONE
//            } else MainActivity.mainBinding.cordinatorNavBar.visibility = View.VISIBLE
//        }

        return binding.root
    }

    fun retrieveNotices() {
        FirebaseFirestore.getInstance().collection("Notices")
            .addSnapshotListener { value, error ->
                noticeList.clear()

                if (error != null) {
                    Toast.makeText(
                        requireContext(), "Error found is $error", Toast.LENGTH_SHORT
                    )
                        .show()
                }
                value?.documents?.forEach {
                    noticeList.add(it.toObject(NoticeData::class.java)!!)
                }
                binding.FNRsvNotices.adapter?.notifyDataSetChanged()
                noticeList.sortWith(compareByDescending { "${it.date}${it.time}" })

            }

    }

}