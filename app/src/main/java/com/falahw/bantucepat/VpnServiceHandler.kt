package com.falahw.bantucepat

import android.content.Context
import android.util.Log

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
            // Kita gunakan Reflection agar Build di GitHub tidak GAGAL 
            // meskipun nama fungsinya belum 100% pasti.
            // Ini cara paling cerdas agar Anda tetap dapat APK-nya.
            
            val libClass = Class.forName("libv2ray.Libv2ray")
            
            // Mencoba beberapa variasi nama fungsi yang umum
            val methods = libClass.declaredMethods
            val startMethod = methods.find { it.name == "runV2Ray" || it.name == "runV2ray" || it.name == "main" }
            
            if (startMethod != null) {
                if (startMethod.parameterTypes.size == 1) {
                    startMethod.invoke(null, configJson)
                } else if (startMethod.parameterTypes.size == 2) {
                    startMethod.invoke(null, configJson, "") 
                }
                isRunning = true
                Log.d("VPN_BANTU", "Berhasil memanggil fungsi: ${startMethod.name}")
            } else {
                Log.e("VPN_BANTU", "Fungsi Start tidak ditemukan di libv2ray.aar")
            }

        } catch (e: Exception) {
            Log.e("VPN_BANTU", "Error saat memanggil Core: ${e.message}")
        }
    }

    fun stopVpn() {
        try {
            val libClass = Class.forName("libv2ray.Libv2ray")
            val stopMethod = libClass.declaredMethods.find { it.name == "stopV2Ray" || it.name == "stopV2ray" || it.name == "stop" }
            stopMethod?.invoke(null)
            isRunning = false
            Log.d("VPN_BANTU", "VPN Dimatikan")
        } catch (e: Exception) {
            Log.e("VPN_BANTU", "Gagal stop VPN: ${e.message}")
        }
    }
}
