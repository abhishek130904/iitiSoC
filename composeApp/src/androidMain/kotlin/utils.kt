

package org.example.ui

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

lateinit var appContext: Context

 actual fun generateTravelPdf() {
    val pdf = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdf.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    var y = 40f
    fun line(text: String) {
        canvas.drawText(text, 40f, y, paint)
        y += 20f
    }

    line("Sample Travel Itinerary")
    line("Name: Mr. Abhishek")
    line("Trip: Himachal Pradesh (5N/6D)")
    line("Total: ₹58,900")

    pdf.finishPage(page)

    val file = File(appContext.getExternalFilesDir(null), "itinerary.pdf")
    pdf.writeTo(FileOutputStream(file))
    pdf.close()

    Toast.makeText(appContext, "PDF saved at: ${file.absolutePath}", Toast.LENGTH_LONG).show()
}
