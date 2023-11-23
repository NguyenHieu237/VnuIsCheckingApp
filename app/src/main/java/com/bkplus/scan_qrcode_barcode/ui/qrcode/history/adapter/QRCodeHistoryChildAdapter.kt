package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.ads.control.ads.nativeAds.AperoNativeAdView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.model.HistoryItemType
import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItemChild
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import com.bkplus.scan_qrcode_barcode.utils.checkSocialMedia
import com.bkplus.scan_qrcode_barcode.utils.checkUrlIcon
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.google.mlkit.vision.barcode.common.Barcode

class QRCodeHistoryChildAdapter(
    private val activity: Activity,
    private val listener: QRCodeHistoryItemListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mLists: List<QRCodeHistoryItemChild> = arrayListOf()
    var barcode: Barcode? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(items: List<QRCodeHistoryItemChild>) {
        this.mLists = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item: QRCodeHistoryItemChild = mLists[position]

        return item.type.type
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.rootView.findViewById(R.id.tvTitle)
        val tvTime: TextView = itemView.rootView.findViewById(R.id.tv_time)
        var imgQRCode: ImageView = itemView.rootView.findViewById(R.id.imgQRCode)
        val tvQRCodedesc: TextView = itemView.rootView.findViewById(R.id.tvQRCodedesc)
        val imgDots: ImageView = itemView.rootView.findViewById(R.id.img_dots)
    }

    inner class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val aperoNativeAds: AperoNativeAdView = itemView.rootView.findViewById(R.id.aperoNativeAds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            HistoryItemType.ITEM.type -> {
                val generatorView = inflater.inflate(R.layout.item_history_child, parent, false)
                ItemViewHolder(generatorView)
            }

            else -> {
                val generatorView = inflater.inflate(R.layout.item_history_ads, parent, false)
                AdsViewHolder(generatorView)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "RestrictedApi", "SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val qrCodeHistoryItemChild: QRCodeHistoryItemChild = mLists[position]

        when (viewHolder.itemViewType) {
            HistoryItemType.ITEM.type -> {
                val itemData = qrCodeHistoryItemChild.itemData.guardLet { return }!!
                val itemViewHolder = (viewHolder as? ItemViewHolder).guardLet { return }!!

                /// Listener
                itemViewHolder.itemView.rootView.setOnClickListener {
                    listener.onTapItem(qrCodeHistoryItemChild)
                }
                itemViewHolder.imgDots.setOnClickListener {
                    val menuBuilder = MenuBuilder(it.context)
                    val inflater = MenuInflater(it.context)
                    inflater.inflate(R.menu.menu_history, menuBuilder)
                    val optionsMenu = MenuPopupHelper(it.context, menuBuilder, it)
                    optionsMenu.setForceShowIcon(true)

                    // Set Item Click Listener
                    menuBuilder.setCallback(object : MenuBuilder.Callback {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onMenuItemSelected(
                            menu: MenuBuilder,
                            item: MenuItem
                        ): Boolean {
                            return when (item.itemId) {
                                R.id.btn_delete -> {
                                    listener.onDeleteItem(qrCodeHistoryItemChild)
                                    this@QRCodeHistoryChildAdapter.notifyDataSetChanged()
                                    true
                                }

                                R.id.btn_share -> {
                                    listener.onShareItem(qrCodeHistoryItemChild)
                                    true
                                }

                                else -> false
                            }
                        }

                        override fun onMenuModeChange(menu: MenuBuilder) {}
                    })
                    optionsMenu.show()
                }

                /// Binding
                itemViewHolder.tvTitle.text = itemData.getTitleForUIHistory()
                itemViewHolder.tvTime.text = itemData.getCreateAt()
                when (itemData.getQRCodeBarCode()) {
                    Barcode.FORMAT_QR_CODE -> {
                        when (itemData.getQRCodeBarCodeType()) {

                            Barcode.TYPE_URL -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    checkUrlIcon(
                                        viewHolder.itemView.rootView.context,itemData.getQRCodeContent()
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text = checkSocialMedia(itemData.getQRCodeContent())

                            }

                            Barcode.TYPE_CALENDAR_EVENT -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_calendar
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.calendar)
                            }

                            Barcode.TYPE_CONTACT_INFO -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_contacts
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.contact)

                            }

                            Barcode.TYPE_SMS -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_sms
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.sms)


                            }

                            Barcode.TYPE_WIFI -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_wifi
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.wifi)
                            }

                            Barcode.TYPE_PHONE -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_phone
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.phone)

                            }

                            Barcode.TYPE_TEXT -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_text
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.text)

                            }

                            Barcode.TYPE_EMAIL -> {
                                itemViewHolder.imgQRCode.setImageDrawable(
                                    viewHolder.itemView.rootView.context.getDrawable(
                                        R.drawable.ic_email
                                    )
                                )
                                itemViewHolder.tvQRCodedesc.text =
                                    viewHolder.itemView.rootView.context.getString(R.string.email)

                            }

                        }
                    }

                    Barcode.FORMAT_EAN_8, Barcode.FORMAT_EAN_13, Barcode.FORMAT_UPC_A,
                    Barcode.FORMAT_UPC_E, Barcode.FORMAT_CODE_39, Barcode.FORMAT_CODE_93,
                    Barcode.FORMAT_CODE_128, Barcode.FORMAT_ITF, Barcode.FORMAT_CODABAR -> {
                        itemViewHolder.imgQRCode.setImageDrawable(
                            viewHolder.itemView.rootView.context.getDrawable(
                                R.drawable.ic_barcode_green
                            )
                        )
                        itemViewHolder.tvQRCodedesc.text =
                            viewHolder.itemView.rootView.context.getString(R.string.barcode)
                    }

                    Barcode.FORMAT_DATA_MATRIX -> {
                        itemViewHolder.imgQRCode.setImageDrawable(
                            viewHolder.itemView.rootView.context.getDrawable(
                                R.drawable.ic_data_matrix
                            )
                        )
                        itemViewHolder.tvQRCodedesc.text =
                            viewHolder.itemView.rootView.context.getString(R.string.barcode)

                    }

                    Barcode.FORMAT_PDF417 -> {
                        itemViewHolder.imgQRCode.setImageDrawable(
                            viewHolder.itemView.rootView.context.getDrawable(
                                R.drawable.ic_pdf_417
                            )
                        )
                        itemViewHolder.tvQRCodedesc.text =
                            viewHolder.itemView.rootView.context.getString(R.string.barcode)
                    }

                    Barcode.FORMAT_AZTEC -> {
                        itemViewHolder.imgQRCode.setImageDrawable(
                            viewHolder.itemView.rootView.context.getDrawable(
                                R.drawable.ic_aztec
                            )
                        )
                        itemViewHolder.tvQRCodedesc.text =
                            viewHolder.itemView.rootView.context.getString(R.string.barcode)

                    }
                }


            }

            else -> { /// Ads
                val itemViewHolder = (viewHolder as? AdsViewHolder).guardLet { return }!!
                itemViewHolder.aperoNativeAds.setLayoutLoading(R.layout.loading_native_small)

                /// Bind ads
                val adsNative = qrCodeHistoryItemChild.itemAds.guardLet { return }!!
                itemViewHolder.aperoNativeAds.populateNativeAdView(activity, adsNative)
            }
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mLists.size
    }
}
