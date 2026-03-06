package com.falahw.bantucepat

import android.content.Context
import android.net.VpnService

object VpnServiceHandler {
    private var isRunning = false

    fun startVpn(context: Context, vmessUrl: String) {
        if (isRunning) return

        val configJson = V2RayHelper.parseVmessUrl(vmessUrl)
        if (configJson.isEmpty()) return

        // Cek izin VPN
        val intent = VpnService.prepare(context)
        if (intent != null) {
            // Jika butuh izin, ini harus dipanggil dari Activity
            return
        }

        try {
            // TODO: Sesuaikan nama class V2RayCore dengan isi libv2ray.aar Anda
            // Contoh: libv2ray.Libv2ray.startCore(configJson)
            isRunning = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopVpn() {
        try {
            // TODO: Sesuaikan dengan isi libv2ray.aar Anda
            isRunning = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
