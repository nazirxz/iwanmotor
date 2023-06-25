package com.projek.iwanmotor.ui.kwitansi

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.projek.iwanmotor.R
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.data.kwitansi.Kwitansi
import com.projek.iwanmotor.databinding.FragmentKwitansiBinding
import com.projek.iwanmotor.utils.Utility.dateNow
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*

class KwitansiFragment: Fragment() {
    lateinit var kwitansi: Kwitansi
    private val viewModel: KwitansiViewModel by activityViewModels {
        KwitansiViewModelFactory(
            (activity?.application as IwanMotorApplication).database2.kwitansiDao()
        )
    }

    private var _binding: FragmentKwitansiBinding? = null
    private val binding get() = _binding as FragmentKwitansiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKwitansiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = KwitansiListAdapter {
            val action =
                KwitansiFragmentDirections.actionNavigationKwitansiToDetailKwitansi(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        viewModel.allKwitansi.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
            Log.d("Data Semua", items.toString())
        }
        binding.addKwitansi.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_kwitansi_to_tambahKwitansi)
        }
        binding.addExportpdf.setOnClickListener {
            exportToPdf()

            Toast.makeText(requireContext(), "PDF berhasil diekspor", Toast.LENGTH_SHORT).show()
        }
        // Mendapatkan referensi ke FAB utama
        val addFab: FloatingActionButton = binding.addFab

        // Mendapatkan referensi ke FAB dan TextView lainnya
        val addKwitansiFab: FloatingActionButton = binding.addKwitansi
        val addTambahKwitansiText: TextView = binding.addTambahkwitansi
        val addExportPdfFab: FloatingActionButton = binding.addExportpdf
        val exportPdfText: TextView = binding.exportpdf

// Menyembunyikan FAB dan TextView lainnya saat awalnya
        addKwitansiFab.hide()
        addTambahKwitansiText.visibility = View.GONE
        addExportPdfFab.hide()
        exportPdfText.visibility = View.GONE

// Menambahkan fungsi klik untuk FAB utama
        addFab.setOnClickListener {
            // Mengecek visibilitas FAB dan TextView lainnya
            if (addKwitansiFab.isOrWillBeHidden) {
                // Menampilkan FAB dan TextView lainnya
                addKwitansiFab.show()
                addTambahKwitansiText.visibility = View.VISIBLE
                addExportPdfFab.show()
                exportPdfText.visibility = View.VISIBLE
            } else {
                // Menyembunyikan FAB dan TextView lainnya
                addKwitansiFab.hide()
                addTambahKwitansiText.visibility = View.GONE
                addExportPdfFab.hide()
                exportPdfText.visibility = View.GONE
            }
        }
        binding.tvDate.dateNow()
    }
    private fun exportToPdf() {
        val filePath = requireContext().getExternalFilesDir(null)?.absolutePath
        val fileName = "kwitansi_data.pdf"
        val file = File(filePath, fileName)

        try {
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Create a new cell for the header title
            val headerCell = PdfPCell(Phrase("Data Kwitansi Iwan Motor"))

            // Set properties for the header cell
            headerCell.backgroundColor = BaseColor.WHITE
            headerCell.colspan = 13 // Set the number of columns the cell should span
            headerCell.horizontalAlignment = Element.ALIGN_CENTER
            headerCell.verticalAlignment = Element.ALIGN_MIDDLE
            headerCell.paddingBottom = 10f


            val table = PdfPTable(13)

            table.setWidthPercentage(100f)


            // Add the header cell to the table
            table.addCell(headerCell)

            // Header table
            val headers = arrayOf(
                "Id", "Diterima", "Alamat", "No. Hp", "Uang Sejumlah",
                "Untuk Pembayaran", "Type", "No Rangka", "No. Mesin",
                "No. Pol", "Tahun", "Warna", "Tanggal"
            )
            for (header in headers) {
                val cell = PdfPCell(Phrase(header))
                table.addCell(cell)
            }

            // Retrieve data from getAllNamaProduk LiveData
            viewModel.allKwitansi.observe(viewLifecycleOwner) { items ->
                items?.let {
                    for (kwitansi in it) {
                        fun getFormattedPrice(): String =
                            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(kwitansi.uangSejumlah)

                        // Add kwitansi data to the table
                        table.addCell(kwitansi.id.toString())
                        table.addCell(kwitansi.diterima)
                        table.addCell(kwitansi.alamat)
                        table.addCell(kwitansi.nohp.toString())
                        table.addCell(getFormattedPrice())
                        table.addCell(kwitansi.untukPembayaran)
                        table.addCell(kwitansi.type)
                        table.addCell(kwitansi.noRangka.toString())
                        table.addCell(kwitansi.noMesin.toString())
                        table.addCell(kwitansi.noPol.toString())
                        table.addCell(kwitansi.tahun.toString())
                        table.addCell(kwitansi.warna)
                        table.addCell(kwitansi.tanggal)
                    }
                }
                document.add(table)
                document.close()

                Toast.makeText(requireContext(), "Data berhasil diekspor ke pdf", Toast.LENGTH_SHORT).show()

                // Generate content URI for the file using FileProvider
                val fileUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.projek.iwanmotor.fileprovider",
                    file
                )

                // Open the PDF file using an appropriate PDF viewer app
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fileUri, "application/pdf")
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "Aplikasi pdf viewer tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal mengekspor data", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}