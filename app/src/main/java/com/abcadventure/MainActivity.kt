package com.abcadventure

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

private data class Letter(val upper: String, val lower: String, val word: String, val emoji: String, val phonics: String, val color: Color)
private data class NumberItem(val number: Int, val word: String, val emoji: String)
private data class ShapeItem(val name: String, val emoji: String, val description: String)
private data class ColorItem(val name: String, val color: Color, val emoji: String)

private val alphabet = listOf(
    Letter("A", "a", "Apple", "🍎", "aah", Color(0xFFFF6B6B)), Letter("B", "b", "Ball", "⚽", "buh", Color(0xFF4D96FF)),
    Letter("C", "c", "Cat", "🐱", "kuh", Color(0xFFFFB84D)), Letter("D", "d", "Dog", "🐶", "duh", Color(0xFF9B7EDE)),
    Letter("E", "e", "Elephant", "🐘", "eh", Color(0xFF5CC8A1)), Letter("F", "f", "Fish", "🐟", "fuh", Color(0xFF45B7D1)),
    Letter("G", "g", "Grapes", "🍇", "guh", Color(0xFF8E7CFF)), Letter("H", "h", "Hat", "🎩", "huh", Color(0xFFFF8E72)),
    Letter("I", "i", "Ice cream", "🍦", "ih", Color(0xFFFF82B2)), Letter("J", "j", "Juice", "🧃", "juh", Color(0xFFFFA94D)),
    Letter("K", "k", "Kite", "🪁", "kuh", Color(0xFF4FB5FF)), Letter("L", "l", "Lion", "🦁", "luh", Color(0xFFFFC857)),
    Letter("M", "m", "Monkey", "🐒", "muh", Color(0xFFB47AEA)), Letter("N", "n", "Nest", "🪺", "nuh", Color(0xFF64C4A2)),
    Letter("O", "o", "Orange", "🍊", "oh", Color(0xFFFF914D)), Letter("P", "p", "Penguin", "🐧", "puh", Color(0xFF6574CD)),
    Letter("Q", "q", "Queen", "👑", "kwuh", Color(0xFFDA77E8)), Letter("R", "r", "Rabbit", "🐰", "ruh", Color(0xFFFF7EA8)),
    Letter("S", "s", "Sun", "☀️", "suh", Color(0xFFFFB52E)), Letter("T", "t", "Tiger", "🐯", "tuh", Color(0xFFFF7657)),
    Letter("U", "u", "Umbrella", "☂️", "uh", Color(0xFF4DA3FF)), Letter("V", "v", "Van", "🚐", "vuh", Color(0xFF57C7A3)),
    Letter("W", "w", "Whale", "🐳", "wuh", Color(0xFF46B6D8)), Letter("X", "x", "Xylophone", "🎵", "ks", Color(0xFF9A7BFF)),
    Letter("Y", "y", "Yo-yo", "🪀", "yuh", Color(0xFFFF719D)), Letter("Z", "z", "Zebra", "🦓", "zuh", Color(0xFF777777))
)

private val numbers = listOf(
    NumberItem(1, "one", "☝️"), NumberItem(2, "two", "✌️"), NumberItem(3, "three", "🤟"), NumberItem(4, "four", "🖐️"),
    NumberItem(5, "five", "🖐️"), NumberItem(6, "six", "🎲"), NumberItem(7, "seven", "🌈"), NumberItem(8, "eight", "🎱"),
    NumberItem(9, "nine", "🐱"), NumberItem(10, "ten", "🔟"), NumberItem(11, "eleven", "1️⃣1️⃣"), NumberItem(12, "twelve", "1️⃣2️⃣"),
    NumberItem(13, "thirteen", "1️⃣3️⃣"), NumberItem(14, "fourteen", "1️⃣4️⃣"), NumberItem(15, "fifteen", "1️⃣5️⃣"), NumberItem(16, "sixteen", "1️⃣6️⃣"),
    NumberItem(17, "seventeen", "1️⃣7️⃣"), NumberItem(18, "eighteen", "1️⃣8️⃣"), NumberItem(19, "nineteen", "1️⃣9️⃣"), NumberItem(20, "twenty", "2️⃣0️⃣")
)

