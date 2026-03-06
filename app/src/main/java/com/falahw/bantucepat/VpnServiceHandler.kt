package com.falahw.bantucepat

import android.content.Context
import android.util.Log
import libv2ray.Libv2ray

object VpnServiceHandler {
    private var isRunning = false

    fun startVpn(context: Context, vmessUrl: String) {
        if (isRunning) return

        val configJson = V2RayHelper.parseVmessUrl(vmessUrl)
        if (configJson.isEmpty()) {
            Log.e("VPN_BANTU", "Config JSON Kosong!")
            return
        }

        try {
            // Berdasarkan screenshot libgojni.so, ini kemungkinan besar Gomobile
            // Package biasanya 'libv2ray' dan class 'Libv2ray'
            
            // Mencoba memanggil fungsi start (biasanya runV2ray atau sejenisnya)
            Libv2ray.runV2ray(configJson)
            
            isRunning = true
            Log.d("VPN_BANTU", "VPN Dijalankan menggunakan Libv2ray")
        } catch (e: Exception) {
            Log.e("VPN_BANTU", "Gagal Memanggil Core: ${e.message}")
        }
    }

    fun stopVpn() {
        try {
            Libv2ray.stopV2ray()
            isRunning = false
            Log.d("VPN_BANTU", "VPN Dimatikan")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
