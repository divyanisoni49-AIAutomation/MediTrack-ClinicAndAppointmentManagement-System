JVM Report — MediTrack Project
1. Class Loader Subsystem
   The Class Loader is the first component of the JVM that loads .class (bytecode) files into memory. It operates in three phases:

Loading: Reads the .class file and creates a Class object in the heap.
Linking: Verifies bytecode integrity, prepares static variables with default values, and resolves symbolic references.
Initialization: Executes static {} blocks and initializes static fields.

In MediTrack: Every class with a static {} block (e.g., IdGenerator, MedicalEntity, Doctor, Patient) demonstrates class loading. When IdGenerator is first accessed, the JVM's Class Loader loads it, triggering the static block that prints the initialization message.
Three Built-in Class Loaders:

Bootstrap ClassLoader – Loads core Java classes (java.lang, java.util)
Extension ClassLoader – Loads JDK extension classes
Application ClassLoader – Loads application classes (our com.airtribe.meditrack.*)


2. Runtime Data Areas
   The JVM divides memory into five key runtime areas:
   2.1 Heap

Largest memory area; shared across all threads
Stores all objects and instance variables (e.g., every Patient, Doctor, Appointment object)
Divided into Young Generation (Eden + Survivor spaces) and Old Generation
Managed by the Garbage Collector
In MediTrack: patientStore, doctorStore (DataStore objects) live here

2.2 Stack (JVM Stack)

One stack per thread; stores stack frames for each method call
Each frame holds: local variables, operand stack, frame data
Follows LIFO (Last In, First Out)
In MediTrack: when addPatient() calls validateName(), a new frame is pushed onto the stack

2.3 Method Area (Metaspace in Java 8+)

Shared across threads
Stores class-level data: bytecode, method definitions, static variables, runtime constant pool
In MediTrack: Constants.TAX_RATE, IdGenerator.INSTANCE (static fields) are stored here

2.4 PC (Program Counter) Register

One per thread
Holds the address of the currently executing JVM instruction
Undefined for native methods

2.5 Native Method Stack

Stores frames for native (C/C++) methods called via JNI
Used by methods like System.currentTimeMillis() internally


3. Execution Engine
   The Execution Engine reads bytecode instructions one by one and executes them. It has three components:
   3.1 Interpreter

Reads and executes bytecode one instruction at a time
Advantage: Fast startup, simple
Disadvantage: Slow repeated execution — re-interprets the same code every time

3.2 JIT Compiler (Just-In-Time)

Monitors method call frequency; methods called often ("hot methods") are compiled to native machine code
Native code is cached — no re-interpretation on future calls
Advantage: Dramatically faster repeated execution
Disadvantage: Compilation takes time upfront

In MediTrack: The searchDoctor() and searchPatient() methods, called frequently, would be JIT-compiled in a long-running production scenario.
3.3 Garbage Collector (GC)

Automatically frees heap memory occupied by unreachable objects
Common algorithms: Serial GC, Parallel GC, G1 GC (default in JDK 9+), ZGC
In MediTrack: when a Patient is removed from patientStore, the object becomes unreachable and eligible for GC


4. JIT Compiler vs Interpreter
   FeatureInterpreterJIT CompilerExecution speedSlow (re-reads each time)Fast (compiles to native code)Startup timeFastSlightly slowerMemory usageLowHigher (stores compiled code)Best forOne-time code pathsHot/repeated code pathsJVM strategyUsed initiallyTakes over hot methods
   The JVM uses both: it interprets initially (fast startup), and the JIT compiles frequently-called methods progressively (fast steady-state performance). This is called adaptive optimization.

5. "Write Once, Run Anywhere" (WORA)
   Java's WORA promise is achieved through the bytecode + JVM abstraction layer:
   Java Source (.java)
   ↓ javac (compiler)
   Java Bytecode (.class)     ← Platform-independent
   ↓ JVM (platform-specific)
   Native Machine Code        ← Platform-dependent
   How it works:

javac compiles .java → .class (bytecode). Bytecode is not machine code — it's an intermediate format.
The .class files are identical on all platforms.
Each OS has its own JVM implementation (Windows JVM, Linux JVM, macOS JVM).
Each JVM knows how to translate the same bytecode into native instructions for its OS/CPU.

In MediTrack: We can compile Main.java on Windows and run the resulting .class files on Linux — as long as a JVM is installed. The bytecode doesn't change.
Limitation: "Write Once, Run Anywhere" applies to the JVM language, not native libraries. JNI-dependent code (e.g., using .dll files) is platform-specific.