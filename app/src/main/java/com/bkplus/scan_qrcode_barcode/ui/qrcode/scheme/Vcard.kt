package com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme

import net.glxn.qrgen.core.scheme.Schema
import net.glxn.qrgen.core.scheme.SchemeUtil

class MVCard : Schema() {

    private val BEGIN_VCARD = "BEGIN:VCARD"
    private val NAME = "N"
    private val COMPANY = "ORG"
    private val TITLE = "TITLE"
    private val PHONE = "TEL"
    private val WEB = "URL"
    private val EMAIL = "EMAIL"
    private val ADDRESS = "ADR"
    private val NOTE = "NOTE"
    private val BDAY = "BDAY"

    var name: String? = null
    var company: String? = null
    var title: String? = null
    var phoneNumber: String? = null
    var email: String? = null
    var address: String? = null
    var website: String? = null
    var note: String? = null
    var birthday: String? = null

    override fun parseSchema(code: String?): Schema {
        require(!(code == null || !code.startsWith(BEGIN_VCARD))) { "this is not a valid VCARD code: $code" }
        val parameters = SchemeUtil.getParameters(code)
        if (parameters.containsKey(NAME)) {
            name = parameters[NAME]
        }
        if (parameters.containsKey(TITLE)) {
            title = (parameters[TITLE])
        }
        if (parameters.containsKey(COMPANY)) {
            company = (parameters[COMPANY])
        }
        if (parameters.containsKey(ADDRESS)) {
            address = (parameters[ADDRESS])
        }
        if (parameters.containsKey(EMAIL)) {
            email = (parameters[EMAIL])
        }
        if (parameters.containsKey(WEB)) {
            website = (parameters[WEB])
        }
        if (parameters.containsKey(PHONE)) {
            phoneNumber = (parameters[PHONE])
        }
        if (parameters.containsKey(NOTE)) {
            note = (parameters[NOTE])
        }
        if (parameters.containsKey(BDAY)) {
            birthday = (parameters[BDAY])
        }
        return this
    }

    override fun generateString(): String {
        val sb = StringBuilder()
        sb.append(BEGIN_VCARD).append(SchemeUtil.LINE_FEED)
        sb.append("VERSION:3.0").append(SchemeUtil.LINE_FEED)
        if (name != null) {
            sb.append(NAME).append(":").append(name)
        }
        if (company != null) {
            sb.append(SchemeUtil.LINE_FEED).append(COMPANY).append(":").append(company)
        }
        if (title != null) {
            sb.append(SchemeUtil.LINE_FEED).append(TITLE).append(":").append(title)
        }
        if (phoneNumber != null) {
            sb.append(SchemeUtil.LINE_FEED).append(PHONE).append(":").append(phoneNumber)
        }
        if (website != null) {
            sb.append(SchemeUtil.LINE_FEED).append(WEB).append(":").append(website)
        }
        if (email != null) {
            sb.append(SchemeUtil.LINE_FEED).append(EMAIL).append(":").append(email)
        }
        if (address != null) {
            sb.append(SchemeUtil.LINE_FEED).append(ADDRESS).append(":").append(address)
        }
        if (note != null) {
            sb.append(SchemeUtil.LINE_FEED).append(NOTE).append(":").append(note)
        }
        if (birthday != null) {
            sb.append(SchemeUtil.LINE_FEED).append(BDAY).append(":").append(birthday)
        }
        sb.append(SchemeUtil.LINE_FEED).append("END:VCARD")
        return sb.toString()
    }

    /**
     * Returns the textual representation of this vcard of the form
     *
     *
     * BEGIN:VCARD N:John Doe ORG:Company TITLE:Title TEL:1234 URL:www.example.org
     * EMAIL:john.doe@example.org ADR:Street END:VCARD
     *
     */
    override fun toString(): String {
        return generateString()
    }

    fun parse(code: String?): MVCard {
        val vCard = MVCard()
        vCard.parseSchema(code)
        return vCard
    }
}