package com.common.util.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class ThreadPoolExecutorFactoryBean
  implements FactoryBean<ThreadPoolExecutor>, ApplicationListener<ContextClosedEvent>
{
  private int corePoolSize = 100;
  private int maxPoolSize = 300;
  private int queueCapacity = 1000;
  private long keepAliveTime = 1L;
  private TimeUnit unit = TimeUnit.MINUTES;
  private BlockingQueue<Runnable> workQueue;
  private ThreadPoolExecutor executor;
  
  public int getCorePoolSize()
  {
    return this.corePoolSize;
  }
  
  public void setCorePoolSize(int corePoolSize)
  {
    this.corePoolSize = corePoolSize;
  }
  
  public int getMaxPoolSize()
  {
    return this.maxPoolSize;
  }
  
  public void setMaxPoolSize(int maxPoolSize)
  {
    this.maxPoolSize = maxPoolSize;
  }
  
  public long getKeepAliveTime()
  {
    return this.keepAliveTime;
  }
  
  public void setKeepAliveTime(long keepAliveTime)
  {
    this.keepAliveTime = keepAliveTime;
  }
  
  public TimeUnit getUnit()
  {
    return this.unit;
  }
  
  public void setUnit(TimeUnit unit)
  {
    this.unit = unit;
  }
  
  public BlockingQueue<Runnable> getWorkQueue()
  {
    return this.workQueue;
  }
  
  public void setWorkQueue(BlockingQueue<Runnable> workQueue)
  {
    this.workQueue = workQueue;
  }
  
  public int getQueueCapacity()
  {
    return this.queueCapacity;
  }
  
  public void setQueueCapacity(int queueCapacity)
  {
    this.queueCapacity = queueCapacity;
  }
  
  public ThreadPoolExecutor getObject()
    throws Exception
  {
    if (this.workQueue == null) {
      this.workQueue = new ArrayBlockingQueue(this.queueCapacity);
    }
    if (this.executor == null) {
      this.executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, 
        this.keepAliveTime, this.unit, this.workQueue);
    }
    return this.executor;
  }
  
  public Class<?> getObjectType()
  {
    return ThreadPoolExecutor.class;
  }
  
  public boolean isSingleton()
  {
    return true;
  }
  
  public void onApplicationEvent(ContextClosedEvent event)
  {
    if (this.executor != null) {
      this.executor.shutdownNow();
    }
  }
}
