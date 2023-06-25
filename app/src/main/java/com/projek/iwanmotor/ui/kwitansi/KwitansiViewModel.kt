package com.projek.iwanmotor.ui.kwitansi

import androidx.lifecycle.*
import com.projek.iwanmotor.data.kwitansi.Kwitansi
import com.projek.iwanmotor.data.kwitansi.KwitansiDao
import com.projek.iwanmotor.data.transaksi.TransaksiDao
import com.projek.iwanmotor.ui.transaksi.TransaksiViewModel
import kotlinx.coroutines.launch

class KwitansiViewModel(private val kwitansiDao: KwitansiDao): ViewModel(){
    // Cache all transaksis form the database using LiveData.
    val allKwitansi: LiveData<List<Kwitansi>> = kwitansiDao.getKwitansis().asLiveData()

    /**
     * Updates an existing transaksi in the database.
     */
    fun insertKwitansi(
        kwitansiId: Int,
        diterima: String,
        alamat: String,
        nohp: Int,
        uangSejumlah: Double,
        untukPembayaran: String,
        type: String,
        noRangka: Int,
        noMesin: Int,
        noPol: Int,
        tahun: Int,
        warna: String,
        tanggal: String
    ) {
        val updatedBarang = getUpdatedKwitansiEntry(kwitansiId,diterima,alamat, nohp,uangSejumlah,untukPembayaran,type,noRangka,noMesin,noPol,tahun,warna,tanggal)
        updateBarang(updatedBarang)
    }
    fun updateKwitansi(
        kwitansiId: Int,
        diterima: String,
        alamat: String,
        nohp: String,
        uangSejumlah: String,
        untukPembayaran: String,
        type: String,
        noRangka: String,
        noMesin: String,
        noPol: String,
        tahun: String,
        warna: String,
        tanggal: String
    ){
        val updatedBarang = getUpdatedKwitansiEntry2(kwitansiId,diterima,alamat,nohp,uangSejumlah,untukPembayaran,type,noRangka,noMesin,noPol,tahun,warna,tanggal)
        updateBarang(updatedBarang)
    }

    /**
     * Launching a new coroutine to update an transaksi in a non-blocking way
     */
    private fun updateBarang(kwitansi:Kwitansi) {
        viewModelScope.launch {
            kwitansiDao.update(kwitansi)
        }
    }

    /**
     * Inserts the new Transaksi into database.
     */
    fun addNewKwitansi(   diterima: String,
                          alamat: String,
                          nohp: String,
                          uangSejumlah: String,
                          untukPembayaran: String,
                          type: String,
                          noRangka: String,
                          noMesin: String,
                          noPol: String,
                          tahun: String,
                          warna: String,
                          tanggal: String) {
        val newKwitansi = getNewKwitansiEntry(diterima,alamat,nohp, uangSejumlah,untukPembayaran,type,noRangka,noMesin,noPol,tahun,warna,tanggal)
        insertKwitansi(newKwitansi)
    }

    /**
     * Launching a new coroutine to insert an transaksi in a non-blocking way
     */
    private fun insertKwitansi(kwitansi: Kwitansi) {
        viewModelScope.launch {
            kwitansiDao.insert(kwitansi)
        }
    }

    /**
     * Launching a new coroutine to delete an transaksi in a non-blocking way
     */
    fun deleteKwitansi(kwitansi: Kwitansi) {
        viewModelScope.launch {
            kwitansiDao.delete(kwitansi)
        }
    }

    /**
     * Retrieve an transaksi from the repository.
     */
    fun retrieveKwitansi(id: Int): LiveData<Kwitansi> {
        return kwitansiDao.getKwitansi(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(diterima: String,
                     alamat: String,
                     uangSejumlah: String,
                     nohp:String,
                     untukPembayaran: String,
                     type: String,
                     noRangka: String,
                     noMesin: String,
                     noPol: String,
                     tahun: String,
                     warna: String,
                     tanggal: String): Boolean {
        if (diterima.isBlank() || alamat.isBlank() ||nohp.isBlank()|| uangSejumlah.isBlank() || untukPembayaran.isBlank()|| type.isBlank() || noRangka.isBlank()|| noMesin.isBlank() || noPol.isBlank()||
                tahun.isBlank()|| warna.isBlank() || tanggal.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [Kwitansi] entity class with the transaksi info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewKwitansiEntry(diterima: String,
                                    alamat: String,
                                    nohp: String,
                                    uangSejumlah: String,
                                    untukPembayaran: String,
                                    type: String,
                                    noRangka: String,
                                    noMesin: String,
                                    noPol: String,
                                    tahun: String,
                                    warna: String,
                                    tanggal: String): Kwitansi {
        return Kwitansi(
            diterima = diterima,
            alamat = alamat,
            nohp = nohp.toInt(),
            uangSejumlah = uangSejumlah.toDouble(),
            untukPembayaran = untukPembayaran,
            type = type,
            noRangka = noRangka.toInt(),
            noMesin = noMesin.toInt(),
            noPol= noPol.toInt(),
            tahun = tahun.toInt(),
            warna = warna,
            tanggal = tanggal
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [transaksi] entity class with the transaksi info updated by the user.
     */
    private fun getUpdatedKwitansiEntry(
        kwitansiId: Int,
        diterima: String,
        alamat: String,
        nohp: Int,
        uangSejumlah: Double,
        untukPembayaran: String,
        type: String,
        noRangka: Int,
        noMesin: Int,
        noPol: Int,
        tahun: Int,
        warna: String,
        tanggal: String
    ): Kwitansi {
        return Kwitansi(
            id = kwitansiId,
            diterima = diterima,
            alamat = alamat,
            nohp = nohp,
            uangSejumlah = uangSejumlah,
            untukPembayaran = untukPembayaran,
            type = type,
            noRangka = noRangka,
            noMesin = noMesin,
            noPol= noPol,
            tahun = tahun,
            warna = warna,
            tanggal = tanggal
        )
    }
    private fun getUpdatedKwitansiEntry2(
        kwitansiId: Int,
        diterima: String,
        alamat: String,
        nohp: String,
        uangSejumlah: String,
        untukPembayaran: String,
        type: String,
        noRangka: String,
        noMesin: String,
        noPol: String,
        tahun: String,
        warna: String,
        tanggal: String
    ): Kwitansi {
        return Kwitansi(
            id = kwitansiId,
            diterima = diterima,
            alamat = alamat,
            nohp = nohp.toInt(),
            uangSejumlah = uangSejumlah.toDouble(),
            untukPembayaran = untukPembayaran,
            type = type,
            noRangka = noRangka.toInt(),
            noMesin = noMesin.toInt(),
            noPol= noPol.toInt(),
            tahun = tahun.toInt(),
            warna = warna,
            tanggal = tanggal
        )
    }
}


class KwitansiViewModelFactory(private val kwitansiDao: KwitansiDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KwitansiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KwitansiViewModel(kwitansiDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
