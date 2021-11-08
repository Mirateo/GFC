package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*


@Entity
public data class RepeatableTask(
    @Id
    public var id: Long = 0,
    public var ownerId: Long = 0,
    public var name: String = "",
    public var description: String = "",
    public var points: Long = 0,
    public var doneToday: Boolean = false,
    public var lastDone: Date = Date()


)
