package com.example.mediam.profile.view

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.PostDetailActivity
import com.example.mediam.databinding.FragmentPerfilBinding
import com.example.mediam.model.entity.Post
import com.example.mediam.post.adapter.PostsAdapter
import com.example.mediam.profile.queries.QueriesProfile
import com.example.mediam.profile.viewModel.PerfilViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    lateinit var viewModel: PerfilViewModel
    lateinit var adapter: PostsAdapter
    val queriesProfile: QueriesProfile = QueriesProfile()

    private val binding get() = _binding!!
    var idUser:String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel =  ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)



        adapter = PostsAdapter(arrayListOf())
        binding.adapter = adapter



        loadInfo()
        loadPosts()

        adapter.onItemClickListener={
            val intentLogin = Intent(requireActivity().getApplicationContext(), PostDetailActivity::class.java)
            intentLogin.apply {
                putExtra("title", it.title)
                putExtra("img", it.urlImage)
                putExtra("desc", it.description)
            }
            startActivity(intentLogin)
        }

        adapter.onItemLongClickListener={
            idUser?.let { idu-> viewModel.deletePost(it, idu) }
            Toast.makeText(requireActivity().getApplicationContext(), "Post ${it.title} eliminado...", Toast.LENGTH_SHORT).show()
        }

        val root: View = binding.root
        return root
    }


    private fun loadInfo() {
        val preferences: SharedPreferences = requireActivity().getSharedPreferences("shad.pref", MODE_PRIVATE)
        idUser = preferences.getString("idUser", "")
        binding.emailInfo.text = preferences.getString("email", "")
        binding.fullName.text = preferences.getString("name", "")

        idUser?.let { id->
            queriesProfile.retrieveInfoFollowUser(id, binding)
        }
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
        idUser?.let { viewModel.loadPosts(it) }
        super.onResume()
    }

}