package com.projek.iwanmotor.ui.transaksi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.databinding.ItemTransaksiListBinding
import java.text.NumberFormat
import java.util.*


class TransaksiListAdapter (private val onItemClicked: (Transaksi) -> Unit) :
    ListAdapter<Transaksi, TransaksiListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemTransaksiListBinding.inflate(
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

    class ItemViewHolder(private var binding: ItemTransaksiListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaksi) {
            fun getFormattedPrice(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(item.jumlah)
            fun getFormattedPrice2(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(item.subtotal)
            binding.itemInvoice.text = item.invoice
            binding.itemJumlah.text = getFormattedPrice()
            binding.itemSubtotal.text = getFormattedPrice2()
            binding.itemTglBeli.text = item.tglPembelian
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Transaksi>() {
            override fun areItemsTheSame(oldItem: Transaksi, newItem: Transaksi): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Transaksi, newItem: Transaksi): Boolean {
                return oldItem.namaProduk== newItem.namaProduk
            }
        }
    }
}