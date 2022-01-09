package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.requests.SignupRequest
import gfc.frontend.service.AuthorizationService
import kotlinx.coroutines.runBlocking

class AuthorizationController(val context: Context?){
    val url = "https://gamefication-for-children.herokuapp.com/"
    var response = ""

    val authService = AuthorizationService(this.context)

    fun registerParent(user: SignupRequest): String{
        return authService.registerParent(url + "auth/signup", user)
    }
}