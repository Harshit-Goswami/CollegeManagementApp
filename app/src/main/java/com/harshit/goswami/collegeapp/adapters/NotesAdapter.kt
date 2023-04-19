package com.harshit.goswami.collegeapp.adapters

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collegeapp.ViewPdfActivity
import com.harshit.goswami.collegeapp.data.NotesData
import com.harshit.goswami.collegeapp.databinding.ItemStudentNotesBinding
import com.harshit.goswami.collegeapp.student.AssignmentNotesActivity
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard

class NotesAdapter(
    private var notesList: ArrayList<NotesData> = ArrayList(),
    private val context: Context
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
val fireDb = FirebaseDatabase.getInstance().reference
    inner class ViewHolder(internal val binding: ItemStudentNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.ViewHolder {
        val view =
            ItemStudentNotesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(notesList[position]) {
                if (AssignmentNotesActivity.cardClick == "S_notes" || AssignmentNotesActivity.cardClick == "S_assignment" ) {
                    binding.IImgDelete.visibility = View.GONE
                }
                binding.IPdfTitle.text = this.title
                binding.IPdfSubject.text = this.subject
                binding.IPdfDescription.text = this.description
                binding.IReLViewPdf.setOnClickListener {
                    val i = Intent(context, ViewPdfActivity::class.java)
                    i.putExtra("pdfUrl", this.downloadUrl)
                    context.startActivity(i)
                }
                binding.IImgDownload.setOnClickListener {
                    val uri: Uri = Uri.parse(this.downloadUrl)
                    val request = DownloadManager.Request(Uri.parse(uri.toString()))
                    request.setDescription("Pdf downloading...")
                    request.setTitle("${this.title}.pdf")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "${this.title}.pdf"
                    )
                    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    manager.enqueue(request)
                }
                binding.IImgDelete.setOnClickListener {
                    if (AssignmentNotesActivity.cardClick == "T_notes"){
                        fireDb.child("Notes").child(TeacherDashboard.loggedTeacherDep)
                            .child(this.year).child(this.teacher).child(this.subject).child(this.title).removeValue().addOnSuccessListener {
                                FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl)
                                    .delete().addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "${this.title} deleted!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                            }
                    }
                    if (AssignmentNotesActivity.cardClick == "T_assignment"){
                        fireDb.child("Given Assignments").child(TeacherDashboard.loggedTeacherDep)
                            .child(this.year).child(this.teacher).child(this.subject).child(this.title).removeValue().addOnSuccessListener {
                                FirebaseStorage.getInstance().getReferenceFromUrl(this.downloadUrl)
                                    .delete().addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "${this.title} deleted!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                            }
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


}