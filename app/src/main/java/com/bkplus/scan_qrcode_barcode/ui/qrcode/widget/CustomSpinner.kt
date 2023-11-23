package com.bkplus.scan_qrcode_barcode.ui.qrcode.widget
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner


class CustomSpinner : androidx.appcompat.widget.AppCompatSpinner {
	constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
	 * An interface which a client of this Spinner could use to receive
	 * open/closed events for this Spinner.
	 */
	interface OnSpinnerEventsListener {
		/**
		 * Callback triggered when the spinner was opened.
		 */
		fun onSpinnerOpened(spinner: Spinner?)

		/**
		 * Callback triggered when the spinner was closed.
		 */
		fun onSpinnerClosed(spinner: Spinner?)
	}

	private var mListener: OnSpinnerEventsListener? = null

	companion object {
		var isSpinnerOpened = false
	}

	// implement the Spinner constructors that you need
	@SuppressLint("ResourceAsColor")
	override fun performClick(): Boolean {
		// register that the Spinner was opened, so we have a status
		// indicator for when the container holding this Spinner may lose focus
		isSpinnerOpened = true
		if (mListener != null) {
			mListener!!.onSpinnerOpened(this)
		}
		return super.performClick()
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		if (hasBeenOpened() && hasFocus) {
			performClosedEvent()
		}
	}

	/**
	 * Register the listener which will listen for events.
	 */
	fun setSpinnerEventsListener(
		onSpinnerEventsListener: OnSpinnerEventsListener?,
	) {
		mListener = onSpinnerEventsListener
	}

	/**
	 * Propagate the closed Spinner event to the listener from outside if needed.
	 */
	fun performClosedEvent() {
		isSpinnerOpened = false
		if (mListener != null) {
			mListener!!.onSpinnerClosed(this)
		}
	}

	/**
	 * A boolean flag indicating that the Spinner triggered an open event.
	 *
	 * @return true for opened Spinner
	 */
	fun hasBeenOpened(): Boolean {
		return isSpinnerOpened
	}

	fun getSelectedValue(): String {
		return this.selectedItem.toString()
	}
}