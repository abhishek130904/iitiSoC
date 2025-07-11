package org.example.project.travel.frontEnd.pdf

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider
import android.content.Intent

actual fun saveTripSummaryPdfFile(context: Any, pdfBytes: ByteArray, fileName: String) {
    val ctx = context as Context
    val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloads, fileName)
    // Open the PDF
    val uri = FileProvider.getUriForFile(
        ctx,
        "org.example.project.travel.fileprovider",
        file
    )
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        ctx.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(ctx, "No PDF viewer found.", Toast.LENGTH_SHORT).show()
    }
} 