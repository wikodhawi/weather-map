package com.dhabasoft.androidintermediate.view.detailstory

import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dhabasoft.androidintermediate.R
import com.dhabasoft.androidintermediate.base.BaseActivity
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    companion object {
        const val KEY_DATA_STORY = "key_data_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        title = getString(R.string.detail_story)
        setContentView(binding.root)

        val storyData = intent.getParcelableExtra<Story>(KEY_DATA_STORY)

        val username = "Author ${storyData?.name}"
        binding.tvDetailName.text = username
        Glide.with(applicationContext).load(storyData?.photoUrl).into(binding.ivDetailPhoto)
        binding.tvDetailDescription.text = storyData?.description

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}