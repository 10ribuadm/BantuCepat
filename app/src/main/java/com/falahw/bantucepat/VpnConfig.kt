package com.falahw.bantucepat

data class VpnConfig(
    val name: String,
    val vmessUrl: String,
    val type: String = "VMess"
)

val sampleServers = listOf(
    VpnConfig(
        name = "XL WA Unlimited",
        vmessUrl = "vmess://ew0KICAidiI6ICIyIiwNCiAgInBzIjogIldIQVRTUCIsDQogICJhZGQiOiAiMTA0LjE4LjEuMTk2IiwNCiAgInBvcnQiOiAiODAiLA0KICAiaWQiOiAiYWQxOTlmMmMtZjZiZi00ZDI1LWE3YzItMDFiZWMxNjc0NDJmIiwNCiAgImFpZCI6ICIwIiwNCiAgInNjeSI6ICJhdXRvIiwNCiAgIm5ldCI6ICJ3cyIsDQogICJ0eXBlIjogIi0tLSIsDQogICJob3N0IjogInkucHRiYW5na2V0LmNvbSIsDQogICJwYXRoIjogIi92bWVzcyIsDQogICJ0bHMiOiAiIiwNCiAgInNuaSI6ICIiLA0KICAiYWxwbiI6ICIiLA0KICAiZnAiOiAiIiwNCiAgImluc2VjdXJlIjogIjAiDQp9"
    )
)
