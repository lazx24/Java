package com.coscon.shipsuite.common.util.concurrent;

public class ArgsRunnable
  implements Runnable
{
  private Object[] runnableArgs;
  
  public ArgsRunnable(Object arg)
  {
    this.runnableArgs = new Object[] { arg };
  }
  
  public ArgsRunnable(Object[] args)
  {
    this.runnableArgs = args;
  }
  
  public void run() {}
  
  public Object[] getRunnableArgs()
  {
    return this.runnableArgs;
  }
}
