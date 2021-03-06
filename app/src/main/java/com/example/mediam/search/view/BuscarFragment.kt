package com.example.mediam.search.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediam.databinding.BuscarFragmentBinding
import com.example.mediam.search.viewModel.BuscarViewModel

class BuscarFragment : Fragment() {

    private var _binding: BuscarFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val searchViewModel =  ViewModelProvider(this).get(BuscarViewModel::class.java)
        _binding = BuscarFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}