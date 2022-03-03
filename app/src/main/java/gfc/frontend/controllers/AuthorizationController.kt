package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.requests.SigninRequest
import gfc.frontend.requests.SignupRequest
import gfc.frontend.service.AuthorizationService
import android.content.Context.MODE_PRIVATE
import gfc.frontend.dataclasses.UserInfo
import kotlin.properties.Delegates


object AuthorizationController {
    lateinit var context: Context
    val url = "https://gamefication-for-children.herokuapp.com/auth"
    val loginUrl = "https://gamefication-for-children.herokuapp.com/login"
    var response = ""
    var userIsParent = true

    fun init(context: Context){
        this.context = context
        AuthorizationService.init(context)
    }

    fun registerParent(user: SignupRequest): String{
        return AuthorizationService.registerParent("$url/signup", user)
    }

    fun login(credentials: SigninRequest): Boolean {
        val token = AuthorizationService.login(loginUrl, credentials)
        if(token != null){
            context.getSharedPreferences("credentials", MODE_PRIVATE)
                .edit()
                .putString("username", credentials.username)
                .putString("password", credentials.password)
                .putString("token", token)
                .apply()

            val userInfo = AuthorizationService.getUserInfo("$url/user_info")

            context.getSharedPreferences("userInfo", MODE_PRIVATE)
                .edit()
                .putLong("id", userInfo.id)
                .putString("username", userInfo.username)
                .putString("email", userInfo.email)
                .putString("role", userInfo.role)
                .putString("friendlyName", userInfo.friendlyName)
                .putLong("points", userInfo.points)
                .apply()

            userIsParent = (userInfo.role == "PARENT")

            return true
        }

        return false
    }

    fun relogin(): Boolean {
        val username = context.getSharedPreferences("credentials", MODE_PRIVATE).getString("username", "")
        val password = context.getSharedPreferences("credentials", MODE_PRIVATE).getString("password", "")

        return login(SigninRequest(username!!, password!!))
    }

    fun editProfile(newUser: UserInfo): String? {
        return AuthorizationService.editProfile("$url/user_info/edit", newUser)
    }

}