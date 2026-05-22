package com.example.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.InsertLink
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.PortfolioItem

// Design System Premium Palette (Luxury Jet & Radiant Gold)
val JetBlack = Color(0xFF0C0D10)
val DarkCharcoal = Color(0xFF13141A)
val MediumGray = Color(0xFF1F202A)
val MetallicGold = Color(0xFFFFD700)
val PaleGold = Color(0xFFE5C158)
val WarmAccent = Color(0xFFFFA726)
val OffWhite = Color(0xFFF3F4F6)
val TransparentGold = Color(0x11FFD700)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val portfolioItems by viewModel.portfolioItems.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    val showPasswordDialog by viewModel.showPasswordDialog.collectAsStateWithLifecycle()
    val showAdminPanel by viewModel.showAdminPanel.collectAsStateWithLifecycle()
    val passwordError by viewModel.passwordError.collectAsStateWithLifecycle()

    // State for Portfolio Category Filtering
    val categories = listOf("All", "Branding", "Poster Art", "Packaging", "UI/UX", "Other")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredItems = if (selectedCategory == "All") {
        portfolioItems
    } else {
        portfolioItems.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    // Colors mapping or custom drawer background
    val meshBackground = Brush.linearGradient(
        colors = listOf(JetBlack, DarkCharcoal, JetBlack),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1500f)
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(meshBackground),
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Main user-facing portfolio view
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Panel layout
                HeaderSection(
                    settings = settings,
                    onHeaderClick = {
                        viewModel.openPasswordDialog()
                    }
                )

                // Navigation Filter Pill Row
                CategoryFilterRow(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                // Scrollable content area
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Introduction text card
                    item {
                        IntroCard(settings = settings)
                    }

                    // Gallery Label
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(MetallicGold, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LATEST MASTERPIECES",
                                style = MaterialTheme.typography.labelLarge,
                                color = MetallicGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    // Empty state indicator
                    if (filteredItems.isEmpty()) {
                        item {
                            EmptyState()
                        }
                    }

                    // Portfolio Gallery list items
                    items(filteredItems, key = { it.id }) { item ->
                        PortfolioCard(
                            item = item,
                            isAdminMode = false,
                            onDelete = {}
                        )
                    }

                    // Contact & Links Card
                    item {
                        ContactSection(
                            settings = settings,
                            context = context
                        )
                    }
                }
            }

            // Absolute Floating Action for quick contact (or indicating Admin overlay back)
            if (showAdminPanel) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.logoutAdmin() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("admin_logout_floating_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Admin")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Exit Admin", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    // Password Challenge Overlay Modal
    if (showPasswordDialog) {
        PasswordOverlayDialog(
            errorText = passwordError,
            onSubmit = { password ->
                viewModel.verifyPassword(password)
            },
            onDismiss = {
                viewModel.closePasswordDialog()
            }
        )
    }

    // Fully-equipped Admin Dashboard Overlay Drawer/Modal
    if (showAdminPanel) {
        AdminDashboardModal(
            viewModel = viewModel,
            portfolioItems = portfolioItems,
            settings = settings,
            onDismiss = { viewModel.logoutAdmin() }
        )
    }
}

