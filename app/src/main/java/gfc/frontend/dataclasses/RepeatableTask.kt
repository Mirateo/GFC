package gfc.frontend.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat

@Entity
@Serializable
data class RepeatableTask(
    @Id
    var id: Long = 0,
    var ownerId: Long = 0,
    var name: String = "",
    var description: String = "",
    var points: Long = 0,
    var doneToday: Boolean = false,
    @Serializable(with = DateSerializer::class)
    var lastDone: Date = Date()
)


object DateSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        return DateFormat.getDateInstance().parse(decoder.decodeString()) ?: Date()
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toString())
    }
}