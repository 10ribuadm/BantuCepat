package com.falahw.bantucepat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.falahw.bantucepat.ui.theme.BantuCepatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BantuCepatTheme {
                val navController = rememberNavController()
                val dataStoreManager = remember { DataStoreManager(this) }
                val factory = remember { 
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(dataStoreManager) as T
                        }
                    }
                }
                val viewModel: MainViewModel = viewModel(factory = factory)
                val licenseKey by viewModel.licenseKey.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = if (licenseKey.isNullOrEmpty()) "license" else "main"
                ) {
                    composable("license") {
                        LicenseScreen(viewModel) {
                            navController.navigate("main") {
                                popUpTo("license") { inclusive = true }
                            }
                        }
                    }
                    composable("main") {
                        MainScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun LicenseScreen(viewModel: MainViewModel, onLicenseSaved: () -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Masukkan Lisensi Anda", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Kode Lisensi") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.saveLicense(text)
                    onLicenseSaved()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Lisensi")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val isActivated by viewModel.isActivated.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bantu Cepat VPN") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Status: ${if (isActivated) "Aktif" else "Belum Aktif"}")
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn {
                items(sampleServers) { server ->
                    ServerItem(server, isActivated, onActivate = {
                        val whatsappUrl = "https://wa.me/628123456789?text=Halo%20saya%20mau%20aktivasi%20VPN%20paket%20sebulan"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                        context.startActivity(intent)
                    }, onConnect = {
                        // Memanggil fungsi Konek VPN
                        VpnServiceHandler.startVpn(context, server.vmessUrl)
                    })
                }
            }

            if (!isActivated) {
                Spacer(modifier = Modifier.weight(1.0f))
                Button(
                    onClick = { viewModel.activateApp() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simulasi Aktivasi (Debug)")
                }
            } else {
                Spacer(modifier = Modifier.weight(1.0f))
                Button(
                    onClick = { VpnServiceHandler.stopVpn() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Stop VPN")
                }
            }
        }
    }
}

@Composable
fun ServerItem(
    server: VpnConfig,
    isActivated: Boolean,
    onActivate: () -> Unit,
    onConnect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(server.name, style = MaterialTheme.typography.titleMedium)
                Text(server.type, style = MaterialTheme.typography.bodySmall)
            }
            if (isActivated) {
                Button(onClick = onConnect) {
                    Text("Konek Sekarang")
                }
            } else {
                Button(onClick = onActivate, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                    Text("Hubungi WA")
                }
            }
        }
    }
}
