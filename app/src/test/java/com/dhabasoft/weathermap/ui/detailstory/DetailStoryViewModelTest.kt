package com.dhabasoft.weathermap.ui.detailstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dhabasoft.weathermap.core.WeatherRepository
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailCityEntity
import com.dhabasoft.weathermap.core.data.source.MapperEntity.mapResponseToCityEntity
import com.dhabasoft.weathermap.core.data.source.MapperEntity.mapToDetailCityEntity
import com.dhabasoft.weathermap.core.domain.usecase.WeatherInteractor
import com.dhabasoft.weathermap.ui.utils.DataDummy
import com.dhabasoft.weathermap.view.detailcity.DetailCityViewModel
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
class DetailStoryViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailCityViewModel

    private val repository = Mockito.mock(WeatherRepository::class.java)

    private val storiesUseCase = WeatherInteractor(repository)

    companion object {
        private const val CITY_ID = "1234"
        private val dataDetailCity = Resource.Success(
            DataDummy.generateGetDetailCity.mapToDetailCityEntity()
        )
        private val mockCity = flow { emit(dataDetailCity)  }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<Resource<List<DetailCityEntity>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        Mockito.`when`(
            repository.getFlowIsFavourite()
        ).thenReturn(
            flow { true }
        )
        viewModel = DetailCityViewModel(storiesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy specific city id when get detail city then success get detail data city`() = runBlocking{
        Mockito.`when`(
            repository.detailCity(CITY_ID)
        ).thenReturn(
            mockCity
        )

        val cityLiveData = viewModel.getDetailCity(CITY_ID)

        cityLiveData.observeForever(observer)
        val cityEntity = cityLiveData.value
        Assert.assertNotNull(cityEntity)

        Assert.assertEquals(mockCity.first().data, cityEntity?.data)

        Mockito.verify(observer).onChanged(dataDetailCity)

    }
}