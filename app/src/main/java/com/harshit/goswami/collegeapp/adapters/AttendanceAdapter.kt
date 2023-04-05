package com.harshit.goswami.collegeapp.adapters

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.data.AttendanceDataModal
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.ItemTakeAttendanceBinding


class AttendanceAdapter(
    private var StudentList: ArrayList<StudentData> = ArrayList(),
    private val context: Context,
    private val work:String
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
//    val attendanceMap = HashMap<String, String>()

    companion object {
        val attendanceList = ArrayList<AttendanceDataModal>()
    }

    class ViewHolder(internal val binding: ItemTakeAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTakeAttendanceBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(StudentList[position]) {
                if (work=="view"){
                    binding.txtAttendanceStatus.setBackgroundColor(  ContextCompat.getColor(
                        context,
                        R.color.shrine_pink_50
                    ))
                    binding.txtAttendanceStatus.setTextColor(Color.BLACK)
//                    binding.txtAttendanceStatus.textSize = 20F
                    binding.IStudentRoll.text = "|${this.rollNo}|"
                    binding.txtAttendanceStatus.text = this.department
                    binding.IStudentName.text = this.fullName

                } else {
                    var isPresent = false

                binding.IStudentName.text = this.fullName
                binding.IStudentRoll.text = "|${this.rollNo}|"

                attendanceList.add(AttendanceDataModal(this.rollNo, this.fullName, "A"))

                binding.txtAttendanceStatus.setOnClickListener {
                    if (isPresent) {
                        isPresent = false
                        binding.txtAttendanceStatus.text = "A"
                        binding.txtAttendanceStatus.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.red_800
                            )
                        )
                        for (i in 0 until attendanceList.size) {
                            if (attendanceList[i].rollNo == this.rollNo) {
                                attendanceList[i] = AttendanceDataModal(
                                    attendanceList[i].rollNo,
                                    attendanceList[i].studName,
                                    "A"
                                )
                            }
                        }
//                        attendanceList.add(AttendanceDataModal(this.rollNo,this.fullName,"A"))
                    } else {
                        isPresent = true
                        binding.txtAttendanceStatus.text = "P"
                        binding.txtAttendanceStatus.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green_800
                            )
                        )
                        for (i in 0 until attendanceList.size) {
                            if (attendanceList[i].rollNo == this.rollNo) {
                                attendanceList[i] = AttendanceDataModal(
                                    attendanceList[i].rollNo,
                                    attendanceList[i].studName,
                                    "P"
                                )
                            }
                        }
//                        attendanceList.add(AttendanceDataModal(this.rollNo,this.fullName,"P"))
                    }
                }
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }

}