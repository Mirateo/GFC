package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Task (
    @Id(assignable = true)
    @Required var id: Long = 0,
    @Required var ownerId: Long = 0,
    @Required var name: String = "",
    @Required var description: String = "",
    @Required var points: Long = 0,
    @Required  val own: Boolean = false
)
