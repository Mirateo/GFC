package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*


@Entity
data class RepeatableTask(
    @Id
    var id: Long = 0,
    var ownerId: Long = 0,
    var name: String = "",
    var description: String = "",
    var points: Long = 0,
    var doneToday: Boolean = false,
    var lastDone: Date = Date()


)
