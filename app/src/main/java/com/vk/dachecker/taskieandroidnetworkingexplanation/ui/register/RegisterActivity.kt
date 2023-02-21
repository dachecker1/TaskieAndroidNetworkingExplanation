package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.register

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vk.dachecker.taskieandroidnetworkingexplanation.App
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.ActivityRegisterBinding
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.request.UserDataRequest
import com.vk.dachecker.taskieandroidnetworkingexplanation.networking.NetworkStatusChecker
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.gone
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.toast
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.visible

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val remoteApi = App.remoteApi

    private val networkStatusChecker by lazy {
        NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.register.setOnClickListener {
            processData(binding.nameInput.text.toString(), binding.emailInput.text.toString(),
                binding.passwordInput.text.toString())
        }
    }

    private fun processData(username: String, email: String, password: String) {
        if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            networkStatusChecker.performIfConnectedToInternet {
                remoteApi.registerUser(UserDataRequest(email, password, username)) { message, error ->
                    if (message != null) {
                        toast(message)
                        onRegisterSuccess()
                    } else if (error != null) {
                        onRegisterError()
                    }
                }
            }
        } else {
            onRegisterError()
        }
    }

    private fun onRegisterSuccess() {
        binding.errorText.gone()
        finish()
    }

    private fun onRegisterError() {
        binding.errorText.visible()
    }
}