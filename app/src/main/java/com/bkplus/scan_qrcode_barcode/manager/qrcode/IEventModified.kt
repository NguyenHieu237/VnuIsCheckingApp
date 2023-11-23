package com.bkplus.scan_qrcode_barcode.manager.qrcode

import net.glxn.qrgen.core.scheme.IEvent
import net.glxn.qrgen.core.scheme.SchemeUtil

class IEventModified: IEvent() {
    private val BEGIN_EVENT = "BEGIN:VEVENT"
    private val UID = "UID"
    private val STAMP = "DTSTAMP"
    private val ORGANIZER = "ORGANIZER"
    private val START = "DTSTART"
    private val END = "DTEND"
    private val SUMMARY = "SUMMARY"
    private val DESCRIPTION = "DESCRIPTION"
    var description: String? = null

    override fun generateString(): String {
        val sb = StringBuilder()
        sb.append(BEGIN_EVENT).append(SchemeUtil.LINE_FEED)
        if (uid != null) {
            sb.append(UID).append(":").append(uid).append(SchemeUtil.LINE_FEED)
        }
        if (stamp != null) {
            sb.append(STAMP).append(":").append(stamp).append(SchemeUtil.LINE_FEED)
        }
        if (organizer != null) {
            sb.append(ORGANIZER).append(";").append(organizer).append(SchemeUtil.LINE_FEED)
        }
        if (start != null) {
            sb.append(START).append(":").append(start).append(SchemeUtil.LINE_FEED)
        }
        if (end != null) {
            sb.append(END).append(":").append(end).append(SchemeUtil.LINE_FEED)
        }
        if (summary != null) {
            sb.append(SUMMARY).append(":").append(summary).append(SchemeUtil.LINE_FEED)
        }
        if (description != null) {
            sb.append(DESCRIPTION).append(":").append(description).append(SchemeUtil.LINE_FEED)
        }
        sb.append(SchemeUtil.LINE_FEED).append("END:VEVENT")
        return sb.toString()
    }
}