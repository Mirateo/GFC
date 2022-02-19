package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Task (
    @Id(assignable = true)
    var id: Long = 0,
    var ownerId: Long = 0,
    var name: String = "",
    var description: String = "",
    var points: Long = 0,
    val own: Boolean = false
)
