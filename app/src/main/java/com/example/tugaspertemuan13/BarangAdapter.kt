import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tugaspertemuan13.Barang
import com.example.tugaspertemuan13.EditBarangActivity
import com.example.tugaspertemuan13.R
import com.google.firebase.firestore.FirebaseFirestore

class BarangAdapter : RecyclerView.Adapter<BarangAdapter.BarangViewHolder>() {

    private var barang: List<Barang> = listOf()
    private val firestore = FirebaseFirestore.getInstance()
    private val barangCollection = firestore.collection("barang")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barang, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val currentBarang = barang[position]

        holder.textViewBarang.text = currentBarang.barang
        holder.textViewDeskripsi.text = "Deskripsi: ${currentBarang.deskripsi}"
        holder.textViewKategori.text = currentBarang.kategori
        holder.textViewHarga.text = "Rp ${currentBarang.harga}"

        holder.btEdit.setOnClickListener {
            try {
                val intent = Intent(holder.itemView.context, EditBarangActivity::class.java)
                intent.putExtra("id", currentBarang.id)
                intent.putExtra("barang", currentBarang.barang)
                intent.putExtra("deskripsi", currentBarang.deskripsi)
                intent.putExtra("kategori", currentBarang.kategori)
                intent.putExtra("harga", currentBarang.harga)
                holder.itemView.context.startActivity(intent)
            }catch (e: Exception){
                showToast(e.toString(),holder)
                Log.d("ERR", e.toString())
            }

        }

        holder.btDelete.setOnClickListener {
            showYesNoAlertDialog(
                holder.itemView.context,
                "Apakah anda yakin akan menghapus ${currentBarang.barang}",
                DialogInterface.OnClickListener { _, _ ->
                    deleteBuku(currentBarang.id,holder)
                }
            )
        }
    }

    private fun deleteBuku(id: String, holder: BarangViewHolder) {
        barangCollection.document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Buku berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    holder.itemView.context,
                    "Error deleting document: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun getItemCount(): Int {
        return barang.size
    }

    fun setBarang(barang: List<Barang>) {
        this.barang = barang
        notifyDataSetChanged()
    }

    inner class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBarang: TextView = itemView.findViewById(R.id.barangTextView)
        val textViewDeskripsi: TextView = itemView.findViewById(R.id.deskripsiTextView)
        val textViewKategori: TextView = itemView.findViewById(R.id.kategoriTextView)
        val textViewHarga: TextView = itemView.findViewById(R.id.hargaTextView)
        val btEdit: Button = itemView.findViewById(R.id.itemBtEdit)
        val btDelete: Button = itemView.findViewById(R.id.itemBtDelete)
    }

    fun showYesNoAlertDialog(
        context: Context,
        message: String,
        onYesClickListener: DialogInterface.OnClickListener
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Yes", onYesClickListener)
        alertDialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showToast(message: String, holder: BarangViewHolder) {
        Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
    }

}
