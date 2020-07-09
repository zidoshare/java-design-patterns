# synchronized关键字

## 锁的内存语义

* 锁可以让临界区互斥执行，还可以让释放锁的线程向同一个锁的线程发送消息
* 锁的释放要遵循Happens-before原则（锁规则：解锁必然发生在最后的加锁之前）
* 锁在java中的具体表现时`Synchronized`和`Lock`

## 复现步骤

通过gradle/javac编译[SynchronizedDemo.java](./src/main/java/site/zido/sync/SynchronizedDemo.java)出对应的class。

> 注意如果使用javac编译，记得加入参数`-encoding UTF-8`

使用jdk命令`javap`查看字节码

运行 `javap -v SynchronizedDemo.class`

输出内容文件见：[SynchronizedDemo.class.javap.txt](./SynchronizedDemo.class.javap.txt).

## jvm实现

通过查看字节码具体指令可以看到：

`synchronized`修饰方法时：

```
  public static synchronized void staticMethod() throws java.lang.InterruptedException;
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
```

会为方法放置一个`ACC_SYNCHRONIZED`标志（flags）。

当使用`synchronized`代码块时：
```
public void blockStaticMethod1() throws java.lang.InterruptedException;
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=1
         0: getstatic     #17                 // Field STATIC_MONITOR:Ljava/lang/Object;
         3: dup
         4: astore_1
         5: monitorenter
         6: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
         9: ldc           #18                 // String 静态对象同步方法1开始
        11: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        14: ldc2_w        #14                 // long 3000l
        17: invokestatic  #9                  // Method java/lang/Thread.sleep:(J)V
        20: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
        23: ldc           #19                 // String 静态对象同步方法1结束
        25: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        28: aload_1
        29: monitorexit
```

在方法上并没有标志，二十在代码中加入`monitorenter`和`monitorexit`指令实现。

