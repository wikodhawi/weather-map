package com.dhabasoft.weathermap.view.stories

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.base.BaseActivity
import com.dhabasoft.weathermap.core.MyPreferences
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import com.dhabasoft.weathermap.databinding.ActivityStoriesBinding
import com.dhabasoft.weathermap.utils.Utils.visibleWhen
import com.dhabasoft.weathermap.view.addstory.AddStoryActivity
import com.dhabasoft.weathermap.view.detailstory.DetailStoryActivity
import com.dhabasoft.weathermap.view.stories.adapter.LoadingStateAdapter
import com.dhabasoft.weathermap.view.stories.adapter.StoryPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class StoriesActivity : BaseActivity() {
    private lateinit var binding: ActivityStoriesBinding
    private lateinit var adapter: StoryPagingAdapter
    private val viewModel: StoriesViewModel by viewModels()
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = MyPreferences(applicationContext).getToken()

        adapter = StoryPagingAdapter(object : StoryPagingAdapter.OnClickCallback {
            override fun toDetailStory(story: Story, bundleTransition: Bundle) {
                val intent = Intent(applicationContext, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.KEY_DATA_STORY, story)
                startActivity(intent, bundleTransition)
            }
        })
        adapter.addLoadStateListener { loadState ->
            loadState.refresh.let {
                binding.rcyStories.visibleWhen(it !is LoadState.Loading)
                binding.progressStories.root.visibleWhen(it is LoadState.Loading)
            }
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcyStories.adapter = adapter
        binding.rcyStories.adapter = adapter.withMySpecificFooter(
            footer = LoadingStateAdapter(adapter)
        )
        binding.rcyStories.layoutManager = layoutManager

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(applicationContext, AddStoryActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        binding.swpStories.setOnRefreshListener {
            getStories()
        }
    }

    private fun getStories() {
        viewModel.getStoriesPaging(token).observe(this) {
            lifecycleScope.launch {
//                EspressoIdlingResource.decrement()
                adapter.submitData(
                    it
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sign_out) {
            MyPreferences(applicationContext).logoutUser()
        } else if (item.itemId == R.id.action_change_language) {
            if (MyPreferences(applicationContext).getLanguage() == LANGUAGE_IN) {
                updateLocale(Locale(LANGUAGE_EN, ""))
            } else {
                updateLocale(Locale(LANGUAGE_IN, ""))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}