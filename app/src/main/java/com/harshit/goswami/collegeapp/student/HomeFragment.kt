package com.harshit.goswami.collegeapp.student

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.appbar.MaterialToolbar
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        toolbar = binding.toolbar
        drawerLayout = binding.drawer
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//         activity!!.applicationContext as Activity
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
//        toolbar.setNavigationIcon(R.drawable.ic_home);
//        toolbar.setTitle("");
//        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);*/
        toggle = ActionBarDrawerToggle(
            container?.context as Activity?, drawerLayout, toolbar,
            R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.scrollView3.viewTreeObserver.addOnScrollChangedListener {
            val x = binding.scrollView3.scrollX
            val y = binding.scrollView3.scrollY
            if (y>500) {
                MainActivity.binding.cordinatorNavBar.visibility = View.GONE
            } else MainActivity.binding.cordinatorNavBar.visibility = View.VISIBLE
        }

//        binding.navView.setNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.home -> {
//
//                }
//            }
//            true
//        }
        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel(R.drawable.slide1))
        imageList.add(SlideModel(R.drawable.slide5))
        imageList.add(SlideModel(R.drawable.slide6))
        imageList.add(SlideModel(R.drawable.slide_gps_map_camera))
        imageList.add(SlideModel(R.drawable.slide_best_college_certificate))
        imageList.add(SlideModel(R.drawable.slide_college_iso_certificate))

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)


        return binding.root
    }

}