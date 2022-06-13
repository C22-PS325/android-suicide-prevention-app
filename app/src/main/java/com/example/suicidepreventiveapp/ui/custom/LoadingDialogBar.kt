package com.example.suicidepreventiveapp.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.example.suicidepreventiveapp.R

class LoadingDialogBar(private val context: Context){

    private lateinit var mDialog: Dialog

    fun showDialog(title: String) {
        mDialog = Dialog(context)
        mDialog.setContentView(R.layout.custom_dialog)
        mDialog.setCancelable(false)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvTitle = mDialog.findViewById<TextView>(R.id.tv_title)

        tvTitle.text = title

        mDialog.create()
        mDialog.show()
    }

    fun hideDialog() {
        mDialog.dismiss()
    }

}