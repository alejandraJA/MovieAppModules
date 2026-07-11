package com.me.daggerhilt.ui.theme.vintage.phone

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.me.daggerhilt.ui.theme.vintage.phone.ui.theme.DaggerHiltTheme
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                }
            }
        }
    }
}

@Composable
fun RotaryDialPhoneKeyboard(
    modifier: Modifier = Modifier,
    onNumberSelected: (String) -> Unit = {}
) {
    val colors = RotaryPhoneDefaults.colors()
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    var selectedNumber by rememberSaveable { mutableStateOf("") }

    val dialSize = 300.dp
    val keySize = 52.dp
    val radius = 100.dp

    fun selectNumber(number: String) {
        selectedNumber += number
        onNumberSelected(number)
    }

    val view = LocalView.current

    LaunchedEffect(view) {
        var lastTickIndex = -1

        snapshotFlow { rotation.value }
            .collect { currentRotation ->
                val currentIndex = nearestNumberIndexAtStopper(currentRotation)

                val shouldTick =
                    currentRotation > MIN_ROTATION_TO_SELECT &&
                            currentIndex != lastTickIndex

                if (shouldTick) {
                    view.performHapticFeedback(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            HapticFeedbackConstants.SEGMENT_TICK
                        } else {
                            HapticFeedbackConstants.CLOCK_TICK
                        }
                    )

                    lastTickIndex = currentIndex
                }

                if (currentRotation <= MIN_ROTATION_TO_SELECT) {
                    lastTickIndex = -1
                }
            }
    }

    fun animateToNumber(index: Int) {
        scope.launch {
            val targetRotation = normalizedDegrees(
                STOPPER_ANGLE - angleForIndex(index)
            )

            rotation.animateTo(
                targetValue = targetRotation,
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                )
            )

            selectNumber(numbers[index])

            rotation.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1500,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DialDisplay(
            selectedNumber = selectedNumber,
            colors = colors,
            modifier = Modifier.padding(bottom = 28.dp)
        )

        Box(
            modifier = Modifier
                .size(dialSize)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val center = Offset(size.width / 2f, size.height / 2f)

                        var lastAngle = angleFrom(center, down.position)
                        var totalDrag = 0f
                        var isDragging = false
                        var localRotation = rotation.value

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes
                                .firstOrNull { it.id == down.id }
                                ?: break

                            if (change.changedToUpIgnoreConsumed()) break

                            val dragDistance = distanceBetween(
                                change.previousPosition,
                                change.position
                            )

                            if (dragDistance > 0f) {
                                totalDrag += dragDistance

                                if (totalDrag > viewConfiguration.touchSlop) {
                                    isDragging = true
                                }

                                if (isDragging) {
                                    val currentAngle = angleFrom(center, change.position)
                                    val delta = shortestAngleDelta(lastAngle, currentAngle)

                                    lastAngle = currentAngle

                                    localRotation = (localRotation + delta)
                                        .coerceIn(0f, MAX_ROTATION)

                                    scope.launch {
                                        rotation.snapTo(localRotation)
                                    }

                                    change.consume()
                                }
                            }
                        }

                        if (isDragging) {
                            val selectedIndex = nearestNumberIndexAtStopper(localRotation)

                            scope.launch {
                                rotation.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(
                                        durationMillis = 900,
                                        easing = FastOutSlowInEasing
                                    )
                                )

                                if (localRotation > MIN_ROTATION_TO_SELECT) {
                                    selectNumber(numbers[selectedIndex])
                                }
                            }
                        } else {
                            val tappedIndex = indexFromTap(
                                position = down.position,
                                center = center,
                                radiusPx = radius.toPx(),
                                keyRadiusPx = keySize.toPx() / 2f
                            )

                            tappedIndex?.let { index ->
                                animateToNumber(index)
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            PhoneBase(colors = colors)

            FixedNumberPlate(
                numbers = numbers,
                dialSize = dialSize,
                keySize = keySize,
                radius = radius,
                colors = colors
            )

            RotaryDialFace(
                dialSize = dialSize,
                keySize = keySize,
                radius = radius,
                rotation = rotation.value,
                colors = colors
            )

            Stopper()
        }
    }
}

