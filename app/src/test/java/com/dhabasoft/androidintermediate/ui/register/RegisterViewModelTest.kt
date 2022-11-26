package com.dhabasoft.androidintermediate.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dhabasoft.androidintermediate.core.RegisterRepository
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.domain.usecase.RegisterInteractor
import com.dhabasoft.androidintermediate.ui.utils.DataDummy
import com.dhabasoft.androidintermediate.view.register.RegisterViewModel
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
class RegisterViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterViewModel

    private val repository = Mockito.mock(RegisterRepository::class.java)

    private val registerUseCase = RegisterInteractor(repository)

    companion object {
        private const val MOCK_NAME = "dhaba"
        private const val MOCK_EMAIL = "dhaba1@test.com"
        private const val MOCK_PASSWORD = "1234567"

        private val dataRegister = Resource.Success(
            DataDummy.generateRegisterResponse
        )
        private val mockRegister = flow { emit(dataRegister) }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<Resource<GeneralResponse>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = RegisterViewModel(registerUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email name and password when register then success register data`() = runBlocking {
        Mockito.`when`(
            repository.register(
                name = MOCK_NAME,
                email = MOCK_EMAIL,
                password = MOCK_PASSWORD
            )
        ).thenReturn(
            mockRegister
        )

        val registerLiveData = viewModel.register(
            name = MOCK_NAME,
            email = MOCK_EMAIL,
            password = MOCK_PASSWORD
        )

        registerLiveData.observeForever(observer)
        val registerEntity = registerLiveData.value
        Assert.assertNotNull(registerEntity)
        Assert.assertEquals(mockRegister.first().data?.message, registerEntity?.data?.message)
        Assert.assertEquals(mockRegister.first().data?.error, registerEntity?.data?.error)
        Mockito.verify(observer).onChanged(dataRegister)
    }
}