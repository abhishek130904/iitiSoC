package com.example.travel.serialization

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import co.touchlab.kermit.Logger

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        Logger.d { "Parsing Instant from string: $string" }
        return try {
            // Try parsing as ISO 8601 (e.g., "2025-06-10T12:35:00Z" or "2025-06-10T12:35:00+05:30")
            Instant.parse(string).also {
                Logger.d { "Parsed as ISO 8601: $it" }
            }
        } catch (e: Exception) {
            Logger.d { "ISO 8601 parsing failed: ${e.message}" }
            try {
                // Parse "yyyy-MM-dd'T'HH:mm:ss" without timezone (e.g., "2025-06-10T12:35:00")
                val (datePart, timePart) = string.split("T")
                Logger.d { "Split into date: $datePart, time: $timePart" }
                val (year, month, day) = datePart.split("-").map { it.toInt() }
                Logger.d { "Parsed date: year=$year, month=$month, day=$day" }
                val timeComponents = timePart.split(":")
                val hour = timeComponents[0].toInt()
                val minute = timeComponents[1].toInt()
                val second = if (timeComponents.size > 2) timeComponents[2].toInt() else 0
                Logger.d { "Parsed time: hour=$hour, minute=$minute, second=$second" }
                // Validate components
                require(month in 1..12) { "Invalid month: $month" }
                require(hour in 0..23) { "Invalid hour: $hour" }
                require(minute in 0..59) { "Invalid minute: $minute" }
                require(second in 0..59) { "Invalid second: $second" }
                // Validate date by constructing a LocalDate first
                val localDate = try {
                    LocalDate(year, month, day)
                } catch (e: Exception) {
                    throw IllegalArgumentException("Invalid date: year=$year, month=$month, day=$day", e)
                }
                Logger.d { "Validated LocalDate: $localDate" }
                val localDateTime = LocalDateTime(
                    year = year,
                    monthNumber = month,
                    dayOfMonth = day,
                    hour = hour,
                    minute = minute,
                    second = second,
                    nanosecond = 0
                )
                Logger.d { "Constructed LocalDateTime: $localDateTime" }
                localDateTime.toInstant(TimeZone.of("Asia/Kolkata")).also {
                    Logger.d { "Parsed as 'yyyy-MM-dd'T'HH:mm:ss': $it" }
                }
            } catch (e: Exception) {
                Logger.e { "Failed to parse Instant from '$string': ${e.message}" }
                throw IllegalArgumentException("Failed to parse Instant from '$string'", e)
            }
        }
    }
}