package com.example.mediam.profile.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.databinding.FragmentPerfilBinding
import com.example.mediam.model.entity.Post
import com.example.mediam.post.adapter.PostsAdapter
import com.example.mediam.profile.viewModel.PerfilViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    lateinit var viewModel: PerfilViewModel
    lateinit var adapter: PostsAdapter

    private val binding get() = _binding!!
    var idUser:String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel =  ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)



        adapter = PostsAdapter(arrayListOf())
        binding.adapter = adapter

        binding.followersCount.text = "0"
        binding.followingCount.text = "0"

        loadInfo()
        loadPosts()
        val root: View = binding.root
        return root
    }

    private fun loadInfo() {
        val preferences: SharedPreferences = requireActivity().getSharedPreferences("shad.pref", MODE_PRIVATE)
        idUser = preferences.getString("id", "")
        binding.emailInfo.text = preferences.getString("email", "")
        binding.fullName.text = preferences.getString("name", "")
    }

    private fun loadPosts() {
        viewModel.posts.observe(viewLifecycleOwner /*this*/){
            adapter.refresh(it as ArrayList<Post>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        idUser?.let { viewModel.loadProducts(it) }
        super.onResume()
    }
}