private val shapes = listOf(
    ShapeItem("Circle", "⚪", "A round shape with no corners."), ShapeItem("Square", "🟦", "A shape with four equal sides."),
    ShapeItem("Rectangle", "▭", "A shape with four sides and four corners."), ShapeItem("Triangle", "🔺", "A shape with three sides and three corners."),
    ShapeItem("Oval", "🥚", "A stretched round shape."), ShapeItem("Star", "⭐", "A shape with five points."),
    ShapeItem("Heart", "❤️", "A shape that looks like a heart."), ShapeItem("Diamond", "🔶", "A tilted square shape.")
)

private val colors = listOf(
    ColorItem("Red", Color(0xFFE53935), "🍎"), ColorItem("Blue", Color(0xFF1E88E5), "💧"), ColorItem("Yellow", Color(0xFFFDD835), "☀️"),
    ColorItem("Green", Color(0xFF43A047), "🍃"), ColorItem("Orange", Color(0xFFFB8C00), "🍊"), ColorItem("Purple", Color(0xFF8E24AA), "🍇"),
    ColorItem("Pink", Color(0xFFD81B60), "🌸"), ColorItem("Brown", Color(0xFF795548), "🐻"), ColorItem("Black", Color(0xFF212121), "🖤"), ColorItem("White", Color(0xFFF5F5F5), "☁️")
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
        if (::tts.isInitialized) { tts.stop(); tts.shutdown() }
        super.onDestroy()
    }
}

private fun speak(tts: TextToSpeech, text: String) {
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "lesson-${System.currentTimeMillis()}")
}

private enum class Screen { WELCOME, HOME, ALPHABET, NUMBERS, SHAPES, COLORS }

@Composable
private fun ABCAdventure(tts: TextToSpeech) {
    var screen by remember { mutableStateOf(Screen.WELCOME) }
    var childName by remember { mutableStateOf("") }
    var childAge by remember { mutableStateOf("") }
    val background = Brush.verticalGradient(listOf(Color(0xFFFFF7E8), Color(0xFFEAF7FF), Color(0xFFF6ECFF)))

    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF6750E8), onPrimary = Color.White, background = Color(0xFFFFF9F0), surface = Color.White, onSurface = Color(0xFF252038))) {
        Box(Modifier.fillMaxSize().background(background)) {
            FloatingBubbles()
            when (screen) {
                Screen.WELCOME -> WelcomeScreen(childName, { if (it.length <= 24) childName = it }, childAge, { if (it.length <= 2 && it.all(Char::isDigit)) childAge = it }) {
                    screen = Screen.HOME; speak(tts, "Welcome to ABC Adventure, $childName!")
                }
                Screen.HOME -> HomeScreen(childName, { screen = it }, { screen = Screen.WELCOME })
                Screen.ALPHABET -> AlphabetScreen(childName, tts) { screen = Screen.HOME }
                Screen.NUMBERS -> NumbersScreen(childName, tts) { screen = Screen.HOME }
                Screen.SHAPES -> ShapesScreen(childName, tts) { screen = Screen.HOME }
                Screen.COLORS -> ColorsScreen(childName, tts) { screen = Screen.HOME }
            }
        }
    }
}

@Composable
private fun FloatingBubbles() {
    val infinite = rememberInfiniteTransition(label = "bubbles")
    val drift by infinite.animateFloat(-12f, 12f, infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "drift")
    Box(Modifier.fillMaxSize()) {
        Text("✦", fontSize = 30.sp, color = Color(0xFFFFB74D), modifier = Modifier.offset(24.dp, 70.dp + drift.dp))
        Text("●", fontSize = 18.sp, color = Color(0xFF79C7FF), modifier = Modifier.align(Alignment.TopEnd).offset((-38).dp, 130.dp - drift.dp))
        Text("✦", fontSize = 24.sp, color = Color(0xFFFF8FB3), modifier = Modifier.align(Alignment.BottomStart).offset(42.dp, (-120).dp - drift.dp))
        Text("●", fontSize = 20.sp, color = Color(0xFF9BD9B6), modifier = Modifier.align(Alignment.BottomEnd).offset((-35).dp, (-210).dp + drift.dp))
    }
}

