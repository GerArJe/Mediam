package com.example.mediam.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.databinding.FragmentHomeBinding
import com.example.mediam.home.viewModel.HomeViewModel
import com.example.mediam.login.view.Register
import com.example.mediam.model.entity.Post
import com.example.mediam.post.adapter.PostsAdapter
import com.example.mediam.post.view.PostActivity
import com.example.mediam.profile.viewModel.PerfilViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var viewModel: HomeViewModel
    lateinit var adapter: PostsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = PostsAdapter(arrayListOf())
        binding.adapter = adapter


        loadPosts()
        createPost()

        val root: View = binding.root
        return root
    }

    private fun loadPosts() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.refresh(it as ArrayList<Post>)
        }
    }


    private fun createPost() {
        _binding?.let {
            it.fabHome.setOnClickListener {
                val intentPost = Intent(this.context, PostActivity::class.java)
                startActivity(intentPost)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        viewModel.loadAllPosts()
        super.onResume()
    }
}