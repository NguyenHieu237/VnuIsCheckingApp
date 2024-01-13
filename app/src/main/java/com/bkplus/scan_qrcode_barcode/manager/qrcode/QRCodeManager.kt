package com.bkplus.scan_qrcode_barcode.manager.qrcode

import android.graphics.Bitmap
import android.os.Parcelable
import android.text.TextUtils
import android.util.Patterns
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeType
import com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme.EventCard
import com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme.MVCard
import com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme.VSMS
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.parcelize.Parcelize
import net.glxn.qrgen.android.QRCode
import net.glxn.qrgen.core.scheme.*
import java.util.*


@Parcelize
class GenerateQRCodeResult(
    val qrCodeBitmap: Bitmap,
    val qrCodeContent: String,
    val resultType: Int,
    val qrCodeType: Int = QRCodeType.QRCODE.type,
    val needInsert: Boolean = true,
) : Parcelable

class QRCodeManager {
    companion object {
        fun instance() = QRCodeManager()
        fun isPhoneNumber(input: String): Boolean {
            if (input.length != 10) return false
            if (input[0] != '0') return false
            var dotOccurred = 0
            return input.all { it in '0'..'9' || it == '.' && dotOccurred++ < 1 } && input.length < 13 && input.length > 6
        }

        fun isValidCode39(input: String): Boolean {
            val regex = Regex(pattern = "^\\*[A-Z0-9\\-. $/+%]*\\*$")
            return regex.matches(input)

        }
        fun isValidEANandUCP(barcode: String): Boolean {
            val lastDigit = barcode.takeLast(1).toIntOrNull()
                ?: return false // Not a valid UPC/EAN

            val arr = barcode.dropLast(1).reversed()
            var oddTotal = 0
            var evenTotal = 0

            for (i in arr.indices) {
                val digit = arr[i].toString().toIntOrNull()
                    ?: return false // Not a valid UPC/EAN

                if (i % 2 == 0) {
                    oddTotal += digit * 3
                } else {
                    evenTotal += digit
                }
            }
            val checkSum = 10 - ((evenTotal + oddTotal) % 10)

            return checkSum == lastDigit
        }
        fun isValidTextInput(text:String): Boolean{
            return !(text.contains("    ") || text.contains("  ") || text.contains("   "))
        }
        fun isValid128(text:String): Boolean{
            return !text.contains(" ")
        }

        fun isValidITF(text: String): Boolean {
            // Check if the text contains an even number of digits
            if (text.length % 2 != 0) {
                return false
            }

            // Check if the text consists of only digits
            if (!text.matches(Regex("\\d+"))) {
                return false
            }

            return true
        }
        fun isValidUPCE(code: String): Boolean {
            // Check if the code consists of 8 digits
            if (code.length != 8 || !code.matches(Regex("\\d+"))) {
                return false
            }

            // Check for specific digit patterns and rules
            val systemDigit = code[0].toString().toInt()
            if (systemDigit != 0 && systemDigit != 1) {
                return false
            }

            val checkDigit = code[7].toString().toInt()
            if (checkDigit != calculateUPCECheckDigit(code.substring(0, 7))) {
                return false
            }

            // Additional checks for specific digit patterns and rules can be added here

            return true
        }
        fun calculateUPCECheckDigit(code: String): Int {
            var sum = 0
            for (i in 0 until code.length step 2) {
                sum += code[i].toString().toInt()
            }
            sum *= 3
            for (i in 1 until code.length step 2) {
                sum += code[i].toString().toInt()
            }
            val checkDigit = (10 - (sum % 10)) % 10
            return checkDigit
        }

        fun isValidEmail(target: CharSequence): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }
        private const val WHITE = -0x1
        private const val BLACK = -0x1000000
    }

    /**
     * Generate qr code for all
     */
    fun generateQRCodeAny(content: String): Bitmap {
        return QRCode.from(content).bitmap()
    }

    /**
     * Generate qr code for card
     */
    fun generateQRCodeCard(
        name: String,
        email: String,
        address: String,
        title: String,
        company: String,
        phoneNumber: String,
        birthday: String
    ): GenerateQRCodeResult {
        val card = MVCard()
        card.name = name
        card.email = email
        card.address = address
        card.company = company
        card.title = title
        card.phoneNumber = phoneNumber
        card.birthday = birthday
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(card.generateString()).bitmap(),
            qrCodeContent = card.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateEventQRCode(title: String,creator: String,date: String, address: String): GenerateQRCodeResult {
        val card = EventCard()
        card.title = title
        card.creator = creator
        card.address = address
        card.date = date
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(card.generateString()).bitmap(),
            qrCodeContent = card.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for calendar
     */
    fun generateQRCodeCalendar(
        title: String,
        startDate: Calendar,
        endDate: Calendar,
        description: String
    ): GenerateQRCodeResult {
        val event = IEventModified()
        event.start = TimeUtils.getTime(TimeUtils.TimeFormat.TimeFormat2, time = startDate.time)
        event.end = TimeUtils.getTime(TimeUtils.TimeFormat.TimeFormat2, time = endDate.time)
        event.summary = title
        event.description = description
//        event.organizer = "organizer"
//        event.stamp = "stamp"
        val generateString = event.generateString()
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(generateString).bitmap(),
            qrCodeContent = generateString,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for website
     */
    fun generateQRCodeWebsite(websiteUrl: String): GenerateQRCodeResult {
        val websiteEvent = Url()
        websiteEvent.url = websiteUrl
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(websiteEvent.generateString()).bitmap(),
            qrCodeContent = websiteEvent.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for wifi
     */
    fun generateQRCodeWifi(ssId: String, password: String, type: String): GenerateQRCodeResult {
        val wifiEvent = Wifi()
        wifiEvent.ssid = ssId
        wifiEvent.authentication = type
        wifiEvent.psk = password
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(wifiEvent).bitmap(),
            qrCodeContent = wifiEvent.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for text
     */
    fun generateQRCodeText(text: String): GenerateQRCodeResult {
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(text).bitmap(),
            qrCodeContent = text,
            resultType = QRCodeResult.CREATE.type,
        )

    }

    /**
     * Generate qr code for contact
     */
    fun generateQRCodeContact(name: String, phone: String, email: String): GenerateQRCodeResult {
        val contactEvent = MeCard()
        contactEvent.name = name
        contactEvent.telephone = phone
        contactEvent.email = email
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(contactEvent.generateString()).bitmap(),
            qrCodeContent = contactEvent.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for phone number
     */
    fun generateQRCodePhone(phone: String): GenerateQRCodeResult {
        val phoneEvent = Telephone()
        phoneEvent.telephone = phone
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(phoneEvent.generateString()).bitmap(),
            qrCodeContent = phoneEvent.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for email address
     */
    fun generateQRCodeEmail(email: String): GenerateQRCodeResult {
        val emailEvent = EMail(email)
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(emailEvent.generateString()).bitmap(),
            qrCodeContent = emailEvent.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    /**
     * Generate qr code for sms
     */
    fun generateQRCodeSms(phone: String, text: String): GenerateQRCodeResult {
        val smsEvent = VSMS()
        smsEvent.number = phone
        smsEvent.subject = text
        return GenerateQRCodeResult(
            qrCodeBitmap = QRCode.from(smsEvent.generateString()).bitmap(),
            qrCodeContent = smsEvent.generateString(),
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarEAN8Code(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.EAN_8, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarEAN13Code(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.EAN_13, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarUPCECode(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.UPC_E, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarUPCACode(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.UPC_A, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }


    fun generateBarCODE39Code(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.CODE_39, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarCODE93Code(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.CODE_93, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarCODE128Code(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.CODE_128, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarITFCode(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.ITF, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarCODABARCode(value: String): GenerateQRCodeResult {
        val bitmap = encodeAsBitmap(value, BarcodeFormat.CODABAR, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarPDF417Code(value: String): GenerateQRCodeResult {
        val bitmap: Bitmap = encodeAsBitmap(value, BarcodeFormat.PDF_417, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }

    fun generateBarDataMatricCode(value: String): GenerateQRCodeResult {
        val bitmap: Bitmap = encodeAsBitmap(value, BarcodeFormat.DATA_MATRIX, 350, 350)!!
        return GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }
    fun generateBarAZTECCode(value: String): GenerateQRCodeResult{
        val bitmap: Bitmap = encodeAsBitmap(value, BarcodeFormat.AZTEC, 350, 350)!!
        return  GenerateQRCodeResult(
            qrCodeBitmap =  bitmap,
            qrCodeContent = value,
            resultType = QRCodeResult.CREATE.type,
        )
    }


    @Throws(WriterException::class)
    fun encodeAsBitmap(
        contents: String?,
        format: BarcodeFormat?,
        img_width: Int,
        img_height: Int
    ): Bitmap? {
        val contentsToEncode = contents ?: return null
        var hints: MutableMap<EncodeHintType?, Any?>? = null
        val encoding = guessAppropriateEncoding(contentsToEncode)
        if (encoding != null) {
            hints = EnumMap(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = encoding
        }
        val writer = MultiFormatWriter()
        val result: BitMatrix = try {
            writer.encode(contentsToEncode, format, img_width, img_height, hints)
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result[x, y]) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun guessAppropriateEncoding(contents: CharSequence): String? {
        // Very crude at the moment
        for (element in contents) {
            if (element.code > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }



}