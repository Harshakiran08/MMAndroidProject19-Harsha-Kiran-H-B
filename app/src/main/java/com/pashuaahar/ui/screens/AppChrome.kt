package com.pashuaahar.ui.screens

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.pashuaahar.R

data class FarmPalette(
    val background: Color,
    val backgroundAlt: Color,
    val surface: Color,
    val surfaceStrong: Color,
    val surfaceMuted: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val accent: Color,
    val accentStrong: Color,
    val accentSoft: Color,
    val highlight: Color
)

@Suppress("UNUSED_PARAMETER")
@Composable
fun rememberFarmPalette(isDark: Boolean): FarmPalette {
    return FarmPalette(
        background = Color(0xFFFFFDF3),
        backgroundAlt = Color(0xFFF7F4D8),
        surface = Color(0xFFFFFFFF),
        surfaceStrong = Color(0xFFF2F7E8),
        surfaceMuted = Color(0xFFE6F0CC),
        textPrimary = Color(0xFF294B1E),
        textSecondary = Color(0xFF5E7A2F),
        border = Color(0x336F8F2F),
        accent = Color(0xFF6F8F2F),
        accentStrong = Color(0xFF3F6B1B),
        accentSoft = Color(0xFFDCE9AE),
        highlight = Color(0xFFD9B43B)
    )
}

@Composable
fun FarmBackground(
    palette: FarmPalette,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp)
                .background(
                    palette.backgroundAlt,
                    shape = RoundedCornerShape(bottomStart = 42.dp, bottomEnd = 42.dp)
                )
                .align(Alignment.TopCenter)
                .alpha(0.95f)
        )
        Image(
            painter = painterResource(id = R.drawable.cow_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(palette.accentStrong),
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.04f)
        )
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .offset(x = (20 + index * 72).dp, y = (56 + (index % 2) * 16).dp)
                    .size(if (index % 2 == 0) 10.dp else 7.dp)
                    .background(palette.accent.copy(alpha = 0.10f), CircleShape)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 92.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size((72 - index * 8).dp)
                        .background(palette.accent.copy(alpha = 0.06f), CircleShape)
                )
            }
        }
        content()
    }
}

@Composable
fun LogoBadge(
    modifier: Modifier = Modifier,
    palette: FarmPalette,
    containerSize: androidx.compose.ui.unit.Dp,
    logoSize: androidx.compose.ui.unit.Dp
) {
    Surface(
        modifier = modifier.size(containerSize),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(2.dp, palette.accent),
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(logoSize)
            )
        }
    }
}

@Composable
fun PashuTopBar(
    title: String,
    onBackClick: () -> Unit,
    palette: FarmPalette
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(palette.surfaceStrong.copy(alpha = 0.95f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = palette.textPrimary
            )
        }
        Text(
            text = title,
            color = palette.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun FarmScrollPage(
    palette: FarmPalette,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
    verticalSpacing: androidx.compose.ui.unit.Dp = 18.dp,
    showTopBar: Boolean = false,
    title: String = "",
    onBackClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    FarmBackground(palette = palette, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            if (showTopBar) {
                PashuTopBar(title = title, onBackClick = onBackClick, palette = palette)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                content = content
            )
        }
    }
}

@Composable
fun farmTextFieldColors(palette: FarmPalette) = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = palette.surface,
    unfocusedContainerColor = palette.surfaceStrong,
    focusedBorderColor = palette.accent,
    unfocusedBorderColor = palette.border,
    focusedLabelColor = palette.accent,
    unfocusedLabelColor = palette.textSecondary,
    focusedTextColor = palette.textPrimary,
    unfocusedTextColor = palette.textPrimary,
    cursorColor = palette.accent,
    focusedPlaceholderColor = palette.textSecondary,
    unfocusedPlaceholderColor = palette.textSecondary
)

@Composable
fun farmCardShape() = RoundedCornerShape(24.dp)

@Composable
fun PressableCard(
    modifier: Modifier = Modifier,
    palette: FarmPalette,
    containerColor: Color = palette.surface,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.97f else 1f, label = "pressScale")
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .scale(scale)
            .then(clickableModifier),
        shape = farmCardShape(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(content = content)
    }
}

@Composable
fun ModuleHeroCard(
    palette: FarmPalette,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    PressableCard(
        modifier = modifier.fillMaxWidth(),
        palette = palette,
        containerColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .background(palette.accentStrong)
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp)
            Text(subtitle, color = Color.White.copy(alpha = 0.88f), lineHeight = 21.sp)
        }
    }
}

fun showFarmToast(context: Context, message: String, palette: FarmPalette) {
    val density = context.resources.displayMetrics.density
    val horizontalPadding = (18 * density).toInt()
    val verticalPadding = (12 * density).toInt()
    val radius = 18 * density
    val iconContainer = (38 * density).toInt()
    val iconInset = (6 * density).toInt()
    val spacing = (10 * density).toInt()

    val logoHolder = LinearLayout(context).apply {
        background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(android.graphics.Color.WHITE)
            setStroke((2 * density).toInt(), palette.accent.toArgb())
        }
        layoutParams = LinearLayout.LayoutParams(iconContainer, iconContainer)
        gravity = android.view.Gravity.CENTER
        addView(
            ImageView(context).apply {
                setImageResource(R.drawable.app_logo)
                scaleType = ImageView.ScaleType.FIT_CENTER
                setPadding(iconInset, iconInset, iconInset, iconInset)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            }
        )
    }

    val textView = TextView(context).apply {
        text = message
        setTextColor(android.graphics.Color.WHITE)
        textSize = 14f
        setPadding(0, verticalPadding, horizontalPadding, verticalPadding)
    }

    val row = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = android.view.Gravity.CENTER_VERTICAL
        setPadding(horizontalPadding, 0, 0, 0)
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radius
            setColor(palette.accentStrong.toArgb())
            setStroke((1 * density).toInt(), palette.highlight.toArgb())
        }
        addView(logoHolder)
        addView(
            textView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = spacing
            }
        )
    }

    Toast(context).apply {
        duration = Toast.LENGTH_SHORT
        view = row
        show()
    }
}
