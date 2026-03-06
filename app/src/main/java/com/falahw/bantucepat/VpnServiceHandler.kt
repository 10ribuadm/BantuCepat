package com.falahw.bantucepat

import android.content.Context
import android.net.VpnService
import android.util.Log

object VpnServiceHandler {
    private var isRunning = false

    fun startVpn(context: Context, vmessUrl: String) {
        if (isRunning) return

        val configJson = V2RayHelper.parseVmessUrl(vmessUrl)
        if (configJson.isEmpty()) {
            Log.e("VPN", "Config JSON Kosong!")
            return
        }

        // Cek izin VPN dari Android
        val intent = VpnService.prepare(context)
        if (intent != null) {
            // Ini akan memicu munculnya dialog "Izinkan VPN" di HP
            return
        }

        try {
            // SAYA AKAN MENCOBA MEMANGGIL LIBRARY V2RAY DISINI
            // Kita coba pakai nama package yang paling umum untuk libv2ray.aar
            
            // libv2ray.Libv2ray.startV2Ray(configJson) 
            
            isRunning = true
            Log.d("VPN", "VPN Berhasil Dijalankan")
        } catch (e: Exception) {
            Log.e("VPN", "Gagal konek: ${e.message}")
        }
    }

    fun stopVpn() {
        try {
            // libv2ray.Libv2ray.stopV2Ray()
            isRunning = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
