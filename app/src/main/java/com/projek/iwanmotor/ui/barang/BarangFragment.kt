package com.projek.iwanmotor.ui.barang

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.databinding.FragmentBarangBinding
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*


class BarangFragment : Fragment() {
    lateinit var item: Barang
    private val viewModel: BarangViewModel by activityViewModels {
        BarangViewModelFactory(
            (activity?.application as IwanMotorApplication).database.barangDao()
        )
    }

    private var _binding: FragmentBarangBinding? = null
    private val binding get() = _binding as FragmentBarangBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BarangListAdapter {
            val action =
                BarangFragmentDirections.actionNavigationBarangToEditBarang(it.id)
            this.findNavController().navigate(action)
        }
//        binding.tvDate.dateNow()
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.addExportpdf.setOnClickListener {
            exportToPdf()

            Toast.makeText(requireContext(), "PDF exported", Toast.LENGTH_SHORT).show()
        }

        binding.addBarang.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_barang_to_tambahBarang)
        }

        // Mendapatkan referensi ke FAB utama
        val addFab: FloatingActionButton = binding.addFab

        // Mendapatkan referensi ke FAB dan TextView lainnya
        val addBarangFab: FloatingActionButton = binding.addBarang
        val addTambahBarangText: TextView = binding.addTambahbarang
        val addExportPdfFab: FloatingActionButton = binding.addExportpdf
        val exportPdfText: TextView = binding.exportpdf

// Menyembunyikan FAB dan TextView lainnya saat awalnya
        addBarangFab.hide()
        addTambahBarangText.visibility = View.GONE
        addExportPdfFab.hide()
        exportPdfText.visibility = View.GONE

// Menambahkan fungsi klik untuk FAB utama
        addFab.setOnClickListener {
            // Mengecek visibilitas FAB dan TextView lainnya
            if (addBarangFab.isOrWillBeHidden) {
                // Menampilkan FAB dan TextView lainnya
                addBarangFab.show()
                addTambahBarangText.visibility = View.VISIBLE
                addExportPdfFab.show()
                exportPdfText.visibility = View.VISIBLE
            } else {
                // Menyembunyikan FAB dan TextView lainnya
                addBarangFab.hide()
                addTambahBarangText.visibility = View.GONE
                addExportPdfFab.hide()
                exportPdfText.visibility = View.GONE
            }
        }
    }
    private fun exportToPdf() {
        val filePath = requireContext().getExternalFilesDir(null)?.absolutePath
        val fileName = "barang_data.pdf"
        val file = File(filePath, fileName)

        try {
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Create a new cell for the header title
            val headerCell = PdfPCell(Phrase("Data Barang Iwan Motor"))

            // Set properties for the header cell
            headerCell.backgroundColor = BaseColor.WHITE
            headerCell.colspan = 6 // Set the number of columns the cell should span
            headerCell.horizontalAlignment = Element.ALIGN_CENTER
            headerCell.verticalAlignment = Element.ALIGN_MIDDLE
            headerCell.paddingBottom = 10f

            val table = PdfPTable(6) // Adjust the number of columns based on the attributes in Barang

            // Add the header cell to the table
            table.addCell(headerCell)

            // Header table
            val headers = arrayOf("Id","Nama Produk", "Stok", "Harga Modal", "Harga Jual", "Tanggal Masuk")
            for (header in headers) {
                val cell = PdfPCell(Phrase(header))
                table.addCell(cell)
            }

            // Retrieve data from getAllNamaProduk LiveData
            viewModel.getAllNamaProduk().observe(viewLifecycleOwner) { items ->

                items?.let {
                    for (barang in it) {
                        fun getFormattedPrice(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(barang.hargaModal)
                        fun getFormattedPrice2(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(barang.hargaJual)
                        // Add Barang data to the table
                        table.addCell(barang.id.toString())
                        table.addCell(barang.namaProduk)
                        table.addCell(barang.stok.toString())
                        table.addCell(getFormattedPrice())
                        table.addCell(getFormattedPrice2())
                        table.addCell(barang.tglMasuk)
                    }
                }
                document.add(table)
                document.close()

                Toast.makeText(requireContext(), "Data berhasil diekspor ke pdf", Toast.LENGTH_SHORT).show()

                // Generate content URI for the file using FileProvider
                val fileUri = FileProvider.getUriForFile(requireContext(), "com.projek.iwanmotor.fileprovider", file)

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