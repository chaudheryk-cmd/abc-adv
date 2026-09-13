package com.hpcai270

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val VBlue = Color(0xFF244F86)
private val VBlue2 = Color(0xFF4B86B9)
private val VPurple = Color(0xFF68458C)
private val VGreen = Color(0xFF4C8A70)
private val VOrange = Color(0xFFC57A38)
private val VInk = Color(0xFF1C3656)
private val VPaper = Color(0xFFEAF3FA)

@Composable
fun LessonVisual(day: Int) {
    when (day) {
        1 -> ClusterMap()
        2 -> ClusterRoles()
        3 -> CpuVsGpu()
        4 -> SoftwareStack()
        5 -> SchedulerQueue()
        6 -> ParallelWorkers()
        7 -> MpiRanks()
        8 -> OpenMpNode()
        9, 30, 31, 32, 33, 34 -> PerformanceLab(day)
        10, 35, 36, 37, 38, 39 -> ParallelFilesystem(day)
        11, 43 -> NvmeAnatomy()
        12 -> NvmeOfPath()
        13 -> RdmaPath()
        14, 41 -> FabricVisual(day)
        15, 42, 45 -> AiDataPath()
        16, 17, 18, 19, 20, 21, 22 -> LinuxWorkbench(day)
        23, 24, 25 -> ScriptingWorkbench(day)
        26 -> ProtocolMap()
        27 -> NfsPath()
        28 -> S3Visual()
        29 -> RaidVisual()
        40 -> CephVisual()
        44 -> GdsVisual()
        else -> GenericEquipmentVisual(day)
    }
}

@Composable
private fun VisualCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = VPaper), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = VPurple, maxLines = 2)
            content()
        }
    }
}

/**
 * All visual nodes have a deliberately bounded footprint. This prevents long
 * labels from forcing a Row outside the card on narrow phone screens.
 */
@Composable
private fun Node(label: String, color: Color = VBlue, modifier: Modifier = Modifier) {
    Box(
        modifier.width(56.dp).heightIn(min = 44.dp, max = 52.dp)
            .background(color, RoundedCornerShape(10.dp))
            .padding(horizontal = 3.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            fontFamily = Handwritten,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 12.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

/** Two-column grid used whenever four nodes would otherwise be squeezed. */
@Composable
private fun NodeGrid(labels: List<String>, colors: List<Color> = listOf(VBlue, VPurple, VGreen, VOrange)) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        labels.take(4).chunked(2).forEachIndexed { row, items ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEachIndexed { col, label ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Node(label, colors[(row * 2 + col) % colors.size])
                    }
                }
            }
        }
    }
}

@Composable
private fun NodePath3(a: String, b: String, c: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node(a, VBlue)
        Text("→", fontSize = 20.sp, color = VPurple)
        Node(b, VPurple)
        Text("→", fontSize = 20.sp, color = VPurple)
        Node(c, VGreen)
    }
}

@Composable
private fun ClusterMap() = VisualCard("HPC CLUSTER — THE REAL MACHINE") {
    NodePath3("GPU\nNODE", "FABRIC", "STORAGE")
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text("8 GPUs", fontFamily = Handwritten, color = VInk, fontSize = 14.sp)
        Text("IB / RoCE", fontFamily = Handwritten, color = VInk, fontSize = 14.sp)
        Text("Lustre / Scale", fontFamily = Handwritten, color = VInk, fontSize = 14.sp)
    }
    Text("A real AI system is a coordinated data path: compute + memory + fabric + storage + software.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun ClusterRoles() = VisualCard("INSIDE THE CLUSTER") {
    NodeGrid(listOf("LOGIN", "CONTROL", "COMPUTE", "STORAGE"))
    Text("Users submit → scheduler allocates → compute runs → shared storage serves data.", fontFamily = Handwritten, fontSize = 16.sp, lineHeight = 21.sp, color = VInk)
}

@Composable
private fun CpuVsGpu() = VisualCard("CPU vs GPU — DIFFERENT TOOLS") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Node("CPU", VBlue)
            Text("few powerful cores\ncomplex control", fontFamily = Handwritten, color = VInk, textAlign = TextAlign.Center)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Node("GPU", VPurple)
            Text("many parallel\nexecution units", fontFamily = Handwritten, color = VInk, textAlign = TextAlign.Center)
        }
    }
    Text("AI training loves massive tensor parallelism — but only when the data arrives fast enough.", fontFamily = Handwritten, fontSize = 16.sp, lineHeight = 21.sp, color = VInk)
}

