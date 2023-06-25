package com.projek.iwanmotor.ui.kwitansi

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.projek.iwanmotor.databinding.FragmentDetailKwitansiBinding
import com.projek.iwanmotor.utils.Utility.dateNow

import java.io.File
import java.io.FileOutputStream
import java.text.DateFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailKwitansi : Fragment() {
    private val navigationArgs: DetailKwitansiArgs by navArgs()
    lateinit var kwitansi : Kwitansi
    private var calendar = Calendar.getInstance()
    private val viewModel: KwitansiViewModel by activityViewModels {
        KwitansiViewModelFactory(
            (activity?.application as IwanMotorApplication).database2.kwitansiDao()
        )
    }


    private var _binding: FragmentDetailKwitansiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailKwitansiBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(item: Kwitansi) {
//     val price = "%.2f".format(item.uangSejumlah)
//        val price2 = "%.2f".format(item.subtotal)

        binding.apply {
            inputNamaPenerima.setText(item.diterima)
            inputAlamat.setText(item.alamat)
            inputNohp.setText(item.nohp.toString())
            inputJumlah.setText(item.uangSejumlah.toString())
            inputPembayaran.setText(item.untukPembayaran)
            inputType.setText(item.type)
            inputNorangka.setText(item.noRangka.toString())
            inputNomesin.setText(item.noMesin.toString())
            inputNopol.setText(item.noPol.toString())
            inputTahun.setText(item.tahun.toString())
            inputWarna.setText(item.warna)
            inputTglPembelian.setText(item.tanggal)
            deleteBtn.setOnClickListener { viewModel.deleteKwitansi(kwitansi) }
            deleteBtn.setOnClickListener { showConfirmationDialog() }
            kwitansiFab.setOnClickListener{ exportToPdf() }
            addBtn.setOnClickListener { updateKwitansi() }
        }
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
            val headerCell = PdfPCell(Phrase("Kwitansi Penjualan Iwan Motor"))

            // Set properties for the header cell
            headerCell.backgroundColor = BaseColor.WHITE
            headerCell.colspan = 12 // Set the number of columns the cell should span
            headerCell.horizontalAlignment = Element.ALIGN_CENTER
            headerCell.verticalAlignment = Element.ALIGN_MIDDLE
            headerCell.paddingBottom = 10f

            val table = PdfPTable(12)
            table.setWidthPercentage(100f)

            // Add the header cell to the table
            table.addCell(headerCell)

            // Header table
            val headers = arrayOf(
                "Diterima", "Alamat", "No. Hp", "Uang Sejumlah",
                "Untuk Pembayaran", "Type", "No Rangka", "No. Mesin",
                "No. Pol", "Tahun", "Warna", "Tanggal"
            )
            for (header in headers) {
                val cell = PdfPCell(Phrase(header))
                table.addCell(cell)
            }

            // Add kwitansi data to the table
            table.addCell(binding.inputNamaPenerima.text.toString())
            table.addCell(binding.inputAlamat.text.toString())
            table.addCell(binding.inputNohp.text.toString())
            table.addCell(binding.inputJumlah.text.toString())
            table.addCell(binding.inputPembayaran.text.toString())
            table.addCell(binding.inputType.text.toString())
            table.addCell(binding.inputNorangka.text.toString())
            table.addCell(binding.inputNomesin.text.toString())
            table.addCell(binding.inputNopol.text.toString())
            table.addCell(binding.inputTahun.text.toString())
            table.addCell(binding.inputWarna.text.toString())
            table.addCell(binding.inputTglPembelian.text.toString())

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

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal mengekspor data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateKwitansi() {
        if (isEntryValid()) {
            viewModel.updateKwitansi(
                this.navigationArgs.itemId,
                binding.inputNamaPenerima.text.toString(),
                binding.inputAlamat.text.toString(),
                binding.inputNohp.text.toString(),
                binding.inputJumlah.text.toString(),
                binding.inputPembayaran.text.toString(),
                binding.inputType.text.toString(),
                binding.inputNorangka.text.toString(),
                binding.inputNomesin.text.toString(),
                binding.inputNopol.text.toString(),
                binding.inputTahun.text.toString(),
                binding.inputWarna.text.toString(),
                binding.inputTglPembelian.text.toString()
            )
            val action = DetailKwitansiDirections.actionDetailKwitansiToNavigationKwitansi()
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.inputNamaPenerima.text.toString(),
            binding.inputAlamat.text.toString(),
            binding.inputNohp.text.toString(),
            binding.inputJumlah.text.toString(),
            binding.inputPembayaran.text.toString(),
            binding.inputType.text.toString(),
            binding.inputNorangka.text.toString(),
            binding.inputNomesin.text.toString(),
            binding.inputNopol.text.toString(),
            binding.inputTahun.text.toString(),
            binding.inputWarna.text.toString(),
            binding.inputTglPembelian.text.toString(),
        )
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        viewModel.deleteKwitansi(kwitansi)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveKwitansi(id).observe(this.viewLifecycleOwner) { selectedItem ->
            kwitansi = selectedItem
            bind(kwitansi)
        }
        setUpDatePicker()
        binding.tvDate.dateNow()

    }
    private fun updateLabel() {
        val formatter = SimpleDateFormat("d MMMM y", Locale.US)
        binding.inputTglPembelian.setText(formatter.format(calendar.time))
    }

    private fun setUpDatePicker() {
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        binding.inputTglPembelian.setOnClickListener {
            val months = DateFormatSymbols.getInstance().months
            val dateNow = binding.inputTglPembelian.text.toString()
            val splitDate = dateNow.split(" ")
            if (splitDate.size == 3) {
                DatePickerDialog(
                    requireActivity(), date, splitDate[2].toInt(), months.indexOf(splitDate[1]),
                    splitDate[0].toInt()
                ).show()
            } else {
                DatePickerDialog(
                    requireActivity(),
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}