package com.projek.iwanmotor.ui.transaksi

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.projek.iwanmotor.R
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.databinding.FragmentDetailTransaksiBinding
import com.projek.iwanmotor.ui.barang.BarangViewModel
import com.projek.iwanmotor.ui.barang.BarangViewModelFactory
import com.projek.iwanmotor.ui.barang.EditBarangArgs
import com.projek.iwanmotor.ui.barang.EditBarangDirections
import com.projek.iwanmotor.utils.Utility.dateNow
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DetailTransaksi : Fragment() {
    private val navigationArgs: DetailTransaksiArgs by navArgs()
    lateinit var transaksi: Transaksi
    private var calendar = Calendar.getInstance()
    private val viewModel: TransaksiViewModel by activityViewModels {
        TransaksiViewModelFactory(
            (activity?.application as IwanMotorApplication).database2.transaksiDao()
        )
    }
    private val viewModelBarang: BarangViewModel by activityViewModels {
        BarangViewModelFactory(
            (activity?.application as IwanMotorApplication).database.barangDao()
        )
    }


    private var _binding: FragmentDetailTransaksiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(item: Transaksi) {
//        val price = "%.2f".format(item.harga)
//        val price2 = "%.2f".format(item.subtotal)

        binding.apply {
            inputNamaProduk.setText(transaksi.namaProduk)
            inputHarga.setText(transaksi.harga.toString())
            inputJumlah.setText(transaksi.jumlah.toString())
            inputSubtotal.setText(transaksi.subtotal.toString())
            inputTglPembelian.setText(transaksi.tglPembelian)
            deleteBtn.setOnClickListener { viewModel.deletetransaksi(transaksi) }
            deleteBtn.setOnClickListener { showConfirmationDialog() }
            addBtn.setOnClickListener { updateTransaksi() }
        }
    }
    private fun updateTransaksi() {
        if (isEntryValid()) {
            viewModel.updateTransaksi(
                this.navigationArgs.itemId,
                transaksi.invoice,
                binding.inputNamaProduk.text.toString(),
                binding.inputHarga.text.toString(),
                binding.inputJumlah.text.toString(),
                binding.inputSubtotal.text.toString(),
                binding.inputTglPembelian.text.toString()
            )
            val action = DetailTransaksiDirections.actionDetailTransaksiToNavigationTransaksi()
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.inputNamaProduk.text.toString(),
            binding.inputHarga.text.toString(),
            binding.inputJumlah.text.toString(),
            binding.inputSubtotal.text.toString(),
            binding.inputTglPembelian.text.toString()
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
        viewModel.deletetransaksi(transaksi)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrievetransaksi(id).observe(this.viewLifecycleOwner) { selectedItem ->
            transaksi = selectedItem
            bind(transaksi)
        }
        binding.tvDate.dateNow()
        setUpDatePicker()
//        binding.inputInvoice.isClickable = false
//        binding.inputInvoice.isFocusable = false
        binding.inputNamaProduk.isClickable = false
        binding.inputNamaProduk.isFocusable = false
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