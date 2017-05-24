package pp.block4.cc.cp;

/**
 * Created by han on 19-5-17. -- to trigger thread dump press Ctrl-\ when not terminating or use
 * Intellij and click the photocamera
 */
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    private int l;
    private int r;

    public void leftRight() {
        synchronized(left) {
            synchronized (right) {
                l += 1;
                r -= 1;
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                r += 1;
                l -= 1;
            }
        }
    }
}


/*
=======================================================================================================================
-- -- -- FROM BELOW:

Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007f0104004e28 (object 0x000000076d56bb40, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007f0104003828 (object 0x000000076d56bb30, a java.lang.Object),
  which is held by "Thread-1"

-- -- -- This explains the deadlock: Thread-1 is waiting for a lock that Thread-0 holds and vice versa. Also:

"Thread-1" #12 prio=5 os_prio=0 tid=0x00007f014837e000 nid=0xf79 waiting for monitor entry [0x00007f0130781000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at pp.block4.cp.LeftRightDeadlock.leftRight(LeftRightDeadlock.java:17)
	- waiting to lock <0x000000076d56bb40> (a java.lang.Object)
	- locked <0x000000076d56bb30> (a java.lang.Object)
	at pp.block4.cp.DeadlockRun.run(DeadlockRun.java:26)
	at java.lang.Thread.run(Thread.java:748)

"Thread-0" #11 prio=5 os_prio=0 tid=0x00007f014836c800 nid=0xf78 waiting for monitor entry [0x00007f0130882000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at pp.block4.cp.LeftRightDeadlock.rightLeft(LeftRightDeadlock.java:26)
	- waiting to lock <0x000000076d56bb30> (a java.lang.Object)
	- locked <0x000000076d56bb40> (a java.lang.Object)
	at pp.block4.cp.DeadlockRun.run(DeadlockRun.java:27)
	at java.lang.Thread.run(Thread.java:748)
=======================================================================================================================
 */



