package com.projek.iwanmotor.ui.transaksi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.projek.iwanmotor.R
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.databinding.FragmentTransaksiBinding
import com.projek.iwanmotor.ui.barang.BarangFragmentDirections
import com.projek.iwanmotor.ui.barang.BarangListAdapter
import com.projek.iwanmotor.ui.barang.BarangViewModel
import com.projek.iwanmotor.ui.barang.BarangViewModelFactory
import com.projek.iwanmotor.utils.Utility.dateNow


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
        }
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_transaksi_to_tambahTransaksi)
        }
        binding.tvDate.dateNow()
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
