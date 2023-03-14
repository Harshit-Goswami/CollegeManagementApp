package com.harshit.goswami.collegeapp.student

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.appbar.MaterialToolbar
import com.harshit.goswami.collegeapp.AttendanceActivity
import com.harshit.goswami.collegeapp.LoginActivity
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.ViewAttendanceActivity
import com.harshit.goswami.collegeapp.databinding.DialogOurCoursesDetailsBinding
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
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowTitleEnabled(false)
        /*  toolbar.setNavigationIcon(R.drawable.ic_home);
          toolbar.setTitle("");
          toolbar.setSubtitle("");
          toolbar.setLogo(R.drawable.ic_toolbar);*/
        toggle = ActionBarDrawerToggle(
            container?.context as Activity?, drawerLayout, toolbar,
            R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
//########################################################################################################
        binding.scrollView3.viewTreeObserver.addOnScrollChangedListener {
            val y = binding.scrollView3.scrollY
            if (y > 500) {
                MainActivity.mainBinding.cordinatorNavBar.visibility = View.GONE
            } else MainActivity.mainBinding.cordinatorNavBar.visibility = View.VISIBLE
        }


        imageSlider()
        navigationSetUp()
        //......................................
        admissionGuidelinesSetUp()
        admissionGuidelinesLinksSetUp()
        ourCoursesSetUp()
        socialMediaIconClicks()
        magazineClickSetUp()


        return binding.root
    }
