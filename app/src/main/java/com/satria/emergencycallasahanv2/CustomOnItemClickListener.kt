package com.satria.emergencycallasahanv2

import android.view.View

//Kelas ini bertugas membuat item CardView bisa diklik di dalam adapter.
class CustomOnItemClickListener(private val position: Int, private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {
    override fun onClick(view: View) {
        onItemClickCallback.onItemClicked(view, position)
    }

    //Kelas OnItemClickCallback dibuat untuk menghindari nilai final.
    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}