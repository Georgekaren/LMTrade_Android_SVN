package com.lianmeng.core.framework.sysactivity;

import android.os.Handler;

public abstract interface ILogicBuilder
{
  public abstract void addHandlerToAllLogics(Handler paramHandler);

  public abstract ILogic getLogicByInterfaceClass(Class<?> paramClass);

  public abstract void removeHandlerToAllLogics(Handler paramHandler);
}
