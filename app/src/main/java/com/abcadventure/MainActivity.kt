package com.abcadventure

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

private data class Letter(
    val upper: String,
    val lower: String,
    val word: String,
    val emoji: String,
    val phonics: String,
    val color: Color
)

private val alphabet = listOf(
    Letter("A", "a", "Apple", "🍎", "aah", Color(0xFFFF6B6B)),
    Letter("B", "b", "Ball", "⚽", "buh", Color(0xFF4D96FF)),
    Letter("C", "c", "Cat", "🐱", "kuh", Color(0xFFFFB84D)),
    Letter("D", "d", "Dog", "🐶", "duh", Color(0xFF9B7EDE)),
    Letter("E", "e", "Elephant", "🐘", "eh", Color(0xFF5CC8A1)),
    Letter("F", "f", "Fish", "🐟", "fuh", Color(0xFF45B7D1)),
    Letter("G", "g", "Grapes", "🍇", "guh", Color(0xFF8E7CFF)),
    Letter("H", "h", "Hat", "🎩", "huh", Color(0xFFFF8E72)),
    Letter("I", "i", "Ice cream", "🍦", "ih", Color(0xFFFF82B2)),
    Letter("J", "j", "Juice", "🧃", "juh", Color(0xFFFFA94D)),
    Letter("K", "k", "Kite", "🪁", "kuh", Color(0xFF4FB5FF)),
    Letter("L", "l", "Lion", "🦁", "luh", Color(0xFFFFC857)),
    Letter("M", "m", "Monkey", "🐒", "muh", Color(0xFFB47AEA)),
    Letter("N", "n", "Nest", "🪺", "nuh", Color(0xFF64C4A2)),
    Letter("O", "o", "Orange", "🍊", "oh", Color(0xFFFF914D)),
    Letter("P", "p", "Penguin", "🐧", "puh", Color(0xFF6574CD)),
    Letter("Q", "q", "Queen", "👑", "kwuh", Color(0xFFDA77E8)),
    Letter("R", "r", "Rabbit", "🐰", "ruh", Color(0xFFFF7EA8)),
    Letter("S", "s", "Sun", "☀️", "suh", Color(0xFFFFB52E)),
    Letter("T", "t", "Tiger", "🐯", "tuh", Color(0xFFFF7657)),
    Letter("U", "u", "Umbrella", "☂️", "uh", Color(0xFF4DA3FF)),
    Letter("V", "v", "Van", "🚐", "vuh", Color(0xFF57C7A3)),
    Letter("W", "w", "Whale", "🐳", "wuh", Color(0xFF46B6D8)),
    Letter("X", "x", "Xylophone", "🎵", "ks", Color(0xFF9A7BFF)),
    Letter("Y", "y", "Yo-yo", "🪀", "yuh", Color(0xFFFF719D)),
    Letter("Z", "z", "Zebra", "🦓", "zuh", Color(0xFF777777))
)

class MainActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { result ->
            if (result == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(0.82f)
                tts.setPitch(1.08f)
            }
        }
        setContent { ABCAdventure(tts) }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

private fun speak(tts: TextToSpeech, text: String) {
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "abc-${System.currentTimeMillis()}")
}

