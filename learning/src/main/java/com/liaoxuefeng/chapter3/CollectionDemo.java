package com.liaoxuefeng.chapter3;

import java.util.*;

/**
 * @author ZHONG Jiquan
 * @create 09/08/2023 - 03:21
 */
public class CollectionDemo {
	public static void main(String[] args) {
		/**
		 * Java中容器包括Collection和Map两种，Collection储存对象的集合，Map储存键值对的映射表
		 * Collection和Map都是Java的接口，具体实现有以下几种
		 * Collection包括了List,Queue和Set
		 * Set实现：TreeSet HashSet LinkedHashSet
		 * List实现：ArrayList Vector LinkedList
		 * Queue实现：LinkedList PriorityList
		 *
		 * Map实现：TreeMap HashMap HashTable
		 */

		/**
		 * ArrayList
		 * ArrayList是顺序容器，基于数组实现的并且能够自动扩容，他实现了List接口，
		 * 但它不是线程安全的，采用了fast-fail机制，即面对并发修改的时候迭代器会很快快速失败，而不是冒着风险做不确定的行为
		 * 由于是基于数组实现的容器，索引他可以在常数时间完成查询和尾部添加，但随机添加和删除都是线性时间
		 *
		 * 自动扩容机制：
		 * 当添加新的数据时，它会先检查新长度是否超出，如果超出则扩容为原长度的1.5倍
		 * 具体为新建一个长度为1.5倍的数组，把旧数据拷贝过去
		 * 如果知道最终大概的长度，我们也可以使用ensureCapacity手动增加容量，避免频繁的扩容操作，或者创建ArrayList的时候传入初始大小
		 *
		 * Vector和ArrayList基本一样，但Vector是线程安全的 性能比较低，不推荐被使用
		 */
		List<Integer> list = new ArrayList<>();

		/**
		 * LinkedList
		 * LinkedList是一个基于双向链表实现的顺序容器，它实现了List和Deque（double ends queue）接口，使得它既可以做队列，也可以作为栈
		 * 它也不是线程安全的，对于并发操作采用了fast-fail机制
		 * 基于双向链表的实现，使得它插入删除首位元素的操作时间复杂度是常数，但访问删除特定下标的复杂度是线性的
		 * 由于实现了Queue和Deque，使得它可以作为队列和栈
		 * 作为栈：
		 * pop() push() peek()
		 * 作为双向队列：
		 * peekFist peekLast offer poll offerFist offerLast pollFirst pollLast
		 */

		/**
		 * Queue和Stack
		 * Queue是一个定义队列的接口，Stack是定义栈的接口
		 * 由于Deque双向队列继承了Queue，所以使用Deque代替Queue和Stack比较好。
		 * 具体实现上，ArrayDeque（基于数组实现的双向队列）的性能比LinkedList好
		 *
		 * Deque的方法：
		 * 不支持null：addFirst addLast removeFirst removeLast
		 * 如果出列元素为空或者添加的元素为空 会抛出异常
		 * 支持null：offerFirst offerLast pollFirst pollLast peekFist peekLast
		 * 可以添加null值，如果队列为空则返回null值
		 */
		Deque<Integer> q1 = new LinkedList<>();
		Deque<Integer> q2 = new ArrayDeque<>();

		/**
		 * PriorityQueue是优先队列的实现，内部保持插入操作后，元素顺序满足自然大小排序或者自定义比较器的排序
		 * Priority是基于完全二叉树的最小堆实现的，每次把根节点出队，不允许放入null值
		 * 入队出队是logN时间复查度，peek常数复杂度
		 */

		/**
		 * HashMap是根据哈希表实现的KV映射表
		 * HashSet底层也是HashMap。
		 * HashMap实现了Map接口。K和V都允许放入null。基于Hash表的查询可以达到常数级的时间复杂度。
		 * 但Hash表肯定会发生哈希冲突，对此一般有两种方法解决：开放地址和冲突链表
		 * HashMap使用的是冲突链表实现哈希冲突
		 * 内部原理如下：
		 * HashMap内部定义了一个Entry<K,V>内部类，并且维护了一个Entry数组
		 * 新的键值对，先对Key计算hash再对Entry数组的长度-1取模，得到的结果就是在数组中的下标，然后把该Entry存入数组中。
		 * 如果碰到哈希冲突，即数组的位置已经被占用，那么会从该位置拉出一个链表，把新数据插到表头。查询操作会从表头遍历，使用hashCode和equal
		 * 方法遍历整个链表。但如果链表过长，时间复杂度就取决于链表的长度
		 * Java8开始，链表长度超过8后会进化成红黑树节点不再是Node而是TreeNode
		 * 故HashMap = 数组 + 链表 + 红黑树
		 * 另外，当Map内容量达到最大长度*负载因子的时候，对会其进行扩容，扩容后大小为原来的两倍，并迁移数据。因为HashMap要求table.length必须是2的倍数
		 * 默认容量为16 负载因子为0.75
		 *
		 * HashSet是对HashMap的一层包装，元素为Key，对应的值都是PRESENT，一个dummy value
		 *
		 */
		Map<Integer, String> map = new HashMap<>();
		Set<Integer> set = new HashSet<>();

		/**
		 * LinkedHashMap和LinkedHashSet
		 * HashMap是无序的容器，内部元素不保证元素的有序。
		 * 而LinkedHashMap基于HashMap上，使用一个双向链表把内部的Entry串联起来，可以按插入顺序遍历内部的元素
		 *
		 * 一种经典用法是：实现一个FIFO替换策略的缓存
		 * LinkedHashMap有一个方法removeEldestEntry。每次put操作都会验证是否满足最老元素出队条件，如果是 则会移除队首元素。
		 * 可以重写该方法，限定内部元素大小，超出限定大小就会执行移除队首操作
		 */
		Map<Integer, String> linkedMap = new LinkedHashMap<>();

		/**
		 * 如果要实现线程安全，可以对Collection和Map使用Collections.synchronizedCollection方法
		 */

		/**
		 * TreeSet和TreeMap
		 * TreeMap是基于红黑树实现的KV映射。其内部元素是有序的，可以基于自然顺序或者传入比较器。
		 * 其实现了SortedMap方法，会按照Key的大小对元素排序。
		 * 红黑树本质上是几乎平衡的二叉查找树，加入红黑颜色以及限定条件，保证最大树高小于最小树高的两倍
		 * 在插入新元素后，通过调整颜色以及左右旋转操作，保证插入后元素的有序性
		 * 添加删除查找的时间复杂度都是logN
		 * TreeSet是对TreeMap的简单包装，所有方法都会转化成TreeMap中的方法
		 */

		/**
		 * WeakHashMap是一种使用弱引用实现Entry的HashMap。
		 * 它内部类Entry继承类WeakReference并实现了Entry接口。当外部没有强引用指向Key时，Key一旦被GC发现就会被回收
		 * 但要注意容易发生内存泄漏问题，即Key被回收了，但value没有被回收，且没有键可以达到该value
		 * 在Java中的经典应用就是Thread.threadLocalMap也是基于WeakHashMap。
		 */
	}
	class FIFOCache<K, V> extends LinkedHashMap<K, V>{
		private final int cacheSize;
		public FIFOCache(int cacheSize){
			this.cacheSize = cacheSize;
		}

		// 当Entry个数超过cacheSize时，删除最老的Entry
		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			return size() > cacheSize;
		}
	}
}
