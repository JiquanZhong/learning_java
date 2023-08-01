package learning.designParttern;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author ZHONG Jiquan
 * @create 01/08/2023 - 15:36
 */
public class DesignPatternTest {
  @Test
  public void test1() {
    // 静态工厂方法（Static Factory Method）
    Integer num1 = Integer.valueOf(1);
    Integer num2 = Integer.valueOf(1);
    System.out.println("num1 == num2? " + (num1 == num2));

    Integer num3 = Integer.valueOf(129);
    Integer num4 = Integer.valueOf(129);
    System.out.println("num3 == num4? " + (num3 == num4));

    // Integer既是产品又是静态工厂。它提供了静态方法valueOf()来创建Integer。
  }

  @Test
  public void test2() {
    // 适配器模式
    String[] exist = new String[] {"Good", "morning", "Bob", "and", "Alice"};
    // to Map
    Set<String> set = new HashSet<>(Arrays.asList(exist));
    System.out.println(set);
  }
}
