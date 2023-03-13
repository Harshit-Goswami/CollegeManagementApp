package com.harshit.goswami.collegeapp.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.adapters.GalleryAdapter
import com.harshit.goswami.collegeapp.databinding.ActivityStudentGalleryBinding

class GalleryActivity : AppCompatActivity() {
private lateinit var binding:ActivityStudentGalleryBinding
private val imageListConvocation = ArrayList<String>()
private val imageListIndependenceDay = ArrayList<String>()
private val imageListOther = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchConvocationImageData()
        fetchIndependenceImageData()
        fetchOtherImageData()
        recyclerViewSetUp()
    }
    private fun fetchConvocationImageData() {
        FirebaseFirestore.getInstance().collection("Campus Gallery Images").document("Convocation")
            .collection("images").get().addOnSuccessListener {
                it.forEach { i->
                    imageListConvocation.add(i["url"].toString())
                }
                binding.rsvGalleryConvocation.adapter?.notifyDataSetChanged()
            }
    }
    private fun fetchIndependenceImageData() {
        FirebaseFirestore.getInstance().collection("Campus Gallery Images").document("Independence Day")
            .collection("images").get().addOnSuccessListener {
                it.forEach { i->
                    imageListIndependenceDay.add(i["url"].toString())
                }
                binding.rsvGalleryRepublicDay.adapter?.notifyDataSetChanged()
            }
    }
    private fun fetchOtherImageData() {
        FirebaseFirestore.getInstance().collection("Campus Gallery Images").document("Other events")
            .collection("images").get().addOnSuccessListener {
                it.forEach { i->
                    imageListOther.add(i["url"].toString())
                }
                binding.rsvGalleryOther.adapter?.notifyDataSetChanged()
            }
    }

    private fun recyclerViewSetUp() {
        binding.rsvGalleryConvocation.layoutManager = GridLayoutManager(this,4, LinearLayoutManager.VERTICAL,false)
        binding.rsvGalleryConvocation.adapter = GalleryAdapter(imageListConvocation,this)
        binding.rsvGalleryConvocation.setHasFixedSize(true)

        binding.rsvGalleryRepublicDay.layoutManager = GridLayoutManager(this,4, LinearLayoutManager.VERTICAL,false)
        binding.rsvGalleryRepublicDay.adapter = GalleryAdapter(imageListIndependenceDay,this)
        binding.rsvGalleryRepublicDay.setHasFixedSize(true)

        binding.rsvGalleryOther.layoutManager = GridLayoutManager(this,4, LinearLayoutManager.VERTICAL,false)
        binding.rsvGalleryOther.adapter = GalleryAdapter(imageListOther,this)
        binding.rsvGalleryOther.setHasFixedSize(true)
    }

}