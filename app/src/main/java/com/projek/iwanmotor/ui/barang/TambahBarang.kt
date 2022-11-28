package com.projek.iwanmotor.ui.barang

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projek.iwanmotor.R
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.databinding.FragmentTambahBarangBinding
import com.projek.iwanmotor.utils.Utility.dateNow
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class TambahBarang : Fragment() {
    private var calendar = Calendar.getInstance()

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: BarangViewModel by activityViewModels {
        BarangViewModelFactory(
            (activity?.application as IwanMotorApplication).database.barangDao()
        )
    }

     private val navigationArgs: EditBarangArgs by navArgs()

    lateinit var item: Barang

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentTambahBarangBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTambahBarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.inputNamaProduk.text.toString(),
            binding.inputHargaModal.text.toString(),
            binding.inputHargaJual.text.toString(),
            binding.inputStok.text.toString(),
            binding.inputTglMasuk.text.toString()
        )
    }

    /**
     * Binds views with the passed in [barang] information.
     */
    private fun bind(item: Barang) {
        val price = "%.2f".format(item.hargaJual)
        val price2 = "%.2f".format(item.hargaModal)

        binding.apply {
            inputNamaProduk.setText(item.namaProduk, TextView.BufferType.SPANNABLE)
            inputHargaModal.setText(price, TextView.BufferType.SPANNABLE)
            inputHargaJual.setText(price2, TextView.BufferType.SPANNABLE)
            inputStok.setText(item.stok.toString(), TextView.BufferType.SPANNABLE)
            inputTglMasuk.setText(item.tglMasuk)
            saveChangesBtn.setOnClickListener { updateItem() }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.inputNamaProduk.text.toString(),
                binding.inputHargaModal.text.toString(),
                binding.inputHargaJual.text.toString(),
                binding.inputStok.text.toString(),
                binding.inputTglMasuk.text.toString()
            )
            val action = TambahBarangDirections.actionTambahBarangToNavigationBarang()
            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                binding.inputNamaProduk.text.toString(),
                binding.inputHargaModal.text.toString(),
                binding.inputHargaJual.text.toString(),
                binding.inputStok.text.toString(),
                binding.inputTglMasuk.text.toString()
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
        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveChangesBtn.setOnClickListener {
                addNewItem()
            }
        }
        binding.tvDate.dateNow()

    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
    private fun updateLabel() {
        val formatter = SimpleDateFormat("d MMMM y", Locale.US)
        binding.inputTglMasuk.setText(formatter.format(calendar.time))
    }

    private fun setUpDatePicker() {
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        binding.inputTglMasuk.setOnClickListener {
            val months = DateFormatSymbols.getInstance().months
            val dateNow = binding.inputTglMasuk.text.toString()
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
