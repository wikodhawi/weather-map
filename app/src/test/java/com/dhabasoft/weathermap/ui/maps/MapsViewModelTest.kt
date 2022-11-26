package com.dhabasoft.weathermap.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dhabasoft.weathermap.core.StoriesRepository
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import com.dhabasoft.weathermap.core.domain.usecase.StoriesInteractor
import com.dhabasoft.weathermap.ui.utils.DataDummy
import com.dhabasoft.weathermap.view.maps.MapsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class MapsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapsViewModel

    private val repository = Mockito.mock(StoriesRepository::class.java)

    private val storiesUseCase = StoriesInteractor(repository)

    companion object {
        private const val MOCK_TOKEN = "Bearer something"
        private val dataStories = Resource.Success(
            DataDummy.generateStoriesResponse.listStory
        )
        private val mockFirstStoriesData = dataStories.data?.get(0)
        private val mockStories = flow { emit(dataStories)  }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<Resource<List<Story>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = MapsViewModel(storiesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email and password when login then success get login data`() {
        Mockito.`when`(
            repository.getStoriesLocation(
                token = MOCK_TOKEN
            )
        ).thenReturn(
            mockStories
        )

        val storiesLocation = viewModel.getStoriesLocation(token = MOCK_TOKEN)

        storiesLocation.observeForever(observer)
        val storiesEntity = storiesLocation.value
        Assert.assertNotNull(storiesEntity)
        Assert.assertEquals(10, storiesEntity?.data?.size)
        Assert.assertEquals(mockFirstStoriesData, storiesEntity?.data?.get(0))

        Mockito.verify(observer).onChanged(dataStories)

    }
}