package com.dhabasoft.androidintermediate.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dhabasoft.androidintermediate.core.LoginRepository
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.login.LoginResult
import com.dhabasoft.androidintermediate.core.domain.usecase.LoginInteractor
import com.dhabasoft.androidintermediate.ui.utils.DataDummy
import com.dhabasoft.androidintermediate.view.login.LoginViewModel
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
class LoginViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    private val repository = Mockito.mock(LoginRepository::class.java)

    private val loginUseCase = LoginInteractor(repository)

    companion object {
        private const val MOCK_EMAIL = "dhaba1@test.com"
        private const val MOCK_PASSWORD = "1234567"
        private val dataLogin = Resource.Success(
            DataDummy.generateLoginResponse.loginResult
        )
        private val mockLogin = flow { emit(dataLogin)  }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var observer: Observer<Resource<LoginResult>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email and password when login then success get login data`() = runBlocking {
        Mockito.`when`(
            repository.login(
                email = MOCK_EMAIL,
                password = MOCK_PASSWORD
            )
        ).thenReturn(
            mockLogin
        )
        val loginLiveData = viewModel.login(
            email = MOCK_EMAIL,
            password = MOCK_PASSWORD
        )

        loginLiveData.observeForever(observer)
        val loginEntities = loginLiveData.value
        Assert.assertNotNull(loginEntities)

        Assert.assertEquals(mockLogin.first().data?.name, loginEntities?.data?.name)
        Assert.assertEquals(mockLogin.first().data?.token, loginEntities?.data?.token)
        Assert.assertEquals(mockLogin.first().data?.userId, loginEntities?.data?.userId)

        Mockito.verify(observer).onChanged(dataLogin)

    }
}