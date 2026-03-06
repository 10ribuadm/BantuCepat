package com.falahw.bantucepat

import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets

object V2RayHelper {

    /**
     * Mengkonversi VMess URL (base64) menjadi JSON config yang dimengerti V2Ray Core.
     * Catatan: Ini adalah versi simpel, Anda mungkin perlu menyesuaikan formatnya 
     * tergantung versi core libv2ray yang Anda pakai.
     */
    fun parseVmessUrl(vmessUrl: String): String {
        try {
            val base64Data = vmessUrl.removePrefix("vmess://")
            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
            val jsonString = String(decodedBytes, StandardCharsets.UTF_8)
            val vmessJson = JSONObject(jsonString)

            // Template JSON minimal untuk V2Ray Core
            val outJson = JSONObject().apply {
                put("log", JSONObject().apply { put("loglevel", "warning") })
                put("outbounds", listOf(
                    JSONObject().apply {
                        put("protocol", "vmess")
                        put("settings", JSONObject().apply {
                            put("vnext", listOf(
                                JSONObject().apply {
                                    put("address", vmessJson.getString("add"))
                                    put("port", vmessJson.getInt("port"))
                                    put("users", listOf(
                                        JSONObject().apply {
                                            put("id", vmessJson.getString("id"))
                                            put("alterId", vmessJson.getInt("aid"))
                                            put("security", "auto")
                                        }
                                    ))
                                }
                            ))
                        })
                        put("streamSettings", JSONObject().apply {
                            put("network", vmessJson.getString("net"))
                            put("security", vmessJson.getString("tls").takeIf { it.isNotEmpty() } ?: "none")
                            
                            // Pengaturan WS (Websocket) - Sering dipakai XL Inject
                            if (vmessJson.getString("net") == "ws") {
                                put("wsSettings", JSONObject().apply {
                                    put("path", vmessJson.getString("path"))
                                    put("headers", JSONObject().apply {
                                        put("Host", vmessJson.getString("host"))
                                    })
                                } )
                            }
                        })
                    },
                    // Direct outbound untuk koneksi lokal
                    JSONObject().apply {
                        put("protocol", "freedom")
                        put("tag", "direct")
                    }
                ))
            }
            return outJson.toString(4)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}
