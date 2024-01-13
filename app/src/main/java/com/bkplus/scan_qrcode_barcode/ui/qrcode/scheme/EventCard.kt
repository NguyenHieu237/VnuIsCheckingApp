package com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme

import net.glxn.qrgen.core.scheme.Schema
import net.glxn.qrgen.core.scheme.SchemeUtil
import kotlin.math.E

class EventCard: Schema() {
    private val BEGIN_ECARD = "BEGIN:ECARD"
    private val CREATOR = "CREA"
    private val TITLE = "TITLE"
    private val EDAY = "EDAY"
    private val ADDRESS = "ADR"


    var title: String? = null
    var creator: String? = null
    var date: String? = null
    var address: String? = null
    override fun parseSchema(code: String?): Schema {
        require(!(code == null || !code.startsWith(BEGIN_ECARD))) { "this is not a valid VCARD code: $code" }
        val parameters = SchemeUtil.getParameters(code)
        if (parameters.containsKey(CREATOR)){
            creator = parameters[CREATOR]
        }
        if (parameters.containsKey(TITLE)) {
            title = parameters[TITLE]
        }

        if (parameters.containsKey(EDAY)) {
            date = parameters[EDAY]
        }

        if (parameters.containsKey(ADDRESS)) {
            address = (parameters[ADDRESS])
        }
        return this
    }

    override fun generateString(): String {
        val sb = StringBuilder()
        sb.append(BEGIN_ECARD).append(SchemeUtil.LINE_FEED)
        sb.append("VERSION:3.0").append(SchemeUtil.LINE_FEED)
        if (title != null) {
            sb.append(SchemeUtil.LINE_FEED).append(TITLE).append(":").append(title)
        }
        if (address != null) {
            sb.append(SchemeUtil.LINE_FEED).append(ADDRESS).append(":").append(address)
        }
        if (date != null) {
            sb.append(SchemeUtil.LINE_FEED).append(EDAY).append(":").append(date)
        }
        if (creator != null) {
            sb.append(SchemeUtil.LINE_FEED).append(CREATOR).append(":").append(creator)
        }
        return sb.toString()
    }
}