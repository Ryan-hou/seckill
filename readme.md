###秒杀系统小demo

###主要实现:
* 商品列表页和详情页展示
* 秒杀接口暴露,执行秒杀操作
* 针对秒杀瓶颈的分析和一些优化点

###主要技术和一些框架
* 前端: Bootstrap 和 jQuery 插件的使用
* 后台框架: Spring/SpringMVC/MyBatis 的整合
* Mysql 数据库及存储过程的使用, Redis 缓存的使用
* 单元测试及 swagger 的使用
* 其他: 序列化插件 protostuff 的使用, lombok, Guava 等

###详情页的交互逻辑
* 首先获取系统的标准时间
* 系统的标准时间与商品秒杀的开启时间和结束时间做比较,存在三种情况:
1) 系统时间大于秒杀结束时间,显示秒杀结束即可
2) 系统时间小于秒杀开始时间,进入倒计时页面; 当倒计时结束,获取秒杀接口地址,展示秒杀按钮
3) 系统时间在秒杀开启和结束时间之间,获取秒杀地址,展示秒杀按钮
4) 执行秒杀

###优化的点:
* 获取系统时间不用优化: 本质上是new一个日期对象,Java访问一次内存大约10ns(不考虑GC)
* 秒杀地址接口: 为了防止用户提前知道秒杀地址,秒杀地址由后台通过加盐的md5组成,
这里可以使用Redis缓存,秒杀商品一般不会随意改动,通过超时来维护数据一致性
* 在使用Redis缓存时,序列化采用 protostuff 而不是jdk自带的序列化机制,时间空间效率更高
* 在秒杀详情页,用户可能会存在大量刷新操作,这里前端可以采用动静态分离的方式优化,把详情页作静态化处理,和css,js等静态
资源一起部署到CDN;秒杀按钮作防重处理
* 执行秒杀操作:存在热点商品的竞争,由于库存问题,也不适合使用缓存,优化方向是减少行级锁的持有时间

###秒杀操作的优化:
* 比较常见的思路是:通过原子计数器(Redis或NoSql实现),来纪录行为消息(分布式MQ),然后消费信息并落地(MySQL),但是
这个方案对运维和稳定性要求较高,开发成本较高(数据一致性维护,回滚方案等),幂等性难保证(重复秒杀问题)
* 一条update压力测试约为4wQPS,一般情况下也够用,我们通过Java代码来控制事务,这里存在时延(与数据库服务器的通信时延,GC的影响
,同城机房的时延一般在0.5ms~2ms之间,那么最大QPS就是2000;异地机房还要计算光在玻璃中的传播时延;update库存后,如果发生
GC,大约要耗时50ms,那么QPS最大为20),延长了其他竞争事务的锁等待时间.所以我们要把客户端逻辑放到MySQL服务器端,避免网络
和GC的影响,有两种方案:定制sql(需要修改MySQL源码)或者采用存储过程

