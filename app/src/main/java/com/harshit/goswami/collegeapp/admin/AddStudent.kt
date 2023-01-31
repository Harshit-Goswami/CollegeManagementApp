package com.harshit.goswami.collegeapp.admin


import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.adapters.YourStudentAdapter
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddStudentBinding
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.IOException


class AddStudent : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddStudentBinding
    private var fileUri: Uri? = null
    val TAG = "ExcelUtil"
    var studentRowDataList = ArrayList<String>()
    private var studentsList = ArrayList<StudentData>()

    //    private val cell: XSSFCell? = null
    private var sheet: XSSFSheet? = null
    private var workbook: XSSFWorkbook? = null

    //    private val headerCellStyle: CellStyle? = null
    private val getResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri!!.port == RESULT_OK) {
            fileUri = uri
            val bookName = binding.txtFileName
            if (fileUri.toString().startsWith("content://")) {
                val cursor: Cursor? = this.contentResolver.query(fileUri!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") val fileTitle: String =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    bookName.text = fileTitle
                    readExcelData()
                }
            } else {
                bookName.text = fileUri!!.queryParameterNames.toString()
                readExcelData()
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardProgressBar.visibility = View.GONE
        initializeRecyclerView()
        binding.btnSelectFile.setOnClickListener {
            getResult.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        }
        binding.btnDownloadFormat.setOnClickListener {
            downloadFileFormat()
        }
        binding.ASTxtUploadData.setOnClickListener {
            if (studentsList.isNotEmpty()) {
                binding.cardProgressBar.visibility = View.VISIBLE
                try {
                    binding.cardProgressBar.visibility = View.VISIBLE
                    for (i in studentsList) {
                        val rollNo = i.rollNo.removeRange(3, 5)
                        FirebaseDatabase.getInstance().reference.child("Students")
                            .child(i.department)
                            .child(i.year)
                            .child(rollNo)
                            .setValue(
                                StudentData(
                                    i.rollNo,
                                    i.fullName,
                                    i.contactNo,
                                    i.password,
                                    i.department,
                                    i.year
                                )
                            )
                    }

                } catch (e: Exception) {
                    Log.d("Storing Data", "Errr-:${e.message}", e)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    val snackBar = Snackbar.make(
                        it, "Data Uploaded Successfully..",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null)
                    snackBar.setActionTextColor(Color.WHITE)
                    val snackBarView = snackBar.view
                    snackBarView.setBackgroundColor(Color.GREEN)
                    val textView =
                        snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.WHITE)
                    snackBar.show()
                    binding.cardProgressBar.visibility = View.GONE
                }, 3000)
            } else Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeRecyclerView() {
        binding.rsvStudentData.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvStudentData.adapter = YourStudentAdapter(studentsList, this)
        binding.rsvStudentData.setHasFixedSize(true)
    }

    private fun readExcelData() {
        studentsList.clear()
        if (fileUri != null) {
//                var fileInputStream: FileInputStream? = null
            try {
                val fileInputStream =
                    contentResolver.openInputStream(fileUri!!) as FileInputStream?
//                    Log.e(TAG, "Reading from Excel$file")
                // Create instance having reference to .xls file
                workbook = XSSFWorkbook(fileInputStream)
                // Fetch sheet at position 'i' from the workbook
                sheet = workbook!!.getSheetAt(0)
                for (row in sheet as XSSFSheet) {
                    if (row.rowNum > 0) {
                        // Iterate through all the columns in a row (Excluding header row)
                        val cellIterator = row.cellIterator()
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()
                            when (cell.cellType) {
                                Cell.CELL_TYPE_STRING -> {
//                                        Log.d(TAG, cell.stringCellValue)
                                    studentRowDataList.add(cell.stringCellValue)
                                }
                                Cell.CELL_TYPE_NUMERIC -> {
                                    studentRowDataList.add(cell.numericCellValue.toString())
                                }
                                else -> {}
                            }

                        }
                        studentsList.add(
                            StudentData(
                                studentRowDataList[0],
                                studentRowDataList[1],
                                studentRowDataList[2],
                                studentRowDataList[3],
                                studentRowDataList[4],
                                studentRowDataList[5]
                            )
                        )
                        binding.rsvStudentData.adapter?.notifyDataSetChanged()
                        studentRowDataList.clear()
                        // Adding cells with phone numbers to phoneNumberList
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error Reading Exception: ", e)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "Failed to read file due to Exception: ", e)
            }
        } else Toast.makeText(this, "File is Empty!", Toast.LENGTH_SHORT).show()
    }

    private fun downloadFileFormat() {
        val url: Uri = Uri.parse(
            "https://firebasestorage.googleapis.com/v0/b/collage-app-8b9e5.appspot.com/o/StudentExcelDataFormatFile%2FstudentsDataFormat.xlsx?alt=media&token=2ec54714-63c3-4436-9699-d69c70ef299a"
        )
        val request = DownloadManager.Request(Uri.parse(url.toString()))
        request.setDescription("Student Data excel Format Downloaded! ")
        request.setTitle("StudentsExcelFormat.xlsx")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "studentDataFormat.xlsx"
        )
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
}
