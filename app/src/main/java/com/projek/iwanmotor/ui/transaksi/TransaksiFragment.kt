package com.projek.iwanmotor.ui.transaksi

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
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
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.databinding.FragmentTransaksiBinding
import com.projek.iwanmotor.utils.Utility.dateNow
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*


class TransaksiFragment : Fragment() {
    lateinit var transaksi: Transaksi
    private val viewModel: TransaksiViewModel by activityViewModels {
        TransaksiViewModelFactory(
            (activity?.application as IwanMotorApplication).database2.transaksiDao()
        )
    }

    private var _binding:FragmentTransaksiBinding? = null
    private val binding get() = _binding as FragmentTransaksiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TransaksiListAdapter {
            val action =
                TransaksiFragmentDirections.actionNavigationTransaksiToDetailTransaksi(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        viewModel.alltransaksis.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
            Log.d("Data Semua", items.toString())
        }
        binding.addTransaksi.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_transaksi_to_tambahTransaksi)
        }
        binding.addExportpdf.setOnClickListener {
            exportToPdf()

            Toast.makeText(requireContext(), "PDF berhasil diekspor", Toast.LENGTH_SHORT).show()
        }
        // Mendapatkan referensi ke FAB utama
        val addFab: FloatingActionButton = binding.addFab

        // Mendapatkan referensi ke FAB dan TextView lainnya
        val addTransaksiFab: FloatingActionButton = binding.addTransaksi
        val addTambahTransaksiText: TextView = binding.addTambahtransaksi
        val addExportPdfFab: FloatingActionButton = binding.addExportpdf
        val exportPdfText: TextView = binding.exportpdf

// Menyembunyikan FAB dan TextView lainnya saat awalnya
        addTransaksiFab.hide()
        addTambahTransaksiText.visibility = View.GONE
        addExportPdfFab.hide()
        exportPdfText.visibility = View.GONE

// Menambahkan fungsi klik untuk FAB utama
        addFab.setOnClickListener {
            // Mengecek visibilitas FAB dan TextView lainnya
            if (addTransaksiFab.isOrWillBeHidden) {
                // Menampilkan FAB dan TextView lainnya
                addTransaksiFab.show()
                addTambahTransaksiText.visibility = View.VISIBLE
                addExportPdfFab.show()
                exportPdfText.visibility = View.VISIBLE
            } else {
                // Menyembunyikan FAB dan TextView lainnya
                addTransaksiFab.hide()
                addTambahTransaksiText.visibility = View.GONE
                addExportPdfFab.hide()
                exportPdfText.visibility = View.GONE
            }
        }
        binding.tvDate.dateNow()
    }
    private fun exportToPdf() {
        val filePath = requireContext().getExternalFilesDir(null)?.absolutePath
        val fileName = "transaksi_data.pdf"
        val file = File(filePath, fileName)

        try {
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Create a new cell for the header title
            val headerCell = PdfPCell(Phrase("Data Transaksi Iwan Motor"))

            // Set properties for the header cell
            headerCell.backgroundColor = BaseColor.WHITE
            headerCell.colspan = 7 // Set the number of columns the cell should span
            headerCell.horizontalAlignment = Element.ALIGN_CENTER
            headerCell.verticalAlignment = Element.ALIGN_MIDDLE
            headerCell.paddingBottom = 10f

            val table = PdfPTable(7) // Adjust the number of columns based on the attributes in transaksi

            // Add the header cell to the table
            table.addCell(headerCell)

            // Header table
            val headers = arrayOf("Id","Invoice", "Nama Produk", "Harga", "Jumlah", "Sub Total","Tanggal Pembelian")
            for (header in headers) {
                val cell = PdfPCell(Phrase(header))
                table.addCell(cell)
            }

            // Retrieve data from getAllNamaProduk LiveData
            viewModel.alltransaksis.observe(viewLifecycleOwner) { items ->
                items?.let {
                    for (transaksi in it) {
                        fun getFormattedPrice(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(transaksi.harga)
                        fun getFormattedPrice2(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(transaksi.subtotal)
                        // Add transaksi data to the table
                        table.addCell(transaksi.id.toString())
                        table.addCell(transaksi.invoice)
                        table.addCell(transaksi.namaProduk)
                        table.addCell(getFormattedPrice())
                        table.addCell(transaksi.jumlah.toString())
                        table.addCell(getFormattedPrice2())
                        table.addCell(transaksi.tglPembelian)
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
