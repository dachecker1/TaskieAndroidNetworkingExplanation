package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.profile

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vk.dachecker.taskieandroidnetworkingexplanation.App
import com.vk.dachecker.taskieandroidnetworkingexplanation.R
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.FragmentProfileBinding
import com.vk.dachecker.taskieandroidnetworkingexplanation.networking.NetworkStatusChecker
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val remoteApi = App.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()

        networkStatusChecker.performIfConnectedToInternet {
            remoteApi.getUserProfile { userProfile, _ ->
                if (userProfile != null) {
                    binding.userEmail.text = userProfile.email
                    binding.userName.text = getString(R.string.user_name_text, userProfile.name)
                    binding.numberOfNotes.text = getString(R.string.number_of_notes_text, userProfile.numberOfNotes)
                }
            }
        }
    }

    private fun initUi() {
        binding.logOut.setOnClickListener {
            App.saveToken("")
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}