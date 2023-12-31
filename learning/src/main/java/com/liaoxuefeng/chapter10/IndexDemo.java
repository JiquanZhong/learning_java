package com.liaoxuefeng.chapter10;

/**
 * @author ZHONG Jiquan
 * @create 12/08/2023 - 17:40
 */
public class IndexDemo {
	/**
	 * MySQL中的索引分为两种，唯一索引和二级索引。
	 * 唯一索引是根据表的唯一非空主键所创建的索引，其B+树叶子结点中包含了完整的记录数据
	 * 二级索引是通过非主键而建立的索引，其B+树叶子结点中除了存储索引信息外，还存储了记录对应的主键
	 */

	/**
	 * 回表，如果二级索引中没有查询的目标字段，则会发生回表，即根据查询到的主键回到唯一索引树中查找，这里对索引树进行了两次查找。
	 * 于之对应的概念是索引覆盖，即在二级索引的叶子结点中已经包含了查询的目标数据，比如查询在满足二级索引条件下的主键值。此时由于叶子结点中
	 * 包含了主键信息，索引不会发生回表操作，而是直接返回数据。
	 */

	/**
	 * 自适应哈希索引
	 * InnoDB独有的一种缓存结构。当某个二级索引频繁被查询时，MySQL会在buffer Pool中创建一个该索引的B+索引树，存放实际记录的地址。
	 * 再次访问该数据时，就可以直接返回数据。
	 * 该缓存很小，树高一般小于3。
	 * 但缺点是会占用buffer pool的内存，只能对二级索引生效，并且只能用于等值查找。只有比较极端的情况下自适应hash才有意义。
	 */

	/**
	 * 索引的规则
	 * 1. 最左前缀匹配，必须使用前缀索引，只匹配前缀的部分值
	 * 2. 范围查询中，索引匹配到<, >, between like会停止
	 * 3. 不能对索引列进行了计算，MySQL8后对这点进行了优化
	 * 4. 不能对or, not, not exist条件进行索引
	 * 5. 没有查询条件，或者查询条件没有建立索引
	 */

	/**
	 * 索引失效的情况
	 * 1. 多表连接时（join）
	 * 2. 使用or，not， not操作时
	 * 3. 使用函数计算
	 * 4. 隐式转换如字符串不加引号
	 * 5. 不满足最左前缀匹配时
	 * 6. 大范围查询，优化器认为做全表扫描更快时
	 */
}
