package com.hpcai270
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TOTAL_DAYS = 50

@Composable fun Cover(day:Int,start:()->Unit){Column(Modifier.fillMaxSize().padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){Text("HPC AI",fontSize=25.sp,fontWeight=FontWeight.Bold,color=Color(0xFF5B4B75));Text("50 DAY JOURNEY",fontSize=37.sp,fontWeight=FontWeight.Black,textAlign=TextAlign.Center);Text("Handwritten field notes for HPC + AI infrastructure",fontFamily=FontFamily.Cursive,fontSize=18.sp,textAlign=TextAlign.Center);Spacer(Modifier.height(20.dp));Box(Modifier.width(245.dp).height(305.dp).shadow(14.dp).background(Color(0xFFEEE2CB),RoundedCornerShape(8.dp)).border(1.dp,Color(0xFF6C5B45),RoundedCornerShape(8.dp)),Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("FIELD NOTES",fontFamily=FontFamily.Cursive,fontSize=18.sp);Text("HPC\n+\nAI",fontFamily=FontFamily.Cursive,fontSize=43.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center);Text("50 DAYS",fontFamily=FontFamily.Cursive,fontSize=22.sp);Text("Day ${(day+1).coerceAtMost(TOTAL_DAYS)} / $TOTAL_DAYS")}};Spacer(Modifier.height(20.dp));Button(start,Modifier.fillMaxWidth(.8f).height(56.dp),shape=RoundedCornerShape(18.dp)){Text(if(day==0)"OPEN THE BOOK" else "CONTINUE — DAY ${day+1}")};Spacer(Modifier.height(8.dp));Text("$day / $TOTAL_DAYS completed",fontFamily=FontFamily.Cursive,fontSize=18.sp)}}
