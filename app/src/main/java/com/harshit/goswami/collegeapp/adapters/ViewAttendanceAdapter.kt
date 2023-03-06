package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.data.AttendanceDataModal
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.data.ViewAttendanceDataModal
import com.harshit.goswami.collegeapp.databinding.ItemTakeAttendanceBinding


class ViewAttendanceAdapter(
    private var StudentList: ArrayList<ViewAttendanceDataModal> = ArrayList(),
    private val context: Context,
) : RecyclerView.Adapter<ViewAttendanceAdapter.ViewHolder>() {
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


            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }

}