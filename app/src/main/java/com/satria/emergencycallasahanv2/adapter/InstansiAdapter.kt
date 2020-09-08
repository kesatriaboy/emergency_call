package com.satria.emergencycallasahanv2.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.satria.emergencycallasahanv2.CustomOnItemClickListener
import com.satria.emergencycallasahanv2.PoliceActivity
import com.satria.emergencycallasahanv2.R
import com.satria.emergencycallasahanv2.Report
import com.satria.emergencycallasahanv2.entity.Emergency
import kotlinx.android.synthetic.main.item_admin.view.tv_item_address
import kotlinx.android.synthetic.main.item_admin.view.tv_item_name
import kotlinx.android.synthetic.main.item_admin.view.tv_item_numberPhn
import kotlinx.android.synthetic.main.item_instansi.view.*
import java.security.PolicySpi

class InstansiAdapter(private val activity: Activity) : RecyclerView.Adapter<InstansiAdapter.EmergencyViewHolder>() {

    //ini adalah Context yang dibutuhkan karena kita akan memanggil fungsi startActivityForResultketika item diklik.
    var listEmergency = ArrayList<Emergency>()
        set(listEmergency) {
            if (listEmergency.size > 0) {
                this.listEmergency.clear()
            }
            this.listEmergency.addAll(listEmergency)

            notifyDataSetChanged()
        }

    //generate getter untuk arraylist-nya dan juga constructor untuk context-nya.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instansi, parent, false)
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
            with(itemView) {
                tv_item_name.text = emergency.name
                tv_item_address.text = emergency.address
                tv_item_numberPhn.text = emergency.numberPhn
                cv_item_instansi.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val builder1 = AlertDialog.Builder(itemView.context)
                                builder1.setMessage("Melaporkan Kejadian Palsu Dianggap Melanggar Hukum Dan Dapat Dipidana.\n\nApakah Anda Yakin Ingin Memanggil Nomor Ini?.")
                                builder1.setCancelable(true)

                                builder1.setPositiveButton(
                                    "Yakin",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        val moveToDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${emergency.numberPhn}"))
                                        itemView.context.startActivity(moveToDial)
                                        Report.send(emergency.name.toString(), itemView.context)

                                    })

                                builder1.setNegativeButton(
                                    "Tidak",
                                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

                                val alert11 = builder1.create()
                                alert11.show()
                            }
                        })
                )
            }
        }
    }
}
