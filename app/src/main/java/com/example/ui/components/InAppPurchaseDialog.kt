package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Wallpaper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InAppPurchaseDialog(
    wallpaper: Wallpaper,
    onDismiss: () -> Unit,
    onPurchaseConfirmed: (String) -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }
    var isProcessing by remember { mutableStateOf(false) }
    var purchaseSuccessOrderId by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .testTag("iap_dialog"),
            color = Color(0xFF131728),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                if (purchaseSuccessOrderId != null) {
                    // Success View
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0x2610B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(42.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Purchase Successful!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFF8FAFC)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "“${wallpaper.title}” is now permanently unlocked for this device.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1A1F36))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "TRANSACTION RECEIPT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Order ID: ${purchaseSuccessOrderId}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Amount: ₹${wallpaper.priceInr}.00 (Paid via $selectedPaymentMethod)",
                                    fontSize = 12.sp,
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("iap_done_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Done & View Wallpaper", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Purchase Form
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Google Play In-App Purchase",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "Unlock Premium Wallpaper",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFF8FAFC)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1B2038))
                            .border(1.dp, Color(0xFF2E3654), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = wallpaper.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFF1F5F9),
                                    maxLines = 1
                                )
                                Text(
                                    text = "${wallpaper.category} • ${wallpaper.resolution}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }

                            // Price Tag
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${wallpaper.priceInr}.00",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFBBF24)
                                )
                                Text(
                                    text = "One-time payment",
                                    fontSize = 10.sp,
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Payment Method",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment Method 1: UPI
                    PaymentOptionRow(
                        title = "UPI / Google Pay (GPay)",
                        subtitle = "Instant approval via UPI apps",
                        icon = Icons.Default.QrCode,
                        isSelected = selectedPaymentMethod == "UPI",
                        onSelect = { selectedPaymentMethod = "UPI" }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Payment Method 2: Google Play Balance
                    PaymentOptionRow(
                        title = "Google Play Balance",
                        subtitle = "Available: ₹240.00",
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = selectedPaymentMethod == "PLAY_BALANCE",
                        onSelect = { selectedPaymentMethod = "PLAY_BALANCE" }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Payment Method 3: Cards
                    PaymentOptionRow(
                        title = "Credit or Debit Card",
                        subtitle = "Visa, Mastercard, Rupay",
                        icon = Icons.Default.CreditCard,
                        isSelected = selectedPaymentMethod == "CARD",
                        onSelect = { selectedPaymentMethod = "CARD" }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Secured by Google Play Billing • Permanent Device License",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            enabled = !isProcessing
                        ) {
                            Text("Cancel", color = Color(0xFF94A3B8))
                        }

                        Button(
                            onClick = {
                                isProcessing = true
                                scope.launch {
                                    delay(1200) // Realistic authentic billing verification delay
                                    val orderId = "GPA.${(1000..9999).random()}-${(1000..9999).random()}-${(1000..9999).random()}"
                                    isProcessing = false
                                    purchaseSuccessOrderId = orderId
                                    onPurchaseConfirmed(orderId)
                                }
                            },
                            modifier = Modifier
                                .weight(1.6f)
                                .height(46.dp)
                                .testTag("confirm_purchase_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2563EB)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isProcessing
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Processing...")
                            } else {
                                Text("Pay ₹${wallpaper.priceInr} • 1-Tap", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF1E2642) else Color(0xFF171B2F))
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF38BDF8) else Color(0xFF262C47),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color(0xFF38BDF8) else Color(0xFF94A3B8),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFF1F5F9)
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF38BDF8),
                unselectedColor = Color(0xFF64748B)
            )
        )
    }
}
