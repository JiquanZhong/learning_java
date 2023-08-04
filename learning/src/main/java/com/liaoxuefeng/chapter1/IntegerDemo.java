package com.liaoxuefeng.chapter1;

public class IntegerDemo {
  public static void main(String[] args) {
    /**
     * 1.1.1 为什么要设计封装类， Integer 和 int 有什么区别
     * 1.1.2 Integer中为什么“1000==1000”为false , "100==100”为true
     */

    /**
     * 1. Integer初始值为null,int的初始值为0 
     * 2. Integer存储在堆内存，int直接存储在栈空间
     * 3. Integer是对象类型，它封装和很多方法和属性
     */
    Integer a = 100, b = 100, c = 1000, d = 1000;
    System.out.println((a == b) + "," + (c == d));

    /**
     * 上面代码在编译时会调用Integer.valueOf的自动装箱操作
     * 等价于： Integer a = Integer.valueOf(100)
     *       Integer c = Integer.valueOf(1000)
     * 而valueOf中使用了缓存设计（享元模式）
     * 默认情况下，从-128-127的Integer都会从缓存池中获取
     * 所以a和b是从缓存池中拿到的同一对象，而c和d是新创建的对象
     * 不仅是Integer，还有其他的包装类都用了缓存
     * 只不过缓存的范围不一样
     */
  }
}
