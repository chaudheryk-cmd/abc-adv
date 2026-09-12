package com.hpcai270
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);setContent{MaterialTheme{Surface(Modifier.fillMaxSize(),Color(0xFFF3E9D7)){HpcBook(this)}}}}}
@Composable fun HpcBook(c:Context){var day by remember{mutableIntStateOf(c.getSharedPreferences("progress",0).getInt("day",0))};var page by remember{mutableIntStateOf(0)};var open by remember{mutableStateOf(false)};if(!open)Cover(day){open=true}else Reader(day,page,{page=it},{open=false}){val n=(day+1).coerceAtMost(269);c.getSharedPreferences("progress",0).edit().putInt("day",n).apply();day=n;page=0}}