private fun magazineClickSetUp(){
    binding.magazineCard1.setOnClickListener {
        getByUrl("https://drive.google.com/file/d/1uQYjyG6rdXkDgrhloCpbKXDj_I25U7RI/view?usp=sharing")
    }
    binding.magazineCard2.setOnClickListener {
        getByUrl("https://drive.google.com/file/d/17d4wWzlWa3Sl36_EM8cnfwbsa4_f_N07/view?usp=sharing")
    }
    binding.magazineCard3.setOnClickListener {
        getByUrl("https://drive.google.com/file/d/1Gb-fHbwNJpJectmOA3E6FaUXqIdWI8bf/view?usp=sharing")
    }
    binding.magazineCard4.setOnClickListener {
        getByUrl("https://drive.google.com/file/d/1krANEJjWq33XPvEn4OgCa8ei2cVHcRMp/view?usp=sharing")
    }
    binding.magazineCard5.setOnClickListener {
        getByUrl("https://drive.google.com/file/d/1854ejko5AnoS7OL22-fgh8OBTGeXqXhr/view?usp=sharing")
    }
}
    private fun navigationSetUp() {
        val hView: View = binding.navView.getHeaderView(0)
        hView.findViewById<TextView>(R.id.nav_stud_name).text = MainActivity.studName
//      hView.findViewById<ImageView>(R.id.imageView)
        if (MainActivity.isCR){
            binding.navView.menu.getItem(0).isVisible = true
            binding.navView.menu.getItem(1).isVisible = true
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_take_attendance -> {
//                    startActivity(Intent(requireContext(), AttendanceActivity::class.java))
                }
                R.id.menu_view_attendance -> {
                    startActivity(Intent(requireContext(), ViewAttendanceActivity::class.java))
                }
                R.id.menu_campus_life -> {
                    startActivity(Intent(requireContext(), GalleryActivity::class.java))

                }
                R.id.menu_imp_links -> {

                }
                R.id.menu_theme -> {

                }
                R.id.menu_faculty -> {

                }
                R.id.menu_logout -> {
                    val pref: SharedPreferences = requireContext().getSharedPreferences(
                        "studentPref",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor = pref.edit()
                    editor.putBoolean("studentLogin", false).apply()
                    editor.putString("studentRollNo", "").apply()
                    editor.putString("studentDep", "").apply()
                    editor.putString("studentYear", "").apply()
                    editor.putString("studentName", "").apply()
                    val i = Intent(requireContext(), LoginActivity::class.java)
                    i.putExtra("SelectedUser", "student")
                    startActivity(i)
                }
                R.id.menu_developer -> {
                }
                R.id.menu_map -> {

                }
                R.id.menu_term_n_condition -> {

                }
            }
            true
        }
    }

    private fun socialMediaIconClicks() {
        binding.FHInstaIcon.setOnClickListener { getByUrl("https://www.instagram.com/chetanas_sfc/?hl=en") }
        binding.FHFacebookIcon.setOnClickListener { getByUrl("https://www.facebook.com/profile.php?id=100064103347725") }
        binding.FHLinkedinIcon.setOnClickListener {getByUrl("https://www.linkedin.com/company/chetana-college/")}
        binding.FHYoutubeIcon.setOnClickListener {
            getByUrl("https://www.youtube.com/channel/UCr2658Nq363khQvTSIxntwQ")
        }
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

    }

    private fun admissionGuidelinesLinksSetUp() {
        binding.admissionNoticeLink.setOnClickListener { getByUrl("https://drive.google.com/file/d/1SgWhy7fWKmmZ7FRlm7ZEZo6viiXawNqV/view?usp=sharing") }
        binding.admissionGuidelinesLink.setOnClickListener { getByUrl("https://drive.google.com/file/d/1W-R1Ynpc4MdXlVB73wwJRP_dwti64a93/view?usp=sharing") }
        binding.addmissionFormLink.setOnClickListener { getByUrl("https://admission.onfees.com/admissionLogin?instituteId=447&formPolicyId=253") }
        binding.meritListLink.setOnClickListener { getByUrl("https://drive.google.com/drive/folders/1OW4u5zqMhI680rDBbj9lhEHDumKDpWlq?usp=sharing") }
    }

    private fun ourCoursesSetUp() {
        binding.FHCardBscit.setOnClickListener {
            val courseBinding =
                DialogOurCoursesDetailsBinding.inflate(layoutInflater)
            val dialog = Dialog(requireContext(), R.style.BottomSheetStyle)
            dialog.setContentView(courseBinding.root)
            dialog.show()
//            courseBinding.ConstraintLayout.setOnClickListener { dialog.dismiss() }

        }
        binding.FHCardBaf.setOnClickListener {
            val courseBinding =
                DialogOurCoursesDetailsBinding.inflate(layoutInflater)
            val dialog = Dialog(requireContext(), R.style.BottomSheetStyle)
            dialog.setContentView(courseBinding.root)
//            dialog
//                .setCanceledOnTouchOutside(true)
//            dialog.window?.setGravity(Gravity.CENTER)
//            dialog.setCancelable(true)
//            builder.setCanceledOnTouchOutside(true)

            courseBinding.txtDepartment.text = "BAF"
            courseBinding.txtCourseDescription.text =
                "The Bachelor of Accounting and Finance (BAF) course provides comprehensive training to students in the field of Accounting & Finance by way of interaction, projects, presentations, industrial visits, practical training, job orientation and placements."
            courseBinding.txtEligibilityCriteria.text =
                "▶Passed XII Std. Examination of the Maharashtra Board of Higher Secondary Education\n" +
                        "▶Not have secured less than 45% marks."
            courseBinding.txtDurationAndInfo.text =
                "▶Six semester spread over three years.\n" +
                        "▶40 modules comprising 39 theory papers and 01 projects.\n" +
                        "▶Lectures per course (subject) shall be a minimum of 50 each of 50 minutes duration."
            dialog.show()
//            courseBinding.root.setOnClickListener { dialog.dismiss() }

        }
        binding.FHCardBms.setOnClickListener {
            val courseBinding =
                DialogOurCoursesDetailsBinding.inflate(layoutInflater)
            val dialog = Dialog(requireContext(), R.style.BottomSheetStyle)
            dialog.setContentView(courseBinding.root)
            courseBinding.txtDepartment.text = "BMS"
            courseBinding.txtCourseDescription.text =
                "The Bachelor of Management Studies (BMS) course has designed to create professionally qualified management executives, with effect and interpersonal communication in the field of management."
            courseBinding.txtEligibilityCriteria.text =
                "▶Passed XII Std. Examination of the Maharashtra Board of Higher Secondary Education\n" +
                        "▶Not have secured less than 45% marks."
            courseBinding.txtDurationAndInfo.text =
                "▶Six semester spread over three years.\n" +
                        "▶40 modules comprising 39 theory papers and 01 projects.\n" +
                        "▶Lectures per course (subject) shall be a minimum of 50 each of 50 minutes duration."
            dialog.show()
//            courseBinding.root.setOnClickListener { dialog.dismiss() }

        }
        binding.FHCardBammc.setOnClickListener {
            val courseBinding =
                DialogOurCoursesDetailsBinding.inflate(layoutInflater)
            val dialog = Dialog(requireContext(), R.style.BottomSheetStyle)
            dialog.setContentView(courseBinding.root)
            courseBinding.txtDepartment.text = "BAMMC"
            courseBinding.txtCourseDescription.text =
                "The Bachelors in Mass Media (BMM) programme has been designed with this very agenda – to produce professionals armed with specialized skills in either advertising or journalism."
            courseBinding.txtEligibilityCriteria.text =
                "▶Passed XII Std. Examination of the Maharashtra Board of Higher Secondary Education\n" +
                        "▶Not have secured less than 35% marks."
            courseBinding.txtDurationAndInfo.text =
                "▶Six semester spread over three years.\n" +
                        "▶Course contain 36 modules. \n" +
                        "▶Lectures per course (subject) shall be a minimum of 50 each of 50 minutes duration."
            dialog.show()
//            courseBinding.root.setOnClickListener { dialog.dismiss() }
        }
    }

    private fun getByUrl(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun imageSlider() {
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