而这在[The Java® Virtual Machine Specification 3.14. Synchronization](https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-3.html#jvms-3.14)中有很详细的描述。

# 同步代码块原理

## monitor监视器

操作系统在面对 进程/线程 间同步时，semaphore 信号量 和 mutex 互斥量是最重要的同步原语。
在使用基本的 mutex 进行并发控制时，需要程序员非常小心地控制 mutex 的 down 和 up 操作，否则很容易引起死锁等问题。
为了更容易地编写出正确的并发程序，所以在 mutex 和 semaphore 的基础上，提出了更高层次的同步原语 monitor。

操作系统本身并不支持 monitor 机制，monitor 是属于编程语言的范畴。

monitor本质上时一种通用同步工具的抽象，**同一时刻只能有一个进程/线程执行该方法或过程，从而简化了并发应用的开发难度。**

如果Monitor内没有线程正在执行，则线程可以进入Monitor执行方法，否则该线程被放入入口队列(entry queue)并使其挂起。当有线程从Monitor中退出时，会唤醒entry queue中的一个线程。

## monitorenter和monitorexit指令

同步代码块使用monitorenter和monitorexit两个指令实现。 [The Java® Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html) 中有关于这两个指令的介绍：

monitorenter:

    Each object is associated with a monitor. A monitor is locked if and only if it has an owner. The thread that executes monitorenter attempts to gain ownership of the monitor associated with objectref, as follows:
    
    If the entry count of the monitor associated with objectref is zero, the thread enters the monitor and sets its entry count to one. The thread is then the owner of the monitor.
    If the thread already owns the monitor associated with objectref, it reenters the monitor, incrementing its entry count.
    If another thread already owns the monitor associated with objectref, the thread blocks until the monitor's entry count is zero, then tries again to gain ownership

每个对象都有一个监视器。当该监视器被占用时即是锁定状态(或者说获取监视器即是获得同步锁)。
线程执行monitorenter指令时会尝试获取监视器的所有权，过程如下：  

* 若该监视器的进入次数为0，则该线程进入监视器并将进入次数设置为1，此时该线程即为该监视器的所有者
* 若线程已经占有该监视器并重入，则进入次数+1
* 若其他线程已经占有该监视器，则线程会被阻塞直到监视器的进入次数为0，之后线程间会竞争获取该监视器的所有权
* 只有首先获得锁的线程才能允许继续获取多个锁

monitorexit:

    The thread that executes monitorexit must be the owner of the monitor associated with the instance referenced by objectref.
    The thread decrements the entry count of the monitor associated with objectref. If as a result the value of the entry count is zero, the thread exits the monitor and is no longer its owner. Other threads that are blocking to enter the monitor are allowed to attempt to do so.
    
执行monitorexit指令将遵循以下步骤：  

* 执行monitorexit指令的线程必须是对象实例所对应的监视器的所有者
* 指令执行时，线程会先将进入次数-1，若-1之后进入次数变成0，则线程退出监视器(即释放锁)
* 其他阻塞在该监视器的线程可以重新竞争该监视器的所有权

由于 wait/notify 等方法底层实现是基于监视器，因此只有在同步方法(块)中才能调用wait/notify等方法，否则会抛出 java.lang.IllegalMonitorStateException 的异常的原因

# 锁优化

通过上面查看的monitor机制，会发现多线程中会有频繁线程的阻塞和唤醒，这需要CPU在用户态和内核态之间频繁的切换，
这对CPU的负担很重，进而对并发性能带来很大的影响。

让我们仔细对情况进行分析，逐步根据各种情况进行锁性能的思考：

1. 我们知道线程切换的开销很高，当线程争抢锁时，当前线程会经历阻塞/唤醒的状态，但是cpu阻塞/唤醒线程代价很高，但是如果实际上这里只需要稍稍等一下，也就是说如果线程阻塞/唤醒时间其实远远大于等待的时间，我们是否应该考虑不让线程切换呢？
2. 看看实际情况中，如果我们写这段代码只是为了应付某些临界情况（万一有另外一个线程争抢锁），一般来说这个锁很快就用完了，根本没有其他的线程来进行竞争，我们还需要申请系统的锁吗？
3. 当上面的情况发生转变，出现了一个新的线程争抢锁，或者说少量的线程来争抢，和大量争抢的情况呢？
4. 查看下面这种代码
   ```java
       public void doSomethingMethod(){
           synchronized(lock){
               //do some thing
           }
           //do something
           synchronized(lock){
               //do other thing
           }
       }
   ```
   如果请求两个同样的锁，中间只是很简单的一个计算，时间很短，这是线程会对同一个锁多次请求、同步、释放。甚至更极端的情况如下：
   ```java
       for(int i=0;i<size;i++){
           synchronized(lock){
           }
       }
   ```
   多次请求同一个锁的情况，如果还需要频繁请求释放，会大大降低效率，那么此时是否应该把这些同步代码合并呢？
5. 如果说此时写了一段代码
   ```java
    public void append(String str1,String str2){
       StringBuffer stringBuffer = new StringBuffer();
       stringBuffer.append(str1).append(str2);
    }
   ```
   这种情况下，虽然StringBuffer内部有锁，但是很明显就能分析出，这是本地变量，实际上不需要进行同步，那么是否可以消除掉这种无意义的锁呢？

如此诸多情况，我们接下来一个一个分析：

## 自旋锁

jdk针对第一种情况引入了自旋锁。

痛点：由于线程的阻塞/唤醒需要CPU在用户态和内核态间切换，频繁的转换对CPU负担很重，进而对并发性能带来很大的影响

其原理是 通过执行一段无意义的空循环让线程等待一段时间，不会立即被挂起，看持有锁的线程是否很快释放锁，
如果锁很快被释放，那当前线程就有机会不用阻塞就能拿到锁，从而减少切换，提高性能。

循环中要进行什么操作呢？先看看需求：
* 我们需要先查看原始的状态是否为未加锁
* 如果已经加锁，则表示没有获取到锁
* 如果没有加锁，则需要替换为已加锁的状态

我们正常的思路大概是这样：
```java
if(state == 0){
    state = 1;
}
```
但是这会产生一个问题，我们如何保证第一行执行而第二行还未执行的情况下，没有另外一个线程把`state`修改为1了呢？如果不保证那么会出现多个线程都去判断修改并且都成功了的情况。

于是为了解决这个问题，我们需要使用一个由cpu提供的CAS原子指令，而java对此进行了相应的调用封装，这就是Java Unsafe类中提供的`compareAndSwap*`方法，包括：
* `compareAndSwapObject(Object var1, long var2, Object var4, Object var5);`
* `compareAndSwapInt(Object var1, long var2, int var4, int var5);`
* `compareAndSwapLong(Object var1, long var2, long var4, long var6);`

将上述三行代码进行原子合并，具体原理大概是：CPU可以自动更新共享数据，而且能够检测到其他线程的干扰。更深处就不再追究了。
我们只需要了解`CAS`即可，多线程中`CAS`由大量的应用，务必牢记。

而自旋锁则是编写一个do-while循环，如果修改数值失败则通过循环来执行自旋，直至修改成功。

代码展示可以查看Unsafe的`getAndAddInt`方法：

```java
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
    return var5;
}
```

至此逻辑也就清晰了，自旋锁实际上就是`CAS`指令+循环实现。

CAS虽然很高效，但是它也存在三大问题，这里也简单说一下：

* ABA问题。CAS需要在操作值的时候检查内存值是否发生变化，没有发生变化才会更新内存值。但是如果内存值原来是A，后来变成了B，然后又变成了A，那么CAS进行检查时会发现值没有发生变化，但是实际上是有变化的。ABA问题的解决思路就是在变量前面添加版本号，每次变量更新的时候都把版本号加一，这样变化过程就从“A－B－A”变成了“1A－2B－3A”。
    * JDK从1.5开始提供了AtomicStampedReference类来解决ABA问题，具体操作封装在compareAndSet()中。compareAndSet()首先检查当前引用和当前标志与预期引用和预期标志是否相等，如果都相等，则以原子方式将引用值和标志的值设置为给定的更新值。
* 循环时间长开销大。CAS操作如果长时间不成功，会导致其一直自旋，给CPU带来非常大的开销。
* 只能保证一个共享变量的原子操作。对一个共享变量执行操作时，CAS能够保证原子操作，但是对多个共享变量操作时，CAS是无法保证操作的原子性的。
    * Java从1.5开始JDK提供了AtomicReference类来保证引用对象之间的原子性，可以把多个变量放在一个对象里来进行CAS操作。

但是这又带来了新的问题，如通过锁能够很快就被释放，那么自选效率确实很好（CPU空转时间少），但是如果锁一直被占用，
那么长时间的自旋毫无意义，并且白白占用了CPU资源，造成资源的浪费。

> 自旋次数必须有限度，如果超过自旋字数还没获得锁，就要被阻塞挂起，使用JDK1.6以上默认开启：`-XX:+UseSpinning`，自旋次数可通过`-XX:PreBlockSpin`调整，默认10次

当出现自旋次数超过时，就说明到了需要阻塞的情况了，但是从自旋锁原理上，我们能看到会有一个自旋次数/时间的限制，但是自旋锁只能指定固定的自旋次数，而线程的争抢肯定会跟随
不同的任务情况有所不同，于是就能够想到，是否能够根据锁每次的自旋情况来自适应呢？

于是引入了自适应自旋锁：

### 自适应自旋锁

痛点：由于自旋锁只能指定固定的自旋次数，但由于任务的差异，导致每次的最佳自旋次数有差异

自适应意味着自旋的时间（次数）不再固定，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。
如果在同一个锁对象上，自旋等待刚刚成功获得过锁，并且持有锁的线程正在运行中，
那么虚拟机就会认为这次自旋也是很有可能再次成功，进而它将允许自旋等待持续相对更长的时间。
如果对于某个锁，自旋很少成功获得过，那在以后尝试获取这个锁时将可能省略掉自旋过程，
直接阻塞线程，避免浪费处理器资源。

> 1： 有了自适应自旋锁，随着程序运行和性能监控信息的不断完善，JVM对锁的状况预测会越来越准确，JVM会变得越来越智能

## 偏向锁

第二个问题中，当只有一个线程访问的时候，jvm使用了偏向锁：
偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁，降低获取锁的代价。

在大多数情况下，锁总是由同一线程多次获得，不存在多线程竞争，所以出现了偏向锁。其目标就是在只有一个线程执行同步代码块时能够提高性能。

那么如何实现呢？

当一个线程访问同步块并获取到锁时，会在对象头和栈帧中的锁记录里存储偏向锁的线程ID，
以后该线程在进入和退出同步块时不需要花费CAS操作来加锁和解锁，
而是先简单检查对象头的MarkWord中是否存储了线程：

* 如果已存储，说明线程已经获取到锁，继续执行任务即可
* 如果未存储，则需要再判断当前锁否是偏向锁(即对象头中偏向锁的标识是否设置为1，锁标识位为01)
* 如果没有设置，则使用CAS竞争锁（说明此时并不是偏向锁，一定是等级高于它的锁）
* 如果设置了，则尝试使用CAS将对象头的偏向锁指向当前线程，也就是结构中的线程ID

从这里看到偏向锁的算法无法使用自旋锁进行优化，因为只要有其他线程在竞争锁，那么偏向锁本身就没有存在的意义了，那么就需要进入下一个阶段：

## 轻量级锁

当锁处于偏向锁状态后，这时出现了一个搅局者，被另外的线程访问了，这时偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，但是不会阻塞，从而提高了性能。

* 在代码进入同步块的时候，如果同步对象锁状态为无锁状态（锁标志位为“01”状态，是否为偏向锁为“0”），虚拟机首先将在当前线程的栈帧中建立一个名为锁记录（Lock Record）的空间，用于存储锁对象目前的Mark Word的拷贝，然后拷贝对象头中的Mark Word复制到锁记录中。
* 拷贝成功后，虚拟机将使用CAS操作尝试将对象的Mark Word更新为指向Lock Record的指针，并将Lock Record里的owner指针指向对象的Mark Word。
* 如果这个更新动作成功了，那么这个线程就拥有了该对象的锁，并且对象Mark Word的锁标志位设置为“00”，表示此对象处于轻量级锁定状态。
* 如果轻量级锁的更新操作失败了，虚拟机首先会检查对象的Mark Word是否指向当前线程的栈帧，如果是就说明当前线程已经拥有了这个对象的锁，那就可以直接进入同步块继续执行，否则说明多个线程竞争锁。
* 若当前只有一个等待线程，则该线程通过自旋进行等待。但是当自旋超过一定的次数，或者一个线程在持有锁，一个在自旋，又有第三个来访时，轻量级锁升级为重量级锁。

当在解锁时：

* 使用CAS操作将Displaced Mark Word替换回到对象头
* 如果解锁成功，则表示没有竞争发生
* 如果解锁失败，表示当前锁存在竞争，锁会膨胀成重量级锁，需要在释放锁的同时唤醒被阻塞的线程，之后线程间要根据重量级锁规则重新竞争重量级锁

通过上述步骤，可以观察到，轻量级锁有一个使用前提：没有多线程竞争环境。一旦越过这个前提，除了互斥的开销外，还会增加额外的自旋操作的开销，如果大量线程争抢，会消耗大量的CPU操作开销，这个时候轻量级锁甚至比重量级锁还要慢。

于是采用最终极的解决方法：重量级锁

## 重量级锁

这也就是依赖于操作系统的锁，我们知道CPU存在`内核态`和`用户态`，如果我们要申请一把锁，那么需要去内核态申请，内核中的锁数量是有限的，所以还需要返还，而与内核态打交道消耗是很大的。
这也是为什么我们会尽量采用在用户态进行优化，而不是去内核中申请锁的原因。但是当多线程争抢导致的CPU竞争消耗比锁还要大的时候，我们就不得不去内核中申请锁了。

重量级锁是通过操作系统底层的`MutexLock`实现的，它内部会为到达的线程维护一个队列（默认无序）。MutexLock最核心的理念就是 尝试获取锁.若可得到就占有.若不能,就进入睡眠等待。

## 锁粗话和锁细化

通过4问题，我们可以看到，通常情况下，为了保证多线程间的有效并发，
会要求每个线程持有锁的时间尽可能短，但是大某些情况下，一个程序对同一个锁不间断、高频地请求、同步与释放，
会消耗掉一定的系统资源，因为锁的讲求、同步与释放本身会带来性能损耗，
这样高频的锁请求就反而不利于系统性能的优化了，虽然单次同步操作的时间可能很短。

于是引入锁粗化，把很多次锁的请求合并成一个请求，从而降低短时间内大量锁请求、同步、释放带来的性能损耗。


而通过5问题，我们可以看到，有时候我们写的代码完全不需要加锁，却执行了加锁操作。

```java
    public void append(String str1,String str2){
       StringBuffer stringBuffer = new StringBuffer();
       stringBuffer.append(str1).append(str2);
    }
```
StringBuffer使用了synchronized关键字，它是线程安全的，但我们可能仅在线程内部把StringBuffer当作局部变量使用，可以通过JVM在编译时通过对运行上下文的描述，去除不可能存在共享资源竞争的锁，通过这种方式消除无用锁，即删除不必要的加锁操作，从而节省开销

> 逃逸分析和锁消除分别可以使用参数-XX:+DoEscapeAnalysis和-XX:+EliminateLocks(锁消除必须在-server模式下)开启

> 在JDK内置的API中，例如StringBuffer、Vector、HashTable都会存在隐性加锁操作，可消除


# 锁升级

* 从JDK1.6开始，锁一共有四种状态：无锁状态、偏向锁状态、轻量锁状态、重量锁状态
* 锁的状态会随着竞争情况逐渐升级，锁允许升级但不允许降级
* 不允许降级的目的是提高获得锁和释放锁的效率

锁升级过程：

**无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁**

补充： 锁升级过程中的数据结构变化(Hotspot)

|锁状态|25位|31位|1位|4bit|1bit 偏向锁位|2bit锁标志位|
|-----|----|----|---|---|------------|-----------|
|无锁态 |空|hashCode(如果有调用)|空|分代年龄|0|0 1|


|锁状态|54位|2位|1位|4bit|1bit 偏向锁位|2bit锁标志位|
|--|--|--|---|--|--|--|
|偏向锁|当前线程指针JavaThread*|Epoch|空|1|0|1|

|锁状态|62位|2bit锁标志位|
|--|--|--|
|轻量级锁|指向线程栈中额Lock Record的指针|0 0|
|重量级锁|指向互斥量（重量级锁）的指针|1 0|
|GC标记信息|CMS过程中用到的标记信息|1 1|

