package com.projek.iwanmotor.ui.transaksi

import androidx.lifecycle.*
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.data.transaksi.TransaksiDao
import com.projek.iwanmotor.ui.barang.BarangViewModel
import kotlinx.coroutines.launch

class TransaksiViewModel(private val transaksiDao: TransaksiDao): ViewModel(){
    // Cache all transaksis form the database using LiveData.
    val alltransaksis: LiveData<List<Transaksi>> = transaksiDao.getTransaksis().asLiveData()

    /**
     * Updates an existing transaksi in the database.
     */
    fun insertTransaksi(
        transaksId: Int,
        invoice: String,
        namaProduk: String,
        harga: String,
        jumlah: String,
        subtotal: String,
        tglBeli: String
    ) {
        val updatedBarang = getUpdatedtransaksiEntry(transaksId,invoice, namaProduk, harga,jumlah, subtotal,tglBeli)
        updateBarang(updatedBarang)
    }
    fun updateTransaksi(
        transaksId: Int,
        namaProduk: String,
        harga: String,
        jumlah: String,
        subtotal: String,
        tglBeli: String
    ){
        val updatedBarang = getUpdatedtransaksiEntry2(transaksId, namaProduk, harga,jumlah, subtotal,tglBeli)
        updateBarang(updatedBarang)
    }



    /**
     * Launching a new coroutine to update an transaksi in a non-blocking way
     */
    private fun updateBarang(transaksi: Transaksi) {
        viewModelScope.launch {
            transaksiDao.update(transaksi)
        }
    }

    /**
     * Inserts the new Transaksi into database.
     */
    fun addNewTransaksi(invoice:String,namaProduk: String, harga: String,jumlah: String, subtotal: String,tglBeli: String) {
        val newtransaksi = getNewtransaksiEntry(invoice,namaProduk, harga,jumlah, subtotal, tglBeli)
        insertTransaksi(newtransaksi)
    }

    /**
     * Launching a new coroutine to insert an transaksi in a non-blocking way
     */
    private fun insertTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            transaksiDao.insert(transaksi)
        }
    }

    /**
     * Launching a new coroutine to delete an transaksi in a non-blocking way
     */
    fun deletetransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            transaksiDao.delete(transaksi)
        }
    }

    /**
     * Retrieve an transaksi from the repository.
     */
    fun retrievetransaksi(id: Int): LiveData<Transaksi> {
        return transaksiDao.getTransaksi(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(namaProduk: String, harga: String,jumlah: String, subtotal: String,tglPembelian :String): Boolean {
        if (namaProduk.isBlank() || harga.isBlank() ||jumlah.isBlank() || subtotal.isBlank()|| tglPembelian.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [transaksi] entity class with the transaksi info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewtransaksiEntry(invoice:String, namaProduk: String, harga: String,jumlah: String, subtotal: String,tglBeli :String): Transaksi{
        return Transaksi(
            invoice = invoice,
            namaProduk = namaProduk,
            harga = harga.toDouble(),
            jumlah = jumlah.toInt(),
            subtotal = subtotal.toDouble(),
            tglPembelian = tglBeli
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [transaksi] entity class with the transaksi info updated by the user.
     */
    private fun getUpdatedtransaksiEntry(
        transaksId: Int,
        invoice: String,
        namaProduk: String,
        harga: String,
        jumlah: String,
        subtotal: String,
        tglBeli: String
    ): Transaksi {
        return Transaksi(
            id = transaksId,
            invoice = invoice,
            namaProduk = namaProduk,
            harga = harga.toDouble(),
            jumlah = jumlah.toInt(),
            subtotal = subtotal.toDouble(),
            tglPembelian = tglBeli
        )
    }
    private fun getUpdatedtransaksiEntry2(
        transaksId: Int,
        namaProduk: String,
        harga: String,
        jumlah: String,
        subtotal: String,
        tglBeli: String
    ): Transaksi {
        return Transaksi(
            id = transaksId,
            invoice = "",
            namaProduk = namaProduk,
            harga = harga.toDouble(),
            jumlah = jumlah.toInt(),
            subtotal = subtotal.toDouble(),
            tglPembelian = tglBeli
        )
    }
}

class TransaksiViewModelFactory(private val transaksiDao: TransaksiDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaksiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransaksiViewModel(transaksiDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
