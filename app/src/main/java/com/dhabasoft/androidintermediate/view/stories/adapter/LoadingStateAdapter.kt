package com.dhabasoft.androidintermediate.view.stories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dhabasoft.androidintermediate.R
import com.dhabasoft.androidintermediate.databinding.ItemLoadMoreBinding

class LoadingStateAdapter<T : Any, VH : RecyclerView.ViewHolder>(private val adapter: PagingDataAdapter<T, VH>): LoadStateAdapter<LoadingStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        return LoadingStateViewHolder.create(parent) { adapter.retry() }
    }
}

class LoadingStateViewHolder(
    private val binding: ItemLoadMoreBinding,
    private val retryCallback: () -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.txtErrorMessage.isVisible =
            !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        binding.txtErrorMessage.text = (loadState as? LoadState.Error)?.error?.message
        binding.retryButton.setOnClickListener { retryCallback() }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): LoadingStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_more, parent, false)

            val binding = ItemLoadMoreBinding.bind(view)

            return LoadingStateViewHolder(
                binding,
                retryCallback
            )
        }
    }
}