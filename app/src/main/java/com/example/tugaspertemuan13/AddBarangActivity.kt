package com.example.tugaspertemuan13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tugaspertemuan13.databinding.ActivityAddBarangBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddBarangActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddBarangBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val barangCollectionRef  = firestore.collection("barang")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            addBTTambah.setOnClickListener{
                var barang = addETBarang.text.toString()
                var deskripsi = addETDeskripsi.text.toString()
                var kategori = addETKategori.text.toString()
                var harga = addETHarga.text.toString()


                if (barang == "" || deskripsi == "" || kategori == "" || harga == "" ){
                    showToast("Cant Empty Data!")
                }else{
                    try {
                        val integerHarga = harga.toInt()
                        add(
                            Barang(
                                barang = barang,
                                deskripsi = deskripsi,
                                kategori = kategori,
                                harga = integerHarga
                            )
                        )
                        showToast("INSERTED!")

                        val intent = Intent(this@AddBarangActivity, MainActivity::class.java)
                        startActivity(intent)
                    } catch (e: NumberFormatException) {
                        showToast("Harga must number!")
                    }
                }
            }
        }

    }

    private fun add(barang: Barang){
        barangCollectionRef.add(barang).addOnFailureListener { e ->
            Log.d("AddBarangActivity", "Error adding barang", e)
            showToast(e.toString())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}