package com.pikaqiu;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.pikaqiu.disruptor.MessageConsumer;
import com.pikaqiu.disruptor.RingBufferWorkerPoolFactory;
import com.pikaqiu.server.MessageConsumerImpl4Server;
import com.pikaqiu.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyServerApplication {
	/**
	 * 大体流程 client端 发起10条消息 server端接受请求消息
	 * 响应回client端 client再消费打印
	 * @param args
	 */

	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class, args);
		//创建四个消费者
		MessageConsumer[] conusmers = new MessageConsumer[4];
		for(int i =0; i < conusmers.length; i++) {
			//创建环形队列信息消费者集合 当存在可消费消息时会调用消费者的onEvent方法
			MessageConsumer messageConsumer = new MessageConsumerImpl4Server("code:serverId:" + i);
			conusmers[i] = messageConsumer;
		}
		//通过工厂类创建实例
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
				1024*1024,
				//new YieldingWaitStrategy(),
				new BlockingWaitStrategy(),
				conusmers);

		new NettyServer();
	}
}
