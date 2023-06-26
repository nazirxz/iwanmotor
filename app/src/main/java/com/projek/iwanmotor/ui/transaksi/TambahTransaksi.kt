package com.projek.iwanmotor.ui.transaksi

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import android.widget.ArrayAdapter
import android.widget.Toast
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.databinding.FragmentTambahTransaksiBinding
import com.projek.iwanmotor.ui.barang.BarangViewModel
import com.projek.iwanmotor.ui.barang.BarangViewModelFactory
import com.projek.iwanmotor.ui.barang.TambahBarangDirections
import com.projek.iwanmotor.utils.Utility.dateNow
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
    private var namaProduk = "Nama Produk"


    private val viewModel: TransaksiViewModel by activityViewModels {
       TransaksiViewModelFactory(
            (activity?.application as IwanMotorApplication).database.transaksiDao()
        )
    }
    private val viewModel2: BarangViewModel by activityViewModels {
        BarangViewModelFactory(
            (activity?.application as IwanMotorApplication).database.barangDao()
        )
    }
    private val navigationArgs: DetailTransaksiArgs by navArgs()

    lateinit var transaksi: Transaksi
    private var _binding: FragmentTambahTransaksiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTambahTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.namaProdukTxt.selectedItem.toString(),
//            binding.namaProdukTxt.toString(),
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
            namaProdukTxt.onItemSelectedListener
            inputHarga.setText(price, TextView.BufferType.SPANNABLE)
            inputJumlah.setText(item.jumlah.toString(), TextView.BufferType.SPANNABLE)
            inputSubtotal.setText(price2, TextView.BufferType.SPANNABLE)
            inputTglPembelian.setText(item.tglPembelian)
            saveChangesBtn.setOnClickListener {
                updateItem()
            }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        val stok = viewModel2.getStok(binding.namaProdukTxt.selectedItem.toString())
        val available = viewModel2.isAvailable(binding.inputJumlah.text.toString(),binding.namaProdukTxt.selectedItem.toString())
        if (isEntryValid()) {
            if (stok) {
                Log.d("Status",stok.toString())
                Log.d("Status isAvailable",available.toString())
                if (available==true) {
                    viewModel.addNewTransaksi(
                        randomString(6),
                        binding.namaProdukTxt.selectedItem.toString(),
                        binding.inputHarga.text.toString(),
                        binding.inputJumlah.text.toString(),
                        binding.inputSubtotal.text.toString(),
                        binding.inputTglPembelian.text.toString()
                    )
                    viewModel2.updateStok(binding.namaProdukTxt.selectedItem.toString(),binding.inputJumlah.text.toString())
                    val action = TambahTransaksiDirections.actionTambahTransaksiToNavigationTransaksi()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Stok barang tidak cukup", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Stok barang tidak tersedia", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.insertTransaksi(
                this.navigationArgs.itemId,
                randomString(6),
                binding.namaProdukTxt.selectedItem.toString(),
//                binding.namaProdukTxt.toString(),
                binding.inputHarga.text.toString(),
                binding.inputJumlah.text.toString(),
                binding.inputSubtotal.text.toString(),
                binding.inputTglPembelian.text.toString()
            )
            val action = TambahBarangDirections.actionTambahBarangToNavigationBarang()
            findNavController().navigate(action)
        }
    }
    fun randomString(stringLength: Int): String {
        val list = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray()
        var randomS = ""
        for (i in 1..stringLength) {
            randomS += list[getRandomNumber(0, list.size - 1)]
        }
        return randomS
    }

    fun getRandomNumber(min: Int, max: Int): Int {
        return Random().nextInt((max - min) + 1) + min
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
            viewModel.retrievetransaksi(id).observe(this.viewLifecycleOwner) { selectedItem ->
                transaksi = selectedItem
                bind(transaksi)
            }
        } else {
            binding.saveChangesBtn.setOnClickListener {
                addNewItem()
            }
        }
        setSpinner()
//        binding.tvDate.dateNow()
//        var total = (binding.inputHarga.text.toString().toInt() * binding.inputJumlah.text.toString().toInt())
//        binding.inputSubtotal.setText(total.toString())

    }
    private fun updateLabel() {
        val formatter = SimpleDateFormat("d MMMM y", Locale.US)
        binding.inputTglPembelian.setText(formatter.format(calendar.time))
    }

    private fun setSpinner() {

        // Display the namaProduk values
        viewModel2.getAllNamaProduk().observe(viewLifecycleOwner) {
            Log.d("total nama produk", it.toString())
            // Make sure that the it variable is a List<Barang>
            val barangList = it

            val namaProdukList = barangList.map { it.namaProduk }

            // Use the ArrayList as the data for the ArrayAdapter
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                namaProdukList
            )
            Log.d("total nama produk", namaProdukList.toString())

            binding.namaProdukTxt.apply {
                adapter = arrayAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        namaProduk = namaProdukList[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
            binding.namaProdukTxt.adapter = arrayAdapter

        }
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