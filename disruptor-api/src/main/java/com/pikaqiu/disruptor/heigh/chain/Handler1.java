package com.pikaqiu.disruptor.heigh.chain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 两个接口 实现任何一个都可以
 */
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade>{

	//EventHandler
	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		this.onEvent(event);
	}

	//WorkHandler
	@Override
	public void onEvent(Trade event) throws Exception {
		System.err.println("handler 1 : SET NAME");
		Thread.sleep(1000);
		event.setName("H1");
	}

}
