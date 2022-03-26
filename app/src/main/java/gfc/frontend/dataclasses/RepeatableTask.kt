package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity
@Serializable
data class RepeatableTask(
    @Id(assignable = true)
    @Required var id: Long = 0,
    @Required var ownerId: Long = 0,
    @Required var name: String = "",
    @Required var description: String = "",
    @Required var points: Long = 0,
    @Serializable(with = DateSerializer::class)
    @Required var lastDone: Date? = null,
    @Required var own: Boolean = false
) {
    constructor(task: Task) : this() {
        id = task.id
        ownerId = task.ownerId
        name = task.name
        description = task.description
        points = task.points
        lastDone = null
        own = task.own
    }
}

object DateSerializer : KSerializer<Date?> {
    override val descriptor = PrimitiveSerialDescriptor("DATE", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date? {
        return SimpleDateFormat("yyyy-MM-dd").parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Date?) {
        encoder.encodeString(value.toString())
    }
}