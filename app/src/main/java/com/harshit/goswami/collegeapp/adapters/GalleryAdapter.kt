package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshit.goswami.collegeapp.ViewImageActivity
import com.harshit.goswami.collegeapp.databinding.ItemGalleryBinding

class GalleryAdapter(
    private var imageList: ArrayList<String> = ArrayList(),
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
                Glide.with(context).load(this).into(binding.itemGalleryImageView)
                binding.itemGalleryImageView.setOnClickListener {
                    val i = Intent(context,ViewImageActivity::class.java)
                    i.putExtra("imageUrl",this)
                    context.startActivity(i)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}