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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val VBlue = Color(0xFF244F86)
private val VBlue2 = Color(0xFF4B86B9)
private val VPurple = Color(0xFF68458C)
private val VGreen = Color(0xFF4C8A70)
private val VOrange = Color(0xFFC57A38)
private val VRed = Color(0xFFB65B62)
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
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(title, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = VPurple)
            content()
        }
    }
}

@Composable
private fun Node(label: String, color: Color = VBlue, modifier: Modifier = Modifier) {
    Box(modifier.sizeIn(minWidth = 66.dp, minHeight = 44.dp).background(color, RoundedCornerShape(10.dp)).padding(horizontal = 8.dp, vertical = 7.dp), contentAlignment = Alignment.Center) {
        Text(label, fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
    }
}

@Composable
private fun ClusterMap() = VisualCard("HPC CLUSTER — THE REAL MACHINE") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("GPU\nNODE"); Text("8 GPUs", fontFamily = Handwritten, color = VInk) }
        Text("⇄", fontSize = 25.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("FABRIC", VPurple); Text("IB / RoCE", fontFamily = Handwritten, color = VInk) }
        Text("⇄", fontSize = 25.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("STORAGE", VGreen); Text("Lustre / Scale", fontFamily = Handwritten, color = VInk) }
    }
    Text("A real AI system is a coordinated data path: compute + memory + fabric + storage + software.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun ClusterRoles() = VisualCard("INSIDE THE CLUSTER") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("LOGIN")
        Node("CONTROL", VPurple)
        Node("COMPUTE", VBlue2)
        Node("STORAGE", VGreen)
    }
    Text("Users submit → scheduler allocates → compute runs → shared storage serves data.", fontFamily = Handwritten, fontSize = 16.sp, color = VInk)
}

@Composable
private fun CpuVsGpu() = VisualCard("CPU vs GPU — DIFFERENT TOOLS") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Node("CPU", VBlue)
            Text("few powerful cores\ncomplex control", fontFamily = Handwritten, color = VInk)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Node("GPU", VPurple)
            Text("many parallel\nexecution units", fontFamily = Handwritten, color = VInk)
        }
    }
    Text("AI training loves massive tensor parallelism — but only when the data arrives fast enough.", fontFamily = Handwritten, fontSize = 16.sp, color = VInk)
}

@Composable
private fun SoftwareStack() = VisualCard("HPC SOFTWARE STACK") {
    listOf("APPLICATION / AI FRAMEWORK", "MPI • NCCL • OpenMP", "SLURM + SERVICES", "LUSTRE / STORAGE SCALE", "RDMA / NETWORK", "LINUX + DRIVERS", "CPU • GPU • NIC • NVMe").forEachIndexed { i, s ->
        Box(Modifier.fillMaxWidth().background(if (i % 2 == 0) Color(0xFFD9E9F6) else Color(0xFFE8DCF3), RoundedCornerShape(7.dp)).padding(6.dp)) {
            Text(s, fontFamily = Handwritten, fontWeight = FontWeight.SemiBold, color = VInk)
        }
    }
}

@Composable
private fun SchedulerQueue() = VisualCard("SLURM — JOBS WAIT FOR RESOURCES") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("JOB 101", VOrange); Node("JOB 102", VOrange) }
        Text("→", fontSize = 28.sp, color = VPurple)
        Node("SLURM", VPurple)
        Text("→", fontSize = 28.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("GPU-01", VBlue); Node("GPU-02", VBlue) }
    }
    Text("The scheduler is a resource broker, not the application itself.", fontFamily = Handwritten, fontSize = 16.sp, color = VInk)
}

@Composable
private fun ParallelWorkers() = VisualCard("PARALLEL WORK — MANY WORKERS") {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Node("DATASET", VGreen)
        Text("↓ split", fontSize = 21.sp, color = VPurple)
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { (1..4).forEach { Node("W$it", VBlue) } }
        Text("↓ combine / synchronize", fontSize = 18.sp, color = VPurple)
        Node("RESULT", VOrange)
    }
}

@Composable
private fun MpiRanks() = VisualCard("MPI — RANKS TALK ACROSS NODES") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("NODE A", VBlue); Text("rank 0\nrank 1", fontFamily = Handwritten, color = VInk) }
        Text("⇄", fontSize = 28.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("FABRIC", VPurple); Text("latency + bandwidth", fontFamily = Handwritten, color = VInk) }
        Text("⇄", fontSize = 28.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("NODE B", VBlue); Text("rank 2\nrank 3", fontFamily = Handwritten, color = VInk) }
    }
}

