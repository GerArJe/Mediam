package com.example.mediam.home.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.R
import com.example.mediam.databinding.ActivitySearchBinding
import com.example.mediam.home.viewModel.SearchActivityViewModel
import com.example.mediam.model.entity.Topic

class SearchActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchActivityViewModel
    private lateinit var spTopics: Spinner
    private var topics: MutableList<Topic> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = ViewModelProvider(this)[SearchActivityViewModel::class.java]

        binding.viewModel = viewModel

        setSpinner()
        reset()
        search()
    }

    private fun search() {
        binding.btnSearchSearch.setOnClickListener {
            val intentResult = Intent(applicationContext, HomeFragment::class.java)
            intentResult.putExtra("filter", viewModel.filter)
            setResult(Activity.RESULT_OK, intentResult)
            finish()
        }
    }


    private fun reset() {
        binding.btnResetSearch.setOnClickListener {
            binding.etTitleSearch.text = null
            setSpinner()
        }
    }

    private fun setSpinner() {
        viewModel.getTopics()
        spTopics = findViewById(R.id.sp_topic_search)
        viewModel.topics.observe(this) {
            val topic = Topic()
            topic.name = "Seleccione una opción"
            topics.add(topic)
            topics.addAll(it as ArrayList)
            val topicsAdapter: ArrayAdapter<Topic> = ArrayAdapter<Topic>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                topics
            )
            spTopics.adapter = topicsAdapter
            with(binding.spTopicSearch) {
                onItemSelectedListener = this@SearchActivity
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.filter.idTopic = topics[position].id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}