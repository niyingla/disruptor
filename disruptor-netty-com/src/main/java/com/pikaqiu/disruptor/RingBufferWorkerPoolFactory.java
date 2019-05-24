package com.pikaqiu.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.pikaqiu.entity.TranslatorDataWapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class RingBufferWorkerPoolFactory {

	private static class SingletonHolder {
		static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
	}
	
	private RingBufferWorkerPoolFactory(){
		
	}
	
	public static RingBufferWorkerPoolFactory getInstance() {
		return SingletonHolder.instance;
	}
	
	private static Map<String, MessageProducer> producers = new ConcurrentHashMap<String, MessageProducer>();
	
	private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<String, MessageConsumer>();

	private RingBuffer<TranslatorDataWapper> ringBuffer;
	
	private SequenceBarrier sequenceBarrier;
	
	private WorkerPool<TranslatorDataWapper> workerPool;
	
	public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
		//1. 构建ringBuffer对象
		this.ringBuffer = RingBuffer.create(type,
				new EventFactory<TranslatorDataWapper>() {
					@Override
					public TranslatorDataWapper newInstance() {
						return new TranslatorDataWapper();
					}
				},
				bufferSize,
				waitStrategy);
		//2.设置序号栅栏
		this.sequenceBarrier = this.ringBuffer.newBarrier();
		//3.设置工作池
		this.workerPool = new WorkerPool<TranslatorDataWapper>(
				this.ringBuffer, this.sequenceBarrier, new EventExceptionHandler(), messageConsumers);
		//4 把所构建的消费者置入池中
		for(MessageConsumer mc : messageConsumers){
			consumers.put(mc.getConsumerId(), mc);
		}
		//5 添加我们的sequences
		this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
		//6 启动我们的工作池
		this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2));
	}
	
	public MessageProducer getMessageProducer(String producerId){
		MessageProducer messageProducer = producers.get(producerId);
		if(null == messageProducer) {
			messageProducer = new MessageProducer(producerId, this.ringBuffer);
			producers.put(producerId, messageProducer);
		}
		return messageProducer;
	}
	
	
	/**
	 * 异常静态类
	 * @author xiaoye
	 *
	 */
	static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWapper> {
		@Override
		public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {
		}

		@Override
		public void handleOnStartException(Throwable ex) {
		}
		@Override
		public void handleOnShutdownException(Throwable ex) {
		}
	}
	
	
	

	
}



