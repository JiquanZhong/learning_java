package com.liaoxuefeng.chapter10;

/**
 * @author ZHONG Jiquan
 * @create 12/08/2023 - 23:46
 */
public class LogDemo {
	public static void main(String[] args) {

	}

	/**
	 * MySQL中的日志一共有多种：
	 * undoLog记录更新前的数据值
	 * redoLog记录更新后的数据值，是InnoDB独有的
	 * binLog记录修改操作
	 * errorLog记录数据库启动关闭奔溃的事件
	 * slowQueryLog记录执行时间草果预设阈值的查询语句
	 */

	/**
	 * redoLog是InnoDB中独有的日志。用于实现事务的持久性和故障恢复的关键组成部分，记录已提交事务的修改后的值，以确保数据库发生事故的时候能够恢复到
	 * 一致的状态。赋予MySQL safe-crash的能力
	 * 工作流程：
	 * 执行事务的DML时，MySQL先将操作修改后的记录保存在redo log buffer中
	 * redo Log并不是马上写入到磁盘，而是一定条件下写入磁盘。比如后台有个线程会把redoLog每秒写入，或者达到缓冲区一半大小时写入。尽可能减少
	 * IO操作，提高性能
	 *
	 * 硬盘上，redoLog日志文件不止一个，而是以日志组形式存在。其中存在两个指针指向两个位置，写入指针写入redo log buffer的，checkpoint是
	 * 一个时间点的快照，checkpoint和write_position之间的为日志恢复的数据， write_position到checkpoint为redolog还能写的空间。
	 * 当write_position快追上write_position的时候，会让checkpoint前移动擦除数据，这样write_position可以继续覆盖写。
	 */

	/**
	 * 一条SQL的执行过程：
	 * 1. 先查询Buffer Pool中是否有对应的数据页缓存，如果没有则在硬盘中加载到buffer pool
	 * 2. 修改前把数据记录到undo log中，随后引擎修改数据值
	 * 3. 再把修改后的值记录到redo log buffer中。（此时redo log进入prepare阶段）
	 * 4. 一旦事务提交，会执行以下操作：
	 * 		1. 把redo log buffer冲刷进磁盘
	 * 		2. 把事务期间执行的操作记录到bin log并写入磁盘
	 * 		3. 把刚刚对bin log的更新记录在redo log中，并追加commit（redo log由prepare进入commit）
	 */

	/**
	 * 两段式提交确保了binlog和redolog的数据一致性
	 * 如果binlog写完，redolog没有commit的情况下宕机，则redolog末尾会缺失commit标识，表示事务失败了
	 * 再次启动服务器的时候，会把redolog中记录的数据覆盖到buffer pool中，最终确保了binlog和redolog的数据一致
	 */

}