@Composable
fun FixedNumberPlate(
    numbers: List<String>,
    dialSize: Dp,
    keySize: Dp,
    radius: Dp,
    colors: RotaryPhoneColors
) {
    Box(
        modifier = Modifier.size(dialSize)
    ) {
        numbers.forEachIndexed { index, number ->
            val angleInDegrees = angleForIndex(index)
            val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

            val x = dialSize / 2 -
                    keySize / 2 +
                    radius * cos(angleInRadians).toFloat()

            val y = dialSize / 2 -
                    keySize / 2 +
                    radius * sin(angleInRadians).toFloat()

            RotaryNumber(
                text = number,
                colors = colors,
                modifier = Modifier
                    .offset(x = x, y = y)
                    .size(keySize)
            )
        }
    }
}

@Composable
fun RotaryNumber(
    text: String,
    colors: RotaryPhoneColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = colors.numberContainer,
        tonalElevation = 4.dp,
        shadowElevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = colors.numberBorder
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.numberContent
            )
        }
    }
}

@Composable
fun RotaryDialFace(
    dialSize: Dp,
    keySize: Dp,
    radius: Dp,
    rotation: Float,
    colors: RotaryPhoneColors
) {
    Canvas(
        modifier = Modifier
            .size(dialSize)
            .graphicsLayer {
                rotationZ = rotation
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        val center = this.center
        val dialRadius = size.minDimension * 0.44f
        val holeRadius = keySize.toPx() / 2f
        val holeDistance = radius.toPx()

        drawCircle(
            color = colors.dialContainer,
            radius = dialRadius,
            center = center
        )

        drawCircle(
            color = colors.dialBorder,
            radius = dialRadius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        repeat(NUMBERS_COUNT) { index ->
            val angleInDegrees = angleForIndex(index)
            val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

            val holeCenter = Offset(
                x = center.x + holeDistance * cos(angleInRadians).toFloat(),
                y = center.y + holeDistance * sin(angleInRadians).toFloat()
            )

            drawCircle(
                color = Color.Transparent,
                radius = holeRadius,
                center = holeCenter,
                blendMode = BlendMode.Clear
            )

            drawCircle(
                color = colors.dialHoleBorder,
                radius = holeRadius,
                center = holeCenter,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        drawCircle(
            color = colors.dialCenter,
            radius = size.minDimension * 0.18f,
            center = center
        )

        drawCircle(
            color = colors.dialCenterBorder,
            radius = size.minDimension * 0.18f,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun PhoneBase(
    colors: RotaryPhoneColors
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val center = this.center
        val baseRadius = size.minDimension / 2f

        drawCircle(
            color = colors.baseOuter,
            radius = baseRadius,
            center = center
        )

        drawCircle(
            color = colors.baseInner,
            radius = baseRadius * 0.84f,
            center = center
        )

        drawCircle(
            color = colors.baseCenter,
            radius = baseRadius * 0.40f,
            center = center
        )

        drawCircle(
            color = colors.baseRing,
            radius = baseRadius * 0.40f,
            center = center,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun Stopper(
    colors: RotaryPhoneColors
) {
    Surface(
        modifier = Modifier
            .offset(x = 105.dp, y = 40.dp)
            .size(width = 44.dp, height = 18.dp),
        shape = RoundedCornerShape(50),
        color = colors.dialCenter,
        tonalElevation = 6.dp,
        shadowElevation = 3.dp,
    ) {}
}

private const val NUMBERS_COUNT = 10
private const val STEP_ANGLE = 36f
private const val START_ANGLE = -90f
private const val STOPPER_ANGLE = 22f
private const val MAX_ROTATION = 330f
private const val MIN_ROTATION_TO_SELECT = 12f

private fun angleForIndex(index: Int): Float {
    return START_ANGLE + index * STEP_ANGLE
}

private fun normalizedDegrees(degrees: Float): Float {
    return ((degrees % 360f) + 360f) % 360f
}

private fun shortestAngleDelta(from: Float, to: Float): Float {
    var delta = normalizedDegrees(to) - normalizedDegrees(from)

    if (delta > 180f) delta -= 360f
    if (delta < -180f) delta += 360f

    return delta
}

private fun angularDistance(a: Float, b: Float): Float {
    val diff = abs(normalizedDegrees(a) - normalizedDegrees(b))
    return min(diff, 360f - diff)
}

private fun angleFrom(center: Offset, point: Offset): Float {
    return Math.toDegrees(
        atan2(
            y = (point.y - center.y).toDouble(),
            x = (point.x - center.x).toDouble()
        )
    ).toFloat()
}

private fun distanceBetween(a: Offset, b: Offset): Float {
    return hypot(
        x = (a.x - b.x).toDouble(),
        y = (a.y - b.y).toDouble()
    ).toFloat()
}

private fun nearestNumberIndexAtStopper(rotation: Float): Int {
    return (0 until NUMBERS_COUNT).minByOrNull { index ->
        angularDistance(
            a = angleForIndex(index) + rotation,
            b = STOPPER_ANGLE
        )
    } ?: 0
}

private fun indexFromTap(
    position: Offset,
    center: Offset,
    radiusPx: Float,
    keyRadiusPx: Float
): Int? {
    val distance = distanceBetween(position, center)

    val minDistance = radiusPx - keyRadiusPx
    val maxDistance = radiusPx + keyRadiusPx

    if (distance !in minDistance..maxDistance) return null

    val tapAngle = angleFrom(center, position)

    val nearestIndex = (0 until NUMBERS_COUNT).minByOrNull { index ->
        angularDistance(angleForIndex(index), tapAngle)
    } ?: return null

    val distanceFromNumber = angularDistance(
        angleForIndex(nearestIndex),
        tapAngle
    )

    return if (distanceFromNumber <= 22f) {
        nearestIndex
    } else {
        null
    }
}

@Immutable
data class RotaryPhoneColors(
    val baseOuter: Color,
    val baseInner: Color,
    val baseCenter: Color,
    val baseRing: Color,

    val numberContainer: Color,
    val numberContent: Color,
    val numberBorder: Color,

    val dialContainer: Color,
    val dialBorder: Color,
    val dialHoleBorder: Color,
    val dialCenter: Color,
    val dialCenterBorder: Color,

    val stopperContainer: Color,
    val stopperBorder: Color,

    val displayContainer: Color,
    val displayContent: Color
)

object RotaryPhoneDefaults {
    @Composable
    fun colors(): RotaryPhoneColors {
        val colorScheme = MaterialTheme.colorScheme

        return RotaryPhoneColors(
            baseOuter = colorScheme.surfaceContainerLowest,
            baseInner = colorScheme.surfaceContainerHigh,
            baseCenter = colorScheme.surface,
            baseRing = colorScheme.outlineVariant,

            numberContainer = colorScheme.primaryContainer,
            numberContent = colorScheme.onPrimaryContainer,
            numberBorder = colorScheme.primary.copy(alpha = 0.35f),

            dialContainer = colorScheme.surfaceContainerHighest,
            dialBorder = colorScheme.outlineVariant,
            dialHoleBorder = colorScheme.primary.copy(alpha = 0.55f),
            dialCenter = colorScheme.secondaryContainer,
            dialCenterBorder = colorScheme.secondary.copy(alpha = 0.45f),

            stopperContainer = colorScheme.tertiaryContainer,
            stopperBorder = colorScheme.tertiary.copy(alpha = 0.55f),

            displayContainer = colorScheme.surfaceContainerHigh,
            displayContent = colorScheme.onSurface
        )
    }
}

@Composable
fun DialDisplay(
    selectedNumber: String,
    colors: RotaryPhoneColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        shape = RoundedCornerShape(28.dp),
        color = colors.displayContainer,
        tonalElevation = 6.dp,
        shadowElevation = 2.dp
    ) {
        Text(
            text = selectedNumber.ifEmpty { "Marca un número" },
            style = MaterialTheme.typography.titleLarge,
            color = colors.displayContent,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        )
    }
}



@Preview(showBackground = true)
@Composable
fun VintagePhoneViewPreview() {
    DaggerHiltTheme(darkTheme = true) {
        Scaffold(Modifier.fillMaxWidth()) { paddingValues ->
            RotaryDialPhoneKeyboard(Modifier.padding(paddingValues))
            // VintagePhoneView("Android")
        }
    }
}