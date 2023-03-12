package com.harshit.goswami.collegeapp.adapters

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.harshit.goswami.collegeapp.ViewPdfActivity
import com.harshit.goswami.collegeapp.data.NotesData
import com.harshit.goswami.collegeapp.databinding.ItemStudentNotesBinding

class GalleryAdapter(
    private var notesList: ArrayList<NotesData> = ArrayList(),
    private val context: Context
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(internal val binding: ItemStudentNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        val view =
            ItemStudentNotesBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(notesList[position]) {
                binding.IPdfTitle.text = this.title
                binding.IPdfSubject.text = this.subject
                binding.IPdfDescription.text = this.description
                binding.IReLViewPdf.setOnClickListener {
                    val i = Intent(context, ViewPdfActivity::class.java)
                    i.putExtra("pdfUrl", this.downloadUrl)
                    context.startActivity(i)
                }
                binding.IImgDownload.setOnClickListener {
//                    val i = Intent(Intent.ACTION_VIEW)
//                    i.data = Uri.parse(this.downloadUrl)
//                    context.startActivity(i)
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
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


}