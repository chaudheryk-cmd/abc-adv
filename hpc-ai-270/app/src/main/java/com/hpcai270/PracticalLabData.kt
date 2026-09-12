package com.hpcai270

/**
 * Senior transition layer: converts the user's existing enterprise-storage
 * experience into HPC/AI evidence through a small practical exercise on every
 * study day. These are deliberately scenario-driven rather than beginner IT.
 */
fun practicalLab(day: Day): String {
    val (lab, failure, evidence) = when (day.number) {
        in 1..5 -> Triple(
            "Draw the end-to-end HPC data path for this lesson. Mark the component you already know from enterprise storage and the component that is new HPC knowledge.",
            "Failure drill: one stage becomes 10x slower. Identify three measurements that tell you where the slowdown starts.",
            "Save a one-page architecture sketch and a five-line explanation."
        )
        in 6..15 -> Triple(
            "Build or simulate the storage path discussed today. Trace client → network → protocol → storage and record latency, throughput and concurrency assumptions.",
            "Failure drill: storage is reachable but application throughput collapses. Separate protocol, host, network and backend hypotheses.",
            "Record commands/observations, expected result, actual result and one corrective action."
        )
        in 16..25 -> Triple(
            "Use Linux tools to inspect CPU, memory, processes, filesystems and networking relevant to the day's topic. Explain every metric you collect.",
            "Failure drill: reproduce a resource bottleneck or construct a controlled example. Identify the first observable symptom and the root cause.",
            "Keep a dated command log plus a short RCA."
        )
        in 26..45 -> Triple(
            "Translate the day's enterprise-storage concept into an HPC workload requirement. State the required protocol, access pattern, latency/throughput target and failure domain.",
            "Failure drill: a storage workload misses its target. Build a hypothesis tree before changing configuration.",
            "Produce a mini design with workload, bottleneck, metric and remediation."
        )
        in 46..61 -> Triple(
            "Operate or simulate a parallel filesystem concept from today's lesson. Focus on metadata, data placement, clients and parallel I/O rather than memorization.",
            "Failure drill: metadata latency rises while bulk bandwidth remains healthy. Explain why and propose tests.",
            "Capture an architecture diagram and an incident checklist."
        )
        in 62..72 -> Triple(
            "Model the distributed-storage behavior covered today: placement, replicas/EC, recovery, client path and failure domains. Calculate the relevant capacity or performance trade-off.",
            "Failure drill: lose a storage component during recovery. Predict the blast radius and the metrics that prove recovery health.",
            "Write the calculation, assumptions and recovery decision."
        )
        in 73..83 -> Triple(
            "Trace an HPC network data path and identify NIC, transport, fabric, congestion and locality considerations introduced today.",
            "Failure drill: connectivity works but RDMA/application performance is poor. Rank the checks from host to fabric.",
            "Produce a troubleshooting flowchart with expected healthy signals."
        )
        in 84..94 -> Triple(
            "Map the AI data pipeline for today's concept: dataset → storage → network → CPU/GPU → checkpoint/artifact. Identify where data movement can starve compute.",
            "Failure drill: GPUs are underutilized during training. Give five storage/network hypotheses and the measurement for each.",
            "Save a pipeline diagram and a bottleneck analysis."
        )
        in 95..117 -> Triple(
            "Perform a senior operations exercise: define an SLO, baseline, alert threshold and first-response procedure for today's production topic.",
            "Failure drill: an alert fires without an obvious outage. Correlate host, storage, network and workload evidence before remediation.",
            "Create an incident timeline and a concise RCA."
        )
        in 118..134 -> Triple(
            "Take the day's enterprise-storage mechanism and redesign it for a high-concurrency HPC workload. Explicitly state what changes and why.",
            "Failure drill: a familiar enterprise tuning approach causes an HPC regression. Explain the workload mismatch.",
            "Submit a before/after architecture and tuning rationale."
        )
        in 135..151 -> Triple(
            "Design or simulate the parallel-filesystem behavior from today's lesson. Address metadata scale, striping/placement, clients and failure domains.",
            "Failure drill: small files or metadata-heavy access dominates workload time. Diagnose without assuming the disks are slow.",
            "Record workload shape, measurements and design changes."
        )
        in 152..168 -> Triple(
            "Trace the high-speed fabric path for today's topic, including NIC/HCA, RDMA transport, topology, QoS/congestion and GPU/storage locality where applicable.",
            "Failure drill: bandwidth is below expectation despite a healthy link. Build a layered isolation plan.",
            "Create a fabric troubleshooting matrix."
        )
        in 169..185 -> Triple(
            "Run a performance experiment or use a reproducible benchmark model for today's concept. Change one variable at a time.",
            "Failure drill: benchmark results improve but real workload performance does not. Identify measurement and workload-shape traps.",
            "Keep baseline, change, result, interpretation and next experiment."
        )
        in 186..202 -> Triple(
            "Design a distributed-storage scenario using today's Ceph concepts. Explain placement, failure domain, client protocol and recovery behavior.",
            "Failure drill: degraded data and recovery traffic compete with production I/O. Choose controls and justify them.",
            "Produce a failure-domain diagram and recovery plan."
        )
        in 203..219 -> Triple(
            "Design a scheduler/container/storage interaction for today's orchestration topic. Explain resource placement and how storage reaches the workload.",
            "Failure drill: a job is scheduled successfully but data access is slow or unavailable. Isolate scheduler, node, network and storage causes.",
            "Write a job-to-storage flow and troubleshooting sequence."
        )
        in 220..236 -> Triple(
            "Trace an AI workload from dataset ingestion through preprocessing, GPU training, checkpointing and artifact storage. Identify locality and data-movement costs.",
            "Failure drill: checkpointing stalls training. Determine whether the constraint is application, GPU/CPU, network or storage.",
            "Capture one AI workload architecture and its performance hypotheses."
        )
        in 237..253 -> Triple(
            "Design a cloud or hybrid implementation of today's HPC requirement. Compare performance, latency, egress, resilience and operational trade-offs.",
            "Failure drill: migration works functionally but violates performance or cost targets. Identify the missing requirement.",
            "Create a decision matrix and a migration/rollback step."
        )
        else -> Triple(
            "Act as the senior architect. Design the smallest production-ready AI/HPC solution that satisfies today's requirement, including SLOs, failure domains, observability and operational ownership.",
            "Failure drill: introduce a realistic component failure and defend your diagnosis, mitigation and long-term corrective action.",
            "Save an architecture decision record and a five-minute interview-style explanation."
        )
    }

    return "FIELD LAB — DAY ${day.number}\n\nDO\n$lab\n\nBREAK IT\n$failure\n\nPROVE IT\n$evidence"
}
