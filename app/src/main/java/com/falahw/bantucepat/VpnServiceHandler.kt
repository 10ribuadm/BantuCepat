package com.falahw.bantucepat

import android.content.Context
import android.content.Intent
import android.net.VpnService
import dev.v2ray.core.V2rayCore

object VpnServiceHandler {
    private var isRunning = false

    fun startVpn(context: Context, vmessUrl: String) {
        if (isRunning) return

        val configJson = V2RayHelper.parseVmessUrl(vmessUrl)
        if (configJson.isEmpty()) return

        // Cek izin VPN
        val intent = VpnService.prepare(context)
        if (intent != null) {
            // Jika butuh izin, ini harus dipanggil dari Activity (startActivityForResult)
            return
        }

        // Jalankan Core V2Ray (Asumsi library libv2ray.aar menyediakan method ini)
        // Catatan: Method ini tergantung pada isi libv2ray.aar yang Anda masukkan
        try {
            // Contoh pemanggilan start (sesuaikan dengan isi .aar Anda)
            // V2rayCore.start(context, configJson)
            isRunning = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopVpn() {
        try {
            // V2rayCore.stop()
            isRunning = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
