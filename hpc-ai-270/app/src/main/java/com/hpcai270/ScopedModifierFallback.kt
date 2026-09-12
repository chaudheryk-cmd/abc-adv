package com.hpcai270

import androidx.compose.ui.Modifier

// Tool cards are reusable composables called from a Row but defined outside
// RowScope. Keep their three-column layout responsive without leaking a
// Row-scoped modifier through the component API.
private fun Modifier.weight(weight: Float): Modifier = fillMaxWidth((weight / 3f).coerceIn(0.01f, 1f))
