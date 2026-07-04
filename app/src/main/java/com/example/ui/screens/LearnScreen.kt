package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.data.entity.OfflinePack
import com.example.ui.PranaViewModel
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlinx.coroutines.delay

@Composable
fun LearnScreen(viewModel: PranaViewModel) {
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    val packs by viewModel.offlinePacks.collectAsState()

    var activePackId by remember { mutableStateOf<String?>("burns") }
    var bookmarks by remember { mutableStateOf(setOf<String>()) }

    val activePack = packs.find { it.id == activePackId } ?: packs.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
    ) {
        // Top video player player if a topic is active
        activePack?.let { pack ->
            SimulatedVideoPlayer(pack, viewModel, isHindi)
        }

        // List of Topics
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = if (isHindi) "प्राथमिक चिकित्सा कक्षा" else "First-Aid Educational Lessons",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            items(packs) { pack ->
                val isSelected = pack.id == activePackId
                val isBookmarked = bookmarks.contains(pack.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activePackId = pack.id }
                        .testTag("learn_topic_card_${pack.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) WarmBeige else Color.White
                    ),
                    border = if (isSelected) {
                        BorderStroke(2.dp, SageGreenPrimary)
                    } else {
                        BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Play icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (isSelected) ForestGreenAccent else SageGreenPrimary.copy(alpha = 0.15f),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else ForestGreenAccent
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) pack.titleHindi else pack.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = if (pack.isDownloaded) {
                                    if (isHindi) "ऑफ़लाइन उपलब्ध • वीडियो सीखें" else "Saved Offline • Play Video"
                                } else {
                                    if (isHindi) "स्ट्रीम करने के लिए टैप करें (ऑफ़लाइन सहेजें)" else "Tap to Stream (Save offline)"
                                },
                                fontSize = 12.sp,
                                color = OliveGreenSecondary
                            )
                        }

                        // Bookmark Button
                        IconButton(
                            onClick = {
                                bookmarks = if (isBookmarked) {
                                    bookmarks - pack.id
                                } else {
                                    bookmarks + pack.id
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) ForestGreenAccent else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Download status icon
                        IconButton(
                            onClick = {
                                if (pack.isDownloaded) {
                                    viewModel.removeOfflinePack(pack.id)
                                } else {
                                    viewModel.downloadOfflinePack(pack.id)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (pack.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                contentDescription = "Download Toggle",
                                tint = if (pack.isDownloaded) ForestGreenAccent else SageGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Spacer
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SimulatedVideoPlayer(pack: OfflinePack, viewModel: PranaViewModel, isHindi: Boolean) {
    var isPlaying by remember { mutableStateOf(true) }
    var speed by remember { mutableStateOf(1.0f) }
    var currentProgress by remember { mutableStateOf(0.1f) }
    var showSubtitles by remember { mutableStateOf(true) }
    var playOnYouTube by remember { mutableStateOf(true) }

    // Video duration and seeking simulation
    LaunchedEffect(isPlaying, speed, pack.id) {
        if (isPlaying) {
            while (currentProgress < 1.0f) {
                delay((1000 / speed).toLong())
                currentProgress = (currentProgress + 0.05f).coerceAtMost(1.0f)
            }
            isPlaying = false
            currentProgress = 0.0f // reset loop
        }
    }

    val subtitlesText = getSyncedSubtitle(pack.id, currentProgress, isHindi)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        // Visual Player frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            if (playOnYouTube) {
                YouTubeWebViewPlayer(
                    videoId = getYouTubeIdForPack(pack.id),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Simulated Animation Canvas for first-aid tutorial video
                VideoIllustrationCanvas(packId = pack.id, progress = currentProgress, isPlaying = isPlaying)
            }

            // Subtitle Overlay at the bottom center of video (only on AR Demo canvas as YouTube has its own subtitles)
            if (!playOnYouTube && showSubtitles && subtitlesText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = subtitlesText,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            // Playback overlay button when paused (only for AR Demo)
            if (!playOnYouTube && !isPlaying) {
                IconButton(
                    onClick = { isPlaying = true },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Custom seek slider and controllers under video
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF222822))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            if (!playOnYouTube) {
                Slider(
                    value = currentProgress,
                    onValueChange = { currentProgress = it },
                    colors = SliderDefaults.colors(
                        thumbColor = SageGreenPrimary,
                        activeTrackColor = SageGreenPrimary,
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!playOnYouTube) {
                        // Play/Pause
                        IconButton(onClick = { isPlaying = !isPlaying }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.FastForward else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause Toggle",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Speed controller
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    speed = when (speed) {
                                        1.0f -> 1.5f
                                        1.5f -> 2.0f
                                        else -> 1.0f
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${speed}x",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = if (isHindi) "यूट्यूब वीडियो प्लेयर" else "YouTube Assist Player",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Mode Selector: YouTube Stream or Offline-friendly AR Demo
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (playOnYouTube) Color(0xFFE53935) else Color.Transparent)
                            .clickable { playOnYouTube = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "YouTube 🎥",
                            color = if (playOnYouTube) Color.White else Color.LightGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (!playOnYouTube) SageGreenPrimary else Color.Transparent)
                            .clickable { playOnYouTube = false }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isHindi) "एनिमेशन 🎨" else "Demo 🎨",
                            color = if (!playOnYouTube) Color.White else Color.LightGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Audio narrator trigger
                    IconButton(
                        onClick = {
                            viewModel.speak(subtitlesText)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Narrate Voice",
                            tint = Color.White
                        )
                    }

                    if (!playOnYouTube) {
                        // Caption toggle
                        IconButton(onClick = { showSubtitles = !showSubtitles }) {
                            Icon(
                                imageVector = Icons.Default.Subtitles,
                                contentDescription = "Subtitle Toggle",
                                tint = if (showSubtitles) SageGreenPrimary else Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoIllustrationCanvas(packId: String, progress: Float, isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "vid_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val center = Offset(size.width / 2, size.height / 2)

        when (packId) {
            "burns" -> {
                // Water flow illustration
                drawCircle(
                    color = Color(0xFF64B5F6).copy(alpha = 0.3f),
                    radius = 90f * pulse,
                    center = center
                )
                // Draw cool falling water stream
                for (i in 0..4) {
                    val streamOffset = (progress * 150f + i * 25f) % 150f
                    drawCircle(
                        color = Color(0xFF2196F3),
                        radius = 8f,
                        center = Offset(center.x, center.y - 80f + streamOffset)
                    )
                }
            }
            "cuts" -> {
                // Hand pressing clean cloth on wound illustration
                drawCircle(color = Color(0xFFEF9A9A).copy(alpha = 0.4f), radius = 80f, center = center)
                drawRect(
                    color = Color.White,
                    topLeft = Offset(center.x - 50f, center.y - 20f * pulse),
                    size = Size(100f, 40f * pulse),
                    style = Stroke(width = 4f)
                )
                drawCircle(color = ForestGreenAccent, radius = 10f, center = center)
            }
            "cpr" -> {
                // Two hands compressing rhythmic indicator
                drawCircle(
                    color = SageGreenPrimary.copy(alpha = 0.5f),
                    radius = 80f * pulse,
                    center = center,
                    style = Stroke(width = 6f)
                )
                drawCircle(color = Color.White, radius = 30f, center = center)
            }
            else -> {
                // General healing illustration
                drawCircle(
                    color = SageGreenPrimary.copy(alpha = 0.25f),
                    radius = 70f * pulse,
                    center = center
                )
                drawCircle(
                    color = ForestGreenAccent,
                    radius = 40f,
                    center = center,
                    style = Stroke(width = 3f)
                )
            }
        }
    }
}

fun getSyncedSubtitle(packId: String, progress: Float, isHindi: Boolean): String {
    return when (packId) {
        "burns" -> when {
            progress < 0.25f -> if (isHindi) "जले हुए घाव को तुरंत बहते ठंडे पानी के नीचे लाएं।" else "Immediately bring the burn wound under cold running water."
            progress < 0.50f -> if (isHindi) "घाव को कम से कम बीस मिनट तक ठंडे पानी से ठंडा करें।" else "Cool the area with running water for at least twenty minutes."
            progress < 0.75f -> if (isHindi) "घाव पर कभी भी मक्खन, बर्फ या टूथपेस्ट न लगाएं।" else "Never apply butter, ice, or toothpaste on the burn wound."
            else -> if (isHindi) "इसे साफ सूखे कपड़े से धीरे से ढकें।" else "Gently secure the area using a sterile, clean dry cloth."
        }
        "cuts" -> when {
            progress < 0.25f -> if (isHindi) "एक साफ कपड़े से सीधे चोट पर मजबूत दबाव डालें।" else "Apply direct, firm pressure on the bleeding cut using a clean cloth."
            progress < 0.50f -> if (isHindi) "दबाव तब तक बनाए रखें जब तक खून बहना पूरी तरह से रुक न जाए।" else "Maintain this compression until the active bleeding completely halts."
            progress < 0.75f -> if (isHindi) "चोट वाले क्षेत्र को साफ पानी से धीरे से धोएं।" else "Wash the cut gently with clean water to prevent local infection."
            else -> if (isHindi) "एंटीसेप्टिक दवा लगाएं और पट्टी बांधें।" else "Apply antiseptic cream and secure with a clean, dry bandage."
        }
        "cpr" -> when {
            progress < 0.25f -> if (isHindi) "मरीज के छाती के ठीक बीच में अपने दोनों हाथ रखें।" else "Place both of your hands locked in the middle of the chest."
            progress < 0.50f -> if (isHindi) "100 संपीड़न प्रति मिनट की रफ्तार से तेज़ी से दबाएं।" else "Push fast and hard at a compression speed of 100 beats per minute."
            progress < 0.75f -> if (isHindi) "दबाने के बाद छाती को पूरी तरह से ऊपर आने दें।" else "Let the chest rebound fully after each rhythmic chest compression."
            else -> if (isHindi) "हर तीस दबाव के बाद दो बार कृत्रिम सांस दें।" else "Provide two mouth rescue breaths after every thirty compressions."
        }
        else -> ""
    }
}

@Composable
fun YouTubeWebViewPlayer(videoId: String, modifier: Modifier = Modifier) {
    val embedUrl = "https://www.youtube.com/embed/$videoId?autoplay=1&mute=1&playlist=$videoId&loop=1"
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.apply {
                    javaScriptEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                }
                loadUrl(embedUrl)
            }
        },
        modifier = modifier,
        update = { webView ->
            val currentUrl = webView.url
            if (currentUrl == null || !currentUrl.contains(videoId)) {
                webView.loadUrl(embedUrl)
            }
        }
    )
}

fun getYouTubeIdForPack(packId: String): String {
    return when (packId) {
        "burns" -> "Ea_I7HreNf0"
        "cuts" -> "NxO5LvgqSqs"
        "cpr" -> "0aV9YSW48QQ"
        "choking" -> "PA9hpOnvtCk"
        "fracture" -> "2v8S7_GInpE"
        "snakebite" -> "t_XG00XJb7A"
        "bleeding" -> "NxO5LvgqSqs"
        else -> "0aV9YSW48QQ"
    }
}
