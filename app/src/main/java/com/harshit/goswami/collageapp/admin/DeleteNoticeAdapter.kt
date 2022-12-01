package com.harshit.goswami.collageapp.admin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collageapp.databinding.ItemNoticeAdapterBinding


class DeleteNoticeAdapter (
   private var noticeList: ArrayList<NoticeData> = ArrayList(),val context: Context) : RecyclerView.Adapter<DeleteNoticeAdapter.ViewHolder>() {
        class ViewHolder(internal val binding: ItemNoticeAdapterBinding) : RecyclerView.ViewHolder(binding.root)


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemNoticeAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
            return ViewHolder(binding)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder){
                with(noticeList[position]){
                    binding.noticeDateAndTime.text = this.date + " : "+ this.time
                    binding.noticeTite.text =this.title
                    Glide.with(context).load(this.downloadUrl).into(binding.noticeImage)
                    binding.deleteNoticeBtn.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Sure, you want to delete the Notice ?")
                        builder.setTitle("Delete Notice !")
                        builder.setCancelable(false)
                        builder.setPositiveButton("Yes",
                            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                                FirebaseDatabase.getInstance().reference.child("Notices").child(this.uniqueKey.toString()).removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(context,"Data deleted Successfully", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(context,"Failed:${it.message}.", Toast.LENGTH_SHORT).show()
                                    }
                                FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl).delete().addOnSuccessListener {
                                    Toast.makeText(context,"Image deleted Successfully", Toast.LENGTH_SHORT).show()
                                    DeleteNotice.noticeList.removeAt(position)
                                    DeleteNotice.binding.rsvNotices.adapter?.notifyDataSetChanged()


                                }.addOnFailureListener {
                                    Toast.makeText(context,"ImageDeletion Failed.", Toast.LENGTH_SHORT).show()
                                }
                            } as DialogInterface.OnClickListener)

                        builder.setNegativeButton("No",
                            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                                dialog.cancel()
                            } as DialogInterface.OnClickListener)

                        // Create the Alert dialog

                        // Create the Alert dialog
                        val alertDialog = builder.create()
                        // Show the Alert Dialog box
                        // Show the Alert Dialog box
                        alertDialog.show()
                    }
                }
            }

        }

        // return the size of languageList
        override fun getItemCount(): Int {
            return noticeList.size
        }
}