@Composable
private fun WelcomeScreen(name: String, onNameChange: (String) -> Unit, age: String, onAgeChange: (String) -> Unit, onStart: () -> Unit) {
    val validAge = age.toIntOrNull()?.let { it in 2..12 } == true
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("🌈", fontSize = 64.sp)
        Text("ABC Adventure", fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color(0xFF5B49D8))
        Text("Learn, play and discover!", fontSize = 18.sp, color = Color(0xFF615A70))
        Spacer(Modifier.height(26.dp))
        Card(Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(28.dp)), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .96f))) {
            Column(Modifier.padding(22.dp)) {
                Text("Who is learning today?", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(name, { onNameChange(it) }, label = { Text("My name") }, placeholder = { Text("e.g. Aarav") }, singleLine = true, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(age, { onAgeChange(it) }, label = { Text("My age") }, placeholder = { Text("2–12") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(18.dp))
                Button(onClick = onStart, enabled = name.trim().isNotEmpty() && validAge, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text("START ADVENTURE  🚀", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(name: String, open: (Screen) -> Unit, back: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = back) { Text("‹ Back", fontSize = 17.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.weight(1f)); Text("Hi, $name! 👋", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text("What shall we learn?", fontSize = 30.sp, fontWeight = FontWeight.Black, color = Color(0xFF302A42))
        Text("Pick an adventure and tap to learn.", fontSize = 16.sp, color = Color(0xFF6A6373))
        Spacer(Modifier.height(18.dp))
        ModuleCard("🔤", "Alphabet A–Z", "26 letters • sounds • words", Color(0xFF6750E8)) { open(Screen.ALPHABET) }
        Spacer(Modifier.height(12.dp))
        ModuleCard("🔢", "Counting 1–20", "Numbers • names • counting", Color(0xFF1E88E5)) { open(Screen.NUMBERS) }
        Spacer(Modifier.height(12.dp))
        ModuleCard("🔷", "8 Basic Shapes", "Circle • square • triangle and more", Color(0xFF00A878)) { open(Screen.SHAPES) }
        Spacer(Modifier.height(12.dp))
        ModuleCard("🎨", "10 Basic Colors", "Learn the colors around us", Color(0xFFE45A3C)) { open(Screen.COLORS) }
    }
}

@Composable
private fun ModuleCard(icon: String, title: String, subtitle: String, accent: Color, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = .97f))) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(64.dp), shape = RoundedCornerShape(20.dp), color = accent.copy(alpha = .14f)) { Box(contentAlignment = Alignment.Center) { Text(icon, fontSize = 34.sp) } }
            Spacer(Modifier.width(16.dp)); Column(Modifier.weight(1f)) { Text(title, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold); Text(subtitle, fontSize = 14.sp, color = Color(0xFF706A78)) }
            Text("›", fontSize = 34.sp, fontWeight = FontWeight.Light, color = accent)
        }
    }
}

@Composable
private fun AlphabetScreen(name: String, tts: TextToSpeech, back: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }; var tapped by remember { mutableStateOf(false) }; val letter = alphabet[index]
    LaunchedEffect(tapped, index) { if (tapped) { delay(3300); if (index < alphabet.lastIndex) { index++; tapped = false } } }
    LessonFrame(name, "Alphabet A–Z", index + 1, alphabet.size, back) {
        AnimatedVisibility(tapped, enter = fadeIn(tween(250)) + scaleIn(), initiallyVisible = true) {
            Text("${letter.emoji}  ${letter.upper} for ${letter.word}!", fontSize = 23.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(10.dp))
        Box(Modifier.size(230.dp).scale(if (!tapped) pulseScale() else 1f).shadow(14.dp, CircleShape).background(letter.color, CircleShape).clickable(enabled = !tapped) {
            tapped = true; speak(tts, "Letter ${letter.upper}. ${letter.upper} says ${letter.phonics}. ${letter.upper} is for ${letter.word}.")
        }, contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(letter.upper, fontSize = 110.sp, fontWeight = FontWeight.Black, color = Color.White); Text(letter.lower, fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = .9f)) }
        }
        Spacer(Modifier.height(14.dp)); Text(if (tapped) "Great! Listen and get ready for the next letter ⭐" else "Tap the big letter 👆", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp)); Button(onClick = { speak(tts, "Letter ${letter.upper}. ${letter.upper} is for ${letter.word}.") }, enabled = tapped) { Text("🔊 Hear again") }
    }
}

