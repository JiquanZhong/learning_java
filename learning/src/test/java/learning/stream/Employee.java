package learning.stream;

import java.util.Objects;

public class Employee implements Comparable<Employee> {
  private int id;
  private String name;
  private int age;
  private double salary;

  public Employee() {
    super();
  }

  public Employee(int id, String name, int age, double salary) {
    super();
    this.id = id;
    this.name = name;
    this.age = age;
    this.salary = salary;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  @Override
  public String toString() {
    return "Employee [id=" + id + ", name=" + name + ", age=" + age + ", salary=" + salary + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(age, id, name, salary);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Employee other = (Employee) obj;
    return age == other.age && id == other.id && Objects.equals(name, other.name)
        && Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary);
  }

  @Override
  public int compareTo(Employee o) {
    return o.getId() - this.getId();
  }

}
