package learning.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class StreamTest {
  /**
   * Stream它主要是用于对集合数据进行处理的工具。Stream流可以用于对集合进行过滤、排序、映射、归约等操作。 Stream 自己不会存储元素。
   * Stream不会改变源对象。相反，他们会返回一个持有结果的新Stream。
   * Stream操作是延迟执行的。这意味着他们会等到需要结果的时候才执行。即一旦执行终止操作，就执行中间操作链，并产生结果。
   * Stream一旦执行了终止操作，就不能再调用其它中间操作或终止操作了。
   */

  List<String> list = Arrays.asList("apple", "banana", "orange");
  List<Employee> employeeData = new ArrayList<>() {
    {
      add(new Employee(1009, "马斯克", 40, 12500.32));
      add(new Employee(1010, "艾克斯", 50, 2500.32));
      add(new Employee(1011, "玛丽", 20, 5000.32));
    }
  };


  @Test
  public void test1() {
    // forEach()方法接受一个Lambda表达式或者方法引用作为参数，Lambda表达式中定义了对每个元素要执行的操作。
    System.out.println("方法引用");
    list.forEach(System.out::println);
    System.out.println("Lambda表达式");
    list.forEach(s -> System.out.println(s));
    System.out.println();
  }

  /**
   * 创建 Stream方式一：通过集合
   */
  @Test
  public void streamAPITest1() {
    // default Stream<E> stream() : 返回一个顺序流
    Stream<String> list1 = list.stream();

    // default Stream<E> parallelStream() : 返回一个并行流
    Stream<String> list2 = list.parallelStream();
    System.out.println(list1);
    System.out.println(list2);
    System.out.println();
  }

  /**
   * 创建 Stream方式二：通过数组
   */
  @Test
  public void streamAPITest2() {
    int[] arr = new int[] {1, 2, 3, 4, 5};
    IntStream stream = Arrays.stream(arr);

    Integer[] arr1 = new Integer[] {1, 2, 3, 4, 5};
    Stream<Integer> stream2 = Arrays.stream(arr1);
  }

  /**
   * 创建 Stream方式三：通过Stream的of()
   */
  @Test
  public void streamAPITest3() {
    Stream<String> stream = Stream.of("aa", "bb", "cc", "dd");
  }

  // 1-筛选与切片
  @Test
  public void test2() {
    // filter(Predicate p)——接收 Lambda，从流中排除某些元素。
    // 练习：查询员工表中薪资大于5000的员工信息
    Stream<Employee> stream1 = employeeData.stream();
    // forEach遍历是终止操作
    stream1.filter(emp -> emp.getSalary() > 5000).forEach(System.out::println);

    System.out.println();

    // limit(n)——截断流，使其元素不超过给定数量。
    // 这里报错，因为stream已经执行了终止操作，就不可以再调用其它的中间操作或终止操作了。要重新调用
    employeeData.stream().limit(2).forEach(System.out::println);

    System.out.println();

    // skip(n) —— 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
    employeeData.stream().skip(1).forEach(System.out::println);

    System.out.println();
    // distinct()——筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
    employeeData.add(new Employee(1009, "马斯克", 40, 12500.32));
    employeeData.add(new Employee(1009, "马斯克", 40, 12500.32));
    employeeData.add(new Employee(1009, "马斯克", 40, 12500.32));
    employeeData.add(new Employee(1009, "马斯克", 40, 12500.32));
    employeeData.stream().distinct().forEach(System.out::println);
    System.out.println();
  }

  // 2-映射
  @Test
  public void test3() {
    list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
    System.out.println();
    list.stream().map(String::toUpperCase).forEach(System.out::println);

    // 练习：获取员工姓名长度大于3的员工。
    employeeData.stream().filter(emp -> emp.getName().length() > 2).forEach(System.out::println);

    System.out.println();
    // 练习：获取员工姓名长度大于3的员工的姓名。
    employeeData.stream().filter(emp -> emp.getName().length() > 2)
        .forEach(emp -> System.out.println(emp.getName()));

    System.out.println();
    employeeData.stream().map(emp -> emp.getName()).filter(name -> name.length() > 2)
        .forEach(System.out::println);

    System.out.println();
    employeeData.stream().map(Employee::getName).filter(name -> name.length() > 2)
        .forEach(System.out::println);

    System.out.println();
  }

  // 3-排序
  @Test
  public void test4() {
    // sorted()——自然排序
    Integer[] arr = new Integer[] {345, 3, 64, 3, 46, 7, 3, 34, 65, 68};
    String[] arr1 = new String[] {"GG", "DD", "MM", "SS", "JJ"};

    Arrays.stream(arr).sorted().forEach(n -> System.out.print(n + " "));
    System.out.println();
    System.out.println(Arrays.toString(arr));

    System.out.println();

    Arrays.stream(arr1).sorted().forEach(n -> System.out.print(n + " "));
    System.out.println();
    // 因为Employee没有实现Comparable接口，所以报错！使用Collections.sort()或者Arrays.sort()方法对一个对象集合进行排序时，需要使用对象的自然排序规则进行比较。而默认情况下，Java并不知道如何比较一个自定义对象的大小，因此需要我们自己定义比较规则。
    employeeData.stream().sorted().forEach(System.out::println);
    System.out.println();

    employeeData.stream().sorted((e1, e2) -> e1.getId() - e2.getId()).forEach(System.out::println);
    employeeData.stream().sorted().forEach(System.out::println);

    System.out.println();
    Arrays.stream(arr1).sorted(String::compareTo).forEach(System.out::print);
    System.out.println();
    Arrays.stream(arr1).sorted((s1, s2) -> s2.compareTo(s1)).forEach(System.out::print);
    System.out.println();
  }


}
