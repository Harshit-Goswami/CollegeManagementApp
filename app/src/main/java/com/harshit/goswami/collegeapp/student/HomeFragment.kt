package com.harshit.goswami.collegeapp.student

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    // ********************** admission guidelines variables ***************
    private var isAdmissionNotice = false
    private var isAdmissionGuidelines = false
    private var isMeritList = false
    private var isAdmissionForm = false
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowTitleEnabled(false)
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
//########################################################################################################
        imageSlider()
        admissionGuidelinesSetUp()
        binding.scrollView3.viewTreeObserver.addOnScrollChangedListener {
            val y = binding.scrollView3.scrollY
            if (y > 500) {
                MainActivity.mainBinding.cordinatorNavBar.visibility = View.GONE
            } else MainActivity.mainBinding.cordinatorNavBar.visibility = View.VISIBLE
        }
        binding.FHYoutubeIcon.setOnClickListener {
            getByUrl("https://www.youtube.com/channel/UCr2658Nq363khQvTSIxntwQ")
        }

// ################################### ON CLICK LISTENERS  ##################################*/
        binding.FHInstaIcon.setOnClickListener { getByUrl("https://www.instagram.com/chetanas_sfc/?hl=en") }
        binding.FHFacebookIcon.setOnClickListener { getByUrl("https://www.facebook.com/profile.php?id=100064103347725") }
        binding.FHEmailIcon.setOnClickListener {}



//        binding.navView.setNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.home -> {
//
//                }
//            }
//            true
//        }

        return binding.root
    }

    private fun admissionGuidelinesSetUp() {
        binding.txtAdmissionNotice.setOnClickListener {
            if (!isAdmissionNotice) {
                isAdmissionNotice = true
                binding.admissionNoticeLink.visibility = View.VISIBLE
                binding.txtAdmissionNotice.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up,
                    0
                )
            } else {
                isAdmissionNotice = false
                binding.admissionNoticeLink.visibility = View.GONE
                binding.txtAdmissionNotice.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_double_arrow_down,
                    0
                )
            }
        }
        binding.txtAdmissionGuidelines.setOnClickListener {
            if (!isAdmissionGuidelines) {
                isAdmissionGuidelines = true
                binding.admissionGuidelinesLink.visibility = View.VISIBLE
                binding.txtAdmissionGuidelines.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up,
                    0
                )
            } else {
                isAdmissionGuidelines = false
                binding.admissionGuidelinesLink.visibility = View.GONE
                binding.txtAdmissionGuidelines.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_double_arrow_down,
                    0
                )
            }
        }
        binding.txtMeritList.setOnClickListener {
            if (!isMeritList) {
                isMeritList = true
                binding.meritListLink.visibility = View.VISIBLE
                binding.txtMeritList.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up,
                    0
                )
            } else {
                isMeritList = false
                binding.meritListLink.visibility = View.GONE
                binding.txtMeritList.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_double_arrow_down,
                    0
                )
            }
        }
        binding.txtAddmissionForm.setOnClickListener {
            if (!isAdmissionForm) {
                isAdmissionForm = true
                binding.addmissionFormLink.visibility = View.VISIBLE
                binding.txtAddmissionForm.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up,
                    0
                )
            } else {
                isAdmissionForm = false
                binding.addmissionFormLink.visibility = View.GONE
                binding.txtAddmissionForm.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_double_arrow_down,
                    0
                )
            }
        }

        binding.admissionNoticeLink.setOnClickListener { getByUrl("https://drive.google.com/file/d/1SgWhy7fWKmmZ7FRlm7ZEZo6viiXawNqV/view?usp=sharing") }
        binding.admissionGuidelinesLink.setOnClickListener { getByUrl("https://drive.google.com/file/d/1W-R1Ynpc4MdXlVB73wwJRP_dwti64a93/view?usp=sharing") }
        binding.addmissionFormLink.setOnClickListener { getByUrl("https://admission.onfees.com/admissionLogin?instituteId=447&formPolicyId=253") }
        binding.meritListLink.setOnClickListener { getByUrl("https://drive.google.com/drive/folders/1OW4u5zqMhI680rDBbj9lhEHDumKDpWlq?usp=sharing") }

    }

    private fun getByUrl(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
private fun imageSlider(){
    val imageList = ArrayList<SlideModel>() // Create image list

    imageList.add(SlideModel(R.drawable.slide1))
    imageList.add(SlideModel(R.drawable.slide5))
    imageList.add(SlideModel(R.drawable.slide6))
    imageList.add(SlideModel(R.drawable.slide_gps_map_camera))
    imageList.add(SlideModel(R.drawable.slide_best_college_certificate))
    imageList.add(SlideModel(R.drawable.slide_college_iso_certificate))

    binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
}
}