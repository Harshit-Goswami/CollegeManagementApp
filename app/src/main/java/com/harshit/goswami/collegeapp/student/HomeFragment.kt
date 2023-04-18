package com.harshit.goswami.collegeapp.student

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.LoginActivity
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.data.AdminLoginData
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.*
import java.io.*


class HomeFragment : Fragment() {
    private lateinit var bindingAttendance:ActivityStudentViewAttendanceBinding
    private lateinit var changePasswordBinding:DialogAdminChangePasswordBinding
    private lateinit var binding: FragmentHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private val fireDb = FirebaseDatabase.getInstance().reference
    var arrayListapkFilepath: ArrayList<Uri>? = null

    var studOldPassword = ""
    private var studNewPassword = ""
    var studReNewPassword = ""
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
        changePasswordBinding = DialogAdminChangePasswordBinding.inflate(layoutInflater)
        val bindingAttendance = ActivityStudentViewAttendanceBinding.inflate(layoutInflater)
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
        binding.imgCollegeLocation.setOnClickListener { getByUrl("https://www.google.com/maps/place/Chetana's+Institute+of+Management+%26+Research/@19.0628721,72.8336613,15z/data=!4m6!3m5!1s0x3be7c9000496fab9:0x225542a39da6c430!8m2!3d19.060986!4d72.8480809!16s%2Fg%2F1tg4jvtk?authuser=0") }
        studentCheck()
        imageSlider()
        navigationSetUp()
        //......................................
        admissionGuidelinesSetUp()
        admissionGuidelinesLinksSetUp()
        ourCoursesSetUp()
        socialMediaIconClicks()
        magazineClickSetUp()

        // for sharing Harshit's Apk
        //
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

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


