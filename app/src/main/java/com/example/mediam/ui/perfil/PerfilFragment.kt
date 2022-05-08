package com.example.mediam.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel =  ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textPerfil
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        println("Hola")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Adios")
        _binding = null
    }
}