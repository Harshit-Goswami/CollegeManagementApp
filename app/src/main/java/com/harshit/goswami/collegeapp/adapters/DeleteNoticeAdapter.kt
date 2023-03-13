package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collegeapp.ViewImageActivity
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.ItemDeleteNoticeBinding
import java.util.*


class DeleteNoticeAdapter(
    private var noticeList: ArrayList<NoticeData> = ArrayList(),
    val context: Context,
    val activity: String
) : RecyclerView.Adapter<DeleteNoticeAdapter.ViewHolder>() {
    class ViewHolder(internal val binding: ItemDeleteNoticeBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeleteNoticeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(noticeList[position]) {
                if (activity == "NoticeFragment") {
                    binding.deleteNoticeBtn.visibility = View.GONE
                }
                binding.noticeDateAndTime.text = "${this.date} : ${this.time}"
                binding.noticeTite.text = this.title
                Glide.with(context).load(this.downloadUrl).into(binding.noticeImage)
                binding.noticeImage.setOnClickListener {
                    val i = Intent(context, ViewImageActivity::class.java)
                    i.putExtra("imageUrl",this.downloadUrl)
                    context.startActivity(i)
                }
                binding.deleteNoticeBtn.setOnClickListener {
                    FirebaseFirestore.getInstance().collection("Notices")
                        .document(this.date+this.time).delete().addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "${this.title} Notice deleted",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl)
                                .delete().addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "${this.title} Notice Image deleted",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                        }

                }
                val sdfDate =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = sdfDate.format(Calendar.getInstance().time)
                if (date == this.deletionDate) {
                    FirebaseFirestore.getInstance().collection("Notices")
                        .document(this.date+this.time).delete().addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "${this.title} Notice deleted",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl)
                                .delete().addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "${this.title} Notice Image deleted",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                        }
                }
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return noticeList.size
    }
}