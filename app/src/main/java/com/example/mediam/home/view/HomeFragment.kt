package com.example.mediam.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.mediam.PostDetailActivity
import com.example.mediam.databinding.FragmentHomeBinding
import com.example.mediam.home.viewModel.HomeViewModel
import com.example.mediam.login.view.Register
import com.example.mediam.model.entity.Filter
import com.example.mediam.model.entity.Post
import com.example.mediam.post.adapter.PostsAdapter
import com.example.mediam.post.view.PostActivity
import com.example.mediam.profile.viewModel.PerfilViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var viewModel: HomeViewModel
    lateinit var adapter: PostsAdapter
    private lateinit var resultFilter: ActivityResultLauncher<Intent>
    private lateinit var resultCreate: ActivityResultLauncher<Intent>
    private lateinit var actionBar: ActionBar

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
        search()

        adapter.onItemClickListener={
            val intentLogin = Intent(requireActivity().getApplicationContext(), PostDetailActivity::class.java)
            intentLogin.apply {
                putExtra("id", it.id)
                putExtra("idUserPost", it.idUser)
            }
            startActivity(intentLogin)
        }

        val root: View = binding.root
        return root
    }



    private fun search() {
        _binding?.let {
            it.fabSearchPostsHome.setOnClickListener {
                val intentSearch = Intent(this.context, SearchActivity::class.java)
                activity?.let { it1 ->
                    intentSearch.resolveActivity(it1.packageManager)?.let {
                        resultFilter.launch(intentSearch)
                    } ?: run {
                        println("HomeFragment: error intentSearch")
                    }
                } ?: run {
                    println("HomeFragment: error activity")
                }


            }
        }

        resultFilter =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.resultCode == AppCompatActivity.RESULT_OK) run {
                    viewModel.filter =
                        activityResult.data!!.getSerializableExtra("filter") as Filter
                }
            }
    }

    private fun loadPosts() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.refresh(it as ArrayList<Post>)
        }
    }


    private fun createPost() {
        _binding?.let {
            it.fabCreatePostHome.setOnClickListener {
                val intentPost = Intent(this.context, PostActivity::class.java)
                activity?.let { it1 ->
                    intentPost.resolveActivity(it1.packageManager)?.let {
                        resultCreate.launch(intentPost)
                    } ?: run {
                        println("HomeFragment: error intentSearch")
                    }
                } ?: run {
                    println("HomeFragment: error activity")
                }
            }
        }

        resultCreate =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.resultCode == AppCompatActivity.RESULT_OK) run {
                    viewModel.filter = Filter()

                }
            }
    }

    private fun hasFilters(): Boolean {
        return viewModel.filter.title.isNotEmpty() || viewModel.filter.idTopic.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        if (!hasFilters()) {
            viewModel.loadAllPosts()
        } else {
            viewModel.loadPostByFilters()
        }
        super.onResume()
    }


}