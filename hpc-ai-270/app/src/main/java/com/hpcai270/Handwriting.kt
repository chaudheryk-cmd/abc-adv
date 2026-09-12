package com.hpcai270

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

private val caveat = GoogleFont("Caveat")

private val caveatProvider = androidx.compose.ui.text.googlefonts.GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = com.hpcai270.R.array.com_google_android_gms_fonts_certs
)

val Handwritten = FontFamily(
    Font(googleFont = caveat, fontProvider = caveatProvider, weight = FontWeight.Normal),
    Font(googleFont = caveat, fontProvider = caveatProvider, weight = FontWeight.Medium),
    Font(googleFont = caveat, fontProvider = caveatProvider, weight = FontWeight.SemiBold),
    Font(googleFont = caveat, fontProvider = caveatProvider, weight = FontWeight.Bold)
)

@Composable
fun HandwritingTextStyle() = androidx.compose.ui.text.TextStyle(fontFamily = Handwritten)
