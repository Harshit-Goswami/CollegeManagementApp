package com.harshit.goswami.collegeapp.student

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harshit.goswami.collegeapp.databinding.FragmentMyClassBinding

class MyClassFragment : Fragment() {
    companion object {

    }

    private lateinit var binding: FragmentMyClassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyClassBinding.inflate(inflater, container, false)
        binding.FMCBtnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.FMCCardAssignment.setOnClickListener {
            val intent = Intent(context,AssignmentActivity::class.java)
            intent.putExtra("cardClick","assignment")
            startActivity(intent)  }
        binding.FMCCardNotes.setOnClickListener {
            val intent = Intent(context,AssignmentActivity::class.java)
            intent.putExtra("cardClick","notes")
            startActivity(intent)
        }
        binding.FMCCardPreviousPapers.setOnClickListener {
            val intent = Intent(context,AssignmentActivity::class.java)
            intent.putExtra("cardClick","previousPaper")
            startActivity(intent)
        }



        return binding.root
    }
}