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
import com.satria.emergencycallasahanv2.DetailReportActivity
import com.satria.emergencycallasahanv2.R
import com.satria.emergencycallasahanv2.entity.ReportKominfo
import kotlinx.android.synthetic.main.item_admin_kominfo.view.*
import kotlinx.android.synthetic.main.item_instansi.view.*

class ReportAdapter(private val activity: Activity) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    //ini adalah Context yang dibutuhkan karena kita akan memanggil fungsi startActivityForResultketika item diklik.
    var listReport = ArrayList<ReportKominfo>()
        set(listReport) {
            if (listReport.size > 0) {
                this.listReport.clear()
            }
            this.listReport.addAll(listReport)

            notifyDataSetChanged()
        }

    //generate getter untuk arraylist-nya dan juga constructor untuk context-nya.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_kominfo, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(listReport[position])
    }

    override fun getItemCount(): Int = this.listReport.size

    //proses inflate layout yang dibuat sebelumnya untuk menjadi tampilan per baris di RecyclerView.
    // Termasuk juga implementasi dari CustomOnItemClickListener yang membuat objek CardViewEmergency
    // bisa diklik untuk mengarahkan ke halaman EmergencyAddUpdateActivity.
    // Tujuannya, untuk melakukan perubahan data oleh pengguna.
    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(report: ReportKominfo) {
            with(itemView) {
                tv_item_instansi.text = report.instansi
                tv_item_status.text = report.status
                tv_item_latitute.text = report.latitute
                tv_item_longitude.text = report.longitude
                tv_item_date.text = report.date
                tv_item_nik.text = report.nik
                tv_item_nama.text = report.nama
                tv_item_no_hp.text = report.no_hp
                cv_item_laporan.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val i = Intent(activity, DetailReportActivity::class.java)
                                i.putExtra("lat",report.latitute)
                                i.putExtra("lng",report.longitude)
                                itemView.context.startActivity(i)
                            }
                        })
                )
            }
        }
    }
}
