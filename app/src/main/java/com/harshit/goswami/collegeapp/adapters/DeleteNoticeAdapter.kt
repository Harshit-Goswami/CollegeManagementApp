package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collegeapp.admin.DeleteNotice
import com.harshit.goswami.collegeapp.admin.DeleteNotice.Companion.noticeList
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.ItemDeleteNoticeBinding
import java.nio.file.Files.delete


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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(noticeList[position]) {
                if (activity == "NoticeFragment") {
                    binding.deleteNoticeBtn.visibility = View.GONE
                }
                binding.noticeDateAndTime.text = this.dateTime
                binding.noticeTite.text = this.title
                Glide.with(context).load(this.downloadUrl).into(binding.noticeImage)
                binding.deleteNoticeBtn.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Sure, you want to delete the Notice ?")
                    builder.setTitle("Delete Notice !")
                    builder.setCancelable(false)
                    builder.setPositiveButton("Yes"
                    ) { dialog: DialogInterface?, which: Int ->
                        if (activity == "EventActivity") {
                            FirebaseDatabase.getInstance().reference.child("Events")
                                .child(this.dateTime.toString()).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Data deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Failed:${it.message}.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl)
                                .delete().addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Image deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    DeleteNotice.noticeList.removeAt(position)
                                    DeleteNotice.binding.rsvNotices.adapter?.notifyDataSetChanged()

                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "ImageDeletion Failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            FirebaseDatabase.getInstance().reference.child("Notices")
                                .child(this.dateTime).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Data deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Failed:${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            FirebaseStorage.getInstance().reference.child("NoticeImages").child(dateTime)
                                .delete().addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Image deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    DeleteNotice.noticeList.removeAt(position)
                                    DeleteNotice.binding.rsvNotices.adapter?.notifyDataSetChanged()

                                }.addOnFailureListener {
                                    Log.d("image delete error",it.message.toString(),it)
                                    Toast.makeText(
                                        context,
                                        "ImageDeletion Failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                        builder.setNegativeButton("No",
                            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                                dialog.cancel()
                            } as DialogInterface.OnClickListener)
//showing the alert dialog by @Harshit Goshwami
                    builder.create().show()

                }
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return noticeList.size
    }
}