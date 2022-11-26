package com.dhabasoft.androidintermediate.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dhabasoft.androidintermediate.core.StoriesRepository
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.domain.usecase.StoriesInteractor
import com.dhabasoft.androidintermediate.ui.utils.DataDummy
import com.dhabasoft.androidintermediate.view.addstory.AddStoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

/**
 * Created by dhaba
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddStoryViewModel

    private val repository = Mockito.mock(StoriesRepository::class.java)

    private val storiesUseCase = StoriesInteractor(repository)

    companion object {
        private const val MOCK_DESCRIPTION = "description"
        private const val MOCK_TOKEN = "Bearer something"
        private const val MOCK_LATITUDE = "-6.0"
        private const val MOCK_LONGITUDE = "-112.0"
        private val dataAddStory = Resource.Success(
            DataDummy.generateAddStoryResponse
        )
        private val mockAddStory = flow { emit(dataAddStory)  }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<Resource<GeneralResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = AddStoryViewModel(storiesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email and password when login then success get login data`() = runBlocking{
        val dummyFilePart = MultipartBody.Part.createFormData(
                "photo",
                "something",
                File("path").asRequestBody("image/*".toMediaTypeOrNull())
            )
        Mockito.`when`(
            repository.addStory(
                description = MOCK_DESCRIPTION,
                filePart = dummyFilePart,
                token = MOCK_TOKEN,
                lat = MOCK_LATITUDE,
                lon = MOCK_LONGITUDE
            )
        ).thenReturn(
            mockAddStory
        )

        val addStoryLiveData = viewModel.addStory(MOCK_DESCRIPTION, dummyFilePart, MOCK_TOKEN, MOCK_LATITUDE, MOCK_LONGITUDE)

        addStoryLiveData.observeForever(observer)
        val addStoryEntity = addStoryLiveData.value
        Assert.assertNotNull(addStoryEntity)

        Assert.assertEquals(mockAddStory.first().data?.message, addStoryEntity?.data?.message)
        Assert.assertEquals(mockAddStory.first().data?.error, addStoryEntity?.data?.error)

        Mockito.verify(observer).onChanged(dataAddStory)

    }
}