@Composable
private fun SoftwareStack() = VisualCard("HPC SOFTWARE STACK") {
    listOf("APPLICATION / AI FRAMEWORK", "MPI • NCCL • OpenMP", "SLURM + SERVICES", "LUSTRE / STORAGE SCALE", "RDMA / NETWORK", "LINUX + DRIVERS", "CPU • GPU • NIC • NVMe").forEachIndexed { i, s ->
        Box(Modifier.fillMaxWidth().background(if (i % 2 == 0) Color(0xFFD9E9F6) else Color(0xFFE8DCF3), RoundedCornerShape(7.dp)).padding(6.dp)) {
            Text(s, fontFamily = Handwritten, fontWeight = FontWeight.SemiBold, color = VInk, fontSize = 14.sp, maxLines = 2)
        }
    }
}

@Composable
private fun SchedulerQueue() = VisualCard("SLURM — JOBS WAIT FOR RESOURCES") {
    NodePath3("JOB 101", "SLURM", "GPU POOL")
    Text("JOB 102 joins the queue until resources are available.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    Text("The scheduler is a resource broker, not the application itself.", fontFamily = Handwritten, fontSize = 16.sp, color = VInk)
}

@Composable
private fun ParallelWorkers() = VisualCard("PARALLEL WORK — MANY WORKERS") {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Node("DATASET", VGreen)
        Text("↓ split", fontSize = 21.sp, color = VPurple)
        NodeGrid(listOf("W1", "W2", "W3", "W4"), listOf(VBlue, VBlue2, VBlue, VBlue2))
        Text("↓ combine / synchronize", fontSize = 18.sp, color = VPurple)
        Node("RESULT", VOrange)
    }
}

