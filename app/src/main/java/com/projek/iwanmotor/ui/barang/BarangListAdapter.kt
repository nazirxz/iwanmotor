package com.projek.iwanmotor.ui.barang

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.databinding.ItemBarangListBinding
import java.text.NumberFormat
import java.util.*

class BarangListAdapter (private val onItemClicked: (Barang) -> Unit) :
    ListAdapter<Barang, BarangListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
           ItemBarangListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ItemBarangListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Barang) {
           fun getFormattedPrice(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(item.hargaModal)
fun getFormattedPrice2(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(item.hargaJual)
            binding.itemNamaproduk.text = item.namaProduk
            binding.itemHargamodal.text = getFormattedPrice()
            binding.itemHargajual.text = getFormattedPrice2()
            binding.itemStok.text = item.stok.toString()
            binding.itemTglmasuk.text = item.tglMasuk
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Barang>() {
            override fun areItemsTheSame(oldItem: Barang, newItem: Barang): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Barang, newItem: Barang): Boolean {
                return oldItem.namaProduk== newItem.namaProduk
            }
        }
    }
}