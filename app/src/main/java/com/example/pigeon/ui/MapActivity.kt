package com.example.pigeon.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

// --- 1. Color Theme (Extracted from your Tailwind Config) ---
val BeigeBg = Color(0xFFF0EDE6)
val BeigeSurface = Color(0xFFFDFBF7)
val BeigeBorder = Color(0xFFE5E0D6)
val InkDark = Color(0xFF2C333A)
val InkMuted = Color(0xFF64748B)
val CalmBlue = Color(0xFF5B8BDF)
val CalmRed = Color(0xFFD65D5D)
val CalmYellow = Color(0xFFD9A536)
val CalmOrange = Color(0xFFD97706)

@Composable
fun MapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBg)
    ) {
        // --- Top Header ---
        TopHeader()

        // --- Main Map Area ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFE8E4DA)) // Map placeholder color
        ) {
            // 1. The Map Background (Placeholder for MapLibre)
            // In a real app, MapView goes here. For now, we simulate the grid texture.
            MapBackgroundGrid()

            // 2. User Location (Center Pulse Animation)
            Box(modifier = Modifier.align(Alignment.Center)) {
                UserLocationPulse()
            }

            // 3. Markers (Positioned via BiasAlignment to match percentages)
            // Fire Hazard (Top 40%, Left 38%)
            MapMarker(
                xBias = -0.24f, yBias = -0.2f,
                color = CalmRed,
                icon = Icons.Default.PriorityHigh,
                label = "Fire Hazard",
                showAction = true
            )

            // Water (Top 20%, Right 25%) -> Right 25% is 75% width
            MapMarker(
                xBias = 0.5f, yBias = -0.6f,
                color = CalmBlue,
                icon = Icons.Default.WaterDrop,
                label = "Water"
            )

            // Conflict (Top 35%, Left 75%)
            MapMarker(
                xBias = 0.5f, yBias = -0.3f,
                color = CalmOrange,
                icon = Icons.Default.WarningAmber,
                label = "Conflict",
                isConflict = true
            )

            // Med Assist (Bottom 35%, Left 25%)
            MapMarker(
                xBias = -0.5f, yBias = 0.3f,
                color = CalmYellow,
                icon = Icons.Default.MedicalServices,
                label = "Med Assist"
            )

            // 4. UI Overlays
            // Search Bar (Top Left)
            Box(modifier = Modifier.padding(16.dp).align(Alignment.TopStart)) {
                GlassSearchBar()
            }

            // Map Controls (Top Right)
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassIconButton(Icons.Default.NearMe)
                GlassControlGroup()
            }

            // Action Buttons (Bottom Center)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(
                    text = "Report",
                    icon = Icons.Default.Emergency,
                    color = CalmRed,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Need Help",
                    icon = Icons.Default.HelpCenter,
                    color = CalmYellow,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // --- Bottom Navigation ---
        BottomNavBar()
    }
}

