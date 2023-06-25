package com.projek.iwanmotor.ui.kwitansi

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.data.kwitansi.Kwitansi
import com.projek.iwanmotor.databinding.FragmentTambahKwitansiBinding
import com.projek.iwanmotor.ui.barang.TambahBarangDirections
import com.projek.iwanmotor.utils.Utility.dateNow
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class TambahKwitansi : Fragment() {
    private var calendar = Calendar.getInstance()

    private val viewModel: KwitansiViewModel by activityViewModels {
        KwitansiViewModelFactory(
            (activity?.application as IwanMotorApplication).database.kwitansiDao()
        )
    }
    private val navigationArgs: DetailKwitansiArgs by navArgs()

    lateinit var kwitansi: Kwitansi
    private var _binding: FragmentTambahKwitansiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTambahKwitansiBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.penerimaTxt.toString(),
            binding.alamatTxt.toString(),
            binding.nohpTxt.toString(),
            binding.jumlahUangTxt.toString(),
            binding.pembayaranTxt.toString(),
            binding.typeTxt.toString(),
            binding.norangkaTxt.toString(),
            binding.nomesinTxt.toString(),
            binding.nopolTxt.toString(),
            binding.tahunTxt.toString(),
            binding.warnaTxt.toString(),
            binding.tglbeliTxt.toString()
        )
    }

    /**
     * Binds views with the passed in [barang] information.
     */

    private fun bind(item: Kwitansi) {
        val price = "%.2f".format(item.uangSejumlah)
        binding.apply {
            inputNamaPenerima.setText(item.diterima)
            inputAlamat.setText(item.alamat)
            inputNohp.setText(item.nohp)
            inputJumlah.setText(price, TextView.BufferType.SPANNABLE)
            inputPembayaran.setText(item.untukPembayaran)
            inputType.setText(item.type)
            inputNorangka.setText(item.noRangka)
            inputNomesin.setText(item.noMesin)
            inputNopol.setText(item.noPol)
            inputTahun.setText(item.tahun)
            inputWarna.setText(item.warna)
            inputTglPembelian.setText(item.tanggal)
            saveChangesBtn.setOnClickListener {
                updateItem()
            }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewKwitansi(
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
            val action = TambahKwitansiDirections.actionTambahKwitansiToNavigationKwitansi()
            findNavController().navigate(action)
        }
    }
    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.insertKwitansi(
                this.navigationArgs.itemId,
                binding.inputNamaPenerima.text.toString(),
                binding.inputAlamat.text.toString(),
                binding.inputNohp.text.toString().toInt(),
                binding.inputJumlah.text.toString().toDouble(),
                binding.inputPembayaran.text.toString(),
                binding.inputType.text.toString(),
                binding.inputNorangka.text.toString().toInt(),
                binding.inputNomesin.text.toString().toInt(),
                binding.inputNopol.text.toString(),
                binding.inputTahun.text.toString().toInt(),
                binding.inputWarna.text.toString(),
                binding.inputTglPembelian.text.toString(),
            )
            val action = TambahBarangDirections.actionTambahBarangToNavigationBarang()
            findNavController().navigate(action)
        }
    }
    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDatePicker()
//        setSpinner()
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveKwitansi(id).observe(this.viewLifecycleOwner) { selectedItem ->
                kwitansi = selectedItem
                bind(kwitansi)
            }
        } else {
            binding.saveChangesBtn.setOnClickListener {
                addNewItem()
            }
        }
        binding.tvDate.dateNow()
//        var total = (binding.inputHarga.text.toString().toInt() * binding.inputJumlah.text.toString().toInt())
//        binding.inputSubtotal.setText(total.toString())

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

}