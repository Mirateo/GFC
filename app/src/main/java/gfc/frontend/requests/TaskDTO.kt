package gfc.frontend.requests
import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(val ownerId: Long = 0, val name: String = "Default", val description: String = "", val points: Long = 0, val repeatable: Boolean = false)
