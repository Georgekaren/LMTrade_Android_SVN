package com.lianmeng.core.framework.sysactivity;


import android.content.Context;
import android.os.Bundle;

public abstract class BasicTabActivity extends BaseTabActivity
{
  private static final String TAG = "BasicTabActivity";

  protected BaseLogicBuilder createLogicBuilder(Context paramContext)
  {
    return LogicBuilder.getInstance(paramContext);
  }

  protected void initLogics()
  {
  }

  protected void onCreate(Bundle paramBundle)
  {
    if (!(isInit()))
    {
      BaseTabActivity.setLogicBuilder(createLogicBuilder(getApplicationContext()));
       
    }
    super.onCreate(paramBundle);
  }
}