@Composable
private fun pulseScale(): Float {
    val infinite = rememberInfiniteTransition(label = "pulse")
    return infinite.animateFloat(1f, 1.045f, infiniteRepeatable(tween(850), RepeatMode.Reverse), label = "pulseScale").value
}

@Composable
private fun NumbersScreen(name: String, tts: TextToSpeech, back: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }; var tapped by remember { mutableStateOf(false) }; val item = numbers[index]
    LaunchedEffect(tapped, index) { if (tapped) { delay(2500); if (index < numbers.lastIndex) { index++; tapped = false } } }
    LessonFrame(name, "Counting 1–20", index + 1, numbers.size, back) {
        Text("${item.emoji}", fontSize = 42.sp); Spacer(Modifier.height(4.dp))
        Box(Modifier.size(210.dp).scale(if (!tapped) pulseScale() else 1f).shadow(14.dp, CircleShape).background(Color(0xFF1E88E5), CircleShape).clickable(enabled = !tapped) {
            tapped = true; speak(tts, "${item.number}. ${item.word}.")
        }, contentAlignment = Alignment.Center) { Text(item.number.toString(), fontSize = 104.sp, fontWeight = FontWeight.Black, color = Color.White) }
        Spacer(Modifier.height(12.dp)); Text(item.word.replaceFirstChar { it.uppercase() }, fontSize = 29.sp, fontWeight = FontWeight.Black)
        Text("Tap the number and say it with me!", fontSize = 17.sp, color = Color(0xFF6A6373))
        Spacer(Modifier.height(10.dp)); Button(onClick = { speak(tts, "${item.number}. ${item.word}.") }) { Text("🔊 Hear again") }
    }
}

@Composable
private fun ShapesScreen(name: String, tts: TextToSpeech, back: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }; var tapped by remember { mutableStateOf(false) }; val item = shapes[index]
    LessonFrame(name, "8 Basic Shapes", index + 1, shapes.size, back) {
        Text(item.emoji, fontSize = 48.sp); Spacer(Modifier.height(6.dp))
        Box(Modifier.size(210.dp).clickable { tapped = true; speak(tts, "This is a ${item.name}. ${item.description}") }, contentAlignment = Alignment.Center) { ShapeGraphic(index, tapped) }
        Spacer(Modifier.height(10.dp)); Text(item.name, fontSize = 30.sp, fontWeight = FontWeight.Black); Text(item.description, fontSize = 16.sp, color = Color(0xFF6A6373))
        Spacer(Modifier.height(12.dp)); Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = { speak(tts, "This is a ${item.name}. ${item.description}") }) { Text("🔊 Hear") }
            Button(onClick = { if (index < shapes.lastIndex) { index++; tapped = false } else { index = 0; tapped = false } }) { Text(if (index < shapes.lastIndex) "Next ›" else "Again ↻") }
        }
    }
}