/*
/usr/lib/jvm/default-java/bin/java -javaagent:/home/han/Programs/idea-IU-171.4249.39/lib/idea_rt.jar=40358:/home/han/Programs/idea-IU-171.4249.39/bin -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/default-java/jre/lib/charsets.jar:/usr/lib/jvm/default-java/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/default-java/jre/lib/ext/dnsns.jar:/usr/lib/jvm/default-java/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/default-java/jre/lib/ext/jaccess.jar:/usr/lib/jvm/default-java/jre/lib/ext/jfxrt.jar:/usr/lib/jvm/default-java/jre/lib/ext/localedata.jar:/usr/lib/jvm/default-java/jre/lib/ext/nashorn.jar:/usr/lib/jvm/default-java/jre/lib/ext/sunec.jar:/usr/lib/jvm/default-java/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/default-java/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/default-java/jre/lib/ext/zipfs.jar:/usr/lib/jvm/default-java/jre/lib/jce.jar:/usr/lib/jvm/default-java/jre/lib/jfxswt.jar:/usr/lib/jvm/default-java/jre/lib/jsse.jar:/usr/lib/jvm/default-java/jre/lib/management-agent.jar:/usr/lib/jvm/default-java/jre/lib/resources.jar:/usr/lib/jvm/default-java/jre/lib/rt.jar:/usr/lib/jvm/default-java/lib/javafx-mx.jar:/home/han/IdeaProjects/CompilerConstruction/out/production/CompilerConstruction:/home/han/IdeaProjects/libs/antlr-runtime-4.7.jar:/home/han/IdeaProjects/libs/junit-4.12.jar:/home/han/IdeaProjects/libs/hamcrest-core-1.3.jar:/home/han/IdeaProjects/libs/concurrent-junit-1.0.jar pp.block4.cp.DeadlockRun
2017-05-19 09:08:55
Full thread dump OpenJDK 64-Bit Server VM (25.131-b11 mixed mode):

"DestroyJavaVM" #13 prio=5 os_prio=0 tid=0x00007f014800b800 nid=0xf52 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Thread-1" #12 prio=5 os_prio=0 tid=0x00007f014837e000 nid=0xf79 waiting for monitor entry [0x00007f0130781000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at pp.block4.cp.LeftRightDeadlock.leftRight(LeftRightDeadlock.java:17)
	- waiting to lock <0x000000076d56bb40> (a java.lang.Object)
	- locked <0x000000076d56bb30> (a java.lang.Object)
	at pp.block4.cp.DeadlockRun.run(DeadlockRun.java:26)
	at java.lang.Thread.run(Thread.java:748)

"Thread-0" #11 prio=5 os_prio=0 tid=0x00007f014836c800 nid=0xf78 waiting for monitor entry [0x00007f0130882000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at pp.block4.cp.LeftRightDeadlock.rightLeft(LeftRightDeadlock.java:26)
	- waiting to lock <0x000000076d56bb30> (a java.lang.Object)
	- locked <0x000000076d56bb40> (a java.lang.Object)
	at pp.block4.cp.DeadlockRun.run(DeadlockRun.java:27)
	at java.lang.Thread.run(Thread.java:748)

"Service Thread" #10 daemon prio=9 os_prio=0 tid=0x00007f0148365800 nid=0xf76 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread3" #9 daemon prio=9 os_prio=0 tid=0x00007f0148362800 nid=0xf75 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread2" #8 daemon prio=9 os_prio=0 tid=0x00007f0148360800 nid=0xf74 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #7 daemon prio=9 os_prio=0 tid=0x00007f014835e000 nid=0xf73 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=0 tid=0x00007f014835c000 nid=0xf72 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Monitor Ctrl-Break" #5 daemon prio=5 os_prio=0 tid=0x00007f014835a800 nid=0xf71 runnable [0x00007f01314a2000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
	at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
	at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
	- locked <0x000000076d6bbf58> (a java.io.InputStreamReader)
	at java.io.InputStreamReader.read(InputStreamReader.java:184)
	at java.io.BufferedReader.fill(BufferedReader.java:161)
	at java.io.BufferedReader.readLine(BufferedReader.java:324)
	- locked <0x000000076d6bbf58> (a java.io.InputStreamReader)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

"Signal Dispatcher" #4 daemon prio=9 os_prio=0 tid=0x00007f01481fd000 nid=0xf70 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=0 tid=0x00007f01481d4800 nid=0xf68 in Object.wait() [0x00007f0131a13000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000076d408ec8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	- locked <0x000000076d408ec8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" #2 daemon prio=10 os_prio=0 tid=0x00007f01481d0000 nid=0xf66 in Object.wait() [0x00007f0131b14000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000076d406b68> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x000000076d406b68> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=0 tid=0x00007f01481c8000 nid=0xf64 runnable

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x00007f0148020800 nid=0xf5b runnable

"GC task thread#1 (ParallelGC)" os_prio=0 tid=0x00007f0148022800 nid=0xf5c runnable

"GC task thread#2 (ParallelGC)" os_prio=0 tid=0x00007f0148024000 nid=0xf5d runnable

"GC task thread#3 (ParallelGC)" os_prio=0 tid=0x00007f0148026000 nid=0xf5e runnable

"GC task thread#4 (ParallelGC)" os_prio=0 tid=0x00007f0148027800 nid=0xf5f runnable

"GC task thread#5 (ParallelGC)" os_prio=0 tid=0x00007f0148029000 nid=0xf60 runnable

"GC task thread#6 (ParallelGC)" os_prio=0 tid=0x00007f014802b000 nid=0xf61 runnable

"GC task thread#7 (ParallelGC)" os_prio=0 tid=0x00007f014802c800 nid=0xf62 runnable

"VM Periodic Task Thread" os_prio=0 tid=0x00007f0148368000 nid=0xf77 waiting on condition

JNI global references: 18


Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007f0104004e28 (object 0x000000076d56bb40, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007f0104003828 (object 0x000000076d56bb30, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
	at pp.block4.cp.LeftRightDeadlock.leftRight(LeftRightDeadlock.java:17)
	- waiting to lock <0x000000076d56bb40> (a java.lang.Object)
	- locked <0x000000076d56bb30> (a java.lang.Object)
	at pp.block4.cp.DeadlockRun.run(DeadlockRun.java:26)
	at java.lang.Thread.run(Thread.java:748)
"Thread-0":
	at pp.block4.cp.LeftRightDeadlock.rightLeft(LeftRightDeadlock.java:26)
	- waiting to lock <0x000000076d56bb30> (a java.lang.Object)
	- locked <0x000000076d56bb40> (a java.lang.Object)
	at pp.block4.cp.DeadlockRun.run(DeadlockRun.java:27)
	at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.

Heap
 PSYoungGen      total 74752K, used 7741K [0x000000076d400000, 0x0000000772700000, 0x00000007c0000000)
  eden space 64512K, 12% used [0x000000076d400000,0x000000076db8f7e8,0x0000000771300000)
  from space 10240K, 0% used [0x0000000771d00000,0x0000000771d00000,0x0000000772700000)
  to   space 10240K, 0% used [0x0000000771300000,0x0000000771300000,0x0000000771d00000)
 ParOldGen       total 171008K, used 0K [0x00000006c7c00000, 0x00000006d2300000, 0x000000076d400000)
  object space 171008K, 0% used [0x00000006c7c00000,0x00000006c7c00000,0x00000006d2300000)
 Metaspace       used 3238K, capacity 4500K, committed 4864K, reserved 1056768K
  class space    used 350K, capacity 388K, committed 512K, reserved 1048576K


 */
