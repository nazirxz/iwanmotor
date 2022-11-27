package com.projek.iwanmotor.ui.barang


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.BarangDao
import kotlinx.coroutines.launch

class BarangViewModel(private val barangDao : BarangDao) : ViewModel() {

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<Barang>> = barangDao.getItems().asLiveData()

    /**
     * Returns true if stock is available to sell, false otherwise.
     */
    fun isStockAvailable(item: Barang): Boolean {
        return (item.stok.toInt() > 0)
    }

    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        barangId: Int,
        namaProduk: String,
        hargaModal: String,
        hargaJual: String,
        itemCount: String,
        tglMasuk: String
    ) {
        val updatedItem = getUpdatedItemEntry(barangId, namaProduk, hargaModal,hargaJual, itemCount,tglMasuk)
        updateItem(updatedItem)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(item: Barang) {
        viewModelScope.launch {
            barangDao.update(item)
        }
    }

    /**
     * Decreases the stock by one unit and updates the database.
     */
    fun sellItem(item: Barang) {
        if (item.stok > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(stok = item.stok - 1)
            updateItem(newItem)
        }
    }

    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(namaProduk: String, hargaModal: String,hargaJual: String, itemCount: String,tglMasuk: String) {
        val newItem = getNewItemEntry(namaProduk, hargaModal,hargaJual, itemCount, tglMasuk)
        insertItem(newItem)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(item: Barang) {
        viewModelScope.launch {
            barangDao.insert(item)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(item: Barang) {
        viewModelScope.launch {
            barangDao.delete(item)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Barang> {
        return barangDao.getItem(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(namaProduk: String, hargaModal: String,hargaJual:String, itemCount: String,tglMasuk:String): Boolean {
        if (namaProduk.isBlank() || hargaModal.isBlank() ||hargaJual.isBlank() || itemCount.isBlank()|| tglMasuk.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(namaProduk: String, hargaModal: String, hargaJual: String,itemCount: String,tglMasuk: String): Barang{
        return Barang(
            namaProduk = namaProduk,
            hargaModal = hargaModal.toDouble(),
            hargaJual = hargaJual.toDouble(),
            stok = itemCount.toInt(),
            tglMasuk = tglMasuk
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [Item] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        barangId: Int,
        namaProduk: String,
        hargaModal: String,
        hargaJual: String,
        itemCount: String,
        tglMasuk: String
    ): Barang {
        return Barang(
            id = barangId,
            namaProduk = namaProduk,
            hargaModal = hargaModal.toDouble(),
            hargaJual = hargaJual.toDouble(),
            stok = itemCount.toInt(),
            tglMasuk = tglMasuk
        )
    }
}

class BarangViewModelFactory(private val barangDao: BarangDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarangViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarangViewModel(barangDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


