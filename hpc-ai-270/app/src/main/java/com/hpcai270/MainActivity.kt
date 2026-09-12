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

private const val TOTAL_DAYS = 270

class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);setContent{MaterialTheme{Surface(Modifier.fillMaxSize(),Color(0xFFF3E9D7)){HpcBook(this)}}}}}

@Composable fun HpcBook(c:Context){
    var completed by remember{mutableIntStateOf(c.getSharedPreferences("progress",0).getInt("completed",0).coerceIn(0,TOTAL_DAYS))}
    var page by remember{mutableIntStateOf(0)}
    var open by remember{mutableStateOf(false)}
    val currentDay = completed.coerceAtMost(TOTAL_DAYS-1)
    if(!open) Cover(completed){open=true}
    else Reader(currentDay,page,{page=it},{open=false}){
        val n=(completed+1).coerceAtMost(TOTAL_DAYS)
        c.getSharedPreferences("progress",0).edit().putInt("completed",n).apply()
        completed=n
        page=0
        open=false
    }
}
