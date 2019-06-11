package com.pikaqiu.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.pikaqiu.entity.TranslatorData;
import com.pikaqiu.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {

	private String producerId;
	
	private RingBuffer<TranslatorDataWapper> ringBuffer;
	
	public MessageProducer(String producerId, RingBuffer<TranslatorDataWapper> ringBuffer) {
		this.producerId = producerId;
		this.ringBuffer = ringBuffer;
	}

	/**
	 * 生成一条消息 并放入队列
	 * @param data
	 * @param ctx
	 */
	public void onData(TranslatorData data, ChannelHandlerContext ctx) {
		long sequence = ringBuffer.next();
		try {
			TranslatorDataWapper wapper = ringBuffer.get(sequence);
			wapper.setData(data);
			wapper.setCtx(ctx);
		} finally {
			ringBuffer.publish(sequence);
		}
	}

}
