package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.ViewImageActivity
import com.harshit.goswami.collegeapp.databinding.ItemGalleryBinding
import com.harshit.goswami.collegeapp.student.GalleryActivity

class GalleryAdapter(
    private var imageList: ArrayList<Map<String,String>> = ArrayList(),
    private val context: Context
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(internal val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        val view =
            ItemGalleryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(imageList[position]) {
                Glide.with(context).load(this["url"]).into(binding.itemGalleryImageView)
                binding.itemGalleryImageView.setOnClickListener {
                    val i = Intent(context, ViewImageActivity::class.java)
                    i.putExtra("imageUrl", this["url"])
                    context.startActivity(i)
                }
                if (GalleryActivity.by == "admin") {
                    binding.itemGalleryImageView.setOnLongClickListener {
                        val popupMenu = PopupMenu(context, binding.itemGalleryImageView)
                        popupMenu.menuInflater.inflate(R.menu.menu_delete, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { menu ->
                            when (menu.itemId) {
                                R.id.menu_delete -> {
                                    FirebaseFirestore.getInstance().collection("Campus Gallery Images")
                                        .document(this["category"].toString())
                                        .collection("images").document(this["key"].toString()).delete()
                                        .addOnSuccessListener {
                                            FirebaseStorage.getInstance().getReferenceFromUrl(this["url"].toString()).delete()
                                                .addOnSuccessListener {
                                                    Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                }
                            }
                            true
                        }
                        popupMenu.show()
                        true
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}