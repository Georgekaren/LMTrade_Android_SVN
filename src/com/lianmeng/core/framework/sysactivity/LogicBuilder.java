package com.lianmeng.core.framework.sysactivity;


import android.content.Context;


public class LogicBuilder extends BaseLogicBuilder
{
  private static BaseLogicBuilder instance;

  private LogicBuilder(Context paramContext)
  {
    super(paramContext);
  }

  public static BaseLogicBuilder getInstance(Context paramContext)
  {
	  BaseLogicBuilder monitorenter;
    try
    {
      if (instance == null)
        instance = new LogicBuilder(paramContext);
      monitorenter = instance;
      return monitorenter;
    }
    finally
    {
    	//monitorenter=null;
    }
  }

  private void registerAllLogics(Context paramContext)
  {
    /*registerLogic(IUserLogic.class, new UserLogicImpl(paramContext));
    registerLogic(IGoodSLogic.class, new GoodsLogicImpl(paramContext));
    registerLogic(IMyLifeLogic.class, new MyLifeLogicImpl(paramContext));
    registerLogic(IShoppingLogic.class, new ShoppingLogicImpl(paramContext));
    registerLogic(ISearchLogic.class, new SearchLogicImpl(paramContext));
    registerLogic(IMallLogic.class, new MallLogicImpl(paramContext));
    registerLogic(IMainLogic.class, new MainLogicImpl(paramContext));
    registerLogic(IMineLogic.class, new MineLogicImpl(paramContext));
    registerLogic(IShareLogic.class, new ShareLogicImpl(paramContext));
    registerLogic(INightLogic.class, new NightLogicImpl(paramContext));
    registerLogic(ITopicLoigc.class, new TopicLogicImpl(paramContext));
    registerLogic(IExpressLogic.class, new ExpressLogicImpl(paramContext));*/
  }

  protected void init(Context paramContext)
  {
    registerAllLogics(paramContext);
  }
}
