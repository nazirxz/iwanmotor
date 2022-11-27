package com.projek.iwanmotor.ui.barang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.projek.iwanmotor.R
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.databinding.FragmentDetailBarangBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditBarang.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditBarang : Fragment() {
    private val navigationArgs: EditBarangArgs by navArgs()
    lateinit var barang: Barang

    private val viewModel: BarangViewModel by activityViewModels {
        BarangViewModelFactory(
            (activity?.application as IwanMotorApplication).database.barangDao()
        )
    }
    private var _binding: FragmentDetailBarangBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBarangBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun bind(barang: Barang) {
        val price = "%.2f".format(barang.hargaJual)
        val price2 = "%.2f".format(barang.hargaModal)
        binding.apply {
            inputNamaProduk.setText(barang.namaProduk)
            inputHargaModal.setText(price)
            inputHargaJual.setText(price2)c 
            inputStok.setText(barang.stok.toString())
            inputTglMasuk.setText(barang.tglMasuk)
            deleteBtn.isEnabled = viewModel.isStockAvailable(barang)
            deleteBtn.setOnClickListener { viewModel.sellItem(barang) }
            deleteBtn.setOnClickListener { showConfirmationDialog() }
            addBtn.setOnClickListener { updateItem() }
        }
    }
    fun formatCurrency(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }
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
            val action = EditBarangDirections.actionEditBarangToNavigationBarang()
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.inputNamaProduk.text.toString(),
            binding.inputHargaModal.text.toString(),
            binding.inputHargaJual.text.toString(),
            binding.inputStok.text.toString(),
            binding.inputTglMasuk.text.toString()
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
        viewModel.deleteItem(barang)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
           barang = selectedItem
            bind(barang)
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