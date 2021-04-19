package com.dicoding.auliarosyida.githubuser

import android.view.View

class CustomClickListener(private val position: Int, private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {
    override fun onClick(view: View) {
        onItemClickCallback.onItemClicked(view, position)
    }
    //untuk menghindari nilai final dari posisi
    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}