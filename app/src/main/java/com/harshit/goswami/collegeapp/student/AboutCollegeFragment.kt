package com.harshit.goswami.collegeapp.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.FragmentAboutCollegeBinding

class AboutCollegeFragment : Fragment() {
    companion object {
    }

    private lateinit var binding: FragmentAboutCollegeBinding
    private var isAboutUsOpen: Boolean = false
    private var isOurGoalsOpen: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutCollegeBinding.inflate(inflater, container, false)



        binding.FABtnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.FAAboutUsHeading.setOnClickListener {
            if (!isAboutUsOpen) {
                isAboutUsOpen = true
                binding.FATxtAboutUs.visibility = View.VISIBLE
                binding.FAAboutUsHeading.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up,
                    0
                )
            } else {
                isAboutUsOpen = false
                binding.FATxtAboutUs.visibility = View.GONE
                binding.FAAboutUsHeading.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_double_arrow_down,
                    0
                )
            }
        }
        binding.FAOurGoalsHeading.setOnClickListener {
            if (!isOurGoalsOpen) {
                isOurGoalsOpen = true
                binding.FATxtOurGoals.visibility = View.VISIBLE
                binding.FAOurGoalsHeading.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up,
                    0
                )
            } else {
                isOurGoalsOpen = false
                binding.FATxtOurGoals.visibility = View.GONE
                binding.FAOurGoalsHeading.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_double_arrow_down,
                    0
                )
            }
        }

        return binding.root
    }

}