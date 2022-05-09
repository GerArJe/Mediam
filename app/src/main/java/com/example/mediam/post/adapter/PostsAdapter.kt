package com.example.mediam.post.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mediam.R
import com.example.mediam.databinding.PostItemBinding
import com.example.mediam.model.entity.Post

class PostsAdapter(private var posts:ArrayList<Post>): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    var onItemClickListener:((Post)->Unit)?=null
    var onItemLongClickListener:((Post)->Unit)?=null



    @SuppressLint("NotifyDataSetChanged")
    fun refresh(myPosts: ArrayList<Post>){
        posts = myPosts
        notifyDataSetChanged()
    }

    class PostsViewHolder(val binding: PostItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind (myPost: Post, onItemClickListener: ((Post) -> Unit)?, onItemLongClickListener: ((Post) -> Unit)?){
            binding.post = myPost

            binding.root.setOnClickListener{
                onItemClickListener?.let{
                    it(myPost)
                }
            }

            binding.root.setOnLongClickListener{
                onItemLongClickListener?.let {
                    it(myPost)
                }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding: PostItemBinding = DataBindingUtil.inflate(inflate,
            R.layout.post_item,
            parent,
            false )
        return PostsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(posts[position], onItemClickListener, onItemLongClickListener)
    }

    override fun getItemCount(): Int = posts.size
}