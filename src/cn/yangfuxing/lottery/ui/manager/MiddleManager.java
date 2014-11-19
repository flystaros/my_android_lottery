package cn.yangfuxing.lottery.ui.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.R.string;
import cn.yangfuxing.lottery.ui.FirstUI;
import cn.yangfuxing.lottery.ui.Hall;
import cn.yangfuxing.lottery.ui.PlaySSQ;
import cn.yangfuxing.lottery.ui.SecondUI;
import cn.yangfuxing.lottery.util.MemoryManager;
import cn.yangfuxing.lottery.util.PromptManager;
import cn.yangfuxing.lottery.util.SorftMap;

public class MiddleManager extends Observable
{
	private MiddleManager(){}
	private static MiddleManager instance = new MiddleManager();
	
	public static MiddleManager getInstance() {
		return instance;
	}
	private RelativeLayout middle;
	//利用手机内存空间换手机运行速递
	private static  Map<String, BaseUI> UICACHE;
	static {
		if(MemoryManager.hasAcailMemory())
		{
			UICACHE = new HashMap<String, BaseUI>();
		}
		else
		{
			UICACHE = new  SorftMap<String, BaseUI>();
		}
	}
	//１，内存不足处理方案
	//1, 控制UICACHE集合的size
	//２，Fragment代替,replace方法　，　不会缓存界面　
	//3,降低BaseUI的引用级别　
	//  当前的级别，　强引用　　　（ＧＣ宁可抛出OOM异常，　也不会回收BaseUI）
	//　软引用：在OOM之前被GC 回收
	//　弱引用:一旦被GC发现了就回收
	//　虚引用:一旦创建了就被回收了
	
	
	//记录浏览历史
	private LinkedList<String> HISTORY = new LinkedList<String>();
	
	//存储正在展示的ＵＩ
	private BaseUI currentUI;
	
	
	public BaseUI getCurrentUI() {
		return currentUI;
	}

	public void setMiddle(RelativeLayout middle) {
		this.middle = middle;
	}
	
	public void changeUI(Class<? extends BaseUI> targetClazz, Bundle bundle) {
		//比较：正在展示的UI和切换的目标ＵＩ是否相同
				//存贮正在展示的ＵＩ
				if(currentUI !=null && currentUI.getClass() == targetClazz)
				{
					return ;
				}
				
				//对于目标界面只创建一次
				//存储创建过的对象
				
				//判断存放目标对象的容器里面，是否有需要的对象
				//如果有直接使用
				//如果没有，重新创建，并且添加到容器中
				BaseUI tagetUI = null;
				String key = targetClazz.getSimpleName();
				if(UICACHE.containsKey(key))
				{
					tagetUI = UICACHE.get(key);
				}else
				{
					//通过构造器创建一个对象
					try {
			                        Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
			                        tagetUI = constructor.newInstance(getContext());   //调用目标界面的构造方法　
			                        UICACHE.put(key, tagetUI);
		                        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
		                                                        | IllegalArgumentException | InvocationTargetException e) {
			                        e.printStackTrace();
		                        }
				}
				
				if(tagetUI != null)
				{
					tagetUI.setBundle(bundle);
				}
				
				if(tagetUI == null)
				{
					return ;
				}
				//在清理掉当前正在展示的界面之前----对应onPause方法
				if(currentUI != null)
				{
					currentUI.onPause();
				}
				middle.removeAllViews(); //删除上一个界面
				//跳转到第二个界面　
				View child2 = tagetUI.getChild();
				middle.addView(child2);
				child2.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ia_view_change));  //开始切换动画
				//在加载完界面之后，开始一些耗费资源的操作　对应onResume方法
				tagetUI.onResume();
				
				
				//存储正在展示的ＵＩ　
				currentUI = tagetUI;
				
				HISTORY.addFirst(key);
				
				
				//三个容器联动
				changeTitleAndBottom();
        }
	
	/**
	 * 切换界面：界面问题三个容器联动
	 * @param targetClazz
	 */
	public void changeUI(Class<? extends BaseUI> targetClazz) 
	{
		//比较：正在展示的UI和切换的目标ＵＩ是否相同
		//存贮正在展示的ＵＩ
		if(currentUI !=null && currentUI.getClass() == targetClazz)
		{
			return ;
		}
		
		//对于目标界面只创建一次
		//存储创建过的对象
		
		//判断存放目标对象的容器里面，是否有需要的对象
		//如果有直接使用
		//如果没有，重新创建，并且添加到容器中
		BaseUI tagetUI = null;
		String key = targetClazz.getSimpleName();
		if(UICACHE.containsKey(key))
		{
			tagetUI = UICACHE.get(key);
		}else
		{
			//通过构造器创建一个对象
			try {
	                        Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
	                        tagetUI = constructor.newInstance(getContext());   //调用目标界面的构造方法　
	                        UICACHE.put(key, tagetUI);
                        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                                                        | IllegalArgumentException | InvocationTargetException e) {
	                        e.printStackTrace();
                        }
		}
		
		if(tagetUI == null)
		{
			return ;
		}
		//在清理掉当前正在展示的界面之前----对应onPause方法
		if(currentUI != null)
		{
			currentUI.onPause();
		}
		middle.removeAllViews(); //删除上一个界面
		//跳转到第二个界面　
		View child2 = tagetUI.getChild();
		middle.addView(child2);
		child2.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ia_view_change));  //开始切换动画
		//在加载完界面之后，开始一些耗费资源的操作　对应onResume方法
		tagetUI.onResume();
		
		
		//存储正在展示的ＵＩ　
		currentUI = tagetUI;
		
		HISTORY.addFirst(key);
		
		
		//三个容器联动
		changeTitleAndBottom();
        }

	private void changeTitleAndBottom() {
	        //１，界面一对应未登录标题和通用导航
		//2,界面二对应通用标题后和玩法导航　
		
		//1,将中间容器变成被观察的对象
		//2,将标题和底部导航变成观察者
		//3,一旦变动了(setchanged) ,通知
		//3,标题和导航更行内容
		// notic : 　　将观察者和被观察者关联到一起，（在集合中添加观察者对象）
		setChanged();
		notifyObservers(currentUI.getId());
        }

	public void changeUI1(BaseUI targetUI) {
		middle.removeAllViews(); //删除上一个界面
		//跳转到第二个界面　
		View child2 = targetUI.getChild();
		middle.addView(child2);
		child2.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ia_view_change));  //开始切换动画
		
        }
	
	public Context getContext()
	{
		return middle.getContext();
	}

	/**
	 * 操作返回键
	 */
	public boolean goBack() {
		//操作返回键的集合，删除第一个，重新获取第一个元素，切换界面　
		if(HISTORY.size() > 0)
		{
			//当返回建集合中仅有一个元素的时候，不能做删除操作　
			if(HISTORY.size() == 1)
			{
				return false;
			}
			HISTORY.removeFirst();
			if(HISTORY.size() > 0)
			{
				String key = HISTORY.getFirst();
				BaseUI targetUI = UICACHE.get(key);
				if(targetUI != null)
				{
					middle.removeAllViews();
					middle.addView(targetUI.getChild());
					
					currentUI = targetUI;  // 返回成功
					//容器联动
					changeTitleAndBottom();
				}else
				{
					//创建一个新的界面：存在问题－如果有其他的界面传递给被删除的界面
					//处理方式二：寻找一个不需要其他界面传递数据的方式　,跳转到首页
					changeUI(Hall.class);
					PromptManager.showToast(getContext(), "内存不足");
					
				}
				return true;
			}
		}
		return false;
        }

	public void clear() {
		HISTORY.clear();
        }

	
}
