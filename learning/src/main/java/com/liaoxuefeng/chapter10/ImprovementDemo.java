package com.liaoxuefeng.chapter10;

/**
 * @author ZHONG Jiquan
 * @create 13/08/2023 - 01:37
 */
public class ImprovementDemo {
	/**
	 * 数据库优化可以从以下几个方面入手
	 *
	 * 查询优化：使用索引避免索引失效，自增id避免页分裂，优化查询实现覆盖索引，切分大查询以利用缓存数据，使用limit减少请求数据量
	 *
	 * 表方面：水平切分把大量数据分到不同的服务器中，垂直切分把大查询切成小查询
	 *
	 * 服务器方面：使用集群部署，主从复制实现读写分离和负载均衡
	 */
}
