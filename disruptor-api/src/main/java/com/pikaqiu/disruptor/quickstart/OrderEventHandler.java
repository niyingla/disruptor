package com.pikaqiu.disruptor.quickstart;

import com.lmax.disruptor.EventHandler;

public class OrderEventHandler implements EventHandler<OrderEvent>{

	/**
	 * 消费监听方法
	 * @param event
	 * @param sequence
	 * @param endOfBatch
	 * @throws Exception
	 */

	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
//		Thread.sleep(Integer.MAX_VALUE);
		System.err.println("消费者: " + event.getValue());
	}

}
