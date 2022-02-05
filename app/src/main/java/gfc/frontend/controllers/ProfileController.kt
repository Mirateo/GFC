package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.requests.AccountRequest
import gfc.frontend.requests.SigninRequest
import gfc.frontend.service.AuthorizationService
import gfc.frontend.service.ProfileService

object ProfileController {
    lateinit var context: Context
    val url = "https://gamefication-for-children.herokuapp.com/acc"
    var response = ""

    fun init(context: Context){
        this.context = context
    }

    fun changePass(newPass: AccountRequest) {
//        ProfileService.updatePasswd("$url/user", SigninRequest(newPass.username, newPass.password))
        return
    }

    fun changeUsername(newUsername: AccountRequest) {
//        ProfileService.updatePasswd("$url/user", SigninRequest(newUsername.username, newUsername.password))
        return
    }

    fun changeEmail(newEmail: AccountRequest) {
//        ProfileService.updateAccount("$url/userinfo", newEmail)
        return
    }
}