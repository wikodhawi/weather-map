package com.dhabasoft.weathermap.data.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dhabasoft.weathermap.core.LoginRepository
import com.dhabasoft.weathermap.core.data.source.LoginRemoteDataSource
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.ui.utils.DataDummy
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dhaba
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: LoginRepository

    private val mockDataSource = Mockito.mock(LoginRemoteDataSource::class.java)

    companion object {
        private const val MOCK_EMAIL = "dhaba1@test.com"
        private const val MOCK_PASSWORD = "1234567"
        private val dataLogin = ApiResponse.Success(
            DataDummy.generateLoginResponse
        )
        private val mockLogin = flow { emit(dataLogin) }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        repository = LoginRepository(mockDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email and password when login then success get login data`(): Unit =
        runBlocking {
            Mockito.`when`(
                mockDataSource.login(
                    email = MOCK_EMAIL,
                    password = MOCK_PASSWORD
                )
            ).thenReturn(
                mockLogin
            )

            val loginEntity = repository.login(
                email = MOCK_EMAIL,
                password = MOCK_PASSWORD
            ).first { it.data != null }
            Assert.assertNotNull(loginEntity)
            Assert.assertEquals(mockLogin.first().data.loginResult.name, loginEntity.data?.name)
            Assert.assertEquals(mockLogin.first().data.loginResult.token, loginEntity.data?.token)
            Assert.assertEquals(mockLogin.first().data.loginResult.userId, loginEntity.data?.userId)
            Mockito.verify(mockDataSource).login(
                email = MOCK_EMAIL,
                password = MOCK_PASSWORD
            )
        }
}