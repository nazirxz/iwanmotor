package com.projek.iwanmotor.ui.kwitansi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projek.iwanmotor.data.kwitansi.Kwitansi
import com.projek.iwanmotor.databinding.ItemKwitansiListBinding
import java.text.NumberFormat
import java.util.*

class KwitansiListAdapter (private val onItemClicked: (Kwitansi) -> Unit) :
    ListAdapter<Kwitansi, KwitansiListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemKwitansiListBinding.inflate(
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

    class ItemViewHolder(private var binding: ItemKwitansiListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Kwitansi) {
            fun getFormattedPrice2(): String = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(item.uangSejumlah)
            binding.itemDiterima.text = item.diterima
            binding.itemAlamat.text = item.alamat
            binding.itemNohp.text=item.nohp.toString()
            binding.itemUangSejumlah.text = getFormattedPrice2()
            binding.itemNoRangka.text = item.noRangka.toString()
            binding.itemNoMesin.text = item.noMesin.toString()
            binding.itemNoPol.text = item.noPol
            binding.itemTahun.text= item.tahun.toString()
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Kwitansi>() {
            override fun areItemsTheSame(oldItem: Kwitansi, newItem: Kwitansi): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Kwitansi, newItem: Kwitansi): Boolean {
                return oldItem.diterima== newItem.diterima
            }
        }
    }

}