@Composable
fun HeaderSection(
    settings: Map<String, String>,
    onHeaderClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onHeaderClick)
            .testTag("hadi_designer_header")
            .background(
                Brush.verticalGradient(
                    colors = listOf(JetBlack.copy(alpha = 0.9f), Color.Transparent)
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Elegant Visual App Logo
            LogoRenderer(
                settings = settings,
                size = 46,
                modifier = Modifier.testTag("hadi_designer_header_logo")
            )
            
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Hadi Designer",
                    color = OffWhite,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "VISUAL COMPOSER & BRAND ARCHITECT",
                    color = MetallicGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun LogoRenderer(
    settings: Map<String, String>,
    size: Int,
    modifier: Modifier = Modifier
) {
    val logoType = settings["inner_logo_type"] ?: "preset"
    val presetName = settings["inner_logo_preset"] ?: "brush"
    val customText = settings["inner_logo_custom_text"] ?: "HD"
    val customUrl = settings["inner_logo_custom_url"] ?: ""

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(MediumGray, JetBlack)
                )
            )
            .border(BorderStroke(1.5.dp, MetallicGold), CircleShape)
            .padding(6.dp)
    ) {
        when (logoType) {
            "preset" -> {
                val icon = when (presetName) {
                    "brush" -> Icons.Default.Brush
                    "palette" -> Icons.Default.Palette
                    "design_services" -> Icons.Default.DesignServices
                    "gesture" -> Icons.Default.Gesture
                    else -> Icons.Default.Brush
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "Hadi Designer Logo",
                    tint = MetallicGold,
                    modifier = Modifier.size((size * 0.55).dp)
                )
            }
            "custom_text" -> {
                Text(
                    text = customText,
                    color = MetallicGold,
                    fontSize = (size * 0.35).sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }
            "custom_url" -> {
                if (customUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(customUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Hadi Custom Design Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    // Fallback to preset if URL empty
                    Icon(
                        imageVector = Icons.Default.Brush,
                        contentDescription = "Hadi Designer Logo Fallback",
                        tint = MetallicGold,
                        modifier = Modifier.size((size * 0.55).dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            val chipBackground = if (isSelected) MetallicGold else MediumGray.copy(alpha = 0.6f)
            val chipTextColor = if (isSelected) JetBlack else OffWhite
            val borderColors = if (isSelected) MetallicGold else MetallicGold.copy(alpha = 0.2f)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(chipBackground)
                    .border(BorderStroke(1.dp, borderColors), RoundedCornerShape(30.dp))
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .testTag("category_pill_$category")
            ) {
                Text(
                    text = category.uppercase(),
                    color = chipTextColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun IntroCard(settings: Map<String, String>) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCharcoal.copy(alpha = 0.85f),
            contentColor = OffWhite
        ),
        border = BorderStroke(1.dp, MetallicGold.copy(alpha = 0.15f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(TransparentGold, CircleShape)
                        .border(1.dp, MetallicGold.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Brush,
                        contentDescription = null,
                        tint = MetallicGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "THE CREATIVE VISION",
                    style = MaterialTheme.typography.titleSmall,
                    color = MetallicGold,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Welcome to my digital gallery. I materialize bold concepts into striking design ecosystems. Every curve, pixel, and letter is arranged with absolute typographic layout intent.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 22.sp,
                color = OffWhite.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.PhotoLibrary,
            contentDescription = "No artworks",
            tint = OffWhite.copy(alpha = 0.3f),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No projects in this category",
            style = MaterialTheme.typography.bodyMedium,
            color = OffWhite.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PortfolioCard(
    item: PortfolioItem,
    isAdminMode: Boolean,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCharcoal,
            contentColor = OffWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, MetallicGold.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column {
            // Project Image Showcase Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Background Fallback Aesthetic Shape
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MediumGray, JetBlack),
                                start = Offset(0f, 0f),
                                end = Offset(100f, 300f)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.title.firstOrNull()?.toString() ?: "H",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Black,
                        color = OffWhite.copy(alpha = 0.05f)
                    )
                }

                // Actual loaded Unsplash image via Coil
                if (item.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Showcase: ${item.title}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Category overlay tag (Top-Left)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(JetBlack.copy(alpha = 0.85f))
                        .border(BorderStroke(0.8.dp, MetallicGold), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        color = MetallicGold,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                // Admin Delete Button Overlay (Top-Right)
                if (isAdminMode) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.9f))
                            .size(36.dp)
                            .testTag("delete_item_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete item",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Description and Title section
            Column(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OffWhite,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Icon(
                        imageVector = if (expanded) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = "Extend detail",
                        tint = MetallicGold.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.description,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    lineHeight = 18.sp,
                    color = OffWhite.copy(alpha = 0.75f),
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!expanded) {
                    Text(
                        text = "Tap to read insight",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MetallicGold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactSection(
    settings: Map<String, String>,
    context: Context
) {
    val phone = settings["contact_phone"] ?: "0998638910"
    val whatsapp = settings["contact_whatsapp"] ?: "https://wa.me/0998638910"
    val instagram = settings["contact_instagram"] ?: "https://instagram.com/hadi.designer"
    val behance = settings["contact_behance"] ?: "https://behance.net/hadi_designer"
    val email = settings["contact_email"] ?: "hadi3mkgamer@gmail.com"

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCharcoal,
            contentColor = OffWhite
        ),
        border = BorderStroke(1.5.dp, MetallicGold.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "COMMISSION A DESIGN",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MetallicGold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Let's work together to construct your ultimate aesthetic statement.",
                fontSize = 12.sp,
                color = OffWhite.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Channels Rows - Interactive targets
            ContactChannelRow(
                icon = Icons.Default.Call,
                label = "Phone Consultation",
                value = phone,
                testTag = "contact_phone_button",
                onClick = {
                    try {
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_DIAL,
                            android.net.Uri.parse("tel:$phone")
                        )
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open dialer", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ContactChannelRow(
                icon = Icons.Default.Share,
                label = "WhatsApp Instant Chat",
                value = "@HadiSavesChat",
                testTag = "contact_whatsapp_button",
                onClick = {
                    try {
                        val fixedUrl = if (!whatsapp.startsWith("http")) "https://wa.me/$phone" else whatsapp
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(fixedUrl)
                        )
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not launch WhatsApp", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ContactChannelRow(
                icon = Icons.Default.Smartphone,
                label = "Instagram Portfolio",
                value = "hadi.designer",
                testTag = "contact_instagram_button",
                onClick = {
                    try {
                        val fixedUrl = if (!instagram.startsWith("http")) "https://instagram.com/$instagram" else instagram
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(fixedUrl)
                        )
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ContactChannelRow(
                icon = Icons.Default.InsertLink,
                label = "Behance Case Studies",
                value = "hadi_designer",
                testTag = "contact_behance_button",
                onClick = {
                    try {
                        val fixedUrl = if (!behance.startsWith("http")) "https://behance.net/$behance" else behance
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(fixedUrl)
                        )
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ContactChannelRow(
                icon = Icons.Default.AlternateEmail,
                label = "Direct Email",
                value = email,
                testTag = "contact_email_button",
                onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                            data = android.net.Uri.parse("mailto:$email")
                            putExtra(android.content.Intent.EXTRA_SUBJECT, "Inquiry for Hadi Designer")
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "No email client found", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun ContactChannelRow(
    icon: ImageVector,
    label: String,
    value: String,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MediumGray.copy(alpha = 0.5f))
            .border(BorderStroke(1.dp, OffWhite.copy(alpha = 0.05f)), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MetallicGold,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OffWhite.copy(alpha = 0.5f),
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OffWhite
            )
        }
    }
}

@Composable
fun PasswordOverlayDialog(
    errorText: String?,
    onSubmit: (String) -> Boolean,
    onDismiss: () -> Unit
) {
    var passwordInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp,
            color = DarkCharcoal,
            border = BorderStroke(1.dp, MetallicGold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MetallicGold,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "SEALED WORKSPACE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MetallicGold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter Password overlay code to open portfolio controls.",
                    fontSize = 12.sp,
                    color = OffWhite.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password", color = OffWhite.copy(alpha = 0.6f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OffWhite,
                        unfocusedTextColor = OffWhite,
                        focusedBorderColor = MetallicGold,
                        unfocusedBorderColor = OffWhite.copy(alpha = 0.2f),
                        cursorColor = MetallicGold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input_field")
                )

                if (errorText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorText,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = OffWhite),
                        modifier = Modifier.testTag("password_cancel_button")
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            onSubmit(passwordInput)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MetallicGold,
                            contentColor = JetBlack
                        ),
                        modifier = Modifier.testTag("password_submit_button")
                    ) {
                        Text("Unlock", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Full admin panel interface layout in a secure full screen Dialog or Modal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardModal(
    viewModel: PortfolioViewModel,
    portfolioItems: List<PortfolioItem>,
    settings: Map<String, String>,
    onDismiss: () -> Unit
) {
    var adminTab by remember { mutableStateOf("Portfolio") } // Portfolio, Logo, Contacts

    // State bindings for logo editing
    var logoType by remember(settings) { mutableStateOf(settings["inner_logo_type"] ?: "preset") }
    var logoPreset by remember(settings) { mutableStateOf(settings["inner_logo_preset"] ?: "brush") }
    var logoCustomText by remember(settings) { mutableStateOf(settings["inner_logo_custom_text"] ?: "HD") }
    var logoCustomUrl by remember(settings) { mutableStateOf(settings["inner_logo_custom_url"] ?: "") }

    // State bindings for adding a portfolio item
    var projTitle by remember { mutableStateOf("") }
    var projDesc by remember { mutableStateOf("") }
    var projImgUrl by remember { mutableStateOf("") }
    var projCat by remember { mutableStateOf("Branding") }

    // State bindings for contacts
    var conPhone by remember(settings) { mutableStateOf(settings["contact_phone"] ?: "0998638910") }
    var conWhatsapp by remember(settings) { mutableStateOf(settings["contact_whatsapp"] ?: "https://wa.me/0998638910") }
    var conInsta by remember(settings) { mutableStateOf(settings["contact_instagram"] ?: "https://instagram.com/hadi.designer") }
    var conBehance by remember(settings) { mutableStateOf(settings["contact_behance"] ?: "https://behance.net/hadi_designer") }
    var conEmail by remember(settings) { mutableStateOf(settings["contact_email"] ?: "hadi3mkgamer@gmail.com") }

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .border(BorderStroke(1.5.dp, MetallicGold), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = JetBlack
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header of Admin Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ADMIN CONTROL PORTAL",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MetallicGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Live changes execute instantly",
                            fontSize = 11.sp,
                            color = OffWhite.copy(alpha = 0.5f)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .background(MediumGray, CircleShape)
                            .size(36.dp)
                            .testTag("admin_close_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = OffWhite)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selector Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MediumGray.copy(alpha = 0.4f)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Portfolio", "Logo", "Contacts").forEach { tab ->
                        val isSelected = adminTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { adminTab = tab }
                                .background(if (isSelected) MetallicGold else Color.Transparent)
                                .padding(vertical = 10.dp)
                                .testTag("admin_tab_$tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) JetBlack else OffWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable subcontent
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (adminTab) {
                        "Portfolio" -> {
                            // Section 1: Add project
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DarkCharcoal)
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "ADD NEW PORTFOLIO ITEM",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MetallicGold,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = projTitle,
                                        onValueChange = { projTitle = it },
                                        label = { Text("Project Title") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("add_project_title"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = projDesc,
                                        onValueChange = { projDesc = it },
                                        label = { Text("Description & Insights") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("add_project_description"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = projImgUrl,
                                        onValueChange = { projImgUrl = it },
                                        label = { Text("Image URL") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("add_project_image"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Category dropdown
                                    var expandedDrop by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedDrop,
                                        onExpandedChange = { expandedDrop = !expandedDrop }
                                    ) {
                                        OutlinedTextField(
                                            readOnly = true,
                                            value = projCat,
                                            onValueChange = {},
                                            label = { Text("Category") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDrop)
                                            },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = OffWhite,
                                                unfocusedTextColor = OffWhite,
                                                focusedBorderColor = MetallicGold,
                                                unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .menuAnchor()
                                                .testTag("add_project_category_selector")
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedDrop,
                                            onDismissRequest = { expandedDrop = false }
                                        ) {
                                            listOf("Branding", "Poster Art", "Packaging", "UI/UX", "Other").forEach { selectionOption ->
                                                DropdownMenuItem(
                                                    text = { Text(text = selectionOption) },
                                                    onClick = {
                                                        projCat = selectionOption
                                                        expandedDrop = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = {
                                            if (projTitle.isNotEmpty() && projDesc.isNotEmpty()) {
                                                viewModel.addPortfolioItem(
                                                    title = projTitle,
                                                    description = projDesc,
                                                    imageUrl = projImgUrl,
                                                    category = projCat
                                                )
                                                projTitle = ""
                                                projDesc = ""
                                                projImgUrl = ""
                                                Toast.makeText(context, "Project Added Successfully!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Please fill required columns", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MetallicGold, contentColor = JetBlack),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("submit_add_project")
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Publish Art Project", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Section 2: List current items with delete ability
                            item {
                                Text(
                                    text = "MANAGE CURRENT GALLERY (${portfolioItems.size} ITEMS)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MetallicGold,
                                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                                    letterSpacing = 1.sp
                                )
                            }

                            items(portfolioItems, key = { "admin_${it.id}" }) { item ->
                                PortfolioCard(
                                    item = item,
                                    isAdminMode = true,
                                    onDelete = {
                                        viewModel.deletePortfolioItem(item.id)
                                        Toast.makeText(context, "Deleted project", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                        "Logo" -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DarkCharcoal)
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Text(
                                        text = "EDIT HEADER LOGO STYLE",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MetallicGold,
                                        letterSpacing = 0.5.sp
                                    )

                                    // Toggle logo type
                                    Column {
                                        Text(text = "Logo Source Type", fontSize = 11.sp, color = OffWhite.copy(alpha = 0.6f))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MediumGray)
                                        ) {
                                            mapOf("preset" to "PRESET ICON", "custom_text" to "MONOGRAM", "custom_url" to "IMAGE URL").forEach { (type, label) ->
                                                val sel = logoType == type
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable { logoType = type }
                                                        .background(if (sel) MetallicGold else Color.Transparent)
                                                        .padding(vertical = 10.dp)
                                                        .testTag("logo_selector_$type"),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = label,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (sel) JetBlack else OffWhite
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Render choices dynamically
                                    when (logoType) {
                                        "preset" -> {
                                            Column {
                                                Text(text = "Select Preset Icon Symbol", fontSize = 11.sp, color = OffWhite.copy(alpha = 0.5f))
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    val icons = listOf("brush", "palette", "design_services", "gesture")
                                                    icons.forEach { iconKey ->
                                                        val isSelIcon = logoPreset == iconKey
                                                        val icDrawable = when (iconKey) {
                                                            "brush" -> Icons.Default.Brush
                                                            "palette" -> Icons.Default.Palette
                                                            "design_services" -> Icons.Default.DesignServices
                                                            else -> Icons.Default.Gesture
                                                        }
                                                        Box(
                                                            modifier = Modifier
                                                                .size(48.dp)
                                                                .clip(RoundedCornerShape(12.dp))
                                                                .background(if (isSelIcon) MetallicGold else MediumGray)
                                                                .clickable { logoPreset = iconKey }
                                                                .padding(10.dp)
                                                                .testTag("preset_icon_$iconKey"),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                imageVector = icDrawable,
                                                                contentDescription = iconKey,
                                                                tint = if (isSelIcon) JetBlack else OffWhite
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        "custom_text" -> {
                                            OutlinedTextField(
                                                value = logoCustomText,
                                                onValueChange = { logoCustomText = it.take(3) }, // Cap 3 letters
                                                label = { Text("Monogram Text (Max 3 chars)") },
                                                singleLine = true,
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = OffWhite,
                                                    unfocusedTextColor = OffWhite,
                                                    focusedBorderColor = MetallicGold,
                                                    unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("input_custom_text_logo")
                                            )
                                        }

                                        "custom_url" -> {
                                            OutlinedTextField(
                                                value = logoCustomUrl,
                                                onValueChange = { logoCustomUrl = it },
                                                label = { Text("Custom Vector/Image URL") },
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = OffWhite,
                                                    unfocusedTextColor = OffWhite,
                                                    focusedBorderColor = MetallicGold,
                                                    unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("input_custom_url_logo")
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = {
                                            viewModel.updateLogoSettings(
                                                type = logoType,
                                                preset = logoPreset,
                                                customText = logoCustomText,
                                                customUrl = logoCustomUrl
                                            )
                                            Toast.makeText(context, "Logo Updated Saved!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MetallicGold, contentColor = JetBlack),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("save_logo_settings_button"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Logo Settings", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        "Contacts" -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DarkCharcoal)
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Text(
                                        text = "MANAGE CONTACT CHANNELS",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MetallicGold,
                                        letterSpacing = 0.5.sp
                                    )

                                    OutlinedTextField(
                                        value = conPhone,
                                        onValueChange = { conPhone = it },
                                        label = { Text("Phone consultation") },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_contact_phone")
                                    )

                                    OutlinedTextField(
                                        value = conWhatsapp,
                                        onValueChange = { conWhatsapp = it },
                                        label = { Text("WhatsApp URL/Number") },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_contact_whatsapp")
                                    )

                                    OutlinedTextField(
                                        value = conInsta,
                                        onValueChange = { conInsta = it },
                                        label = { Text("Instagram Profile URL") },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_contact_instagram")
                                    )

                                    OutlinedTextField(
                                        value = conBehance,
                                        onValueChange = { conBehance = it },
                                        label = { Text("Behance Showcase URL") },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_contact_behance")
                                    )

                                    OutlinedTextField(
                                        value = conEmail,
                                        onValueChange = { conEmail = it },
                                        label = { Text("Email Contact Address") },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = OffWhite,
                                            unfocusedTextColor = OffWhite,
                                            focusedBorderColor = MetallicGold,
                                            unfocusedBorderColor = OffWhite.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_contact_email")
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = {
                                            viewModel.updateContactInfo(
                                                phone = conPhone,
                                                whatsapp = conWhatsapp,
                                                instagram = conInsta,
                                                behance = conBehance,
                                                email = conEmail
                                            )
                                            Toast.makeText(context, "Contact Info Saved!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MetallicGold, contentColor = JetBlack),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("save_contact_settings_button"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Contacts", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
