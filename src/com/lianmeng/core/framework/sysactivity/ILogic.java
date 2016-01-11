package com.lianmeng.core.framework.sysactivity;

import android.content.Context;
import android.os.Handler;

public abstract interface ILogic
{
  public abstract void addHandler(Handler paramHandler);

  public abstract void init(Context paramContext);

  public abstract void removeHandler(Handler paramHandler);
}