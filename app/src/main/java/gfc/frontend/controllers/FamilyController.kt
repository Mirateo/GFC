package gfc.frontend.controllers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.SigninRequest
import gfc.frontend.requests.SignupChildRequest
import gfc.frontend.requests.SignupRequest
import gfc.frontend.service.FamilyService
import java.lang.IllegalArgumentException

object FamilyController {
    lateinit var context: Context
    val url = "https://gamefication-for-children.herokuapp.com/family"
    var response = ""

    fun init(context: Context){
        this.context = context
        FamilyService.init(context)
    }

    fun getAll(): List<UserInfo>{
        return FamilyService.getAll(url + "/")
    }

    fun addChild(username: String, friendlyName: String, password: String): String? {
        val email = context.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("email", "nope:(").toString()

        return FamilyService.addChild("$url/add", SignupChildRequest(username, friendlyName, email, password))
    }

    fun delChild(childId: Long): String? {
        return FamilyService.delChild("$url/remove/${childId}")
    }

//    fun editChild(user: UserInfo): String? {
//        return FamilyService.editChild("$url/edit", user)
//    }
}
