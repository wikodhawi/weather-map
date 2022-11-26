package com.dhabasoft.androidintermediate.view.register

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dhabasoft.androidintermediate.R
import com.dhabasoft.androidintermediate.base.BaseActivity
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.register)

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterName.text.toString(),
                binding.edRegisterPasswodrd.text.toString()
            ).observe(this) {
                if (it != null) {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressRegister.root.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressRegister.root.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "${it.data?.message} ${getString(R.string.now_you_can_login)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        is Resource.Error -> {
                            binding.progressRegister.root.visibility = View.GONE
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}