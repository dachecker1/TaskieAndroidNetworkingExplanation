package com.vk.dachecker.taskieandroidnetworkingexplanation.networking

import com.vk.dachecker.taskieandroidnetworkingexplanation.App
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.Task
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.UserProfile
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.request.AddTaskRequest
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.request.UserDataRequest
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi(private val apiService: RemoteApiService) {

    fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {
        apiService.loginUser(userDataRequest).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, error: Throwable) {
                onUserLoggedIn(null, error)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if (loginResponse == null || loginResponse.token.isNullOrEmpty()) {
                    onUserLoggedIn(null, NullPointerException("No response body!"))
                } else {
                    onUserLoggedIn(loginResponse.token, null)
                }
            }
        })

    }

    fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
        apiService.registerUser(userDataRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, error: Throwable) {
                onUserCreated(null, error)
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val message = response.body()?.message
                if (message == null) {
                    onUserCreated(null, NullPointerException("No response body!"))
                    return
                }

                onUserCreated(message, null)
            }
        })
    }

    fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {
        apiService.getNotes(App.getToken()).enqueue(object : Callback<GetTasksResponse> {
            override fun onFailure(call: Call<GetTasksResponse>, error: Throwable) {
                onTasksReceived(emptyList(), error)
            }

            override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {
                val data = response.body()

                if (data != null && data.notes.isNotEmpty()) {
                    onTasksReceived(data.notes.filter { !it.isCompleted }, null)
                } else {
                    onTasksReceived(emptyList(), NullPointerException("No data available!"))
                }
            }
        })
    }

    fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
        onTaskDeleted(null)
    }

    fun completeTask(taskId: String, onTaskCompleted: (Throwable?) -> Unit) {
        apiService.completeTask(App.getToken(), taskId).enqueue(object :
            Callback<CompleteNoteResponse> {
            override fun onFailure(call: Call<CompleteNoteResponse>, error: Throwable) {
                onTaskCompleted(error)
            }

            override fun onResponse(call: Call<CompleteNoteResponse>,
                                    response: Response<CompleteNoteResponse>
            ) {
                val completeNoteResponse = response.body()

                if (completeNoteResponse?.message == null) {
                    onTaskCompleted(NullPointerException("No response!"))
                } else {
                    onTaskCompleted(null)
                }
            }
        })
    }

    fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {
        apiService.addTask(App.getToken(), addTaskRequest).enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, error: Throwable) {
                onTaskCreated(null, error)
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                val data = response.body()

                if (data == null) {
                    onTaskCreated(null, NullPointerException("No response!"))
                    return
                } else {
                    onTaskCreated(data, null)
                }
            }
        })

    }

    fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
        getTasks { tasks, error ->
            if (error != null && error !is NullPointerException) {
                onUserProfileReceived(null, error)
                return@getTasks
            }

            apiService.getMyProfile(App.getToken()).enqueue(object : Callback<UserProfileResponse> {
                override fun onFailure(call: Call<UserProfileResponse>, error: Throwable) {
                    onUserProfileReceived(null, error)
                }

                override fun onResponse(call: Call<UserProfileResponse>,
                                        response: Response<UserProfileResponse>
                ) {
                    val userProfileResponse = response.body()

                    if (userProfileResponse?.email == null || userProfileResponse.name == null) {
                        onUserProfileReceived(null, error)
                    } else {
                        onUserProfileReceived(
                            UserProfile(userProfileResponse.email, userProfileResponse.name, tasks.size), null
                        )
                    }
                }
            })
        }
    }
}