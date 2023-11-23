package com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme

import net.glxn.qrgen.core.scheme.Schema
import net.glxn.qrgen.core.scheme.SchemeUtil
import java.util.Locale

class VSMS : Schema() {
    private val SMS = "sms"
    var number: String? = null
    var subject: String? = null


    override fun parseSchema(code: String?): Schema {
        require(
            !(code == null || !code.trim { it <= ' ' }.lowercase(Locale.getDefault())
                .startsWith(SMS))
        ) { "this is not a valid sms code: $code" }
        val parameters = SchemeUtil.getParameters(code.trim { it <= ' ' }
            .lowercase(Locale.getDefault()))
        if (parameters.containsKey(SMS)) {
            number = (parameters[SMS])
        }
        if (number != null && parameters.containsKey(number)) {
            subject = (parameters[number])
        }
        return this
    }

    override fun generateString(): String {
        return SMS + "to:" + number + if (subject != null) ":$subject" else ""
    }

    override fun toString(): String {
        return generateString()
    }

    companion object {
        fun parse(code: String): VSMS {
            val sms = VSMS()
            sms.parseSchema(code)
            return sms
        }
    }
}