@Composable
private fun OpenMpNode() = VisualCard("OPENMP — THREADS INSIDE ONE NODE") {
    Node("ONE SERVER", VBlue)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { (1..4).forEach { Node("T$it", VBlue2) } }
    Text("Threads share the node's memory; MPI is normally used when work spans nodes.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun PerformanceLab(day: Int) = VisualCard(when(day) {31 -> "FIO — YOUR STORAGE MICROSCOPE"; 32 -> "QUEUE DEPTH — OUTSTANDING I/O"; 33 -> "LATENCY — THE WAIT"; 34 -> "THROUGHPUT — DATA PER SECOND"; else -> "PERFORMANCE — MEASURE, DON'T GUESS"}) {
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val base = size.height - 20f
        drawLine(VInk, Offset(20f, base), Offset(size.width - 15f, base), 3f)
        drawLine(VInk, Offset(20f, 15f), Offset(20f, base), 3f)
        val bars = listOf(.35f, .55f, .72f, .9f)
        bars.forEachIndexed { i, h ->
            val x = 55f + i * 75f
            drawRect(if (i % 2 == 0) VBlue else VPurple, Offset(x, base - 95f*h), Size(42f, 95f*h))
        }
    }
    Text("A benchmark is only meaningful when block size, read/write mix, concurrency, queue depth and client count are known.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun ParallelFilesystem(day: Int) = VisualCard(when(day) {10 -> "PARALLEL FILESYSTEM — MANY TARGETS"; 35 -> "HPC STORAGE ARCHITECTURE"; 36 -> "LUSTRE DATA PATH"; 37 -> "METADATA IS THE CATALOG"; 38 -> "STRIPING — SPLIT THE FILE"; else -> "STORAGE SCALE — MANY CLIENTS"}) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("CLIENTS", VBlue); Text("many", fontFamily = Handwritten, color = VInk) }
        Text("→", fontSize = 28.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("MDS", VPurple); Text("metadata", fontFamily = Handwritten, color = VInk) }
        Text("+", fontSize = 25.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("OSS", VGreen); Text("data", fontFamily = Handwritten, color = VInk) }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { (1..4).forEach { Node("OST$it", VGreen) } }
    Text("Metadata and bulk file data are different workloads. Parallel storage separates and scales them.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun NvmeAnatomy() = VisualCard("NVMe — PARALLEL QUEUES CLOSE TO THE MEDIA") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("CPU", VBlue)
        Text("PCIe", fontFamily = Handwritten, fontSize = 16.sp, color = VPurple)
        Node("NVMe", VGreen)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { (1..4).forEach { Node("Q$it", VOrange) } }
    Text("NVMe uses multiple submission/completion queues and is designed for modern solid-state storage. Queue parallelism is one reason it scales far beyond legacy single-queue models.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun NvmeOfPath() = VisualCard("NVMe-oF — NVMe OVER A FABRIC") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node("NVMe\nCLIENT", VBlue)
        Text("→", fontSize = 26.sp, color = VPurple)
        Node("RDMA / TCP", VPurple)
        Text("→", fontSize = 26.sp, color = VPurple)
        Node("NVMe\nTARGET", VGreen)
    }
    Text("The media can be remote while the command model remains NVMe. Transport choices change the network path and latency profile.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun RdmaPath() = VisualCard("RDMA — MOVE DATA WITH LESS CPU INVOLVEMENT") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node("HOST A", VBlue)
        Text("⇢", fontSize = 30.sp, color = VPurple)
        Node("HCA / NIC", VPurple)
        Text("⇢", fontSize = 30.sp, color = VPurple)
        Node("HOST B", VGreen)
    }
    Text("Key vocabulary: verbs, QP, CQ, MR, HCA, one-sided operations, zero-copy concepts and the difference between RDMA semantics and the physical fabric.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun FabricVisual(day: Int) = VisualCard(if (day == 14) "INFINIBAND / ROCE — THE HIGH-SPEED FABRIC" else "FABRIC TROUBLESHOOTING") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("GPU", VBlue); Node("HCA", VBlue2) }
        Text("⇄", fontSize = 28.sp, color = VPurple)
        Node("SWITCH", VPurple)
        Text("⇄", fontSize = 28.sp, color = VPurple)
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Node("HCA", VGreen); Node("GPU", VGreen) }
    }
    Text("At scale, link speed alone is not enough: topology, congestion, QoS, routing, MTU and error counters matter.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun AiDataPath() = VisualCard("AI DATA PATH — KEEP THE GPU FED") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node("DATA", VGreen)
        Text("→", fontSize = 24.sp, color = VPurple)
        Node("STORAGE", VBlue)
        Text("→", fontSize = 24.sp, color = VPurple)
        Node("FABRIC", VPurple)
        Text("→", fontSize = 24.sp, color = VPurple)
        Node("GPU", VOrange)
    }
    Text("Training: read batches → preprocess → transfer → compute → synchronize → checkpoint. Any slow stage can leave expensive accelerators waiting.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun LinuxWorkbench(day: Int) = VisualCard("LINUX FIELD KIT — DAY $day") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("CPU", VBlue); Node("RAM", VPurple); Node("DISK", VGreen); Node("NIC", VOrange)
    }
    Text("Use the shell to observe the system: ps/top for processes, free/vmstat for memory, lsblk/df/mount for storage, ip/ss for networking, journalctl/dmesg for logs.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun ScriptingWorkbench(day: Int) = VisualCard(if (day == 25) "PYTHON — TURN DATA INTO TOOLS" else "BASH — AUTOMATE THE BORING WORK") {
    Node(if (day == 25) "PYTHON" else "BASH", VPurple)
    Text("INPUT → PARSE → CHECK → ACT → LOG → REPORT", fontFamily = Handwritten, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = VInk)
    Text("The goal is not clever code. It is repeatable operations, safe failure handling, useful logs and predictable output.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun ProtocolMap() = VisualCard("STORAGE PROTOCOL MAP") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("BLOCK", VBlue); Node("FILE", VPurple); Node("OBJECT", VGreen)
    }
    Text("FC / iSCSI / NVMe-oF  •  NFS / SMB / Lustre  •  S3 API", fontFamily = Handwritten, fontSize = 16.sp, color = VInk)
}

@Composable
private fun NfsPath() = VisualCard("NFS — FILES OVER IP") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node("APP", VBlue); Text("→", fontSize = 25.sp, color = VPurple); Node("NFS\nCLIENT", VPurple); Text("→", fontSize = 25.sp, color = VPurple); Node("NFS\nSERVER", VGreen)
    }
    Text("APP → VFS → NFS client → network/RPC → server → filesystem → media", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun S3Visual() = VisualCard("S3 — OBJECTS, NOT MOUNT POINTS") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node("APP", VBlue); Text("HTTPS / API", fontFamily = Handwritten, color = VPurple); Node("BUCKET", VGreen)
    }
    Text("Bucket → key → object + metadata. Think API requests and object identity, not POSIX paths and inode operations.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun RaidVisual() = VisualCard("RAID — CAPACITY, PERFORMANCE, PROTECTION") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("0\nSTRIPE", VOrange); Node("1\nMIRROR", VBlue); Node("6\nPARITY", VPurple); Node("10\nMIRROR+STRIPE", VGreen)
    }
    Text("RAID is one protection layer. Distributed HPC platforms may add replication, erasure coding and filesystem-level resilience above it.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun CephVisual() = VisualCard("CEPH — DISTRIBUTED SOFTWARE STORAGE") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("MON", VPurple); Node("MGR", VOrange); Node("OSD", VBlue); Node("OSD", VBlue); Node("OSD", VBlue)
    }
    Text("RBD = block • CephFS = file • RGW = object/S3. CRUSH decides data placement; OSDs store data and participate in recovery.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun GdsVisual() = VisualCard("GPU DIRECT STORAGE — SHORTEN THE DATA PATH") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Node("NVMe /\nLUSTRE", VGreen); Text("⇢", fontSize = 28.sp, color = VPurple); Node("NIC", VPurple); Text("⇢", fontSize = 28.sp, color = VPurple); Node("GPU\nMEMORY", VOrange)
    }
    Text("The design goal is to reduce unnecessary CPU-side copies and move data efficiently between storage/network and GPU memory where supported.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}

@Composable
private fun GenericEquipmentVisual(day: Int) = VisualCard("FIELD EQUIPMENT — DAY $day") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Node("SERVER", VBlue); Node("SWITCH", VPurple); Node("STORAGE", VGreen)
    }
    Text("Real deployments are built from physical servers, NICs, switches, storage targets, power and cooling — then software turns them into a system.", fontFamily = Handwritten, fontSize = 15.sp, color = VInk)
}