        if (MainActivity.user == "student" || MainActivity.isCR){
            binding.navView.menu.getItem(0).isVisible = true
            hView.findViewById<TextView>(R.id.nav_stud_name).text = MainActivity.studName
        }
        if (MainActivity.user == "other") {
            binding.navView.menu.getItem(0).isVisible = false
            binding.navView.menu.getItem(5).isVisible = false
            binding.navView.menu.getItem(6).isVisible = false
            hView.findViewById<ImageView>(R.id.nav_had_imageView).visibility = View.GONE
            hView.findViewById<TextView>(R.id.nav_stud_name).text = MainActivity.studName
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_view_attendance -> {
                    viewAttendanceSetupByHarshit()
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
                R.id.menu_change_password -> {
                    studentChangePasswordSetUp()
                }
                R.id.menu_logout -> {
                    studentLogOutSetUp()
                }

                R.id.menu_developer -> {
//        try {
                       val developerProfileDialog = DialogDeveloperProfileBinding.inflate(layoutInflater)
                        val bottomDialog = Dialog(requireContext(), R.style.BottomSheetStyle)
                        bottomDialog.setContentView(developerProfileDialog.root)
                        bottomDialog
                            .setCanceledOnTouchOutside(false)
                        bottomDialog.show()
//        } catch (e: Exception) {
//            Log.d("dialogError","${e.message}")
//        }
                }
                R.id.menu_map -> {

                }
                R.id.menu_share_apk->{
                    arrayListapkFilepath = ArrayList<Uri>()

                    shareAPK(requireContext().packageName)
                    // you can pass bundle id of installed app in your device instead of getPackageName()
                    // you can pass bundle id of installed app in your device instead of getPackageName()
                    val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
                    intent.type = "application/vnd.android.package-archive"
                    intent.putParcelableArrayListExtra(
                        Intent.EXTRA_STREAM,
                        arrayListapkFilepath
                    )
                    startActivity(
                        Intent.createChooser(
                            intent, "Share " +
                                    arrayListapkFilepath!!.size.toString() + " Files Via"
                        )
                    )

                }
                R.id.menu_term_n_condition -> {

                }
            }
            true
        }
    }

    private fun studentLogOutSetUp() {
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
        if(MainActivity.isCR){
            editor.putBoolean("isCR", false).apply()
        }
        val i = Intent(requireContext(), LoginActivity::class.java)
        i.putExtra("SelectedUser", "student")
        startActivity(i)

    }

    private fun studentChangePasswordSetUp() {
        changePasswordBinding = DialogAdminChangePasswordBinding.inflate(layoutInflater)
        try {
                val changePasswordDialog = AlertDialog.Builder(
                    requireContext(),
                    R.style.CustomAlertDialogEditProfile
                ).create()
                changePasswordDialog.window?.setGravity(Gravity.TOP)
                changePasswordDialog.setCancelable(true)
                changePasswordDialog.setView(changePasswordBinding.root)
                changePasswordDialog.setCanceledOnTouchOutside(false)
                changePasswordDialog.show()
            } catch (e: Exception) {
                Log.d("dialog Error-", "${e.message}")
            }
            changePasswordBinding.DEPBtnSubmit.setOnClickListener {
                changePasswordValidation()
                uploadPasswordData()
            }
    }
    private fun changePasswordValidation() {
        if (changePasswordBinding.DEPEdtOldPassword.text.toString().isNotEmpty()) studOldPassword =
            changePasswordBinding.DEPEdtOldPassword.text.toString()
        else changePasswordBinding.DEPEdtOldPassword.error = "Please Enter your Old Password"

        if (changePasswordBinding.DEPEdtNewPassword.text.toString().isNotEmpty()) studNewPassword =
            changePasswordBinding.DEPEdtNewPassword.text.toString()
        else changePasswordBinding.DEPEdtNewPassword.error = "Please Enter your New Password"

        if (changePasswordBinding.DEPEdtReNewPassword.text.toString().isEmpty())
            changePasswordBinding.DEPEdtReNewPassword.error = "Please Re-Enter your New Password"

        if (changePasswordBinding.DEPEdtReNewPassword.text.toString() == studNewPassword) studReNewPassword =
            changePasswordBinding.DEPEdtReNewPassword.text.toString()
        else changePasswordBinding.DEPEdtReNewPassword.error = "Password Does not match!!"
    }
    private fun uploadPasswordData() {
        if (studOldPassword != "" && studReNewPassword != "") {
            fireDb.child("Students").child(MainActivity.studentDep).child(MainActivity.studentYear)
                .child(MainActivity.studRollNo).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        if (studOldPassword == snapshot.getValue(AdminLoginData::class.java)?.password) {
                            fireDb.child("Students").child(MainActivity.studentDep).child(MainActivity.studentYear)
                                .child(MainActivity.studRollNo)
                                .setValue(
                                    StudentData(
                                        snapshot.getValue(StudentData::class.java)?.rollNo.toString(),
                                        snapshot.getValue(StudentData::class.java)?.fullName.toString(),
                                        snapshot.getValue(StudentData::class.java)?.department.toString(),
                                        snapshot.getValue(StudentData::class.java)?.year.toString(),
                                        snapshot.getValue(StudentData::class.java)?.contactNo.toString(),
                                        studReNewPassword,
                                    )
                                ).addOnSuccessListener {
                                    Toast
                                        .makeText(
                                            requireContext(),
                                            "Password Updated Successfully!",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                        } else changePasswordBinding.DEPEdtOldPassword.error =
                            "Incorrect Old Password!!"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast
                        .makeText(
                            requireContext(),
                            "Something went wong!!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            })
        }
    }

    private fun viewAttendanceSetupByHarshit() {
        bindingAttendance = ActivityStudentViewAttendanceBinding.inflate(layoutInflater)
        try {
            val dialog = AlertDialog.Builder(
                requireContext(),
                R.style.CustomAlertDialogEditProfile
            ).create()
            dialog.window?.setGravity(Gravity.TOP)
            dialog.setCancelable(true)
            dialog.setView(bindingAttendance.root)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        catch (e: Exception) {
            Log.d("dialog Error-", "${e.message}")
        }
        bindingAttendance.VAIcSort.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), bindingAttendance.VAIcSort)
            popupMenu.menuInflater.inflate(R.menu.view_attendance_sort_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.menu_searchby_month -> {
                        bindingAttendance.TILSelectSubject.visibility = View.GONE
                        bindingAttendance.TILMonth.visibility = View.VISIBLE
                        bindingAttendance.btnSearch.visibility = View.VISIBLE
                    }
                    R.id.menu_searchby_subject -> {
                        bindingAttendance.TILMonth.visibility = View.GONE
                        bindingAttendance.TILSelectSubject.visibility = View.VISIBLE
                        bindingAttendance.btnSearch.visibility = View.VISIBLE
                    }
                    R.id.menu_searchby_month_n_sub -> {
                        bindingAttendance.TILMonth.visibility = View.VISIBLE
                        bindingAttendance.TILSelectSubject.visibility = View.VISIBLE
                        bindingAttendance.btnSearch.visibility = View.VISIBLE

                    }
                    R.id.menu_default -> {
                        bindingAttendance.TILMonth.visibility = View.GONE
                        bindingAttendance.TILSelectSubject.visibility = View.GONE
                        bindingAttendance.btnSearch.visibility = View.GONE

                    }
                }
                true
            }
            popupMenu.show()
        }
        bindingAttendance.btnSearch.setOnClickListener {
            if (bindingAttendance.TILMonth.isVisible && !bindingAttendance.TILSelectSubject.isVisible) {
                if (bindingAttendance.ACTVMonth.text.isNotEmpty()) {
                    fetchAttendanceDataBYMonth()
                } else bindingAttendance.TILMonth.error = "Please Select Month"

            }
            if (!bindingAttendance.TILMonth.isVisible && bindingAttendance.TILSelectSubject.isVisible) {

                if (bindingAttendance.ACTVSelectSubject.text.isNotEmpty()) {
                    fetchAttendanceDataBYSub()
                } else bindingAttendance.TILSelectSubject.error = "Please Select Subject"
            }
            if (bindingAttendance.TILMonth.isVisible && bindingAttendance.TILSelectSubject.isVisible) {
                if (bindingAttendance.ACTVMonth.text.isNotEmpty()) {
                    if (bindingAttendance.ACTVSelectSubject.text.isNotEmpty()) {
                        fetchAttendanceDataBYMonthNSub()
                    } else bindingAttendance.TILSelectSubject.error = "Please Select Subject"
                } else bindingAttendance.TILMonth.error = "Please Select Month"
            }
//                        if (!bindingAttendance.TILMonth.isVisible && !bindingAttendance.TILSelectSubject.isVisible) {
//                                fetchAttendanceDataDefault()
//                        }
        }
        autoCompleteTV()
        fetchAttendanceDataDefault()
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

    private fun fetchAttendanceDataDefault() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child(MainActivity.studRollNo).child(MainActivity.studName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var p = 0F
                        var a = 0F
                        snapshot.children.forEach { month ->
                            month.children.forEach { dates ->
                                dates.children.forEach { subs ->
                                    if (subs.child("status").value.toString() == "P") p += 1 else a += 1
                                }
                            }
                        }
                        try {
                            val per = ((p/(a+p)) * 100).toInt()
                            bindingAttendance.txtAttendanceHeading.text = "Total Attendance"
                            bindingAttendance.txtPercentage.text = "${per}%"
                            bindingAttendance.txtPresentCount.text = "Present - ${p.toInt()}"
                            bindingAttendance.txtAbsentCount.text = "Absent - ${a.toInt()}"
                        } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }
    private fun fetchAttendanceDataBYMonth() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child(MainActivity.studRollNo).child(MainActivity.studName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var p = 0F
                        var a = 0F
//                        val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
//                        val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                        snapshot.child(bindingAttendance.ACTVMonth.text.toString()).children.forEach { dates ->
                            dates.children.forEach { subs ->
                                if (subs.child("status").value.toString() == "P") p += 1 else a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                            }
                        }
                        try {
                            val per = ((p/(a+p)) * 100).toInt()
                            bindingAttendance.txtAttendanceHeading.text = "${bindingAttendance.ACTVMonth.text} Attendance"
                            bindingAttendance.txtPercentage.text = "$per%"
                            bindingAttendance.txtPresentCount.text = "Present - ${p.toInt()}"
                            bindingAttendance.txtAbsentCount.text = "Absent - ${a.toInt()}"
                        } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }
    private fun fetchAttendanceDataBYSub() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child(MainActivity.studRollNo).child(MainActivity.studName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var p = 0F
                        var a = 0F
                        val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
                        val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                        snapshot.children.forEach { month ->
                            month.children.forEach { dates ->
                                dates.children.forEach { subs ->
                                    if (subs.key.toString() == bindingAttendance.ACTVSelectSubject.text.toString()) {
                                        if (subs.child("status").value.toString() == "P") p += 1 else a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                    }
                                }
                            }
                        }
                        try {
                            val per = ((p/(a+p)) * 100).toInt()
                            bindingAttendance.txtAttendanceHeading.text = "${bindingAttendance.ACTVSelectSubject.text} Attendance"
                            bindingAttendance.txtPercentage.text = "$per%"
                            bindingAttendance.txtPresentCount.text = "Present - ${p.toInt()}"
                            bindingAttendance.txtAbsentCount.text = "Absent - ${a.toInt()}"
                            Log.d("attendance","$p / ${a+p} = ${((p/(a+p))*100).toInt()}")
                        } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                        }
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }
    private fun fetchAttendanceDataBYMonthNSub() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child(MainActivity.studRollNo).child(MainActivity.studName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var p = 0F
                        var a = 0F
//                        val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
//                        val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                        snapshot.child(bindingAttendance.ACTVMonth.text.toString()).children.forEach { dates ->
                            dates.children.forEach { subs ->
                                if (subs.key.toString() == bindingAttendance.ACTVSelectSubject.text.toString()) {
                                    if (subs.child("status").value.toString() == "P") p += 1 else a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                }
                            }
                        }
                        try {
                            val per = ((p/(a+p)) * 100).toInt()
                            bindingAttendance.txtAttendanceHeading.text = "${bindingAttendance.ACTVMonth.text} ${bindingAttendance.ACTVSelectSubject.text} Attendance"
                            bindingAttendance.txtPercentage.text = "$per%"
                            bindingAttendance.txtPresentCount.text = "Present - ${p.toInt()}"
                            bindingAttendance.txtAbsentCount.text = "Absent - ${a.toInt()}"
                        } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }
    private fun autoCompleteTV(){
    val subjects = ArrayList<String>()
    FirebaseFirestore.getInstance()
        .collection("${MainActivity.studentYear}${MainActivity.studentDep}-Subjects")
        .addSnapshotListener { value, error ->
            subjects.clear()
            if (error != null) {
                Toast.makeText(
                    requireContext(),
                    "Error found is $error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            value?.forEach { ds ->
                subjects.add(ds["subjectName"].toString())
            }
        }
//    subjects.forEach {
//        Log.i("subjects", it)
//    }
    val adapterSubject = ArrayAdapter(
        requireContext(),
        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
        subjects
    )
        bindingAttendance.ACTVSelectSubject.setAdapter(adapterSubject)

    val itemsMonth = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    val adapterMonth = ArrayAdapter(
        requireContext(),
        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
        itemsMonth
    )
    bindingAttendance.ACTVMonth.setAdapter(adapterMonth)
}
    private fun studentCheck(){
        if (MainActivity.user == "student"){
            fireDb.child("Students").child(MainActivity.studentDep).child(MainActivity.studentYear)
                .child(MainActivity.studRollNo).addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()){
                            studentLogOutSetUp()
                            Toast.makeText(requireContext(),"Student Not exist!!",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(),"${error.message}!!",Toast.LENGTH_SHORT).show()
                        studentLogOutSetUp()
                    }
                })
        }
    }
    fun shareAPK(bundle_id: String) {
        var f1: File
        var f2: File? = null
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList: List<*> = requireContext().packageManager.queryIntentActivities(mainIntent, 0)
        val z = 0
        for (`object` in pkgAppsList) {
            val info = `object` as ResolveInfo
            if (info.activityInfo.packageName == bundle_id) {
                f1 = File(info.activityInfo.applicationInfo.publicSourceDir)
                Log.v(
                    "file--",
                    " " + f1.getName().toString().toString() + "----" + info.loadLabel(
                        requireContext().packageManager
                    )
                )
                try {
                    val file_name = info.loadLabel(requireContext().packageManager).toString()
                    Log.d("file_name--", " $file_name")
                    f2 = File(Environment.getExternalStorageDirectory().toString() + "/Folder")
                    f2.mkdirs()
                    f2 = File(f2.getPath() + "/" + file_name + ".apk")
                    f2.createNewFile()
                    val `in`: InputStream = FileInputStream(f1)
                    val out: OutputStream = FileOutputStream(f2)

                    // byte[] buf = new byte[1024];
                    val buf = ByteArray(4096)
                    var len: Int
                    while (`in`.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    `in`.close()
                    out.close()
                    println("File copied.")
                } catch (ex: FileNotFoundException) {
                    println(ex.message + " in the specified directory.")
                } catch (e: IOException) {
                    System.out.println(e.message)
                }
            }
        }
        if (f2 != null) {
            arrayListapkFilepath!!.add(Uri.fromFile(File(f2.getAbsolutePath())))
        }
    }
}