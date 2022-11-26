package com.dhabasoft.androidintermediate.utils

import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

object UtilsTest {
    @ExperimentalCoroutinesApi
    suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                onListPresentable()
                return null
            }

//            override suspend fun presentNewList(
//                previousList: NullPaddedList<T>,
//                newList: NullPaddedList<T>,
//                newCombinedLoadStates: CombinedLoadStates,
//                lastAccessedIndex: Int,
//                onListPresentable: () -> Unit
//            ): Int? {
//                for (idx in 0 until newList.size)
//                    items.add(newList.getFromStorage(idx))
//                onListPresentable()
//                return null
//            }
        }
        dif.collectFrom(this)
        return items
    }
}