package com.pikaqiu;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.pikaqiu.client.MessageConsumerImpl4Client;
import com.pikaqiu.client.NettyClient;
import com.pikaqiu.disruptor.MessageConsumer;
import com.pikaqiu.disruptor.RingBufferWorkerPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyClientApplication.class, args);
		
		MessageConsumer[] conusmers = new MessageConsumer[4];
		for(int i =0; i < conusmers.length; i++) {
			MessageConsumer messageConsumer = new MessageConsumerImpl4Client("code:clientId:" + i);
			conusmers[i] = messageConsumer;
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
				1024*1024,
				//new YieldingWaitStrategy(),
				new BlockingWaitStrategy(),
				conusmers);
		
		//建立连接 并发送消息
		new NettyClient().sendData();
	}
}
