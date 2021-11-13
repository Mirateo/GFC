package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Task (
    @Id
    var id: Long = 0,
    var ownerId: Long = 0,
    var name: String = "",
    var description: String = "",
    var points: Long = 0
)
