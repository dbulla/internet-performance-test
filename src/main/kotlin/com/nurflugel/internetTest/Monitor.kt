package com.nurflugel.internetTest

import java.io.File
import java.io.FileOutputStream
import java.net.NetworkInterface
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.text.NumberFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.fixedRateTimer


class Monitor {


    companion object {

        @JvmStatic
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        @JvmStatic
        fun main(args: Array<String>) {
            val formattedTime: String = timeFormatter.format(LocalDateTime.now())
            val pingFile = File("$formattedTime pingFile.log")
            if (!pingFile.exists()) {
                pingFile.createNewFile()
            } else {
//                pingFile.delete()
            }
            val downloadFile = File("$formattedTime downloadFile.log")
            if (!downloadFile.exists()) {
                downloadFile.createNewFile()
            } else {
//                downloadFile.delete()
            }
            create10SecondTimer(pingFile)
            createDownloadTimer(downloadFile)
            while (true) {
                Thread.sleep(2_000)
            }
        }


        private fun createDownloadTimer(logFile: File) {
            fixedRateTimer(
                name = "download-timer",
                initialDelay = 3_000,
                period = 60_000,
                daemon = true
            ) {
                println("Download file ${timeFormatter.format(LocalDateTime.now())}")
                downloadFile(
                    "http://www.nurflugel.com/Home/pictures/2000-09-06_GemStone_Brokat_Party/partypics.zip",
                    "partypics.zip",
                    logFile
                )
            }
        }

        private fun create10SecondTimer(logFile: File) {
            fixedRateTimer(
                name = "ping-timer",
                initialDelay = 0,
                period = 10_000,
                daemon = true
            ) {
                println("ping! ${timeFormatter.format(LocalDateTime.now())}")
                downloadFile(
                    "https://www.nurflugel.com/clearpixel.gif",
                    "clearpixel.gif",
                    logFile
                )
            }
        }

        private fun downloadFile(url: String, outputFileName: String, logFile: File) {
            try {
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
                val start = Instant.now()
                // ping doesn't seem to work, so download a very small, light file
                downloadFile(URL(url), outputFileName)
                val duration = Duration.between(start, Instant.now())
                val file = File(outputFileName)
                val size: Long = Files.size(file.toPath())
                val durationMillis: Long = duration.toMillis()
                logFile.appendText(
                    "${
                        LocalDateTime.now().format(formatter)
                    }\t${numberFormat.format(durationMillis)}\t${
                        numberFormat.format(size * 1000 / durationMillis)} bytes/sec for ${
                        numberFormat.format(size)} bytes\n"
                )

                file.delete()
            } catch (e: Exception) {
                println("got an unexpected error ${e.message}")
            }
        }

        private fun pingUrl() {
            //                val ipAddress = "144.208.71.150"
            //                val geek = InetAddress.getByName(ipAddress)
            //                println("Sending Ping Request to $ipAddress")
            //                if (geek.isReachable(1000))
            //                    println("Host is reachable")
            //                else
            //                    println("Sorry ! We can't reach to this host")
        }

        private fun downloadFile(url: URL, outputFileName: String) {
            url.openStream().use {
                Channels.newChannel(it).use { rbc ->
                    FileOutputStream(outputFileName).use { fos ->
                        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                    }
                }
            }
        }
    }
}