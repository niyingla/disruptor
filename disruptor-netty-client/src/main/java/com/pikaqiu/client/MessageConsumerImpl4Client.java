package com.pikaqiu.client;

import com.pikaqiu.disruptor.MessageConsumer;
import com.pikaqiu.entity.TranslatorData;
import com.pikaqiu.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumerImpl4Client extends MessageConsumer {

	public MessageConsumerImpl4Client(String consumerId) {
		super(consumerId);
	}

	/**
	 * 处理发送过来的消息
	 * @param event
	 * @throws Exception
	 */
	@Override
	public void onEvent(TranslatorDataWapper event) throws Exception {
		TranslatorData response = event.getData();
		ChannelHandlerContext ctx = event.getCtx();
		//业务逻辑处理:
		try {
    		System.err.println("Client端: id= " + response.getId() + ", name= " + response.getName() + ", message= " + response.getMessage());
		} finally {

			//最后手动释放消息
			ReferenceCountUtil.release(response);
		}
	}

}
