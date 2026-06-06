package org.myprofileapp.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Composable 1: ProfileHeader ──────────────────────────────
@Composable
fun ProfileHeader(name: String, title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6750A4))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Avatar circle (Box untuk stack)
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD0BCFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto profil",
                    modifier = Modifier.size(56.dp),
                    tint = Color(0xFF21005D)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFFD0BCFF)
            )
        }
    }
}

// ── Composable 2: InfoItem ────────────────────────────────────
@Composable
fun InfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6750A4),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Composable 3: ProfileCard ─────────────────────────────────
@Composable
fun ProfileCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6750A4),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

// ── App utama ─────────────────────────────────────────────────
@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Header (Box di dalamnya)
            ProfileHeader(
                name = "Mega Zayyani",
                title = "Mahasiswa Teknik Informatika — ITERA"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Card bio
            ProfileCard(title = "TENTANG SAYA") {
                var expanded by remember { mutableStateOf(false) }

                Text(
                    text = "Saya adalah mahasiswa yang antusias dalam " +
                            "pengembangan aplikasi mobile.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )

                // ← Ini yang dapat bonus +10%
                AnimatedVisibility(visible = expanded) {
                    Text(
                        text = "\n\nSaya tertarik pada Kotlin Multiplatform, " +
                                "Compose, dan pengembangan UI lintas platform. " +
                                "Saat ini sedang belajar State Management dan MVVM.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Tampilkan lebih sedikit ↑"
                    else "Tampilkan selengkapnya ↓")
                }
            }

            // Card informasi kontak
            ProfileCard(title = "INFORMASI KONTAK") {
                InfoItem(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = "mega.123140180@student.itera.ac.id"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                InfoItem(
                    icon = Icons.Default.Phone,
                    label = "Telepon",
                    value = "+62 851-5883-2215"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                InfoItem(
                    icon = Icons.Default.LocationOn,
                    label = "Lokasi",
                    value = "Lampung Selatan, Indonesia"
                )
            }

            // Tombol aksi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Edit Profil")
                }
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Bagikan")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}