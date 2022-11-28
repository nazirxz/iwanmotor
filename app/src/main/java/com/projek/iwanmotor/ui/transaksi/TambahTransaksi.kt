package com.projek.iwanmotor.ui.transaksi

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projek.iwanmotor.R
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.databinding.FragmentDetailTransaksiBinding
import com.projek.iwanmotor.databinding.FragmentTambahTransaksiBinding
import com.projek.iwanmotor.ui.barang.BarangViewModel
import com.projek.iwanmotor.ui.barang.BarangViewModelFactory
import com.projek.iwanmotor.ui.barang.EditBarangArgs
import com.projek.iwanmotor.ui.barang.TambahBarangDirections
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TambahTransaksi.newInstance] factory method to
 * create an instance of this fragment.
 */
class TambahTransaksi : Fragment() {
    private var calendar = Calendar.getInstance()

    private val viewModel: TransaksiViewModel by activityViewModels {
       TransaksiViewModelFactory(
            (activity?.application as IwanMotorApplication).database.transaksiDao()
        )
    }
    private val navigationArgs: DetailTransaksiArgs by navArgs()

    lateinit var transaksi: Transaksi
    private var _binding: FragmentTambahTransaksiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTambahTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.inputNamaProduk.text.toString(),
            binding.inputHarga.text.toString(),
            binding.inputJumlah.text.toString(),
            binding.inputSubtotal.text.toString(),
            binding.inputTglPembelian.text.toString()
        )
    }
    /**
     * Binds views with the passed in [barang] information.
     */
    private fun bind(item: Transaksi) {
        val price = "%.2f".format(item.harga)
        val price2 = "%.2f".format(item.subtotal)

        binding.apply {
            inputNamaProduk.setText(item.namaProduk, TextView.BufferType.SPANNABLE)
            inputHarga.setText(price, TextView.BufferType.SPANNABLE)
            inputJumlah.setText(item.jumlah.toString(), TextView.BufferType.SPANNABLE)
            inputSubtotal.setText(price2, TextView.BufferType.SPANNABLE)
            inputTglPembelian.setText(item.tglPembelian)
            saveChangesBtn.setOnClickListener { updateItem() }
        }
    }
    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewTransaksi(
                invoice(6),
                binding.inputNamaProduk.text.toString(),
                binding.inputHarga.text.toString(),
                binding.inputJumlah.text.toString(),
                binding.inputSubtotal.text.toString(),
                binding.inputTglPembelian.text.toString()
            )
            val action = TambahTransaksiDirections.actionTambahTransaksiToNavigationTransaksi()
            findNavController().navigate(action)
        }
    }
    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.insertTransaksi(
                this.navigationArgs.itemId,
                invoice(6),
                binding.inputNamaProduk.text.toString(),
                binding.inputHarga.text.toString(),
                binding.inputJumlah.text.toString(),
                binding.inputSubtotal.text.toString(),
                binding.inputTglPembelian.text.toString()
            )
            val action = TambahBarangDirections.actionTambahBarangToNavigationBarang()
            findNavController().navigate(action)
        }
    }
    fun invoice(desiredStrLength: Int): String {
        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

        return (1..desiredStrLength)
            .map{ kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
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
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrievetransaksi(id).observe(this.viewLifecycleOwner) { selectedItem ->
                transaksi = selectedItem
                bind(transaksi)
            }
        } else {
            binding.saveChangesBtn.setOnClickListener {
                addNewItem()
            }
        }
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