@Composable
private fun ABCAdventure(tts: TextToSpeech) {
    var screen by remember { mutableStateOf(AppScreen.WELCOME) }
    var childName by remember { mutableStateOf("") }
    var childAge by remember { mutableStateOf("") }
    var letterIndex by remember { mutableIntStateOf(0) }
    var hasTapped by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }

    val background = Brush.verticalGradient(
        listOf(Color(0xFFFFF7E8), Color(0xFFEAF7FF), Color(0xFFF6ECFF))
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6750E8),
            onPrimary = Color.White,
            background = Color(0xFFFFF9F0),
            surface = Color.White,
            onSurface = Color(0xFF252038)
        )
    ) {
        Box(Modifier.fillMaxSize().background(background)) {
            FloatingBubbles()
            when (screen) {
                AppScreen.WELCOME -> WelcomeScreen(
                    name = childName,
                    onNameChange = { childName = it },
                    age = childAge,
                    onAgeChange = { childAge = it },
                    onStart = {
                        letterIndex = 0
                        hasTapped = false
                        screen = AppScreen.LESSON
                        speak(tts, "Let's learn the alphabet, $childName!")
                    }
                )
                AppScreen.LESSON -> LessonScreen(
                    childName = childName,
                    letter = alphabet[letterIndex],
                    index = letterIndex,
                    hasTapped = hasTapped,
                    isSpeaking = isSpeaking,
                    onTapLetter = {
                        if (!hasTapped) {
                            hasTapped = true
                            isSpeaking = true
                            val current = alphabet[letterIndex]
                            speak(tts, "Letter ${current.upper}. ${current.upper} says ${current.phonics}. ${current.upper} is for ${current.word}.")
                        }
                    },
                    onReplay = {
                        val current = alphabet[letterIndex]
                        isSpeaking = true
                        speak(tts, "Letter ${current.upper}. ${current.upper} says ${current.phonics}. ${current.upper} is for ${current.word}.")
                    },
                    onAutoAdvance = {
                        if (hasTapped) {
                            isSpeaking = false
                            if (letterIndex < alphabet.lastIndex) {
                                letterIndex++
                                hasTapped = false
                            } else {
                                screen = AppScreen.COMPLETE
                            }
                        }
                    },
                    onBack = {
                        tts.stop()
                        hasTapped = false
                        isSpeaking = false
                        screen = AppScreen.WELCOME
                    }
                )
                AppScreen.COMPLETE -> CompleteScreen(
                    name = childName,
                    onReplay = {
                        letterIndex = 0
                        hasTapped = false
                        isSpeaking = false
                        screen = AppScreen.LESSON
                        speak(tts, "Let's play again! Letter A.")
                    },
                    onHome = {
                        tts.stop()
                        hasTapped = false
                        screen = AppScreen.WELCOME
                    }
                )
            }
        }
    }
}

private enum class AppScreen { WELCOME, LESSON, COMPLETE }

@Composable
private fun FloatingBubbles() {
    val infinite = rememberInfiniteTransition(label = "bubbles")
    val drift by infinite.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "drift"
    )
    Box(Modifier.fillMaxSize()) {
        Text("✦", fontSize = 30.sp, color = Color(0xFFFFB74D), modifier = Modifier.offset(24.dp, 70.dp + drift.dp))
        Text("●", fontSize = 18.sp, color = Color(0xFF79C7FF), modifier = Modifier.align(Alignment.TopEnd).offset((-38).dp, 130.dp - drift.dp))
        Text("✦", fontSize = 24.sp, color = Color(0xFFFF8FB3), modifier = Modifier.align(Alignment.BottomStart).offset(42.dp, (-120).dp - drift.dp))
        Text("●", fontSize = 20.sp, color = Color(0xFF9BD9B6), modifier = Modifier.align(Alignment.BottomEnd).offset((-35).dp, (-210).dp + drift.dp))
    }
}

