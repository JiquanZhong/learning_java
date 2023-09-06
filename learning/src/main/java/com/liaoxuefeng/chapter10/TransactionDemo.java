package com.liaoxuefeng.chapter10;

/**
 * @author ZHONG Jiquan
 * @create 12/08/2023 - 14:16
 */
public class TransactionDemo {
	public static void main(String[] args) {

	}
	/**
	 * MySQL中只有InnoDB支持事务操作。
	 */

	/**
	 * 事务的ACID性质：
	 * A：原子性。事务的修改操作是原子性的，要么全部成功，要么全部失败。比如转账操作，两个账户的加钱和扣钱一致。
	 * C：一致性。事务修改的数据前后满足数据一致性约束。比如两个账号转账，金额总额需要保持一致。
	 * I：隔离性。多个事务之间的操作互不影响。比如A和B同时往B转账，那么A的转账行为不应该影响B的转账行为
	 * D：持久性。事务的修改结果一旦提交就是永久的。
	 */

	/**
	 * MySQL中对ACID的实现：
	 * A：通过undo log和redo log实现。事务失败用undolog回滚数据，成功用redolog保存数据修改后的值
	 * C：通过原子性+隔离性+持久性保证。
	 * I：通过MVCC+锁实现。MVCC保证读操作下在使用版本控制合适的数据版本，锁保证写操作下数据的独立
	 * D：使用redolog和binlog实现。保证数据一旦被修改就可以持久化到硬盘。
	 */

	/**
	 * 为什么redolog能保证持久性？redolog中存的是什么？
	 * redolog是一种物理日志，记录了数据修改后的值，事务一旦提交后就会被记录在redolog buffer中，在合适的时候写入到硬盘。
	 * redolog保存的具体记录为：表空间地址+数据页编号+记录偏移量+记录长度+修改后的数据值
	 */

	/**
	 * 多事务中，会出现以下问题：
	 * 脏读：读到其他事务未提交的数据
	 * 不可重复读：同一记录的两次读取记录内容不一致
	 * 幻读：同一个查询，读到了之前未读到的记录
	 * 严重程度：脏读 > 不可重复读 > 幻读
	 * MySQL中设置了不同的事务隔离等级解决以上的问题：
	 * RU（读未提交）：可读取其他事务没有提交的数据，会出现脏读，不可重复读和幻读问题
	 * RC（读已提交）：可读取其他事务已经提交的数据，会出现不可重复读和幻读问题
	 * RR（重复读）：两次读取的记录始终保持一致，会出现幻读问题
	 * SERIALIZABLE（串行化）：事务开始后就会加锁，其他事务必须等到该事务结束才能操作，不会有事务并发问题
	 */

	/**
	 * MySQL的InnoDB如何解决在RR下的幻读问题？
	 * InnoDB中使用了MVCC对事务进行版本控制，保证了在不加锁的情况下可以避免幻读。
	 * 但如果要对数据进行修改，则需要使用MVCC+锁的方式避免幻读。
	 * 但实际上，InnoDB没被办法完全保证避免RR下的幻读问题。
	 * 以下例子中会发生幻读问题：
	 * 1. A事务开启，对表中一段范围内的数据进行查询。
	 * 2. B事务开启，并对该范围内数据进行插入并提交
	 * 3. A不加条件地（对所有行）进行update
	 * 4. A再次查询后发现，结果带有B插入的新数据
	 */


}