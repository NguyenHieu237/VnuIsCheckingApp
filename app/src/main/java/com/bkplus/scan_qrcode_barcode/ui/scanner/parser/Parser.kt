package com.bkplus.scan_qrcode_barcode.ui.scanner.parser

import com.bkplus.scan_qrcode_barcode.ui.qrcode.scheme.MVCard
import net.glxn.qrgen.core.scheme.MeCard

object Parser {
    fun parseVcard(contents: String): MVCard {
        val card = MVCard()
        if (contents.startsWith("BEGIN:")) {
            val tokens = contents.split("\n")
            for (i in tokens.indices) {
                if (tokens[i].startsWith("N:")) {
                    card.name = tokens[i].substring(2);
                } else if (tokens[i].startsWith("ORG:")) {
                    card.company = tokens[i].substring(4);
                } else if (tokens[i].startsWith("TEL:")) {
                    card.phoneNumber = tokens[i].substring(4);
                } else if (tokens[i].startsWith("URL:")) {
                    card.website = tokens[i].substring(4);
                } else if (tokens[i].startsWith("EMAIL:")) {
                    card.email = tokens[i].substring(6);
                } else if (tokens[i].startsWith("ADR:")) {
                    card.address = tokens[i].substring(4);
                } else if (tokens[i].startsWith("NOTE:")) {
                    card.note = tokens[i].substring(5);
                } else if (tokens[i].startsWith("BDAY:")) {
                    card.birthday = tokens[i].substring(5);
                }
            }
        }
        return card
    }

    fun parseMeCard(contents: String): MeCard {
        val card = MeCard()
        if (contents.startsWith("MECARD:")) {
            val tokens = contents.substring(7).split(";")
            for (i in tokens.indices) {
                if (tokens[i].startsWith("N:")) {
                    card.name = tokens[i].substring(2);
                } else if (tokens[i].startsWith("TEL:")) {
                    card.telephone = tokens[i].substring(4);
                } else if (tokens[i].startsWith("EMAIL:")) {
                    card.email = tokens[i].substring(6);
                }
            }
        }
        return card
    }
}