// --- Components ---

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BeigeSurface) // Background colors the status bar area
            .statusBarsPadding()      // <--- ADDS SAFE SPACE AT THE TOP
            .border(1.dp, BeigeBorder) // Border draws below the status bar
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ... (Keep your existing content inside here: Mesh Status, Time, etc.)
        // Mesh Status
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CalmBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Sensors, contentDescription = null, tint = CalmBlue)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(10.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                        .border(1.dp, BeigeSurface, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "MESH ACTIVE",
                    color = InkDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Connected • Low Latency",
                    color = InkMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Time / Sync Status
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "2m ago",
                color = InkDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "SYNCED",
                color = InkMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun UserLocationPulse() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Restart
        ), label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Restart
        ), label = "alpha"
    )

    Box(contentAlignment = Alignment.Center) {
        // Outer Radar Ping
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .alpha(alpha)
                .background(CalmBlue.copy(alpha = 0.3f), CircleShape)
        )
        // Static Center Ring
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(CalmBlue.copy(alpha = 0.1f), CircleShape)
                .border(1.dp, CalmBlue.copy(alpha = 0.2f), CircleShape)
        )
        // Center Dot
        Box(
            modifier = Modifier
                .size(16.dp)
                .shadow(4.dp, CircleShape)
                .background(CalmBlue, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

@Composable
fun MapMarker(
    xBias: Float,
    yBias: Float,
    color: Color,
    icon: ImageVector,
    label: String,
    showAction: Boolean = false,
    isConflict: Boolean = false
) {
    // We use a Box scope with alignment to position markers relatively
    // xBias/yBias ranges from -1 (left/top) to 1 (right/bottom)
    Box(
        modifier = Modifier
            .fillMaxSize() // Takes up the whole map area to allow alignment
            .wrapContentSize(Align(xBias, yBias)) // Position the child specifically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Marker Icon
            Box(
                modifier = Modifier
                    .size(if (isConflict) 56.dp else 48.dp)
                    .shadow(8.dp, CircleShape)
                    .background(color, CircleShape)
                    .border(4.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Label Chip
            if (isConflict) {
                GlassLabel(icon = Icons.Default.SyncProblem, text = "Conflict", color = color)
            } else {
                GlassLabel(text = label, color = if(label == "Med Assist") InkDark else if(label=="Water") InkDark else color)
            }

            // Optional Action Button (like "Resolve")
            if (showAction) {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = CalmBlue),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RESOLVE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Custom Alignment Helper
data class Align(val x: Float, val y: Float) : Alignment {
    override fun align(size: androidx.compose.ui.unit.IntSize, space: androidx.compose.ui.unit.IntSize, layoutDirection: androidx.compose.ui.unit.LayoutDirection): androidx.compose.ui.unit.IntOffset {
        // Convert bias -1..1 to position
        val centerX = (space.width - size.width) / 2f
        val centerY = (space.height - size.height) / 2f
        val offsetX = centerX + (centerX * x)
        val offsetY = centerY + (centerY * y)
        return androidx.compose.ui.unit.IntOffset(offsetX.toInt(), offsetY.toInt())
    }
}

@Composable
fun GlassSearchBar() {
    Row(
        modifier = Modifier
            .width(280.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(50))
            .background(BeigeSurface.copy(alpha = 0.85f))
            .border(1.dp, BeigeBorder.copy(alpha = 0.6f), RoundedCornerShape(50))
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BeigeBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Radar, contentDescription = null, tint = InkMuted)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Lat: 40.7128, Lon: -74.0060",
            color = InkDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun GlassIconButton(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(BeigeSurface.copy(alpha = 0.85f))
            .border(1.dp, BeigeBorder.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = InkDark)
    }
}

@Composable
fun GlassControlGroup() {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(BeigeSurface.copy(alpha = 0.85f))
            .border(1.dp, BeigeBorder.copy(alpha = 0.6f), RoundedCornerShape(50))
            .padding(4.dp)
    ) {
        IconButton(onClick = {}) { Icon(Icons.Default.Add, contentDescription = null, tint = InkDark) }
        Divider(color = InkDark.copy(alpha = 0.1f), modifier = Modifier.width(24.dp).align(Alignment.CenterHorizontally))
        IconButton(onClick = {}) { Icon(Icons.Default.Remove, contentDescription = null, tint = InkDark) }
    }
}

@Composable
fun GlassLabel(text: String, color: Color, icon: ImageVector? = null) {
    Row(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(6.dp))
            .border(1.dp, BeigeBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text.uppercase(),
            color = if(text == "Med Assist" || text == "Water") InkDark else color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(64.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text.uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun BottomNavBar() {
    Surface( // Wrapping in a Surface can help manage the system area color
        color = BeigeSurface,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Pushes icons/text above the gesture bar
                .padding(vertical = 12.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavLoopItem(icon = Icons.Default.Map, label = "Map", isActive = true, color = CalmBlue)
            NavLoopItem(icon = Icons.Default.History, label = "Log")
            NavLoopItem(icon = Icons.Default.Sync, label = "Sync", hasBadge = true)
            NavLoopItem(icon = Icons.Default.Person, label = "Profile")
        }
    }
}

@Composable
fun NavLoopItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean = false,
    color: Color = InkMuted,
    hasBadge: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isActive) CalmBlue.copy(alpha = 0.1f) else Color.Transparent)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = if (isActive) CalmBlue else color,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (hasBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                        .size(8.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                        .border(1.dp, BeigeSurface, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) CalmBlue else color,
            letterSpacing = 0.5.sp
        )
    }
}

// Simple Grid Pattern for Map Background
@Composable
fun MapBackgroundGrid() {
    // This is a dummy background to simulate the "City Map Texture"
    // In production, MapLibre MapView replaces this.
    Box(modifier = Modifier.fillMaxSize().alpha(0.3f)) {
        // You could add an Image(painterResource(id = R.drawable.map_bg)...) here
    }
}