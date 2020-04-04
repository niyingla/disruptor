package com.pikaqiu.disruptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *   * 单个服务器上RingBuffer数量不建议超过cpu数
 *
 *	 RingBuffer环形队列  内容手动set 对象由队列创建 生产者-》投递-》消费者
 *
 *  多线程之间传递数据的环形buffer 拥有一个序号 指向下一个可用元素
 *
 *  没元素  消费者等待
 *
 *  多元素 生产者等待
 *
 *  槽（数组长度）的数量最好是 2n次方 方便取模
 *  比如一共十个 第一轮是10 当到12时取模就是第二个槽
 *
 *
 *  Disruptor 类似于一个辅助类  拥有环形队列运行过程中需要的 线程池 数组大小 等参数
 *
 *  sequence  有两个实体对象 sequencer Disruptor的核心 分别为singleProducer（单线程） multiProducer（多线程）
 *
 *  sequence barrier 平衡消费和生产 还定义了是否有可处理的事件和逻辑
 *
 *  sequence 循序递增编号 管理交换数据 沿着序号逐个处理 Sequence 堪称 AtomicLong 进行进度标识
 *
 *  防止不同sequence 伪共享
 *
 *  waitStrategy生产者将event 置入Disruptor Blocking* Sleeping* yield*
 *  										阻塞		休眠     切换
 *  Blocking	效率和消耗低	（内部有锁）
 *  Sleeping	生产者影响小 适用于异步日志
 *	yield   性能最好
 *
 *	EventProcessor 主要事件循环 处理Event 拥有消费者的Sequence
 * 	实现类有BatchEventProcessor
 *
 * 	workProcessor 确保每个Sequence只能被一个processor消费
 *
 * 	消费逻辑编写于EventHandler
 */
@SpringBootApplication
public class DisruptorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisruptorApplication.class, args);
	}
}
