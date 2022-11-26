package com.projek.iwanmotor.data

import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.BarangDao
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.data.transaksi.TransaksiDao
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.data.user.UserDao
import com.projek.iwanmotor.transaksi

class LocalDataSource(
    private val barangDao: BarangDao,
    private val transaksiDao: TransaksiDao,
    private val userDao: UserDao,
) {
    fun getBarangs() = barangDao.getBarangs()
    fun getBarang(id: Int) = barangDao.getBarang(id)
    suspend fun insert(barang: Barang) = barangDao.insert(barang)
    suspend fun update(barang: Barang) = barangDao.update(barang)
    suspend fun delete(barang: Barang) = barangDao.delete(barang)

    fun getTransaksis() = transaksiDao.getTransaksis()
    fun getTransaksi(invoice: String) = transaksiDao.getTransaksi(invoice)
    suspend fun insert(transaksi: Transaksi) = transaksiDao.insert(transaksi)
    suspend fun update(transaksi: Transaksi) = transaksiDao.update(transaksi)
    suspend fun delete(transaksi: Transaksi) = transaksiDao.delete(transaksi)

    fun getUsers() = userDao.getUsers()
    fun getUser(id: Int) = userDao.getUser(id)
    fun getUser(alamatEmail: String, password: String) = userDao.getUser(alamatEmail,password)
    suspend fun insert(user: User) = userDao.insert(user)
    suspend fun update(user: User) = userDao.update(user)
    suspend fun delete(user: User) = userDao.delete(user)
}
