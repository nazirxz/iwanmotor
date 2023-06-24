package com.projek.iwanmotor.ui.dashboard

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.IwanMotorApplication
import com.projek.iwanmotor.databinding.ActivityHomeBinding.bind
import com.projek.iwanmotor.databinding.FragmentDashboardBinding
import com.projek.iwanmotor.ui.barang.BarangViewModel
import com.projek.iwanmotor.ui.barang.BarangViewModelFactory
import com.projek.iwanmotor.ui.transaksi.TransaksiViewModel
import com.projek.iwanmotor.ui.transaksi.TransaksiViewModelFactory
import com.projek.iwanmotor.utils.Utility.dateNow
import java.text.NumberFormat
import java.util.*


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding as FragmentDashboardBinding

    private val viewModel: BarangViewModel by activityViewModels {
        BarangViewModelFactory(
            (activity?.application as IwanMotorApplication).database.barangDao()
        )
    }
    private val viewModel2: TransaksiViewModel by activityViewModels {
        TransaksiViewModelFactory(
            (activity?.application as IwanMotorApplication).database2.transaksiDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var total: Int
        var kasMasuk:Int
        var kasKeluar:Int
        viewModel.getTotalStok().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.angkaTotalStok.text = 0.toString()
            } else {
                binding.angkaTotalStok.text = it.toString()
                Log.d("total stok", it.toString())
            }
        }
        viewModel.getTotalKasKeluar().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.angkaKaskeluar.text = 0.toString()
            } else {
                binding.angkaKaskeluar.text = it.toString()
                Log.d("total kas keluar", it.toString())
            }
        }
        viewModel2.getTotalKasMasuk().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.angkaKasmasuk.text = 0.toString()
            } else {
                binding.angkaKasmasuk.text = it.toString()
                Log.d("total kas masuk", it.toString()).toDouble().toString()
            }
        }
        viewModel2.getTotalPenjualan().observe(viewLifecycleOwner) {
            if (it==null) {
                binding.angkaTotalPenjualan.text = 0.toString()
            }else {
                binding.angkaTotalPenjualan.text = it.toString()
                Log.d("total total penjualan", it.toString())
            }
        }

        viewModel2.getTotalKasMasuk().observe(viewLifecycleOwner){
            if (it==null) {
                binding.angkaKas.text = 0.toString()
            }else {
                kasMasuk = it
                viewModel.getTotalKasKeluar().observe(viewLifecycleOwner){
                    kasKeluar = it
                    total = (kasMasuk-kasKeluar)
                    binding.angkaKas.text = total.toString()
                    Log.d("total kas",total.toString())
                }
            }
        }
//        binding.tvDate.dateNow()
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