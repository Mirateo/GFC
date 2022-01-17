package gfc.frontend.dataclasses
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val id: Long, val username: String, val email:String, val role: String, val friendlyName: String)
