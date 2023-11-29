package com.example.tugaspertemuan13

import com.google.firebase.firestore.Exclude

data class Barang(
    @set:Exclude @get:Exclude @Exclude var id : String = "",
    var barang: String = "",
    var harga: Int = 0,
    var kategori: String = "",
    var deskripsi: String = ""
)
