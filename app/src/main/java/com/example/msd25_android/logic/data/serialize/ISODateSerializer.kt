package com.example.msd25_android.logic.data.serialize


import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object ISODateSerializer: KSerializer<Long> {
    override val descriptor: SerialDescriptor =        PrimitiveSerialDescriptor("IsoDate", PrimitiveKind.STRING)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {        timeZone = TimeZone.getTimeZone("UTC")    }

    override fun serialize(encoder: Encoder, value: Long) {
        val dateString = dateFormat.format(Date(value))
        encoder.encodeString(dateString)
    }

    override fun deserialize(decoder: Decoder): Long {
        val dateString = decoder.decodeString()
        return dateFormat.parse(dateString)?.time
            ?: throw IllegalArgumentException("Invalid date format: $dateString")
    }
}