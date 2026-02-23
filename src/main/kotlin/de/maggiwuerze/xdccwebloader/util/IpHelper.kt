package de.maggiwuerze.xdccwebloader.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.MalformedURLException
import java.net.URL

object IpHelper {
    val publicIp: InetAddress
        get() {
            val urlString = "http://checkip.amazonaws.com/"
            var url: URL? = null
            try {
                url = URL(urlString)
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            }
            try {
                BufferedReader(InputStreamReader(url.openStream())).use { br ->
                    return InetAddress.getByName(br.readLine())
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
}
