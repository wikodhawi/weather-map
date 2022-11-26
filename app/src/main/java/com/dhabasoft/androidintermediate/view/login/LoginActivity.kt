package com.dhabasoft.androidintermediate.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dhabasoft.androidintermediate.base.BaseActivity
import com.dhabasoft.androidintermediate.core.MyPreferences
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.databinding.ActivityLoginBinding
import com.dhabasoft.androidintermediate.utils.EspressoIdlingResource
import com.dhabasoft.androidintermediate.view.main.MainActivity
import com.dhabasoft.androidintermediate.view.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isLogin = MyPreferences(applicationContext).getToken().isNotBlank()
        if (isLogin) {
            goToMainActivity()
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPasswodrd.text.toString()
            ).observe(this) { loginResponse ->
                if (loginResponse != null) {
                    when (loginResponse) {
                        is Resource.Loading -> {
                            EspressoIdlingResource.increment()
                            binding.progressLogin.root.visibility = View.VISIBLE
                            binding.edLoginPasswodrd.isEnabled = false
                            binding.edLoginEmail.isEnabled = false
                            binding.btnLogin.isEnabled = false
                        }
                        is Resource.Success -> {
                            EspressoIdlingResource.decrement()
                            binding.edLoginPasswodrd.isEnabled = true
                            binding.edLoginEmail.isEnabled = true
                            binding.btnLogin.isEnabled = true
                            binding.progressLogin.root.visibility = View.GONE
                            MyPreferences(applicationContext).setToken(
                                loginResponse.data?.token ?: ""
                            )
                            goToMainActivity()
                        }
                        is Resource.Error -> {
                            EspressoIdlingResource.decrement()
                            binding.edLoginPasswodrd.isEnabled = true
                            binding.edLoginEmail.isEnabled = true
                            binding.btnLogin.isEnabled = true
                            binding.progressLogin.root.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                loginResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.tvLoginRegister.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}