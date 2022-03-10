package gfc.frontend.requests
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(@Required val ownerId: Long = 0,
                   @Required val name: String = "Zadanie bez nazwy",
                   @Required val description: String = "",
                   @Required val points: Long = 0,
                   @Required val repeatable: Boolean = false,
                   @Required val own: Boolean = false)
