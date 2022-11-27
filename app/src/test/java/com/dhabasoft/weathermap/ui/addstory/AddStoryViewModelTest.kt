package com.dhabasoft.weathermap.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dhabasoft.weathermap.core.WeatherRepository
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.source.MapperEntity.mapResponseToCityEntity
import com.dhabasoft.weathermap.core.domain.usecase.WeatherInteractor
import com.dhabasoft.weathermap.ui.utils.DataDummy
import com.dhabasoft.weathermap.view.weather.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class AddStoryViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel

    private val repository = Mockito.mock(WeatherRepository::class.java)

    private val storiesUseCase = WeatherInteractor(repository)

    companion object {
        private const val MOCK_CITY = "jakarta"
        private val dataCity = Resource.Success(
            listOf(DataDummy.generateGetCity.mapResponseToCityEntity())
        )
        private val mockCity = flow { emit(dataCity)  }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<Resource<List<CityEntity>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = WeatherViewModel(storiesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email and password when login then success get login data`() = runBlocking{
        Mockito.`when`(
            repository.findCity(MOCK_CITY)
        ).thenReturn(
            mockCity
        )

        val cityLiveData = viewModel.findCity(MOCK_CITY)

        cityLiveData.observeForever(observer)
        val cityEntity = cityLiveData.value
        Assert.assertNotNull(cityEntity)

        Assert.assertEquals(mockCity.first().data, cityEntity?.data)

        Mockito.verify(observer).onChanged(dataCity)

    }
}