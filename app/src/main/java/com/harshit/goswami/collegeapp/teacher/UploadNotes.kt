package com.harshit.goswami.collegeapp.teacher

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.harshit.goswami.collegeapp.modal.NotesData
import com.harshit.goswami.collegeapp.databinding.ActivityTeacherUploadNotesBinding


class UploadNotes : AppCompatActivity() {
    private lateinit var bindind: ActivityTeacherUploadNotesBinding
    private var fileUri: Uri? = null
    private var downloadUri = ""
    private val fireDb = FirebaseDatabase.getInstance().reference
    private var storageRef = FirebaseStorage.getInstance().reference
    private var clickedButton = ""
    private val getResult = registerForActivityResult(
        GetContent()
    ) { uri: Uri? ->
        if (uri!!.port == RESULT_OK) {
            fileUri = uri
            val bookName = bindind.txtBookName
            if (fileUri.toString().startsWith("content://")) {
                val cursor: Cursor? = this.contentResolver.query(fileUri!!, null, null, null, null)
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
        bindind = ActivityTeacherUploadNotesBinding.inflate(layoutInflater)
        setContentView(bindind.root)
        clickedButton = intent.getStringExtra("clickedButton").toString()
        bindind.cardSelectpdf.setOnClickListener { getResult.launch("application/*") }
        bindind.btnUploadBook.setOnClickListener {
            if (fileUri == null) Toast.makeText(
                this@UploadNotes,
                "Please select Pdf",
                Toast.LENGTH_SHORT
            ).show()
            else uploadFileAndData()
        }
        setUpAutoCompleteTextView()
    }

    private fun setUpAutoCompleteTextView() {
        val itemsYear = listOf("FY", "SY", "TY")
        val adapterYear = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            itemsYear
        )
        bindind.selectClass.setAdapter(adapterYear)
        bindind.selectClass.setOnItemClickListener { _, _, p2, _ ->
            bindind.selectSubject.text.clear()
            val itemsSubj = ArrayList<String>()
             fireDb.child("Faculty Data")
                 .child(TeacherDashboard.loggedTeacherDep)
                 .child(TeacherDashboard.loggedTeacherName)
                 .child("Assigned Subject")
                 .child(itemsYear[p2]).addListenerForSingleValueEvent(object : ValueEventListener {
                     override fun onDataChange(snapshot: DataSnapshot) {
                         snapshot.children.forEach { sub ->
                             itemsSubj.add(sub.value.toString())
                         }
                     }
                     override fun onCancelled(error: DatabaseError) {
                     }
                 })
                val adapterSub = ArrayAdapter(
                    this, R.layout.support_simple_spinner_dropdown_item, itemsSubj
                )
                bindind.selectSubject.setAdapter(adapterSub)
        }

    }

    private fun uploadFileAndData() {
        bindind.UNProgressBox.visibility = View.VISIBLE
        if (clickedButton == "notes") {
            storageRef.child("Notes").child(TeacherDashboard.loggedTeacherDep)  .child(bindind.selectClass.text.toString())
                .child(TeacherDashboard.loggedTeacherName)
                .child(bindind.selectSubject.text.toString())
                .child(bindind.edtBookTitle.text.toString())
                .putFile(fileUri!!)
                .addOnSuccessListener {
                    bindind.UNProgressBox.visibility = View.GONE
                    Toast.makeText(this@UploadNotes, "Pdf uploaded", Toast.LENGTH_SHORT).show()
                    it.storage.downloadUrl.addOnSuccessListener { it1 ->
                        downloadUri = it1.toString()
                        uploadData()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this@UploadNotes,
                            "downloadUriError!-${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    bindind.UNProgressBox.visibility = View.GONE
                    Toast.makeText(
                        this@UploadNotes,
                        "Firebase Error!-${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    bindind.UNTxtProgress.text = "Uploaded " + progress.toInt() + "%"
                }
        }
        if (clickedButton == "assignment") {
            storageRef.child("Given Assignments").child(TeacherDashboard.loggedTeacherDep) .child(bindind.selectClass.text.toString())
                .child(TeacherDashboard.loggedTeacherName)
                .child(bindind.selectSubject.text.toString())
                .child(bindind.edtBookTitle.text.toString())
                .putFile(fileUri!!)
                .addOnSuccessListener {
                    bindind.UNProgressBox.visibility = View.GONE
                    Toast.makeText(this@UploadNotes, "Pdf uploaded", Toast.LENGTH_SHORT).show()
                    it.storage.downloadUrl.addOnSuccessListener { it1 ->
                        downloadUri = it1.toString()
                        uploadData()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this@UploadNotes,
                            "downloadUriError!-${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    bindind.UNProgressBox.visibility = View.GONE
                    Toast.makeText(
                        this@UploadNotes,
                        "Firebase Error!-${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    bindind.UNTxtProgress.text = "Uploaded " + progress.toInt() + "%"
                }
        }
    }

    private fun uploadData() {
        if (clickedButton == "notes") {
            fireDb.child("Notes").child(TeacherDashboard.loggedTeacherDep).child(bindind.selectClass.text.toString())
                .child(TeacherDashboard.loggedTeacherName)
                .child(bindind.selectSubject.text.toString())
                .child(bindind.edtBookTitle.text.toString())
                .setValue(
                    NotesData(
                        bindind.edtBookTitle.text.toString(),
                        downloadUri,
                        bindind.selectSubject.text.toString(),
                        TeacherDashboard.loggedTeacherName,
                        bindind.selectClass.text.toString(),
                        bindind.edtDescription.text.toString()
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(this@UploadNotes, "Data Uploaded", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        this@UploadNotes,
                        "Firebase Error!-${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        if (clickedButton == "assignment") {
            fireDb.child("Given Assignments").child(TeacherDashboard.loggedTeacherDep).child(bindind.selectClass.text.toString())
                .child(TeacherDashboard.loggedTeacherName)
                .child(bindind.selectSubject.text.toString())
                .child(bindind.edtBookTitle.text.toString())
                .setValue(
                    NotesData(
                        bindind.edtBookTitle.text.toString(),
                        downloadUri,
                        bindind.selectSubject.text.toString(),
                        TeacherDashboard.loggedTeacherName,
                        bindind.selectClass.text.toString(),
                        bindind.edtDescription.text.toString()
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(this@UploadNotes, "Data Uploaded", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        this@UploadNotes,
                        "Firebase Error!-${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}