package com.bkplus.scan_qrcode_barcode.ui.ratting

import android.app.ActionBar.LayoutParams
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentRattingPopupBinding

class RattingDialog (
	private val onAccept: (Boolean) -> Unit
): DialogFragment() {

	private lateinit var rattingPopupBinding: FragmentRattingPopupBinding
	private lateinit var starsList: ArrayList<ImageView>
	private var currentStar = 5

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		rattingPopupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ratting_popup, container, false)

		if (dialog != null) {
			dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
			dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
		}

		return rattingPopupBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		starsList = mutableListOf(
			rattingPopupBinding.star1,
			rattingPopupBinding.star2,
			rattingPopupBinding.star3,
			rattingPopupBinding.star4,
			rattingPopupBinding.star5,
		) as ArrayList<ImageView>

		listener()
	}
	
	private fun listener() {
		rattingPopupBinding.star1.setOnClickListener {
			onIndexChanged(1)
		}
		rattingPopupBinding.star2.setOnClickListener {
			onIndexChanged(2)
		}
		rattingPopupBinding.star3.setOnClickListener {
			onIndexChanged(3)
		}
		rattingPopupBinding.star4.setOnClickListener {
			onIndexChanged(4)
		}
		rattingPopupBinding.star5.setOnClickListener {
			onIndexChanged(5)
		}

		rattingPopupBinding.rateButton.setOnClickListener {
			onAccept(currentStar > 3)
			dismiss()
			AppOpenManager.getInstance().disableAppResume()
		}

		rattingPopupBinding.cancelButton.setOnClickListener {
			this@RattingDialog.dismiss()
		}
	}

	private fun onIndexChanged(index: Int) {
		currentStar = index
		for (i in 1 until starsList.size) {
			if (i < index) {
				starsList[i].setImageResource(R.drawable.ic_star)
			} else {
				starsList[i].setImageResource(R.drawable.ic_no_star)
			}
		}
	}

}