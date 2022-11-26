package com.dhabasoft.androidintermediate.view.stories.adapter.viewholder

import android.app.Activity
import android.app.ActivityOptions
import android.util.Pair
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dhabasoft.androidintermediate.R
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.databinding.ItemStoryBinding
import com.dhabasoft.androidintermediate.view.stories.adapter.StoryPagingAdapter

/**
 * Created by dhaba
 */

class StoryViewHolder(private val binding: ItemStoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Story, onClickCallback: StoryPagingAdapter.OnClickCallback) {
        binding.tvItemName.text = item.name
        Glide.with(itemView.context).load(item.photoUrl).into(binding.ivItemPhoto)
        binding.crdStory.setOnClickListener {
            val bundleTransition = ActivityOptions.makeSceneTransitionAnimation(
                itemView.context as Activity,
                Pair(binding.tvItemName, "story_author"),
                Pair(binding.ivItemPhoto, "story_photo")
            ).toBundle()
            onClickCallback.toDetailStory(item, bundleTransition)
        }
    }

    companion object {
        fun create(parent: ViewGroup): StoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_story, parent, false)

            val binding = ItemStoryBinding.bind(view)

            return StoryViewHolder(
                binding
            )
        }
    }


}