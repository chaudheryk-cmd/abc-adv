package com.hpcai270

val day1Expanded = Day(
    1,
    "What is HPC?",
    "From one computer to an AI factory",
    "HPC means High-Performance Computing. Start with a normal computer: CPU, memory, storage and networking. When a problem becomes too large or too slow for one machine, many computers can be connected and coordinated. Those computers are called nodes; together they form a cluster.\n\nThe important idea is coordinated parallel work — not simply buying a bigger server.",
    "REAL-LIFE ANALOGY\n\nImagine one person unloading 1,000 boxes. Now imagine 100 people working together. The job can finish much faster, but only if someone coordinates them and the boxes can move efficiently.\n\nIn HPC: workers = compute nodes; roads = network; warehouse = storage; supervisor = scheduler.",
    "Explain HPC to a non-technical person in 30 seconds. Draw three nodes working on one problem.",
    listOf(
        "THE BASIC IDEA\n\nBIG PROBLEM → SPLIT INTO WORK → MANY COMPUTE NODES → COMMUNICATE → COMBINE RESULTS\n\nSome workloads parallelize extremely well. Others contain serial work or require frequent communication, so adding machines eventually gives diminishing returns.",
        "THE SIX BUILDING BLOCKS\n\n1. COMPUTE — CPU/GPU executes instructions.\n2. MEMORY — active working data.\n3. NETWORK — moves data and messages.\n4. STORAGE — datasets, applications and results.\n5. SCHEDULER — allocates resources to jobs.\n6. SOFTWARE — Linux, drivers, libraries, filesystems and applications.",
        "HPC VS ONE POWERFUL SERVER\n\nA powerful server can solve many problems. HPC becomes valuable when a workload can use multiple machines, needs very large aggregate compute or memory, or must finish within a strict time window.\n\nThe price of scale is complexity: synchronization, networking, failures, scheduling and data movement all matter.",
        "WHY AI IS HPC-LIKE\n\nAI training can use many GPUs across many servers. GPUs repeatedly consume training data, communicate with other GPUs and write checkpoints.\n\nDATASET → STORAGE → NETWORK → GPU MEMORY → GPU COMPUTE → CHECKPOINT → STORAGE\n\nIf data delivery is slow, expensive GPUs may wait. AI infrastructure is therefore a full pipeline, not just a GPU purchase.",
        "REAL PRODUCTS TO RECOGNIZE\n\nGPU platforms: NVIDIA DGX/HGX and AMD Instinct systems.\n\nHPC/AI storage: DDN EXAScaler, IBM Storage Scale, Dell PowerScale, WEKA and VAST Data.\n\nNetworking: NVIDIA ConnectX adapters, Quantum InfiniBand and Spectrum Ethernet platforms.\n\nToday, learn the category and purpose — not product specifications.",
        "YOUR STORAGE CONNECTION\n\nYour enterprise-storage starting question is exactly right: HOW DOES A SERVER ACCESS DATA?\n\nIn a conventional environment, fewer servers may access NAS or SAN. In HPC, hundreds or thousands of compute nodes may access a parallel filesystem concurrently. Capacity, latency, throughput, redundancy and failure handling still matter — but the performance envelope is much higher.",
        "DAY 1 CHECKPOINT\n\nYou should be able to define: node, cluster, parallelism, compute, storage, network, scheduler and GPU starvation.\n\nIf you can draw the complete data path without looking, you have the foundation for the rest of the book."
    )
)
