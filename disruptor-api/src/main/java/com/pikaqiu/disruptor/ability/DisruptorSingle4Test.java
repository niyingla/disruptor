package com.pikaqiu.disruptor.ability;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * 测试同样 1000 0000
 */
public class DisruptorSingle4Test {

    public static void main(String[] args) {
        int ringBufferSize = 65536;
        final Disruptor<Data> disruptor = new Disruptor<Data>(
                //创建新的存储对象工厂 用于创造对象
                new EventFactory<Data>() {
                    @Override
                    public Data newInstance() {
                        return new Data();
                    }
                },
                //环形队列大小
                ringBufferSize,
                //线程池
                Executors.newSingleThreadExecutor(),
                //创建一个带有单个事件发布者的
                ProducerType.SINGLE,
                //new BlockingWaitStrategy()
                //阻塞等待策略
                new YieldingWaitStrategy()
        );

        DataConsumer consumer = new DataConsumer();
        //消费数据
        disruptor.handleEventsWith(consumer);
        disruptor.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取环形队列
                RingBuffer<Data> ringBuffer = disruptor.getRingBuffer();
                for (long i = 0; i < Constants.EVENT_NUM_OHM; i++) {
                    long seq = ringBuffer.next();
                    //获取对应序号的数据
                    Data data = ringBuffer.get(seq);
                    //设置内容
                    data.setId(i);
                    data.setName("c" + i);
                    //发布当前序号的数据
                    ringBuffer.publish(seq);
                }
            }
        }).start();
    }
}
