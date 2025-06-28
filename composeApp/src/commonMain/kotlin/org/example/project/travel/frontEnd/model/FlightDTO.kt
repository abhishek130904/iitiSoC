package com.example.travel.model.dto

import com.example.travel.serialization.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class FlightDTO(
    val flightNumber: String,
    val airlineCode: String,
    val departure: FlightPointDTO,
    val arrival: FlightPointDTO,
    @Serializable(with = DurationSerializer::class)
    val duration: String,
    val price: Double,
    val currency: String
)

@Serializable
data class FlightPointDTO(
    val iataCode: String,
    @Serializable(with = InstantSerializer::class)
    val time: Instant,
    val terminal: String? = null
)

object DurationSerializer : KSerializer<String> {
    override val descriptor = PrimitiveSerialDescriptor("Duration", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return decoder.decodeString()
    }
}