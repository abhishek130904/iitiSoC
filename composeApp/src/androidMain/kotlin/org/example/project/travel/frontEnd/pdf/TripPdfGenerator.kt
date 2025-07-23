package org.example.project.travel.frontEnd.pdf

import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.RectF
import java.io.InputStream

actual fun generateTripSummaryPdf(trip: TripSummary): ByteArray {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 1100, 1).create() // A4+ size, taller
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    var y = 40
    val titlePaint = android.graphics.Paint().apply {
        textSize = 24f
        isFakeBoldText = true
        color = android.graphics.Color.rgb(23, 111, 243)
    }
    val sectionPaint = android.graphics.Paint().apply {
        textSize = 18f
        isFakeBoldText = true
        color = android.graphics.Color.rgb(23, 111, 243)
    }
    val labelPaint = android.graphics.Paint().apply {
        textSize = 16f
        isFakeBoldText = true
        color = android.graphics.Color.BLACK
    }
    val valuePaint = android.graphics.Paint().apply {
        textSize = 16f
        color = android.graphics.Color.DKGRAY
    }
    val dividerPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.LTGRAY
        strokeWidth = 2f
    }

    // Draw app logo at the top center (load from assets for reliability)
    try {
        val contextField = canvas.javaClass.getDeclaredField("mContext")
        contextField.isAccessible = true
        val context = contextField.get(canvas) as android.content.Context
        val assetManager = context.assets
        val inputStream = assetManager.open("logo.png")
        val logoBitmap = BitmapFactory.decodeStream(inputStream)
        val logoWidth = 80
        val logoHeight = 80
        val xLogo = (595 - logoWidth) / 2f
        canvas.drawBitmap(logoBitmap, null, RectF(xLogo, y.toFloat(), xLogo + logoWidth, y + logoHeight.toFloat()), null)
        y += logoHeight + 20
        inputStream.close()
    } catch (e: Exception) {
        // Logo not found or error, skip drawing logo
        y += 20
    }

    // Draw colored title background
    val titleBgPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.rgb(23, 111, 243)
    }
    canvas.drawRoundRect(30f, y.toFloat(), 565f, (y + 50).toFloat(), 20f, 20f, titleBgPaint)
    canvas.drawText("Trip Summary", 60f, y + 36f, titlePaint.apply { color = android.graphics.Color.WHITE })
    y += 70

    // Main content area with rounded border (visual only)
    val borderPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.rgb(200, 220, 255)
        style = android.graphics.Paint.Style.STROKE
        strokeWidth = 4f
    }
    canvas.drawRoundRect(35f, y.toFloat(), 560f, 1050f, 18f, 18f, borderPaint)
        y += 30

    fun drawSectionHeader(text: String) {
        canvas.drawText(text, 50f, y.toFloat(), sectionPaint)
        y += 32
        canvas.drawLine(50f, y.toFloat(), 545f, y.toFloat(), dividerPaint)
        y += 22
    }
    fun drawLabelValue(label: String, value: String, forceInt: Boolean = false) {
        val maxWidth = 480f
        val displayValue = if (forceInt) value.replace(Regex("([0-9]+)\\.[0-9]+"), "$1") else value
        val lines = wrapText(displayValue, valuePaint, maxWidth)
        canvas.drawText(label, 60f, y.toFloat(), labelPaint)
        y += 24
        for (line in lines) {
            canvas.drawText(line, 80f, y.toFloat(), valuePaint)
            y += 24
        }
        y += 10
    }

    fun drawCostLine(text: String) {
        val maxWidth = 480f
        val lines = wrapText(text, valuePaint, maxWidth)
        for (line in lines) {
            canvas.drawText(line, 80f, y.toFloat(), valuePaint)
            y += 18 // tighter spacing for cost lines
        }
    }

    drawLabelValue("Destination:", trip.destination)
    drawLabelValue("Dates:", trip.dates)

    drawSectionHeader("Transport")
    drawLabelValue("Details:", trip.flightDetails)

    drawSectionHeader("Accommodation")
    drawLabelValue("Hotel:", trip.hotelDetails, forceInt = true)

    drawSectionHeader("Activities")
    drawLabelValue("Activities:", trip.activities)

    drawSectionHeader("Meals")
    drawLabelValue("Meals:", trip.meals)

    drawSectionHeader("Cost Breakdown")
    // Remove the old drawLabelValue for cost
    // drawLabelValue("Cost:", trip.costBreakdown)
    // Split cost breakdown by comma and draw each on a new line with compact spacing
    trip.costBreakdown.split(",").forEach { item ->
        drawCostLine(item.trim())
    }
    y += 18 // add a bit more space after cost breakdown before notes

    if (!trip.notes.isNullOrBlank()) {
        drawSectionHeader("Notes")
        drawLabelValue("", trip.notes)
    }

    document.finishPage(page)

    val outputStream = ByteArrayOutputStream()
    document.writeTo(outputStream)
    document.close()
    return outputStream.toByteArray()
}

// Helper function to wrap text for PDF
fun wrapText(text: String, paint: android.graphics.Paint, maxWidth: Float): List<String> {
    val words = text.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = ""
    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) <= maxWidth) {
            currentLine = testLine
        } else {
            lines.add(currentLine)
            currentLine = word
        }
    }
    if (currentLine.isNotEmpty()) lines.add(currentLine)
    return lines
} 