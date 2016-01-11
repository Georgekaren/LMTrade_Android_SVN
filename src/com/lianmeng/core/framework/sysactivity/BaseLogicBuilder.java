package com.lianmeng.core.framework.sysactivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BaseLogicBuilder
  implements ILogicBuilder
{
  private static final String TAG = "BaseLogicBuilder";
  private final Handler mHandler = new MyHandler();
  private Map<String, ILogic> mLogicCache = new HashMap();

  protected BaseLogicBuilder(Context paramContext)
  {
    init(paramContext);
    initAllLogics(paramContext);
  }

  private void initAllLogics(Context paramContext)
  {
    Iterator localIterator = this.mLogicCache.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      ILogic localILogic = (ILogic)localEntry.getValue();
      ((ILogic)localEntry.getValue()).init(paramContext);
      localILogic.addHandler(this.mHandler);
    }
  }

  private boolean isInterface(Class<?> paramClass, String paramString)
  {
    Class[] arrayOfClass1 = paramClass.getInterfaces();
    int i = 0;
    int k = arrayOfClass1.length;
    while (i < k)
    {
      if (arrayOfClass1[i].getName().equals(paramString))
        return true;
      Class[] arrayOfClass2 = arrayOfClass1[i].getInterfaces();
      int j = 0;
      while (j < arrayOfClass2.length)
      {
        if ((!(arrayOfClass2[j].getName().equals(paramString))) && (!(isInterface(arrayOfClass2[j], paramString))));
        j += 1;
      }
      i += 1;
    }
    if (paramClass.getSuperclass() != null)
      return isInterface(paramClass.getSuperclass(), paramString);
    return false;
  }

  public void addHandlerToAllLogics(Handler paramHandler)
  {
    Iterator localIterator = this.mLogicCache.entrySet().iterator();
    while (localIterator.hasNext())
      ((ILogic)((Map.Entry)localIterator.next()).getValue()).addHandler(paramHandler);
  }

  public ILogic getLogicByInterfaceClass(Class<?> paramClass)
  {
    return ((ILogic)this.mLogicCache.get(paramClass.getName()));
  }

  protected void handleStateMessage(Message paramMessage)
  {
  }

  protected abstract void init(Context paramContext);

  protected void registerLogic(Class<?> paramClass, ILogic paramILogic)
  {
    Class localClass;
    String sparamClass="";
    if (!(this.mLogicCache.containsKey(paramClass.getName())))
    {
    	sparamClass = paramClass.getName();
      localClass = paramILogic.getClass();
      if ((!(isInterface(localClass, sparamClass))) || (!(isInterface(localClass, ILogic.class.getName()))))
         return;
      this.mLogicCache.put(sparamClass, paramILogic);
      Log.i("BaseLogicBuilder", "Register logic(" + localClass.getName() + ") successful.");
    }
    return;
  }

  public void removeHandlerToAllLogics(Handler paramHandler)
  {
    Iterator localIterator = this.mLogicCache.entrySet().iterator();
    while (localIterator.hasNext())
      ((ILogic)((Map.Entry)localIterator.next()).getValue()).removeHandler(paramHandler);
  }

  public void removeLogic(Class<?> paramClass)
  {
    this.mLogicCache.remove(paramClass.getName());
  }

  private class MyHandler extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
      BaseLogicBuilder.this.handleStateMessage(paramMessage);
    }
  }
}