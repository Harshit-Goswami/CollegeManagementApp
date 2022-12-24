package com.harshit.goswami.collegeapp.student

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

        return binding.root
    }
}