package com.falahw.bantucepat

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor

class MyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Di sini nantinya akan memanggil V2Ray Core
        // Untuk saat ini hanya placeholder agar tidak error
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVpn()
    }

    private fun stopVpn() {
        vpnInterface?.close()
        vpnInterface = null
    }
}
