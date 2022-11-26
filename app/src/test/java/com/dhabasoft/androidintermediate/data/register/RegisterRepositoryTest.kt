package com.dhabasoft.androidintermediate.data.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dhabasoft.androidintermediate.core.RegisterRepository
import com.dhabasoft.androidintermediate.core.data.source.RegisterRemoteDataSource
import com.dhabasoft.androidintermediate.core.data.source.remote.ApiResponse
import com.dhabasoft.androidintermediate.ui.utils.DataDummy
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
class RegisterRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: RegisterRepository

    private val mockDataSource = Mockito.mock(RegisterRemoteDataSource::class.java)

    companion object {
        private const val MOCK_NAME = "dhaba"
        private const val MOCK_EMAIL = "dhaba1@test.com"
        private const val MOCK_PASSWORD = "1234567"
        private val dataRegister = ApiResponse.Success(
            DataDummy.generateRegisterResponse
        )
        private val mockRegister = flow { emit(dataRegister) }
    }

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        repository = RegisterRepository(mockDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given dummy email, name and password when register then success get register data`(): Unit =
        runBlocking {
            Mockito.`when`(
                mockDataSource.register(
                    email = MOCK_EMAIL,
                    password = MOCK_PASSWORD,
                    name = MOCK_NAME
                )
            ).thenReturn(
                mockRegister
            )

            val registerEntity = repository.register(
                email = MOCK_EMAIL,
                password = MOCK_PASSWORD,
                name = MOCK_NAME
            ).first { it.data != null }
            Assert.assertNotNull(registerEntity)
            Assert.assertEquals(mockRegister.first().data.message, registerEntity.data?.message)
            Assert.assertEquals(mockRegister.first().data.error, registerEntity.data?.error)
            Mockito.verify(mockDataSource).register(
                email = MOCK_EMAIL,
                password = MOCK_PASSWORD,
                name = MOCK_NAME
            )
        }
}