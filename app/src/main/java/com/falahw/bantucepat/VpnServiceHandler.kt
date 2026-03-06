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
            // Kita coba panggil library v2ray.
            // Berdasarkan screenshot, libv2ray menggunakan gomobile.
            // Kita harus panggil setup dan start jika ada.
            
            val libClass = Class.forName("libv2ray.Libv2ray")
            
            // Cari semua method yang tersedia untuk debug
            val methods = libClass.declaredMethods
            methods.forEach { Log.d("VPN_BANTU", "Method tersedia: ${it.name}") }

            // Coba panggil 'init' jika ada (beberapa lib butuh ini)
            val initMethod = methods.find { it.name.lowercase().contains("init") }
            initMethod?.let {
                Log.d("VPN_BANTU", "Memanggil init...")
                if (it.parameterTypes.isEmpty()) it.invoke(null)
            }

            // Cari method untuk start
            val startMethod = methods.find { 
                val name = it.name.lowercase()
                name == "runv2ray" || name == "startv2ray" || name == "main"
            }
            
            if (startMethod != null) {
                Log.d("VPN_BANTU", "Memanggil ${startMethod.name}")
                when (startMethod.parameterTypes.size) {
                    0 -> startMethod.invoke(null)
                    1 -> startMethod.invoke(null, configJson)
                    2 -> startMethod.invoke(null, configJson, "")
                }
                isRunning = true
            } else {
                Log.e("VPN_BANTU", "Method start TIDAK DITEMUKAN!")
            }

        } catch (e: Exception) {
            Log.e("VPN_BANTU", "Error: ${e.message}")
        }
    }

    fun stopVpn() {
        try {
            val libClass = Class.forName("libv2ray.Libv2ray")
            val stopMethod = libClass.declaredMethods.find { 
                val name = it.name.lowercase()
                name == "stopv2ray" || name == "stop" 
            }
            stopMethod?.invoke(null)
            isRunning = false
        } catch (e: Exception) {
            Log.e("VPN_BANTU", "Gagal stop: ${e.message}")
        }
    }
}
