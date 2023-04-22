package com.harshit.goswami.collegeapp.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.adapters.GalleryAdapter
import com.harshit.goswami.collegeapp.admin.UploadImage
import com.harshit.goswami.collegeapp.databinding.ActivityStudentGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentGalleryBinding
    private val imageListConvocation = ArrayList<Map<String, String>>()
    private val imageListIndependenceDay = ArrayList<Map<String, String>>()
    private val imageListOther = ArrayList<Map<String, String>>()

    companion object {
        var by: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        by = intent.getStringExtra("by").toString()
        fetchConvocationImageData()
        fetchIndependenceImageData()
        fetchOtherImageData()
        recyclerViewSetUp()
        if (by == "student") binding.btnAddGallery.visibility = View.GONE
        binding.btnAddGallery.setOnClickListener {
            startActivity(Intent(this, UploadImage::class.java))
        }
    }

    private fun fetchConvocationImageData() {
        FirebaseFirestore.getInstance().collection("Campus Gallery Images").document("Convocation")
            .collection("images").addSnapshotListener { value, error ->
                imageListConvocation.clear()
                value?.forEach { i ->
                    imageListConvocation.add(
                        mapOf(
                            "url" to i["url"].toString(),
                            "category" to i["category"].toString(),
                            "key" to i["key"].toString()
                        )
                    )
                }
                binding.rsvGalleryConvocation.adapter?.notifyDataSetChanged()
            }
    }

    private fun fetchIndependenceImageData() {
        FirebaseFirestore.getInstance().collection("Campus Gallery Images")
            .document("Independence Day")
            .collection("images").addSnapshotListener { value, error ->
                imageListIndependenceDay.clear()
                value?.forEach { i->
                    imageListIndependenceDay.add(
                        mapOf(
                            "url" to i["url"].toString(),
                            "category" to i["category"].toString(),
                            "key" to i["key"].toString()
                        )
                    )
                }
                binding.rsvGalleryRepublicDay.adapter?.notifyDataSetChanged()
            }
    }

    private fun fetchOtherImageData() {
        FirebaseFirestore.getInstance().collection("Campus Gallery Images").document("Other events")
            .collection("images").addSnapshotListener { value, error ->
                imageListOther.clear()
                value?.forEach { i->
                    imageListOther.add(
                        mapOf(
                            "url" to i["url"].toString(),
                            "category" to i["category"].toString(),
                            "key" to i["key"].toString()
                        )
                    )
                }
                binding.rsvGalleryOther.adapter?.notifyDataSetChanged()
            }
    }

    private fun recyclerViewSetUp() {
        binding.rsvGalleryConvocation.layoutManager =
            GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        binding.rsvGalleryConvocation.adapter = GalleryAdapter(imageListConvocation, this)
        binding.rsvGalleryConvocation.setHasFixedSize(true)

        binding.rsvGalleryRepublicDay.layoutManager =
            GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        binding.rsvGalleryRepublicDay.adapter = GalleryAdapter(imageListIndependenceDay, this)
        binding.rsvGalleryRepublicDay.setHasFixedSize(true)

        binding.rsvGalleryOther.layoutManager =
            GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        binding.rsvGalleryOther.adapter = GalleryAdapter(imageListOther, this)
        binding.rsvGalleryOther.setHasFixedSize(true)
    }

}