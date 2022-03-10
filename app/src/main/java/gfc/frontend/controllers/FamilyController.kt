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
    var familyList = ArrayList<UserInfo>()

    fun init(context: Context){
        this.context = context
        FamilyService.init(context)
        getAll()
    }

    fun getChildrenName(id: Long): String {
        for (child in familyList) {
            if (child.id == id) {
                return child.friendlyName
            }
        }
        return "dziecko"
    }

    fun getChildrenId(response: String): Long {
        for (child in familyList) {
            if (child.username == response) {
                return child.id
            }
        }

        return -1
    }

    fun getChildrenUsername(fn: String): String {
        if(fn == "prywatne") {
            for (child in familyList) {
                if (child.role == "PARENT") {
                    return child.username
                }
            }
            return "---"
        }
        for (child in familyList) {
            if (child.friendlyName == fn) {
                return child.username
            }
        }
        return "---"
    }


    fun uniqFriendlyName(friendlyName: String): Boolean {
        for (child in familyList) {
            if (child.friendlyName == friendlyName) {
                return false
            }
        }
        return true

    }

    fun getAll(): List<UserInfo> {
        familyList = FamilyService.getAll(url + "/") as ArrayList<UserInfo>
        return familyList
    }

    fun addChild(username: String, friendlyName: String, password: String): String? {
        val email = context.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("email", "nope:(").toString()

        return FamilyService.addChild("$url/add", SignupChildRequest(username, friendlyName, email, password))
    }

    fun delChild(childId: Long): String? {
        return FamilyService.delChild("$url/remove/${childId}")
    }
    
}
