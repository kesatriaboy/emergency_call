package com.satria.emergencycallasahanv2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.satria.emergencycallasahanv2.CustomOnItemClickListener
import com.satria.emergencycallasahanv2.EmergencyAddUpdateActivity
import com.satria.emergencycallasahanv2.R
import com.satria.emergencycallasahanv2.entity.Emergency
import kotlinx.android.synthetic.main.item_admin.view.*


//kelas adapter ini berfungsi untuk menampilkan data per baris di komponen viewgroup seperti RecyclerView dengan
// data yang berasal dari objek linkedlist bernama listEmergency.
class EmergencyAdapter(private val activity: Activity) : RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder>() {

    //ini adalah Context yang dibutuhkan karena kita akan memanggil fungsi startActivityForResultketika item diklik.
    var listEmergency = ArrayList<Emergency>()
        set(listEmergency) {
            if (listEmergency.size > 0) {
                this.listEmergency.clear()
            }
            this.listEmergency.addAll(listEmergency)

            notifyDataSetChanged()
        }

    //3 metode untuk menambahkan, memperbaharui dan menghapus Item di RecyclerView
    fun addItem(emergency: Emergency) {
        this.listEmergency.add(emergency)
        notifyItemInserted(this.listEmergency.size - 1)
    }

    fun updateItem(position: Int, emergency: Emergency) {
        this.listEmergency[position] = emergency
        notifyItemChanged(position, emergency)
    }

    fun removeItem(position: Int) {
        this.listEmergency.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listEmergency.size)
    }

    //generate getter untuk arraylist-nya dan juga constructor untuk context-nya.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin, parent, false)
        return EmergencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        holder.bind(listEmergency[position])
    }

    override fun getItemCount(): Int = this.listEmergency.size

    //proses inflate layout yang dibuat sebelumnya untuk menjadi tampilan per baris di RecyclerView.
    // Termasuk juga implementasi dari CustomOnItemClickListener yang membuat objek CardViewEmergency
    // bisa diklik untuk mengarahkan ke halaman EmergencyAddUpdateActivity.
    // Tujuannya, untuk melakukan perubahan data oleh pengguna.
    inner class EmergencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(emergency: Emergency) {
            with(itemView){
                tv_item_name.text = emergency.name
                tv_item_date.text = emergency.date
                tv_item_ka.text = emergency.kategori
                tv_item_address.text = emergency.address
                tv_item_numberPhn.text = emergency.numberPhn
                tv_item_longitude.text = emergency.longitude
                tv_item_latitude.text = emergency.latitude
                cv_item_note.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, EmergencyAddUpdateActivity::class.java)
                        intent.putExtra(EmergencyAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(EmergencyAddUpdateActivity.EXTRA_EMERGENCY, emergency)
                        activity.startActivityForResult(intent, EmergencyAddUpdateActivity.REQUEST_UPDATE)

                    }
                }))
            }
        }
    }
}