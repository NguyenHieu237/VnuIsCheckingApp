package com.bkplus.scan_qrcode_barcode.ui.generator

import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem
import com.bkplus.scan_qrcode_barcode.model.QRCodeTypeEnum
import javax.inject.Inject

class GeneratorViewModel @Inject constructor(): ViewModel() {
    fun makeListItem(): ArrayList<GeneratorItem> {
        val items = ArrayList<GeneratorItem>()
        //Personal
        items.add(GeneratorItem(R.string.clipboard, R.drawable.ic_clipboard, type = QRCodeTypeEnum.CLIPBOARD.type))
        items.add(GeneratorItem(R.string.website, R.drawable.ic_website, type = QRCodeTypeEnum.WEBSITE.type))
        items.add(GeneratorItem(R.string.wifi, R.drawable.ic_wifi, type = QRCodeTypeEnum.WIFI.type))
        items.add(GeneratorItem(R.string.text, R.drawable.ic_text, type = QRCodeTypeEnum.TEXT.type))
        items.add(GeneratorItem(R.string.contact, R.drawable.ic_contacts, type = QRCodeTypeEnum.CONTACT.type))
        items.add(GeneratorItem(R.string.phone, R.drawable.ic_phone, type = QRCodeTypeEnum.PHONE.type))
        items.add(GeneratorItem(R.string.email, R.drawable.ic_email, type = QRCodeTypeEnum.EMAIL.type))
        items.add(GeneratorItem(R.string.sms, R.drawable.ic_sms, type = QRCodeTypeEnum.SMS.type))
        items.add(GeneratorItem(R.string.calendar, R.drawable.ic_calendar, type = QRCodeTypeEnum.CALENDER.type))
        items.add(GeneratorItem(R.string.card, R.drawable.ic_mycard, type = QRCodeTypeEnum.CARD.type))
        return items
    }

    fun makeListItem2(): ArrayList<GeneratorItem> {
        val items = ArrayList<GeneratorItem>()
        //Social Media
        items.add(GeneratorItem(R.string.facebook, R.drawable.ic_facebook, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.instagram, R.drawable.ic_instagram, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.messenger, R.drawable.ic_messenger, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.youtube, R.drawable.ic_youtube, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.linkedin, R.drawable.ic_linkedin, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.telegram, R.drawable.ic_telegram, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.twitter, R.drawable.ic_twitter, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        items.add(GeneratorItem(R.string.paypal, R.drawable.ic_paypal, type = QRCodeTypeEnum.SOCIAL_MEDIA.type))
        return items
    }
}