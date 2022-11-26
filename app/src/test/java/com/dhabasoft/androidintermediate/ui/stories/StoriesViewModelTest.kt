package com.dhabasoft.androidintermediate.ui.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.dhabasoft.androidintermediate.core.StoriesRepository
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.core.domain.usecase.StoriesInteractor
import com.dhabasoft.androidintermediate.ui.utils.DataDummy
import com.dhabasoft.androidintermediate.utils.UtilsTest.collectDataForTest
import com.dhabasoft.androidintermediate.view.stories.StoriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dhaba
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StoriesViewModel

    private val repository = Mockito.mock(StoriesRepository::class.java)

    private val storiesUseCase = StoriesInteractor(repository)

    companion object {
        private val dataStories = PagingData.from(DataDummy.generateStoriesResponse.listStory)
        private val mockStory = flow { emit(dataStories)  }
        private val mockFirstStoriesData = DataDummy.generateStoriesResponse.listStory[0]
        private const val DUMMY_TOKEN = "Bearer something"
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<PagingData<Story>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = StoriesViewModel(storiesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test when get all 10 products success then product entities size equals 10 and check livedata variable products value from viewmodel value changed to dataProducts`() = runBlocking {

        Mockito.`when`(repository.getStoriesPaging(DUMMY_TOKEN)).thenReturn(
            mockStory
        )

        val storiesLiveData = viewModel.getStoriesPaging(DUMMY_TOKEN)
        storiesLiveData.observeForever(observer)

        val storiesEntities = storiesLiveData.value

        Assert.assertNotNull(storiesEntities)

        val data = storiesEntities?.collectDataForTest()
        Assert.assertEquals(10, data?.size)
        Assert.assertEquals(mockFirstStoriesData, data?.get(0))

        Mockito.verify(observer).onChanged(dataStories)
    }
}