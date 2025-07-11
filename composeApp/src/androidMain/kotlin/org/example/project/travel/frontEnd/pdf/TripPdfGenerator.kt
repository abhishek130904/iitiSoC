package org.example.project.travel.frontEnd.pdf

import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

actual fun generateTripSummaryPdf(trip: TripSummary): ByteArray {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    var y = 40
    val paint = android.graphics.Paint().apply { textSize = 16f }

    fun drawLine(text: String) {
        canvas.drawText(text, 40f, y.toFloat(), paint)
        y += 30
    }

    drawLine("Trip Summary")
    drawLine("Destination: ${trip.destination}")
    drawLine("Dates: ${trip.dates}")
    drawLine("Flight: ${trip.flightDetails}")
    drawLine("Hotel: ${trip.hotelDetails}")
    drawLine("Activities: ${trip.activities}")
    drawLine("Meals: ${trip.meals}")
    drawLine("Cost: ${trip.costBreakdown}")
    trip.notes?.let { drawLine("Notes: $it") }

    document.finishPage(page)

    val outputStream = ByteArrayOutputStream()
    document.writeTo(outputStream)
    document.close()
    return outputStream.toByteArray()
} 