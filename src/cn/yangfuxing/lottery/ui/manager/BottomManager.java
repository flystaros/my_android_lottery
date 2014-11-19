package cn.yangfuxing.lottery.ui.manager;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.ui.PlaySSQ;
import cn.yangfuxing.lottery.ui.manager.PlayGame;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottomManager implements Observer
{
	//单例模式
	//1,构造的
	private BottomManager(){}
	//2,创建一个静态的实例
	private static BottomManager instance; 
	// 3,提供一格统一的对外获取实例的入口
	public static BottomManager getInstance() {
		if(instance == null)
		{
			instance = new BottomManager();
		}
		return instance;
	}
	
	//底部菜单容器
	private RelativeLayout bottomMenuContainer;
	//底部导航
	private LinearLayout commonBottom;  //普通导航
	private LinearLayout playBottom; //购彩
	
	//导航按钮
	private ImageButton cleanButton;
	private ImageButton addButton;
	private TextView playBottomNotice;
	
	//通用导航底部按钮
	private ImageButton homeButton;
	private ImageButton hallButton;
	private ImageButton rechargeButton;
	private ImageButton myselfButton;
	
	public void init(Activity activity)
	{
		bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ii_bottom);
		commonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_common);
		playBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);

		playBottomNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
		cleanButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_clean);
		addButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_ok);

		// 设置监听
		setListener();
	}

	private void setListener() {
		//清空按钮
		cleanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//.获取当前正在展示的view
				BaseUI current = MiddleManager.getInstance().getCurrentUI();
				/*if(current instanceof PlaySSQ)
				{
					((PlaySSQ)current).clear();
				}*/
				
				if(current instanceof PlayGame)
				{
					((PlayGame)current).clear();
				}
			}
		});
		
		//选好按钮
		addButton.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaseUI current = MiddleManager.getInstance().getCurrentUI();
				if(current instanceof PlayGame)
				{
					((PlayGame)current).done();
				}
			}
		});
        }
	
	public void showCommonBottom()
	{
		if(bottomMenuContainer.getVisibility() == View.GONE  || bottomMenuContainer.getVisibility() == View.INVISIBLE)
		{
			bottomMenuContainer.setVisibility(View.VISIBLE);
		}
		commonBottom.setVisibility(View.VISIBLE);
		playBottom.setVisibility(View.INVISIBLE);
	}

	/**
	 * 转换到购彩
	 */
	public void showGameBottom() {
		if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE) 
		{
			bottomMenuContainer.setVisibility(View.VISIBLE);
		}
		commonBottom.setVisibility(View.INVISIBLE);
		playBottom.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 改变底部导航容器显示情况
	 */
	public void changeBottomVisiblity(int type) {
		if (bottomMenuContainer.getVisibility() != type)
			bottomMenuContainer.setVisibility(type);
	}

	/*********************************************************************************************/
	/*********************** 第四步：控制玩法导航内容显示 ********************************************/
	/**
	 * 设置玩法底部提示信息
	 * 
	 * @param notice
	 */
	public void changeGameBottomNotice(String notice) {
		playBottomNotice.setText(notice);
	}

	@Override
        public void update(Observable observable, Object data) {
		if(data != null && StringUtils.isNumeric(data.toString()))
		{
			int id = Integer.parseInt(data.toString());
			switch (id) {
			case ConstantValue.VIEW_FRIST:
			case ConstantValue.VIEW_HALL:
			case ConstantValue.VIEW_LOGIN:
				showCommonBottom();
				break;
			case ConstantValue.VIEW_SECOND:
			case ConstantValue.VIEW_PLAYSSQ:
				showGameBottom();
				break;
			case ConstantValue.VIEW_SHOPPING:
			case ConstantValue.VIEW_PREBET:
				changeBottomVisiblity(View.GONE);
				break;

			default:
				break;
			}
		}
        }

	
	
}
