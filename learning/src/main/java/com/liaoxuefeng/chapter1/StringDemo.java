package com.liaoxuefeng.chapter1;

public class StringDemo {
  public static void main(String[] args) {    
    /**
     * 1.1.3 new String("hello"）之后，到底创建了几个对象
     */
    
    /**
     * 字符串常量池
     * 在JDK6.0及之前版本，字符串常量池存放在方法区中在JDK7.0版本以后，字符串常量池被移到了堆中了。至于为什么移到堆内，大概是由于方法区的内存空间太小了。
     */
    String s = new String("hello");
    /**
     * 1 ）如果hello 这个字符串常量不存在，则创建两个对象，分别是hello这个字符串常量，以及new String 这个实例对象。 
     * 2 ）如果hello 这个字符串常量存在，则只会创建 new String(）这一个实例对象。
     */
    String abc = "abc";
    /**
     * 先在栈中创建一个String对象的引用，再去字符串常量池里找"abc"，等价于"abc".intern()
     * 1 ）如果abc这个字符串常量存在，直接把常量池的abc地址赋值给变量s2
     * 2 ）如果abc不在常量池，则先将"abc"创建并存入常量池，再让变量s2指向常量池的"abc"
     */
    
    /**
     * 每个Java类被编译后，就会形成一个class文件，class文件中有一项信息为常量池，用于存放编译器生成的各种字面量和符号引用
     * 在编译期就能确定的字符串常量会被编译进常量池
     * 其中字面量包括  1. 文本字符串 2. 八种基本类型的值 3. 被声明为final的常量
     */
    String s1 = "hello";
    String s2 = "hello";
    String s3 = "he" + "llo";// 在编译期间就已经对它进行了优化
    String s4 = "hel" + new String("lo");
    String s5 = new String("hello");
    String s6 = s5.intern();
    String s7 = "h";
    String s8 = "ello";
    String s9 = s7 + s8;
    // 12 13 14 19 45 16
    System.out.println(s1 == s2); // true: 因为都是在常量池的同一个常量池对象
    System.out.println(s1 == s3); // true: 因为都是在常量池的同一个常量池对象，编译器在编译过程中会将可以合并的字面量合并
    System.out.println(s1 == s4); // false：编译器无法识别new String("lo")，而字符串的拼接发生在运行时，底层使用StringBuilder，
    //最后返回的是一个新的堆对象。因为String Builder的toString方法使用了new String、
    System.out.println(s1 == s9); // false：编译期无法识别s7和s8的值，拼接发生在运行时，返回的新字符串堆对象
    System.out.println(s4 == s5); // false：s5使用了关键字new就是堆的新对象
    System.out.println(s1 == s6); // true： 考察intern原理。intern会先去常量池找该字符串（使用equals方法判断），如果没有相同的字符串，则把该字符串先添加进常量池，再让s5指向该引用
    
    /**
     * 1.1.4 String 、 StringBuffer、 StringBuilder的区别是什么
     * 
     * 
     * 更正： “String 存储在字符串常量池中，而StringBuffer和StringBuilder存储在堆内存中。”不正确
     * 使用String构造器的话：会现在常量池中查找，不存在的话再往常量池加入字面量。接着在堆中创建String对象，再把该对象指向常量池中的字符串
     * 不论如何，使用new String都会在堆中创建一个String对象，所以尽量避免使用该方法
     */
    
    /**
     * 问题从以下四个方面回答：
     * 1.）值可变性方面：String对象一旦被创建就不可修改。因为String类被final修饰，不可被继承。同时String内部使用char[]存储字符串，并使用的final关键字修饰。导致其一旦被赋值就不可改变。String没有暴露出可以修改char[]的方法
     *    而StringBuffer和StringBuilder的出现就是为了应对频繁修改某一字符串的场景。外部可以修改它们。
     * 2.）线程安全方面：String对象是不可修改的所以没有线程安全问题，而StringBuilder是线程不安全的，StringBuffer是线程安全的因为其内部的方法都被synchronized修饰
     * 3.）性能方面：StringBuilder > StringBuffer > String （因为上锁会影响性能）需要频繁地获取和释放锁。而String每次修改会返回一个新的对象
     * 4.）数据存储方面：StringBuffer和StringBuilder存储在堆内存中。而一般来讲String存储在字符串常量池中
     */
     
    /**
     * operation for String: 1401ms
     * operation for StringBuilder: 2ms
     * operation for StringBuffer: 3ms
     */
    String str1 = "";
    long start1 = System.currentTimeMillis();
    for(int i = 0; i < 100000; i++) {
      str1 += "a";
    }
    long end1 = System.currentTimeMillis();
    System.out.println("operation for String: " + (end1-start1) + "ms");
    
    StringBuilder sbuilder = new StringBuilder();
    long start2 = System.currentTimeMillis();
    for(int i = 0; i < 100000; i++) {
      sbuilder.append("a");
    }
    long end2 = System.currentTimeMillis();
    System.out.println("operation for StringBuilder: " + (end2-start2) + "ms");
    
    StringBuffer sbuffer = new StringBuffer();
    long start3 = System.currentTimeMillis();
    for(int i = 0; i < 100000; i++) {
      sbuffer.append("a");
    }
    long end3 = System.currentTimeMillis();
    System.out.println("operation for StringBuffer: " + (end3-start3) + "ms");
  }
}
