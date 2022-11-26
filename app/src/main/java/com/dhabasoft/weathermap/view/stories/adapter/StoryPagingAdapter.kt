package com.dhabasoft.weathermap.view.stories.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import com.dhabasoft.weathermap.view.stories.adapter.viewholder.StoryViewHolder

/**
 * Created by dhaba
 */
class StoryPagingAdapter(private val onClickCallback: OnClickCallback) :
    PagingDataAdapter<Story, RecyclerView.ViewHolder>(
        COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StoryViewHolder.create(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_story
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as StoryViewHolder).bind(it, onClickCallback)
        }
    }

    fun withMySpecificFooter(
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = when (loadStates.refresh) {
                is LoadState.NotLoading -> loadStates.append
                else -> loadStates.refresh
            }
        }
        return ConcatAdapter(this, footer)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnClickCallback {
        fun toDetailStory(story: Story, bundleTransition: Bundle)
    }
}