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
    var id: Long = 0,
    var ownerId: Long = 0,
    var name: String = "",
    var description: String = "",
    var points: Long = 0,
    @Serializable(with = DateSerializer::class)
    var lastDone: Date? = null,
    val own: Boolean = false
)


object DateSerializer : KSerializer<Date?> {
    override val descriptor = PrimitiveSerialDescriptor("DATE", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date? {
        return SimpleDateFormat("yyyy-MM-dd").parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Date?) {
        encoder.encodeString(value.toString())
    }
}