package com.liaoxuefeng.chapter1;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;

public class ObjectDemo {
  public static void main(String[] args) {
    /** 1.2.1 如何理解Java对象的创建过程 */

    /**
     * new 类名()->检查类的加载->分配内存空间->初始化零值->设置对象头->执行init方法
     * https://www.cnblogs.com/chenyangyao/p/5296807.html
     */

    /**
     * 检查类的加载：先去运行时常量池查找该对象所指向的类有没有被JVM加载。如果没有加载，则需要使用类加载器把类加载进内存（类加载过程包括加载、验证、准备、解析和初始化阶段。）， 再调用new TargetClass（）实例化类。
     * 分配内存空间：然后为对象分配好内存，内存分配方式有两种：指针碰撞和空闲列表（根据Java堆内存是否规则而决定）
     * 初始化零值：为对象的普通成员变量初始化零值，值变量赋值为0，引用变量则赋值为null
     * 设置对象头：JVM还需要对对象头做一些设置，比如对象所属的类元信息，GC年龄，hashCode，锁信息等
     * 执行init方法：至上一步，JVM方面的初始化已经完成，这一步是Java语言的初始化。其中会执行构造代码块，成员变量初始值设置，构造方法。至此对象的创建才算完成。
     * 注：构造代码块和构造方法的执行顺序取决于其在Java代码中出现的顺序。每次调用构造方法都会出发构造代码块
     */

    /**
     * Java中，引用类型的实现方式有两种：直接指针访问和句柄访问 直接地址访问：栈中的引用变量存储的就是对象实例的地址值，而对象实例的对象头中存放类类信息，可以指向方法区的类型数据的地址
     * 句柄访问：在Java堆中会有一个句柄池，里面存放了实例对象的地址。栈中的引用变量指向句柄池中的句柄地址。而句柄地址又分别指向对象实例和对象类型数据
     * 具体的引用类型的实现跟GC的实现有关。句柄池的好处是，频繁GC后即使对象实例的地址改变了，也不需要在栈空间修改多个引用变量的地址，而是在句柄池中更新。
     */

    /** 1.2.2 什么是深克隆和浅克隆 */

    /**
     * 首先看定义 浅克隆在复制一个对象时，会复制成员变量中基本数据类型的值和引用数据类型存储的地址值，但不会复制引用数据类型所指向的对象本身。 而深克隆会复制引用数据类型所指向的对象
     * 假设有一学生类，其中有两个引用类型String和Teacher。那么学生A会与其浅克隆B的name和teacher共享堆中的同一个引用对象
     * 而如果B是深克隆，那么复制过程中，会继续对A的name和teacher属性进行深克隆
     */

    /**
     * class Student{
     *  int studentId;
     *  String name;
     *  Teacher teacher;
     * }
     * class Teacher{
     *  int teacherId;
     *  String name;
     *  }
     */

    /**
     * 浅克隆例子：Arrays.copyOf 深克隆的实现: 1. 重写clone()方法，每个对象都要实现Cloneable接口并实现Object的clone()方法 2.
     * 序列化，必须实现Serializable接口 3. Apache commons工具包SerializationUtils.clone(T object) 4. JSON工具 5.
     * 通过构造方法实现深克隆，即手动new对象
     */

    /**
     * 1.2.3 强引用、软引用、弱引用、虚引用有什么区别
     */

    /**
     *  强引用：垃圾回收器永远无法回收这一类对象
     *  软引用：在内存溢出前会对其进行回收，如内存中的缓存。
     *  弱引用：非必须存活的对象，不管内存是否够用，GC下次一定回收
     *  虚引用：等于没有引用，在Java中使用较少，必须配合引用队列使用。主要用来接受对象被回收时的通知
     */
    // 强引用
    Object obj = new Object();
    // 软引用
    SoftReference<Object> softRef = new SoftReference<>(new Object());
    // 弱引用
    WeakReference<Object> weakRef = new WeakReference<>(new Object());
    // 虚引用
    ReferenceQueue<Object> queue = new ReferenceQueue<>();
    PhantomReference<Object> phantomRef = new PhantomReference<>(new Object(), queue);

    /** 1.2.4 一个空的Object对象占用多大内存 */

    /**
     * 压缩指针是什么？ 压缩指针是一种优化技术，用于减少对象引用所占的空间。一般情况下，一个对象引用占64位即8字节。
     * 在开启压缩指针的情况下，JVM会使用更少的位数存储对象引用，不需要使用全部64位地址空间 开启压缩指针： -XX:+UseCompressedOops 启用压缩指针
     * -XX:+UseCompressedClassPointers 启用压缩类指针（仅在JVM支持时有效）
     */

    /**
     * 首先要了解一个对象的存储结构 对象=对象头+实例数据+对齐填充 空对象的实例数据为0，而对齐填充是为了让对象大小满足8的倍数，避免伪共享问题 故大小就等于对象头（8的倍数）
     * 其中对象头=markword+类元指针+数组信息 markword存放类对象运行时的相关数据：hashCode，GC年龄等，在64位操作系统占8字节，32位的占4字节
     * 类元指针是指向对象所属的类信息，在开启压缩指针的时候占4字节，未开启占8字节 数组信息只有对象数组才有，占4字节 故开启压缩指针下：8 + 4 = 12字节，对齐填充后会占16字节
     * 未开启压缩指针下：8 + 8 = 16字节，不需要对齐填充 综上所述，空对象会占16字节
     */

    /**
     * 1.2.5 为什么重写equals()方法就一定要重写hashCode()方法
     */

    /**
     * 在Java中hashCode用于计算对象的哈希码，而equals对象用于比较对象逻辑上是否相等。
     * 如果重写equals方法而不重写hashCode方法，就会导致两个逻辑上相等的对象hashCode不同的情况
     * 而在哈希表中，键的位置是由哈希码确定的，如果哈希码不同，他们就会被认为是不同的，这就导致可以加入两个逻辑相等的对象在哈希表中不同位置，那就会出现悖论
     * 另一种情况是，如果他们的哈希码相同，则会调用equals比较是否相同，导致实际调用equals次数增多，效率太低
     * 注：1.如果两个对象逻辑上相等，那么他们的哈希码必须相等
     *    2.如果两个对象哈希码相等，他们逻辑上不一定相等（哈希碰撞）
     */
  }
}
