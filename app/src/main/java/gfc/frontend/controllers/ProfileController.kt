package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.AccountRequest
import gfc.frontend.requests.SigninRequest
import gfc.frontend.service.AuthorizationService
import gfc.frontend.service.ProfileService

object ProfileController {
    lateinit var context: Context
    var response = ""

    fun init(context: Context){
        this.context = context
    }

}