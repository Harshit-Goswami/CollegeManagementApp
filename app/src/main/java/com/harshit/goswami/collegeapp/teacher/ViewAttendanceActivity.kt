package com.harshit.goswami.collegeapp.teacher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.adapters.AttendanceAdapter
import com.harshit.goswami.collegeapp.data.*
import com.harshit.goswami.collegeapp.databinding.ActivityViewAttendanceBinding
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFRichTextString
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ViewAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewAttendanceBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    val studAttendList = ArrayList<StudentData>()
    val allAttendanceList = ArrayList<AllAttendanceData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
//                    writeExcel()
                }
                /*else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }*/
            }
        autoCompleteTextViewSetUp()

        binding.rsvAllViewAttendance.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvAllViewAttendance.adapter = AttendanceAdapter(studAttendList, this, "view")
        binding.rsvAllViewAttendance.setHasFixedSize(true)
        binding.rsvAllViewAttendance.adapter?.notifyDataSetChanged()


        binding.btnSearch.setOnClickListener {
            if (binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    if (binding.ACTVMonth.text.isNotEmpty()) {
                        fetchAttendanceDataBYMonth()
                    } else binding.TILMonth.error = "Please Select Month"
                } else binding.TILSelectYear.error = "Please Select Year"
            }
            if (!binding.TILMonth.isVisible && binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    if (binding.ACTVSelectSubject.text.isNotEmpty()) {
                        fetchAttendanceDataBYSub()
                    } else binding.TILSelectSubject.error = "Please Select Subject"
                } else binding.TILSelectYear.error = "Please Select Year"
            }
            if (binding.TILMonth.isVisible && binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    if (binding.ACTVMonth.text.isNotEmpty()) {
                        if (binding.ACTVSelectSubject.text.isNotEmpty()) {
                            fetchAttendanceDataBYMonthNSub()
                        } else binding.TILSelectSubject.error = "Please Select Subject"
                    } else binding.TILMonth.error = "Please Select Month"
                } else binding.TILSelectYear.error = "Please Select Year"
            }
            if (!binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    fetchAttendanceDataDefault()
                } else binding.TILSelectYear.error = "Please Select Year"
            }
        }
        binding.exportAttendanseExcel.setOnClickListener {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> {
//                    Log.d("rollname", "${studAttendList[0].rollNo}")
                    if (binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                        if (binding.ACTVSelectYear.text.isNotEmpty()) {
                            if (binding.ACTVMonth.text.isNotEmpty()) {
                                writeExcel()
                            } else binding.TILMonth.error = "Please Select Month"
                        } else binding.TILSelectYear.error = "Please Select Year"
                    }
                    if (!binding.TILMonth.isVisible && binding.TILSelectSubject.isVisible) {
                        if (binding.ACTVSelectYear.text.isNotEmpty()) {
                            if (binding.ACTVSelectSubject.text.isNotEmpty()) {
                                writeExcel()
                            } else binding.TILSelectSubject.error = "Please Select Subject"
                        } else binding.TILSelectYear.error = "Please Select Year"
                    }
                    if (binding.TILMonth.isVisible && binding.TILSelectSubject.isVisible) {
                        if (binding.ACTVSelectYear.text.isNotEmpty()) {
                            if (binding.ACTVMonth.text.isNotEmpty()) {
                                if (binding.ACTVSelectSubject.text.isNotEmpty()) {
                                    writeExcel()
                                } else binding.TILSelectSubject.error = "Please Select Subject"
                            } else binding.TILMonth.error = "Please Select Month"
                        } else binding.TILSelectYear.error = "Please Select Year"
                    }
                    if (!binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                        if (binding.ACTVSelectYear.text.isNotEmpty()) {
                            writeExcel()
                        } else binding.TILSelectYear.error = "Please Select Year"
                    }

                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            }
        }
        binding.VAIcSort.setOnClickListener {
            val popupMenu = PopupMenu(this@ViewAttendanceActivity, binding.VAIcSort)
            popupMenu.menuInflater.inflate(R.menu.view_attendance_sort_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.menu_searchby_month -> {
                        binding.TILSelectSubject.visibility = View.GONE
                        binding.TILMonth.visibility = View.VISIBLE
                    }
                    R.id.menu_searchby_subject -> {
                        binding.TILMonth.visibility = View.GONE
                        binding.TILSelectSubject.visibility = View.VISIBLE
                    }
                    R.id.menu_searchby_month_n_sub -> {
                        binding.TILMonth.visibility = View.VISIBLE
                        binding.TILSelectSubject.visibility = View.VISIBLE
                    }
                    R.id.menu_default -> {
                        binding.TILMonth.visibility = View.GONE
                        binding.TILSelectSubject.visibility = View.GONE
                    }
                }
                true
            }
            popupMenu.show()
        }
       /* fireDb.child("Student Attendance").child(TeacherDashboard.loggedTeacherDep)
            .child(binding.ACTVSelectYear.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        snapshot.children.forEach { rollno ->
                            var p = 0F
                            var a = 0F
                            var studName:String = null.toString()
                            val rollNo = rollno.key.toString()
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
                            fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                .child(binding.ACTVSelectYear.text.toString()).child(rollNo).child("fullName").addListenerForSingleValueEvent(object:ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        studName = snapshot.value.toString()
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
//                            val studName = rollno.key.toString().removeRange(0, 4).trim()
                            rollno.children.forEach { month ->
                                month.children.forEach { dates ->
                                    dates.children.forEach { subs ->
                                        val subData =  SubjectData(subs.key.toString(),subs.child("status").value.toString())
                                        val dateData = DateData(dates.key.toString(),subData)
                                        val monthData= MonthData(month.key.toString(),dateData)
                                        val allAttendanceData = AllAttendanceData(rollNo,studName,monthData)
                                        allAttendanceList.add(allAttendanceData)
                                    }
                                }
                            }
                        }
//                        Log.d("attendance",allAttendanceList.size.toString())
//                        allAttendanceList.forEach {
//                            Log.d("attendance","${it.rollNo} ${it.name} ${it.month.month} ${it.month.dateData.date} " +
//                                    "${it.month.dateData.subData.subject} ${it.month.dateData.subData.status}")
//                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })*/
    }

    private fun writeExcel() {
        fireDb.child("Student Attendance").child(TeacherDashboard.loggedTeacherDep)
            .child(binding.ACTVSelectYear.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var rowIndex = 2
                        val workbook = XSSFWorkbook()

                        val firstSheet = workbook.createSheet("Sheet 1")
                        val row0 = firstSheet.createRow(0)
                        val row1 = firstSheet.createRow(1)
                        row0.createCell(0).apply {
                            setCellValue("Roll No")
                            cellStyle = workbook.createCellStyle().apply {
                                setAlignment(HorizontalAlignment.CENTER)
                                setVerticalAlignment(VerticalAlignment.CENTER)
                                setFont(workbook.createFont().apply {
                                    bold = true
                                    underline
                                })
                            }
                        }
                        row0.createCell(1).apply {
                            setCellValue(XSSFRichTextString("FullName"))
                            cellStyle = workbook.createCellStyle().apply {
                                setAlignment(HorizontalAlignment.CENTER)
                                setVerticalAlignment(VerticalAlignment.CENTER)
                                setFont(workbook.createFont().apply {
                                    bold = true
                                    underline
                                })
                            }
                        }
                        row0.createCell(2).apply {
                            setCellValue(XSSFRichTextString("Attended"))
                            cellStyle = workbook.createCellStyle().apply {
                                setAlignment(HorizontalAlignment.CENTER)
                                setVerticalAlignment(VerticalAlignment.CENTER)
                                setFont(workbook.createFont().apply {
                                    bold = true
                                    underline

                                })
                            }
                        }
                        row0.createCell(3).apply {
                            setCellValue(XSSFRichTextString("Percentage"))
                            cellStyle = workbook.createCellStyle().apply {
                                setAlignment(HorizontalAlignment.CENTER)
                                setVerticalAlignment(VerticalAlignment.CENTER)
                                setFont(workbook.createFont().apply {
                                    bold = true
                                    underline

                                })
                            }
                        }
                        firstSheet.addMergedRegion(CellRangeAddress(0, 1, 0, 0))
                        firstSheet.addMergedRegion(CellRangeAddress(0, 1, 1, 1))
                        firstSheet.addMergedRegion(CellRangeAddress(0, 1, 2, 2))
                        firstSheet.addMergedRegion(CellRangeAddress(0, 1, 3, 3))

                        var cellIndexDate = 5
                        var cellIndexSub = 5
                        if (binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible && binding.ACTVMonth.text.isNotEmpty()) {
                            val month = binding.ACTVMonth.text.toString()

                            snapshot.child("${studAttendList[0].rollNo}")
                                .child(month).children.forEach { date ->
                                    var subjCount = 0
                                    row0.apply {
                                        createCell(cellIndexDate).apply {
                                            setCellValue(XSSFRichTextString(date.key.toString()))
                                            cellStyle = workbook.createCellStyle().apply {
                                                setAlignment(HorizontalAlignment.CENTER)
                                                setFont(workbook.createFont().apply {
                                                    bold = true
                                                })
                                            }
                                        }
                                    }
                                    date.children.forEach { sub ->
                                        subjCount += 1
                                        row1.apply {
                                            createCell(cellIndexSub).setCellValue(
                                                XSSFRichTextString(
                                                    sub.key.toString()
                                                )
                                            )
                                        }
                                        cellIndexSub += 1
                                    }
                                    firstSheet.addMergedRegion(
                                        CellRangeAddress(
                                            0,
                                            0,
                                            cellIndexDate,
                                            cellIndexDate + subjCount - 1
                                        )
                                    )
                                    cellIndexDate += subjCount
                                }

                            snapshot.children.forEach { rollNo ->
                                var p = 0F
                                var a = 0F
                                var cellIndexA = 5
                                val dataRow = firstSheet.createRow(rowIndex)
                                dataRow.createCell(0).setCellValue(
                                    XSSFRichTextString(
                                        rollNo.key.toString()
                                    )
                                )

                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo.key.toString()).child("fullName").get().addOnSuccessListener{
                                        dataRow.createCell(1).setCellValue(
                                            XSSFRichTextString(
                                                it.value.toString()
                                            )
                                        )
                                    }


                                rollNo.child(month).children.forEach { date ->
                                    date.children.forEach { sub ->
//                                     val cell = row
                                        if (sub.child("status").value == "P") p += 1 else a += 1
                                        dataRow.createCell(cellIndexA)
                                            .setCellValue(XSSFRichTextString(sub.child("status").value.toString()))
                                        cellIndexA += 1
                                    }
                                }
                                dataRow.createCell(2)
                                    .setCellValue(XSSFRichTextString("${p.toInt()} / ${a.toInt() + p.toInt()}"))
                                try {
                                    val per = ((p / (p + a)) * 100)
                                    dataRow.createCell(3)
                                        .setCellValue(XSSFRichTextString("${per}%"))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                rowIndex += 1
                            }
                            val fos: FileOutputStream? = null
                            try {
//                            val file_path =
//                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
//                                    .toString()
//
//                            val file = File(file_path, "attendance.xlsx")
//                            fos = FileOutputStream(file)
//                            workbook.write(fos)

                                val file = File(
                                    "${Environment.getExternalStorageDirectory()}/Documents/${month}_Attendance.xlsx"
                                )
                                if (file.exists()) file.delete()
                                workbook.write(FileOutputStream(file))

                                val uri = FileProvider.getUriForFile(
                                    this@ViewAttendanceActivity,
                                    "$packageName.provider",
                                    file
                                )
                                val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .setAction(Intent.ACTION_VIEW)
                                    .setDataAndType(uri, "application/vnd.ms-excel")
                                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    .also {
                                        if (it.resolveActivity(packageManager) == null) {
                                            it.setDataAndType(uri, "*/*")
                                        }
                                    }
                                startActivity(intent)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.flush()
                                        fos.close()
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                                Toast.makeText(
                                    this@ViewAttendanceActivity,
                                    "Excel Sheet Generated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        if (binding.TILSelectSubject.isVisible && !binding.TILMonth.isVisible && binding.ACTVSelectSubject.text.isNotEmpty()) {
                            snapshot.child("${studAttendList[0].rollNo}")
                                .children.forEach { month ->
                                    month.children.forEach { date ->
                                        var subjCount = 0

                                        date.children.forEach { sub ->
                                            if (sub.key.toString() == binding.ACTVSelectSubject.text.toString()) {
                                                subjCount += 1
                                                row1.apply {
                                                    createCell(cellIndexSub).apply {
                                                        setCellValue(XSSFRichTextString(sub.key.toString()))
                                                        cellStyle =
                                                            workbook.createCellStyle().apply {
                                                                setAlignment(HorizontalAlignment.CENTER)
                                                                setFont(
                                                                    workbook.createFont().apply {
                                                                        bold = true

                                                                    })
                                                            }
                                                    }
                                                }
                                                row0.apply {
                                                    createCell(cellIndexDate).setCellValue(
                                                        XSSFRichTextString(date.key.toString())
                                                    )
                                                }
                                                cellIndexSub += 1
                                            }
                                        }
                                        try {
                                            firstSheet.addMergedRegion(
                                                CellRangeAddress(
                                                    0,
                                                    0,
                                                    cellIndexDate,
                                                    cellIndexDate + subjCount - 1
                                                )
                                            )
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        cellIndexDate += subjCount
                                    }
                                }
                            snapshot.children.forEach { rollNo ->
                                var p = 0F
                                var a = 0F
                                var cellIndexA = 5
                                val dataRow = firstSheet.createRow(rowIndex)
                                dataRow.createCell(0).setCellValue(
                                    XSSFRichTextString(
                                        rollNo.key.toString()
                                    )
                                )
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo.key.toString()).child("fullName").get().addOnSuccessListener {
                                        dataRow.createCell(1).setCellValue(
                                            XSSFRichTextString(
                                                it.value.toString()
                                            )
                                        )
                                    }
                                rollNo.children.forEach { month ->
                                    month.children.forEach { date ->
                                        date.children.forEach { sub ->
                                            if (sub.key.toString() == binding.ACTVSelectSubject.text.toString()) {
//                                     val cell = row
                                                if (sub.child("status").value == "P") p += 1 else a += 1
                                                dataRow.createCell(cellIndexA)
                                                    .setCellValue(XSSFRichTextString(sub.child("status").value.toString()))
                                                cellIndexA += 1
                                            }
                                        }
                                    }
                                }
                                dataRow.createCell(2)
                                    .setCellValue(XSSFRichTextString("${p.toInt()} / ${a.toInt() + p.toInt()}"))
                                try {
                                    val per = ((p / (p + a)) * 100)
                                    dataRow.createCell(3)
                                        .setCellValue(XSSFRichTextString("${per}%"))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                rowIndex += 1
                            }
                            var fos: FileOutputStream? = null
                            try {

                                val file = File(
                                    "${Environment.getExternalStorageDirectory()}/Documents/${binding.ACTVSelectSubject.text}_Attendance.xlsx"
                                )
                                if (file.exists()) file.delete()
                                workbook.write(FileOutputStream(file))

                                val uri = FileProvider.getUriForFile(
                                    this@ViewAttendanceActivity,
                                    "$packageName.provider",
                                    file
                                )
                                val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .setAction(Intent.ACTION_VIEW)
                                    .setDataAndType(uri, "application/vnd.ms-excel")
                                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    .also {
                                        if (it.resolveActivity(packageManager) == null) {
                                            it.setDataAndType(uri, "*/*")
                                        }
                                    }
                                startActivity(intent)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.flush()
                                        fos.close()
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                                Toast.makeText(
                                    this@ViewAttendanceActivity,
                                    "Excel Sheet Generated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        if (binding.TILSelectSubject.isVisible
                            && binding.TILMonth.isVisible
                            && binding.ACTVMonth.text.isNotEmpty()
                            && binding.ACTVSelectSubject.text.isNotEmpty()
                        ) {
                            val month = binding.ACTVMonth.text.toString()
                            snapshot.child("${studAttendList[0].rollNo}")
                                .child(month).children.forEach { date ->
                                    var subjCount = 0

                                    date.children.forEach { sub ->
                                        if (sub.key.toString() == binding.ACTVSelectSubject.text.toString()) {
                                            subjCount += 1
                                            row1.apply {
                                                createCell(cellIndexSub).apply {
                                                    setCellValue(XSSFRichTextString(sub.key.toString()))
                                                    cellStyle = workbook.createCellStyle().apply {
                                                        setAlignment(HorizontalAlignment.CENTER)
                                                        setFont(workbook.createFont().apply {
                                                            bold = true
                                                        })
                                                    }
                                                }
                                            }
                                            row0.apply {
                                                createCell(cellIndexDate)
                                                    .apply {
                                                        setCellValue(XSSFRichTextString(date.key.toString()))
                                                    }
                                            }
                                            cellIndexSub += 1
                                        }
                                    }
                                    try {
                                        firstSheet.addMergedRegion(
                                            CellRangeAddress(
                                                0,
                                                0,
                                                cellIndexDate,
                                                cellIndexDate + subjCount - 1
                                            )
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    cellIndexDate += subjCount
                                }
                            snapshot.children.forEach { rollNo ->
                                var p = 0F
                                var a = 0F
                                var cellIndexA = 5
                                val dataRow = firstSheet.createRow(rowIndex)
                                dataRow.createCell(0).setCellValue(
                                    XSSFRichTextString(
                                        rollNo.key.toString()
                                    )
                                )
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo.key.toString()).child("fullName").get().addOnSuccessListener {
                                        dataRow.createCell(1).setCellValue(
                                            XSSFRichTextString(
                                                it.value.toString()
                                            )
                                        )
                                    }
                                rollNo.child(month).children.forEach { date ->
                                    date.children.forEach { sub ->
                                        if (sub.key.toString() == binding.ACTVSelectSubject.text.toString()) {
//                                     val cell = row
                                            if (sub.child("status").value == "P") p += 1 else a += 1
                                            dataRow.createCell(cellIndexA)
                                                .setCellValue(XSSFRichTextString(sub.child("status").value.toString()))
                                            cellIndexA += 1
                                        }
                                    }
                                }
                                dataRow.createCell(2)
                                    .setCellValue(XSSFRichTextString("${p.toInt()} / ${a.toInt() + p.toInt()}"))
                                try {
                                    val per = ((p / (p + a)) * 100)
                                    dataRow.createCell(3)
                                        .setCellValue(XSSFRichTextString("${per}%"))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                rowIndex += 1
                            }
                            val fos: FileOutputStream? = null
                            try {

                                val file = File(
                                    "${Environment.getExternalStorageDirectory()}/Documents/${month}_${binding.ACTVSelectSubject.text}_Attendance.xlsx"
                                )
                                if (file.exists()) file.delete()
                                workbook.write(FileOutputStream(file))

                                val uri = FileProvider.getUriForFile(
                                    this@ViewAttendanceActivity,
                                    "$packageName.provider",
                                    file
                                )
                                val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .setAction(Intent.ACTION_VIEW)
                                    .setDataAndType(uri, "application/vnd.ms-excel")
                                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    .also {
                                        if (it.resolveActivity(packageManager) == null) {
                                            it.setDataAndType(uri, "*/*")
                                        }
                                    }
                                startActivity(intent)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.flush()
                                        fos.close()
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                                Toast.makeText(
                                    this@ViewAttendanceActivity,
                                    "Excel Sheet Generated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        if (!binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                            Log.d("studroll","${studAttendList[0].rollNo}")
                            snapshot.child("${studAttendList[0].rollNo}")
                                .children.forEach { month ->
                                    month.children.forEach { date ->
                                        var subjCount = 0
                                        row0.apply {
                                            createCell(cellIndexDate)
                                                .apply {
                                                    setCellValue(XSSFRichTextString(date.key.toString()))
                                                    cellStyle = workbook.createCellStyle().apply {
                                                        setAlignment(HorizontalAlignment.CENTER)
                                                        setFont(workbook.createFont().apply {
                                                            bold = true
                                                            underline

                                                        })
                                                    }
                                                }
                                        }
                                        date.children.forEach { sub ->
                                            subjCount += 1
                                            row1.apply {
                                                createCell(cellIndexSub).apply {
                                                    setCellValue(XSSFRichTextString(sub.key.toString()))
                                                }
                                            }
                                            cellIndexSub += 1
                                        }
                                        firstSheet.addMergedRegion(
                                            CellRangeAddress(
                                                0,
                                                0,
                                                cellIndexDate,
                                                cellIndexDate + subjCount - 1
                                            )
                                        )

                                        cellIndexDate += subjCount
                                    }
                                }

                            snapshot.children.forEach { rollNo ->
                                var p = 0F
                                var a = 0F
                                var cellIndexA = 5
                                val dataRow = firstSheet.createRow(rowIndex)
                                dataRow.createCell(0).setCellValue(
                                    XSSFRichTextString(
                                        rollNo.key.toString()
                                    )
                                )
//                                dataRow.createCell(1).setCellValue(
//                                    XSSFRichTextString(
//                                       it.value.toString()
//                                    )
//                                )
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo.key.toString()).child("fullName")
                                    .addListenerForSingleValueEvent(object:ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            dataRow.createCell(1).setCellValue(
                                            XSSFRichTextString(
                                                snapshot.value.toString()
                                            )
                                        )
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
//                                        dataRow.createCell(1).setCellValue(
//                                            XSSFRichTextString(
//                                                it.value.toString()
//                                            )
//                                        )
//                                    }
                                rollNo.children.forEach { month ->
                                    month.children.forEach { date ->
                                        date.children.forEach { sub ->
//                                     val cell = row
                                            if (sub.child("status").value == "P") p += 1 else a += 1
                                            dataRow.createCell(cellIndexA)
                                                .setCellValue(XSSFRichTextString(sub.child("status").value.toString()))
                                            cellIndexA += 1
                                        }
                                    }
                                }
                                dataRow.createCell(2)
                                    .setCellValue(XSSFRichTextString("${p.toInt()} / ${a.toInt() + p.toInt()}"))
                                try {
                                    val per = ((p / (p + a)) * 100)
                                    dataRow.createCell(3)
                                        .setCellValue(XSSFRichTextString("${per}%"))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                rowIndex += 1
                            }
                            var fos: FileOutputStream? = null
                            try {
//                            val file_path =
//                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
//                                    .toString()
//
//                            val file = File(file_path, "attendance.xlsx")
//                            fos = FileOutputStream(file)
//                            workbook.write(fos)

                                val file = File(
                                    "${Environment.getExternalStorageDirectory()}/Documents/total_Attendance.xlsx"
                                )
                                if (file.exists()) file.delete()
                                workbook.write(FileOutputStream(file))

                                val uri = FileProvider.getUriForFile(
                                    this@ViewAttendanceActivity,
                                    "$packageName.provider",
                                    file
                                )
                                val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .setAction(Intent.ACTION_VIEW)
                                    .setDataAndType(uri, "application/vnd.ms-excel")
                                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    .also {
                                        if (it.resolveActivity(packageManager) == null) {
                                            it.setDataAndType(uri, "*/*")
                                        }
                                    }
                                startActivity(intent)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.flush()
                                        fos.close()
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                                Toast.makeText(
                                    this@ViewAttendanceActivity,
                                    "Excel Sheet Generated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataDefault() {
        fireDb.child("Student Attendance").child(TeacherDashboard.loggedTeacherDep)
            .child(binding.ACTVSelectYear.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        snapshot.children.forEach { rollno ->
                            var p = 0F
                            var a = 0F
                            val rollNo = rollno.key.toString()
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()

//                                .addListenerForSingleValueEvent(object:ValueEventListener{
//                                    override fun onDataChange(snapshot: DataSnapshot) {
//                                        studName = snapshot.value.toString()
////                                        Log.d("studName",studName)
////                                        Toast.makeText(this@ViewAttendanceActivity,snapshot.value.toString(),Toast.LENGTH_SHORT).show()
//                                    }
//                                    override fun onCancelled(error: DatabaseError) {
//                                    }
//                                })

                            rollno.children.forEach { month ->
                                month.children.forEach { dates ->
                                    dates.children.forEach { subs ->
                                        if (subs.child("status").value.toString() == "P") {
                                            p += 1
                                        }
                                        //Subjects
                                        a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                    }
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo).child("fullName").get().addOnSuccessListener {
                                        studAttendList.add(
                                            StudentData(
                                                rollNo,
                                                it.value.toString(),/*Percentage*/
                                                "${per}%"
                                            )
                                        )
                                        binding.rsvAllViewAttendance.adapter?.notifyDataSetChanged()
                                    }

//                                binding.rsvAllViewAttendance.adapter?.notifyDataSetChanged()
                            } catch (e: Exception) {
                                Log.e("errrr",e.message,e)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataBYMonth() {
        fireDb.child("Student Attendance").child(TeacherDashboard.loggedTeacherDep)
            .child(binding.ACTVSelectYear.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        snapshot.children.forEach { rollno ->
                            var p = 0F
                            var a = 0F

                            val rollNo = rollno.key.toString()
                            rollno.child(binding.ACTVMonth.text.toString()).children.forEach { dates ->
                                dates.children.forEach { subs ->
                                    if (subs.child("status").value.toString() == "P") {
                                        p += 1
                                    }
                                    //Subjects
                                    a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo).child("fullName").get().addOnSuccessListener {
                                        studAttendList.add(
                                            StudentData(
                                                rollNo,
                                                it.value.toString(),/*Percentage*/
                                                "${per}%"
                                            )
                                        )
                                        binding.rsvAllViewAttendance.adapter?.notifyDataSetChanged()
                                    }

                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataBYSub() {
        fireDb.child("Student Attendance").child(TeacherDashboard.loggedTeacherDep)
            .child(binding.ACTVSelectYear.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        snapshot.children.forEach { rollno ->
                            var p = 0F
                            var a = 0F
                            val rollNo = rollno.key.toString()
                            rollno.children.forEach { month ->
                                month.children.forEach { dates ->
                                    dates.children.forEach { subs ->
                                        if (subs.key.toString() == binding.ACTVSelectSubject.text.toString()) {
                                            if (subs.child("status").value.toString() == "P") {
                                                p += 1
                                            } //Subjects
                                            a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                        }
                                    }
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo).child("fullName").get().addOnSuccessListener {
                                        studAttendList.add(
                                            StudentData(
                                                rollNo,
                                                it.value.toString(),/*Percentage*/
                                                "${per}%"
                                            )
                                        )
                                        binding.rsvAllViewAttendance.adapter?.notifyDataSetChanged()
                                    }

                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataBYMonthNSub() {
        fireDb.child("Student Attendance").child(TeacherDashboard.loggedTeacherDep)
            .child(binding.ACTVSelectYear.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        snapshot.children.forEach { rollno ->
                            var p = 0F
                            var a = 0F
                            val rollNo = rollno.key.toString()
                            rollno.child(binding.ACTVMonth.text.toString()).children.forEach { dates ->
                                dates.children.forEach { subs ->
                                    if (subs.key.toString() == binding.ACTVSelectSubject.text.toString()) {
                                        if (subs.child("status").value.toString() == "P") p += 1
                                        a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                    }
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                fireDb.child("Students").child(TeacherDashboard.loggedTeacherDep)
                                    .child(binding.ACTVSelectYear.text.toString()).child(rollNo).child("fullName").get().addOnSuccessListener {
                                        studAttendList.add(
                                            StudentData(
                                                rollNo,
                                                it.value.toString(),/*Percentage*/
                                                "${per}%"
                                            )
                                        )
                                        binding.rsvAllViewAttendance.adapter?.notifyDataSetChanged()
                                    }

                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun autoCompleteTextViewSetUp() {
        val itemsYear = listOf("FY", "SY", "TY")
        val adapterYear = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            itemsYear
        )
        binding.ACTVSelectYear.setAdapter(adapterYear)
        binding.ACTVSelectYear.setOnItemClickListener { _, _, i, _ ->
            binding.ACTVSelectSubject.text.clear()
            val subjects = ArrayList<String>()
            FirebaseFirestore.getInstance()
                .collection("${itemsYear[i]}${TeacherDashboard.loggedTeacherDep}-Subjects")
                .addSnapshotListener { value, error ->
                    subjects.clear()
                    if (error != null) {
                        Toast.makeText(
                            this@ViewAttendanceActivity,
                            "Error found is $error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    value?.forEach { ds ->
                        subjects.add(ds["subjectName"].toString())
                    }
                }
            subjects.forEach {
                Log.i("subjects", it)
            }
            val adapterSubject = ArrayAdapter(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                subjects
            )
            binding.ACTVSelectSubject.setAdapter(adapterSubject)
        }
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
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            itemsMonth
        )
        binding.ACTVMonth.setAdapter(adapterMonth)
    }
}