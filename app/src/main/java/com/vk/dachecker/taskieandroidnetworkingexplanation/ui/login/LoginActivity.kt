package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.login

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vk.dachecker.taskieandroidnetworkingexplanation.App
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.ActivityLoginBinding
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.request.UserDataRequest
import com.vk.dachecker.taskieandroidnetworkingexplanation.networking.NetworkStatusChecker
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.main.MainActivity
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.register.RegisterActivity
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.gone
import com.vk.dachecker.taskieandroidnetworkingexplanation.utils.visible

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val remoteApi = App.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

        if (App.getToken().isNotBlank()) {
            startActivity(MainActivity.getIntent(this))
        }
    }

    private fun initUi() {
        binding.login.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                logUserIn(UserDataRequest(email, password))
            } else {
                showLoginError()
            }
        }
        binding.register.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }

    private fun logUserIn(userDataRequest: UserDataRequest) {
        networkStatusChecker.performIfConnectedToInternet {
            remoteApi.loginUser(userDataRequest) { token: String?, throwable: Throwable? ->
                if (token != null && token.isNotBlank()) {
                    onLoginSuccess(token)
                } else if (throwable != null) {
                    showLoginError()
                }
            }
        }
    }

    private fun onLoginSuccess(token: String) {
        binding.errorText.gone()
        App.saveToken(token)
        startActivity(MainActivity.getIntent(this))
    }

    private fun showLoginError() {
        binding.errorText.visible()
    }
}