@Composable
private fun ShapeGraphic(index: Int, tapped: Boolean) {
    val fill = if (tapped) Color(0xFFFFB52E) else Color(0xFF6750E8)
    Canvas(Modifier.size(180.dp)) {
        when (index) {
            0 -> drawCircle(fill)
            1 -> drawRoundRect(fill, size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f))
            2 -> drawRect(fill, topLeft = androidx.compose.ui.geometry.Offset(8f, 28f), size = androidx.compose.ui.geometry.Size(size.width - 16f, size.height - 56f))
            3 -> drawPath(Path().apply { moveTo(size.width / 2, 8f); lineTo(size.width - 8f, size.height - 8f); lineTo(8f, size.height - 8f); close() }, fill)
            4 -> drawOval(fill)
            5 -> { val path = Path(); val cx = size.width / 2; val cy = size.height / 2; val r1 = size.minDimension * .48f; val r2 = r1 * .42f; for (i in 0 until 10) { val r = if (i % 2 == 0) r1 else r2; val a = (-90 + i * 36) * Math.PI / 180; val x = cx + cos(a).toFloat() * r; val y = cy + sin(a).toFloat() * r; if (i == 0) path.moveTo(x, y) else path.lineTo(x, y) }; path.close(); drawPath(path, fill) }
            6 -> { val path = Path(); path.moveTo(size.width / 2, size.height * .85f); path.cubicTo(size.width * .12f, size.height * .58f, size.width * .12f, size.height * .12f, size.width * .36f, size.height * .22f); path.cubicTo(size.width * .48f, size.height * .27f, size.width * .5f, size.height * .36f, size.width / 2, size.height * .4f); path.cubicTo(size.width * .5f, size.height * .36f, size.width * .52f, size.height * .27f, size.width * .64f, size.height * .22f); path.cubicTo(size.width * .88f, size.height * .12f, size.width * .88f, size.height * .58f, size.width / 2, size.height * .85f); path.close(); drawPath(path, fill) }
            else -> { val path = Path(); path.moveTo(size.width / 2, 8f); path.lineTo(size.width - 8f, size.height / 2); path.lineTo(size.width / 2, size.height - 8f); path.lineTo(8f, size.height / 2); path.close(); drawPath(path, fill) }
        }
    }
}

@Composable
private fun ColorsScreen(name: String, tts: TextToSpeech, back: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }; var tapped by remember { mutableStateOf(false) }; val item = colors[index]
    LessonFrame(name, "10 Basic Colors", index + 1, colors.size, back) {
        Text(item.emoji, fontSize = 42.sp); Spacer(Modifier.height(8.dp))
        Box(Modifier.size(210.dp).scale(if (!tapped) pulseScale() else 1f).shadow(14.dp, CircleShape).background(item.color, CircleShape).clickable { tapped = true; speak(tts, "This color is ${item.name}.") }, contentAlignment = Alignment.Center) {
            Text(item.name, fontSize = 30.sp, fontWeight = FontWeight.Black, color = if (item.name == "White" || item.name == "Yellow") Color(0xFF333333) else Color.White)
        }
        Spacer(Modifier.height(14.dp)); Text("${item.name}!", fontSize = 30.sp, fontWeight = FontWeight.Black)
        Text("Tap the color and say its name.", fontSize = 17.sp, color = Color(0xFF6A6373))
        Spacer(Modifier.height(10.dp)); Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = { speak(tts, "This color is ${item.name}.") }) { Text("🔊 Hear") }
            Button(onClick = { if (index < colors.lastIndex) { index++; tapped = false } else { index = 0; tapped = false } }) { Text(if (index < colors.lastIndex) "Next ›" else "Again ↻") }
        }
    }
}

@Composable
private fun LessonFrame(name: String, title: String, position: Int, total: Int, back: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { TextButton(onClick = back) { Text("‹ Back", fontSize = 17.sp, fontWeight = FontWeight.Bold) }; Spacer(Modifier.weight(1f)); Text("Hi, $name! 👋", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(4.dp)); Text(title, fontSize = 25.sp, fontWeight = FontWeight.Black, color = Color(0xFF302A42)); Text("$position of $total", fontSize = 14.sp, color = Color(0xFF777080))
        Spacer(Modifier.height(10.dp)); ProgressDots(position - 1, total); Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun ProgressDots(index: Int, total: Int) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        itemsIndexed(List(total) { it }) { i, _ -> Box(Modifier.size(if (i == index) 9.dp else 6.dp).background(if (i <= index) Color(0xFF6750E8) else Color(0xFFD9D3E5), CircleShape)) }
    }
}