@Composable
private fun MpiRanks() = VisualCard("MPI — RANKS TALK ACROSS NODES") {
    NodePath3("NODE A\nRANKS", "FABRIC", "NODE B\nRANKS")
    Text("rank 0 / 1  ↔  latency + bandwidth  ↔  rank 2 / 3", fontFamily = Handwritten, fontSize = 14.sp, color = VInk, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun OpenMpNode() = VisualCard("OPENMP — THREADS INSIDE ONE NODE") {
    Node("ONE SERVER", VBlue)
    NodeGrid(listOf("T1", "T2", "T3", "T4"), listOf(VBlue2, VBlue2, VBlue2, VBlue2))
    Text("Threads share the node's memory; MPI is normally used when work spans nodes.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun PerformanceLab(day: Int) = VisualCard(when(day) {
    31 -> "FIO — YOUR STORAGE MICROSCOPE"
    32 -> "QUEUE DEPTH — OUTSTANDING I/O"
    33 -> "LATENCY — THE WAIT"
    34 -> "THROUGHPUT — DATA PER SECOND"
    else -> "PERFORMANCE — MEASURE, DON'T GUESS"
}) {
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val base = size.height - 20f
        val left = 20f
        val right = size.width - 15f
        drawLine(VInk, Offset(left, base), Offset(right, base), 3f)
        drawLine(VInk, Offset(left, 15f), Offset(left, base), 3f)
        val bars = listOf(.35f, .55f, .72f, .9f)
        val available = (right - left - 28f).coerceAtLeast(20f)
        val step = available / bars.size
        bars.forEachIndexed { i, h ->
            val barWidth = (step * .55f).coerceAtMost(42f)
            val x = left + 10f + i * step
            val height = 95f * h
            drawRect(if (i % 2 == 0) VBlue else VPurple, Offset(x, base - height), Size(barWidth, height))
        }
    }
    Text("A benchmark is only meaningful when block size, read/write mix, concurrency, queue depth and client count are known.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun ParallelFilesystem(day: Int) = VisualCard(when(day) {
    10 -> "PARALLEL FILESYSTEM — MANY TARGETS"
    35 -> "HPC STORAGE ARCHITECTURE"
    36 -> "LUSTRE DATA PATH"
    37 -> "METADATA IS THE CATALOG"
    38 -> "STRIPING — SPLIT THE FILE"
    else -> "STORAGE SCALE — MANY CLIENTS"
}) {
    NodePath3("CLIENTS", "MDS", "OSS")
    Text("many clients  →  metadata  +  data service", fontFamily = Handwritten, fontSize = 14.sp, color = VInk, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    NodeGrid(listOf("OST1", "OST2", "OST3", "OST4"), listOf(VGreen, VGreen, VGreen, VGreen))
    Text("Metadata and bulk file data are different workloads. Parallel storage separates and scales them.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun NvmeAnatomy() = VisualCard("NVMe — PARALLEL QUEUES CLOSE TO THE MEDIA") {
    NodePath3("CPU", "PCIe", "NVMe")
    NodeGrid(listOf("Q1", "Q2", "Q3", "Q4"), listOf(VOrange, VOrange, VOrange, VOrange))
    Text("NVMe uses multiple submission/completion queues and is designed for modern solid-state storage. Queue parallelism is one reason it scales far beyond legacy single-queue models.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun NvmeOfPath() = VisualCard("NVMe-oF — NVMe OVER A FABRIC") {
    NodePath3("NVMe\nCLIENT", "RDMA / TCP", "NVMe\nTARGET")
    Text("The media can be remote while the command model remains NVMe. Transport choices change the network path and latency profile.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun RdmaPath() = VisualCard("RDMA — MOVE DATA WITH LESS CPU INVOLVEMENT") {
    NodePath3("HOST A", "HCA / NIC", "HOST B")
    Text("Key vocabulary: verbs, QP, CQ, MR, HCA, one-sided operations, zero-copy concepts and the difference between RDMA semantics and the physical fabric.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun FabricVisual(day: Int) = VisualCard(if (day == 14) "INFINIBAND / ROCE — THE HIGH-SPEED FABRIC" else "FABRIC TROUBLESHOOTING") {
    NodePath3("GPU / HCA", "SWITCH", "HCA / GPU")
    Text("At scale, link speed alone is not enough: topology, congestion, QoS, routing, MTU and error counters matter.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun AiDataPath() = VisualCard("AI DATA PATH — KEEP THE GPU FED") {
    NodeGrid(listOf("DATA", "STORAGE", "FABRIC", "GPU"), listOf(VGreen, VBlue, VPurple, VOrange))
    Text("DATA → STORAGE → FABRIC → GPU", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = VPurple, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    Text("Training: read batches → preprocess → transfer → compute → synchronize → checkpoint. Any slow stage can leave expensive accelerators waiting.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun LinuxWorkbench(day: Int) = VisualCard("LINUX FIELD KIT — DAY $day") {
    NodeGrid(listOf("CPU", "RAM", "DISK", "NIC"))
    Text("Use the shell to observe the system: ps/top for processes, free/vmstat for memory, lsblk/df/mount for storage, ip/ss for networking, journalctl/dmesg for logs.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun ScriptingWorkbench(day: Int) = VisualCard(if (day == 25) "PYTHON — TURN DATA INTO TOOLS" else "BASH — AUTOMATE THE BORING WORK") {
    Node(if (day == 25) "PYTHON" else "BASH", VPurple)
    Text("INPUT → PARSE → CHECK → ACT → LOG → REPORT", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = VInk, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    Text("The goal is not clever code. It is repeatable operations, safe failure handling, useful logs and predictable output.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun ProtocolMap() = VisualCard("STORAGE PROTOCOL MAP") {
    NodeGrid(listOf("BLOCK", "FILE", "OBJECT", "NVMe-oF"))
    Text("FC / iSCSI / NVMe-oF  •  NFS / SMB / Lustre  •  S3 API", fontFamily = Handwritten, fontSize = 15.sp, color = VInk, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun NfsPath() = VisualCard("NFS — FILES OVER IP") {
    NodePath3("APP", "NFS\nCLIENT", "NFS\nSERVER")
    Text("APP → VFS → NFS client → network/RPC → server → filesystem → media", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun S3Visual() = VisualCard("S3 — OBJECTS, NOT MOUNT POINTS") {
    NodePath3("APP", "HTTPS / API", "BUCKET")
    Text("Bucket → key → object + metadata. Think API requests and object identity, not POSIX paths and inode operations.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun RaidVisual() = VisualCard("RAID — CAPACITY, PERFORMANCE, PROTECTION") {
    NodeGrid(listOf("0\nSTRIPE", "1\nMIRROR", "6\nPARITY", "10\nMIRROR+STRIPE"))
    Text("RAID is one protection layer. Distributed HPC platforms may add replication, erasure coding and filesystem-level resilience above it.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun CephVisual() = VisualCard("CEPH — DISTRIBUTED SOFTWARE STORAGE") {
    NodeGrid(listOf("MON", "MGR", "OSD", "OSD"), listOf(VPurple, VOrange, VBlue, VBlue))
    Text("RBD = block • CephFS = file • RGW = object/S3. CRUSH decides data placement; OSDs store data and participate in recovery.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun GdsVisual() = VisualCard("GPU DIRECT STORAGE — SHORTEN THE DATA PATH") {
    NodePath3("NVMe /\nLUSTRE", "NIC", "GPU\nMEMORY")
    Text("The design goal is to reduce unnecessary CPU-side copies and move data efficiently between storage/network and GPU memory where supported.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}

@Composable
private fun GenericEquipmentVisual(day: Int) = VisualCard("FIELD EQUIPMENT — DAY $day") {
    NodePath3("SERVER", "SWITCH", "STORAGE")
    Text("Real deployments are built from physical servers, NICs, switches, storage targets, power and cooling — then software turns them into a system.", fontFamily = Handwritten, fontSize = 15.sp, lineHeight = 20.sp, color = VInk)
}
