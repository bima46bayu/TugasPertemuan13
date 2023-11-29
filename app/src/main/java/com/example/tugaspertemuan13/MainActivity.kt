package com.example.tugaspertemuan13

import BarangAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugaspertemuan13.Barang
import com.example.tugaspertemuan13.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private  lateinit var binding : ActivityMainBinding

    private lateinit var barangAdapter: BarangAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        0

        firestore = FirebaseFirestore.getInstance()

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        barangAdapter = BarangAdapter()
        recyclerView.adapter = barangAdapter

        // Fetch and observe buku data from Firestore
        fetchDataAndObserve()

        with(binding) {
            addButton.setOnClickListener {
                val intent = Intent(this@MainActivity, AddBarangActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun fetchDataAndObserve() {

        try {
            val bukuCollection = firestore.collection("buku")
            // Observe Firestore changes
            bukuCollection.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    showToast(this@MainActivity, "Error fetching data from Firestore")
                    return@addSnapshotListener
                }

                snapshot?.let { documents ->
                    val barang = mutableListOf<Barang>()
                    for (document in documents) {
                        val bukuId = document.id
                        val buku = document.toObject(Barang::class.java).copy(id = bukuId)
                        barang.add(buku)
                    }

                    // Update the UI with the Firestore data
                    barangAdapter.setBarang(barang)
                }
            }
        }catch (e: Exception){
            showToast(this@MainActivity, e.toString())
            Log.d("ERRORKU", e.toString())
        }

    }

    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

}