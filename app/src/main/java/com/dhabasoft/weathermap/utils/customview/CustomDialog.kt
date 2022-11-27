package com.dhabasoft.weathermap.utils.customview

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.dhabasoft.weathermap.databinding.CustomAlertDialogBinding

object CustomDialog {
    fun createDialogWithTwoButton(
        activity: Activity,
        cancelable: Boolean,
        message: String,
        labelPositiveButton: String? = null,
        labelNegativeButton: String? = null,
        positiveAction: ((activity: Activity) -> Unit)? = null,
        negativeAction: ((activity: Activity) -> Unit)? = null,
        closeAction: ((activity: Activity) -> Unit)? = null
    ): Dialog {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(activity))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)

        binding.lblMessage.text = message
        if (labelPositiveButton != null) {
            binding.btnConfirmationPositive.text = labelPositiveButton
            binding.btnConfirmationPositive.setOnClickListener {
                positiveAction?.let { it1 -> it1(activity) }
                dialog.dismiss()
            }
        } else {
            binding.btnConfirmationPositive.visibility = View.GONE
        }
        if (labelNegativeButton != null) {
            binding.btnConfirmationNegative.text = labelNegativeButton
            binding.btnConfirmationNegative.setOnClickListener {
                negativeAction?.let { it1 -> it1(activity) }
            }
        } else {
            binding.btnConfirmationNegative.visibility = View.GONE
        }

        binding.imgClose.setOnClickListener {
            closeAction?.let { it1 -> it1(activity) }
            dialog.dismiss()
        }
        return dialog
    }
}