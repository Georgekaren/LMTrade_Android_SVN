package com.lianmeng.core.framework.sysactivity;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseTabActivity extends TabActivity
{
  private static final String TAG = "BaseTabActivity";
  private static BaseLogicBuilder mLogicBuilder = null;
  private boolean isPrivateHandler = false;
  private Handler mHandler = null;
  private final List<ILogic> mLogicList = new ArrayList();

  protected static final void setLogicBuilder(BaseLogicBuilder paramBaseLogicBuilder)
  {
    mLogicBuilder = paramBaseLogicBuilder;
  }

  protected Handler getHandler()
  {
    if (this.mHandler == null)
      this.mHandler = new Handler()
      {
        public void handleMessage(Message paramMessage)
        {
          BaseTabActivity.this.handleStateMessage(paramMessage);
        }
      };
    return this.mHandler;
  }

  protected final ILogic getLogicByInterfaceClass(Class<?> paramClass)
  {
    ILogic localILogic2 = mLogicBuilder.getLogicByInterfaceClass(paramClass);
    if (isPrivateHandler())
    {
      localILogic2.addHandler(getHandler());
      this.mLogicList.add(localILogic2);
    }
    ILogic localILogic1 = localILogic2;
    if (localILogic2 == null)
    {
      Log.e("BaseTabActivity", "Not found logic by interface class (" + paramClass + ")", new Throwable());
      localILogic1 = null;
    }
    return localILogic1;
  }

  protected void handleStateMessage(Message paramMessage)
  {
  }

  protected abstract void initLogics();

  protected final boolean isInit()
  {
    return (mLogicBuilder != null);
  }

  protected boolean isPrivateHandler()
  {
    return this.isPrivateHandler;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!(isInit()))
    {
      Log.e("BaseTabActivity", "Launched the first should be the LauncheActivity's subclass:" + super.getClass().getName(), new Throwable());
      return;
    }
    if (!(isPrivateHandler()))
      mLogicBuilder.addHandlerToAllLogics(getHandler());
    try
    {
      initLogics();
      setVolumeControlStream(3);
      return;
    }
    catch (Exception ex)
    {
      Log.e("BaseTabActivity", "Init logics failed :" + ex.getMessage(), new Throwable());
    }
  }

  protected void onDestroy()
  {
    Handler localHandler = getHandler();
    if (localHandler != null)
    {
      if ((this.mLogicList.size() > 0) && (isPrivateHandler()))
      {
        Iterator localIterator = this.mLogicList.iterator();
        while (true)
        {
          if (!(localIterator.hasNext()))
            break;
          ((ILogic)localIterator.next()).removeHandler(localHandler);
        }
      }
      if (mLogicBuilder != null)
        mLogicBuilder.removeHandlerToAllLogics(localHandler);
    }
    label78: super.onDestroy();
  }
}