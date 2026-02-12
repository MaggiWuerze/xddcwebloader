package de.maggiwuerze.xdccwebloader.util

import java.text.DecimalFormat

object FilesizeFormatter {
    fun createAutoReadableString(sizeInBytes: Long): String {
        val hrSize: String?

        val b = sizeInBytes.toDouble()
        val k = sizeInBytes / 1024.0
        val m = ((sizeInBytes / 1024.0) / 1024.0)
        val g = (((sizeInBytes / 1024.0) / 1024.0) / 1024.0)
        val t = ((((sizeInBytes / 1024.0) / 1024.0) / 1024.0) / 1024.0)

        val dec = DecimalFormat("0.00")

        if (t > 1) {
            hrSize = "${dec.format(t)} TB"
        } else if (g > 1) {
            hrSize = "${dec.format(g)} GB"
        } else if (m > 1) {
            hrSize = "${dec.format(m)} MB"
        } else if (k > 1) {
            hrSize = "${dec.format(k)} KB"
        } else {
            hrSize = "${dec.format(b)} Bytes"
        }

        return hrSize
    }
}