@Composable
private fun WelcomeScreen(
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    onStart: () -> Unit
) {
    val validAge = age.toIntOrNull()?.let { it in 2..12 } == true
    val canStart = name.trim().isNotEmpty() && validAge

    Column(
        Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌈", fontSize = 64.sp)
        Text("ABC Adventure", fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color(0xFF5B49D8))
        Text("A happy little journey from A to Z!", fontSize = 18.sp, color = Color(0xFF615A70))
        Spacer(Modifier.height(26.dp))

        Card(
            modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .96f))
        ) {
            Column(Modifier.padding(22.dp)) {
                Text("Who is learning today?", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { if (it.length <= 24) onNameChange(it) },
                    label = { Text("My name") },
                    placeholder = { Text("e.g. Aarav") },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { if (it.length <= 2 && it.all(Char::isDigit)) onAgeChange(it) },
                    label = { Text("My age") },
                    placeholder = { Text("2–12") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text("Age helps us make the adventure feel just right.", fontSize = 13.sp, color = Color(0xFF777080))
                Spacer(Modifier.height(18.dp))
                Button(
                    onClick = onStart,
                    enabled = canStart,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                ) {
                    Text("START ADVENTURE  🚀", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("🔊 Tap • Listen • Learn • Smile", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF665F73))
    }
}

@Composable
private fun LessonScreen(
    childName: String,
    letter: Letter,
    index: Int,
    hasTapped: Boolean,
    isSpeaking: Boolean,
    onTapLetter: () -> Unit,
    onReplay: () -> Unit,
    onAutoAdvance: () -> Unit,
    onBack: () -> Unit
) {
    var entrance by remember(letter) { mutableStateOf(false) }
    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(tween(850), RepeatMode.Reverse),
        label = "pulseScale"
    )

    LaunchedEffect(letter) {
        entrance = false
        delay(80)
        entrance = true
    }

    LaunchedEffect(hasTapped, letter) {
        if (hasTapped) {
            delay(3600)
            onAutoAdvance()
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Back", fontSize = 17.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.weight(1f))
            Text("Hi, $childName! 👋", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(2.dp))
        ProgressDots(index)
        Spacer(Modifier.height(18.dp))

        Text("Letter ${letter.upper}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF625A70), modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(
            if (hasTapped) "Great listening! ⭐" else "Tap the big letter! 👆",
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF302A42),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(14.dp))

        AnimatedVisibility(
            visible = entrance,
            enter = fadeIn(tween(350)) + scaleIn(initialScale = .65f, animationSpec = spring()),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                Modifier
                    .size(240.dp)
                    .scale(if (!hasTapped) pulseScale else 1f)
                    .shadow(14.dp, CircleShape)
                    .background(Brush.linearGradient(listOf(letter.color, letter.color.copy(alpha = .72f))), CircleShape)
                    .clickable(enabled = !hasTapped, onClick = onTapLetter),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(letter.upper, fontSize = 112.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text(letter.lower, fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = .88f))
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .95f))
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(letter.emoji, fontSize = 56.sp)
                Text("${letter.upper} for ${letter.word}", fontSize = 27.sp, fontWeight = FontWeight.Black, color = Color(0xFF332C45))
                Spacer(Modifier.height(4.dp))
                Text("Sound: ${letter.phonics}", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF6B6376))
                Spacer(Modifier.height(14.dp))
                if (hasTapped) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onReplay,
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.weight(1f).height(54.dp)
                        ) {
                            Text("🔊", fontSize = 20.sp)
                            Spacer(Modifier.width(6.dp))
                            Text("Hear again", fontWeight = FontWeight.Bold)
                        }
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFFE8F7EA),
                            modifier = Modifier.weight(1f).height(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (index < alphabet.lastIndex) "Next letter…" else "Finishing…", fontWeight = FontWeight.Bold, color = Color(0xFF2C7540))
                            }
                        }
                    }
                } else {
                    Text("Tap the letter to hear it, then we’ll move on!", fontSize = 15.sp, color = Color(0xFF756E7E))
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Text("${index + 1} of ${alphabet.size}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6E6678), modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun ProgressDots(index: Int) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        itemsIndexed(alphabet) { i, _ ->
            Box(
                Modifier
                    .padding(horizontal = 2.dp)
                    .size(if (i == index) 9.dp else 6.dp)
                    .background(if (i <= index) Color(0xFF6750E8) else Color(0xFFD7D0E0), CircleShape)
            )
        }
    }
}

@Composable
private fun CompleteScreen(name: String, onReplay: () -> Unit, onHome: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "celebrate")
    val scale by infinite.animateFloat(
        initialValue = .95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "celebrateScale"
    )
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉", fontSize = 88.sp, modifier = Modifier.scale(scale))
        Spacer(Modifier.height(12.dp))
        Text("Amazing, $name!", fontSize = 38.sp, fontWeight = FontWeight.Black, color = Color(0xFF5B49D8))
        Text("You learned all 26 letters!", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4C4658))
        Spacer(Modifier.height(26.dp))
        Button(onClick = onReplay, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth().height(60.dp)) {
            Text("PLAY AGAIN 🔁", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onHome, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("CHANGE PLAYER", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}
