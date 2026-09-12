package com.hpcai270

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

@Composable
fun Reader(di:Int,page:Int,setPage:(Int)->Unit,close:()->Unit,complete:()->Unit){
    val all=hpcDaysPart1+hpcDaysPart2+hpcDaysPart3+hpcDaysPart4
    val d=all.getOrNull(di)
    Column(Modifier.fillMaxSize().padding(9.dp)){
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){
            TextButton(close){Text("‹ Close")}
            Spacer(Modifier.weight(1f))
            Text("DAY ${di+1} / $TOTAL_DAYS",fontWeight=FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text("${((di+1).toFloat()/TOTAL_DAYS*100).toInt()}%")
        }
        Box(Modifier.fillMaxWidth().weight(1f),Alignment.Center){
            if(d==null) Text("📖\n\nDay ${di+1}\n\nThis chapter is not released yet.",fontFamily=FontFamily.Cursive,fontSize=24.sp,textAlign=TextAlign.Center)
            else Page(d,page)
        }
        Row(Modifier.fillMaxWidth().padding(7.dp),Arrangement.SpaceBetween,Alignment.CenterVertically){
            Button({setPage(0)},enabled=page>0){Text("← Previous")}
            Text("${page+1} / 2",fontFamily=FontFamily.Cursive,fontSize=18.sp)
            if(page==0) Button({setPage(1)}){Text("Turn page →")}
            else Button(complete){Text(if(di==TOTAL_DAYS-1)"✓ Finish 50 days" else "✓ Complete day")}
        }
    }
}

@Composable
fun Page(d:Day,page:Int){
    Card(Modifier.fillMaxWidth().fillMaxHeight(.93f).padding(3.dp).shadow(12.dp),colors=CardDefaults.cardColors(Color(0xFFFFFCF2)),shape=RoundedCornerShape(5.dp)){
        Column(Modifier.fillMaxSize().padding(19.dp)){
            Text("DAY ${d.number}",fontSize=13.sp,fontWeight=FontWeight.Bold,color=Color(0xFF77647E))
            Text(d.title,fontFamily=FontFamily.Cursive,fontWeight=FontWeight.Bold,fontSize=30.sp)
            Text(d.topic,fontFamily=FontFamily.Cursive,fontSize=16.sp,color=Color(0xFF715D78))
            Spacer(Modifier.height(17.dp))
            Text(if(page==0)d.page1 else d.page2,fontFamily=FontFamily.Cursive,fontSize=18.sp,lineHeight=27.sp)
            Spacer(Modifier.height(14.dp))
            if(page==1) Card(colors=CardDefaults.cardColors(Color(0xFFF0E6D1)),shape=RoundedCornerShape(14.dp)){
                Column(Modifier.padding(13.dp)){
                    Text("TODAY'S TASK",fontWeight=FontWeight.Bold)
                    Text(d.task,fontFamily=FontFamily.Cursive,fontSize=17.sp,lineHeight=23.sp)
                }
            } else Text("✎ draw it • say it • remember it",fontFamily=FontFamily.Cursive,fontSize=18.sp,color=Color(0xFF77647E))
        }
    }
}
