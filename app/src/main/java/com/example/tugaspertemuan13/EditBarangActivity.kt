package com.example.tugaspertemuan13

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspertemuan13.databinding.ActivityEditBarangBinding

import com.google.firebase.firestore.FirebaseFirestore

class EditBarangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBarangBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val baranges = intent.getStringExtra("barang")
        val deskripsires = intent.getStringExtra("deskripsi")
        val kategorires = intent.getStringExtra("kategori")
        val hargares = intent.getStringExtra("harga")

        with(binding) {
            // Add null checks for each retrieved extra
            editETBarang.text = baranges?.let { Editable.Factory.getInstance().newEditable(it) }
            editETDeskripsi.text = deskripsires?.let { Editable.Factory.getInstance().newEditable(it) }
            editETKategori.text = kategorires?.let { Editable.Factory.getInstance().newEditable(it) }
            editETHarga.text = hargares?.let { Editable.Factory.getInstance().newEditable(it) }

            editBTEdit.setOnClickListener {
                val barang = editETBarang.text.toString()
                val deskripsi = editETDeskripsi.text.toString()
                val kategori = editETKategori.text.toString()
                val harga = editETHarga.text.toString()

                if (barang.isBlank() || deskripsi.isBlank() || kategori.isBlank() || harga.isBlank()) {
                    showToast("Cant Empty Data!")
                } else {
                    try {
                        val integerHarga = harga.toInt()
                        id?.let {
                            // Update data in Firestore
                            firestore.collection("barang").document(it)
                                .update(
                                    mapOf(
                                        "barang" to barang,
                                        "deskripsi" to deskripsi,
                                        "kategori" to kategori,
                                        "harga" to integerHarga
                                    )
                                )
                                .addOnSuccessListener {
                                    showToast("Data Updated successfully")
                                    val intent = Intent(this@EditBarangActivity, MainActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    showToast("Failed to update data: $e")
                                }
                        }
                    } catch (e: NumberFormatException) {
                        showToast("Harga must be a number!")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
