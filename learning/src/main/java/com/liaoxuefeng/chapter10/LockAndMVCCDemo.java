package com.liaoxuefeng.chapter10;

/**
 * @author ZHONG Jiquan
 * @create 13/08/2023 - 00:17
 */
public class LockAndMVCCDemo {
	/**
	 * InnoDB和MyISM的对比
	 * 1. InnoDB支持事务而MyISM不支持
	 * 2. InnoDB支持行级锁，提供记录锁，间隙锁和临键锁，MyISM只支持表级锁
	 * 3. InnoDB提供外键和主键约束，MyISM不支持外键和主键约束
	 * 4. InnoDB支持在线热备份，可以在线的时候全局备份和恢复数据，而MyISM只能停机的时候进行
	 * 5. InnoDB提供redo log支持崩溃恢复，MyISM不支持，需要手动恢复
	 * 6. InnoDB占用较大磁盘，但处理并发能力强，MyISM占用磁盘小，但并发操作慢
	 */

	/**
	 * MySQL中锁的分类
	 *
	 * 按是否加锁：分为乐观锁和悲观锁
	 * 乐观锁对数据操作时不会加锁，而是使用版本号控制，更新时比较版本号，如果一致则事务成功
	 * 悲观锁使用X锁和S锁实现，对操作记录加共享锁或者排他锁
	 *
	 * 按互斥性：S锁和X锁
	 * S锁之间不互斥，SX锁互斥，XX锁也互斥，即
	 * 在S锁情况下可以对记录再加S锁，但不能加X锁
	 * 在X锁情况下不能对记录加S锁和X锁
	 * InnoDB中默认会对DML操作加X锁
	 *
	 * 按粒度分
	 * 全局锁，整个库被锁，用于做全局备份
	 * 页级锁，给一整个数据页上锁
	 * 表级锁，MyISM和InnoDB都支持，对整张表上锁
	 * 行级锁，只有InnoDB支持，对操作的部分记录上锁，又分为记录锁，间隙锁和临键锁
	 *
	 * 行级锁：
	 * 记录锁，只锁住当前记录
	 * 间隙锁，锁住两条记录间的开区间
	 * 临键锁，即记录锁加间隙锁，左开右闭的区间
	 *
	 * 临键锁用于解决幻读问题，但如果把隔离等级降为RC则临键锁会失效
	 */

	/**
	 * 临键锁只能对二级索引使用，不能对唯一索引使用
	 * 因为临键锁是用来解决幻读问题的，即在查询一个范围内的数据时，其他事务不能向这个区间插入新数据，锁定的是左开右闭的区间
	 * 由于唯一索引插入操作需满足唯一性约束，所以对唯一索引使用临键锁没有意义，属于是画蛇添足
	 */

	/**
	 * 共享锁例子：
	 * select * from products where id = 1 lock in share mode
	 *
	 * 排他锁例子：
	 * select * from products where id = 1 for update
	 *
	 * 间隙锁例子：
	 * select * from products where id > 10 and id < 20 for update
	 *
	 * 意向锁
	 * select * from products where id > 10 and id < 20 in share mode
	 */

	/**
	 * 当前读和快照读
	 * 当前读指的是，读取的数据是最新版本。在RU会读取其他事务没提交的数据，RC读取已提交的数据，RR和Serializable下会在事务开启时创建
	 * 一个一致性视图
	 * 快照读指的是，在事务读取数据时，读取事务开始时创建的数据快照
	 */

	/**
	 * MVCC
	 * 即多版本并发控制，用于控制事务读取数据的版本，实现不加锁的情况下对数据进行安全访问，如果对事务上锁，会导致性能太慢。
	 * 使用MVCC+锁可以在RR隔离等级下解决部分幻读问题
	 * InnoDB使用的MVCC实现的事务隔离
	 *
	 * MVCC实现原理是基于undo log + read view + 隐藏字段
	 * 表除了定义的显式字段，还包括一系列隐藏的字段如自增id，回滚指针，上一次修改的事务id和删除标志
	 * read view即读视图，在事务开启读取数据的时候创建的视图。其中还包含了一些读视图属性如当前活跃事务id（不包括自己），活跃事务最小id，
	 * 下一个新建事务会被分配的id以及视图创建事务的id
	 * MVCC通过规则选择合适的数据版本，从而对读操作不加锁也可以保证安全
	 * 具体规则如下：
	 * 1. 判断数据事务修改id是否小于活跃事务最小id，如果是则代表该版本数据在事务创建前就存在，否则进入下一个判断
	 * 2. 判断数据事务修改id是否大于等于下一个新建事务会被分配的id，是则代表该版本数据在事务创建后已经被修改，数据不可用。然后使用回滚指针
	 * 进入undo log找到上一版的数据，再循环此判断，否则进入下一个判断
	 * 3. 数据事务修改i 处于中间，又会有以下几种情况：
	 * 		1. 数据事务修改id 在活跃事务id中 说明读取了其他事务没提交的数据，不可用
	 * 		2. 数据事务修改id等于创建事务的id，自己改的，可用
	 * 		3. 数据事务修改id不在活跃事务id中，说明事务开启后，有其他事务对其进行了更改并已经提交了，数据可用
	 */
}
