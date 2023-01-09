package com.harshit.goswami.collegeapp.admin


import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.adapters.RegisterdStudentAdapter
import com.harshit.goswami.collegeapp.data.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddStudentBinding
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*

class AddStudent : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddStudentBinding
    private var fileUri: Uri? = null
    val TAG = "ExcelUtil"
    var studentRowDataList = ArrayList<String>()
    var studentsList = ArrayList<RegisteredStudentData>()

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
                var cursor: Cursor? = null
                cursor =
                    this.contentResolver.query(fileUri!!, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") val fileTitle: String =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    bookName.text = fileTitle
                }
            } else bookName.text = fileUri!!.queryParameterNames.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intitializeRecyclerView()
        binding.btnSelectFile.setOnClickListener {
            getResult.launch("application/xlsx")
        }
        binding.btnAddData.setOnClickListener {
            readExcelData()
            binding.rsvStudentData.adapter?.notifyDataSetChanged()
        }
        binding.btnDownloadFormat.setOnClickListener {
            downloadFileFormat()
        }
    }

    private fun intitializeRecyclerView() {
        binding.rsvStudentData.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvStudentData.adapter = RegisterdStudentAdapter(studentsList, this)
        binding.rsvStudentData.setHasFixedSize(true)
    }

    private fun readExcelData() {
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
                            RegisteredStudentData(
                                studentRowDataList[0],
                                studentRowDataList[1],
                                studentRowDataList[2],
                                studentRowDataList[3],
                                studentRowDataList[4],
                                studentRowDataList[5]
                            )
                        )
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
        var inputStream: InputStream? = null
        var fout: FileOutputStream? = null
        try {
            inputStream = resources.openRawResource(R.raw.students)
            val downloadsDirectoryPath: String =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
            val filename = "students.xlsx"
            fout = FileOutputStream(File(downloadsDirectoryPath + filename))
            val data = ByteArray(1024)
            var count: Int
            while ((inputStream.read(data, 0, 1024).also { count = it }) != -1) {
                fout.write(data, 0, count)
            }
        } finally {
            inputStream?.close()
            fout?.close()
        }
    }
}
