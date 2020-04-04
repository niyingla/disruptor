package com.pikaqiu.disruptor.heigh.multi;

import com.lmax.disruptor.RingBuffer;

public class Producer {

	private RingBuffer<Order> ringBuffer;

	public Producer(RingBuffer<Order> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void sendData(String uuid) {
		//获取当前槽
		long sequence = ringBuffer.next();
		try {
			//获取当前槽的空数据
			Order order = ringBuffer.get(sequence);
			//设置当前槽的数据
			order.setId(uuid);
		} finally {
			//发布
			ringBuffer.publish(sequence);
		}
	}

}
