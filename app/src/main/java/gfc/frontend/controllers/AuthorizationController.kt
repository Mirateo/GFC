package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.requests.SigninRequest
import gfc.frontend.requests.SignupRequest
import gfc.frontend.service.AuthorizationService
import android.R.attr.password
import android.accounts.AccountManager
import android.content.SharedPreferences
import android.net.wifi.hotspot2.pps.Credential
import android.widget.Toast
import android.content.Context.MODE_PRIVATE
import gfc.frontend.LoginActivity


class AuthorizationController(val context: Context?) {
    val url = "https://gamefication-for-children.herokuapp.com"
    var response = ""
    val authService = AuthorizationService(context)

    fun registerParent(user: SignupRequest): String{
        return authService.registerParent("$url/auth/signup", user)
    }

    fun login(credentials: SigninRequest): Boolean {
        val token = authService.login("$url/login", credentials)
        if(token != null){
            context!!.getSharedPreferences("credentials", MODE_PRIVATE)
                .edit()
                .putString("username", credentials.username)
                .putString("password", credentials.password)
                .putString("token", token)
                .apply()

            val userInfo = authService.getUserInfo("$url/auth/user_info")

            context!!.getSharedPreferences("userInfo", MODE_PRIVATE)
                .edit()
                .putLong("id", userInfo.id)
                .putString("username", userInfo.username)
                .putString("email", userInfo.email)
                .putString("role", userInfo.role)
                .putString("friendlyName", userInfo.friendlyName)
                .apply()

            println(context!!.getSharedPreferences("userInfo", MODE_PRIVATE).getString("friendlyName", "nope:(").toString())

            return true
        }

        return false
    }

}