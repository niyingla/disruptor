package com.pikaqiu.disruptor.heigh.chain;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

public class TradePushlisher implements Runnable {

	private Disruptor<Trade> disruptor;
	private CountDownLatch latch;
	
	private static int PUBLISH_COUNT = 1;
	
	public TradePushlisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
		this.disruptor = disruptor;
		this.latch = latch;
	}

	@Override
	public void run() {
		/**
		 * 假设一次循环 是一个订单
		 */
		TradeEventTranslator eventTranslator = new TradeEventTranslator();
		for(int i =0; i < PUBLISH_COUNT; i ++){
			//新的提交任务的方式（ringBuffer提交）
			disruptor.publishEvent(eventTranslator);			
		}
		latch.countDown();
	}
}


/**
 * disruptor直接提交任务  需要实现的方法
 */
class TradeEventTranslator implements EventTranslator<Trade> {

	private Random random = new Random();

	/**
	 * 生产任务时调取的方法
	 * @param event
	 * @param sequence
	 */
	@Override
	public void translateTo(Trade event, long sequence) {
		//向空的对象中set内容 （同之前）
		this.generateTrade(event);
	}

	private void generateTrade(Trade event) {
		event.setPrice(random.nextDouble() * 9999);
	}
	
}











