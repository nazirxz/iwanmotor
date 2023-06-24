package com.projek.iwanmotor.utils

import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.projek.iwanmotor.data.barang.BarangDao
import java.io.FileOutputStream

class pdfexporterbarang {
    companion object {
        fun exportToPdf(data: List<BarangDao>, filePath: String) {
            val document = Document()
            try {
                PdfWriter.getInstance(document, FileOutputStream(filePath))

                document.open()

                for (entity in data) {
                    document.add(Paragraph(entity.toString()))
                }

                document.close()
            } catch (e: DocumentException) {
                e.printStackTrace()
            }
        }
    }
}