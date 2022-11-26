package com.dhabasoft.weathermap.data.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.dhabasoft.weathermap.core.StoriesRepository
import com.dhabasoft.weathermap.core.data.source.StoriesRemoteDataSource
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.ui.utils.DataDummy
import com.dhabasoft.weathermap.utils.UtilsTest.collectDataForTest
import kotlinx.coroutines.Dispatchers
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

/**
 * Created by dhaba
 */
@RunWith(MockitoJUnitRunner::class)
class StoriesRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: StoriesRepository

    private val mockDataSource = Mockito.mock(StoriesRemoteDataSource::class.java)

    companion object {
        private const val MOCK_BEARER = "Bearer something"
        private val MOCK_DUMMY_IMAGE = MultipartBody.Part.createFormData(
            "photo",
            "something",
            File("path").asRequestBody("image/*".toMediaTypeOrNull())
        )
        private const val MOCK_DESCRIPTION = "description"
        private const val MOCK_TOKEN = "Bearer something"
        private const val MOCK_LATITUDE = "-6.0"
        private const val MOCK_LONGITUDE = "-112.0"
        private val dataStories = ApiResponse.Success(
            DataDummy.generateStoriesResponse
        )
        private val mockFirstStoriesData = dataStories.data.listStory[0]
        private val mockStoriesLocation = flow { emit(dataStories) }
        private val dataAddStory = ApiResponse.Success(
            DataDummy.generateAddStoryResponse
        )
        private val mockAddStory = flow { emit(dataAddStory) }
        private val dataPagingStories = PagingData.from(DataDummy.generateStoriesResponse.listStory)
        val mockStoryPaging = flow { emit(dataPagingStories)  }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        repository = StoriesRepository(mockDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy token when get data stories location then success get data with correct size and value`(): Unit =
        runBlocking {
            Mockito.`when`(
                mockDataSource.getStoriesLocation(
                    token = MOCK_BEARER
                )
            ).thenReturn(
                mockStoriesLocation
            )

            val storiesEntity = repository.getStoriesLocation(
                token = MOCK_BEARER
            ).first { it.data != null }
            Assert.assertNotNull(storiesEntity)
            Assert.assertEquals(10, storiesEntity.data?.size)
            Assert.assertEquals(mockFirstStoriesData, storiesEntity.data?.get(0))
            Mockito.verify(mockDataSource).getStoriesLocation(
                token = MOCK_BEARER
            )
        }

    @Test
    fun `given image and description when add story then success add story`(): Unit =
        runBlocking {
            Mockito.`when`(
                mockDataSource.addStory(
                    description = MOCK_DESCRIPTION,
                    filePart = MOCK_DUMMY_IMAGE,
                    token = MOCK_TOKEN,
                    lat = MOCK_LATITUDE,
                    lon = MOCK_LONGITUDE
                )
            ).thenReturn(
                mockAddStory
            )

            val addStoryEntity = repository.addStory(
                description = MOCK_DESCRIPTION,
                filePart = MOCK_DUMMY_IMAGE,
                token = MOCK_TOKEN,
                lat = MOCK_LATITUDE,
                lon = MOCK_LONGITUDE
            ).first { it.data != null }
            Assert.assertNotNull(addStoryEntity)
            Assert.assertEquals(mockAddStory.first().data.message, addStoryEntity.data?.message)
            Assert.assertEquals(mockAddStory.first().data.error, addStoryEntity.data?.error)
            Mockito.verify(mockDataSource).addStory(
                description = MOCK_DESCRIPTION,
                filePart = MOCK_DUMMY_IMAGE,
                token = MOCK_TOKEN,
                lat = MOCK_LATITUDE,
                lon = MOCK_LONGITUDE
            )
        }

    @Test
    fun `given token when get paging data story then success get data with correct size`(): Unit =
        runBlocking {
            Mockito.`when`(
                mockDataSource.getStoriesPaging(
                    authorization = MOCK_TOKEN
                )
            ).thenReturn(
                mockStoryPaging
            )

            val storiesEntity = repository.getStoriesPaging(
                token = MOCK_TOKEN,
            ).first()
            Assert.assertNotNull(storiesEntity)
            Mockito.verify(mockDataSource).getStoriesPaging(
                authorization = MOCK_TOKEN
            )
            val data = storiesEntity.collectDataForTest()
            Assert.assertEquals(10, data?.size)
            Assert.assertEquals(mockFirstStoriesData, data?.get(0))
        }
}