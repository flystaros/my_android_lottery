package cn.yangfuxing.lottery.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Path.Op;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.bean.ShoppingCart;
import cn.yangfuxing.lottery.bean.Ticket;
import cn.yangfuxing.lottery.engine.CommonInfoEngine;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.Oelement;
import cn.yangfuxing.lottery.net.protocal.element.CurrentIssueElement;
import cn.yangfuxing.lottery.ui.adapter.PoolAdapter;
import cn.yangfuxing.lottery.ui.custom.MyGridView;
import cn.yangfuxing.lottery.ui.custom.MyGridView.OnActionUpListener;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.ui.manager.BottomManager;
import cn.yangfuxing.lottery.ui.manager.MiddleManager;
import cn.yangfuxing.lottery.ui.manager.PlayGame;
import cn.yangfuxing.lottery.ui.manager.TitleManager;
import cn.yangfuxing.lottery.ui.manager.BaseUI.MyHttpTask;
import cn.yangfuxing.lottery.util.BeanFactory;
import cn.yangfuxing.lottery.util.PromptManager;

/**
 * 双色球的选号界面　
 * @author flystar
 *
 */

public class PlaySSQ extends BaseUI implements PlayGame
{
	//1,标题
	//２，填充选号容器
	//3,选号：单击＋机选
	//4,手机摇一摇
	//5,提示信息＋清空＋选好了
	
	//机选按钮
	private Button randomRed;
	private Button randomBlue;
	//选号容器
	private MyGridView redContainer;
	private GridView blueContainer;
	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;
	
	private List<Integer> redNums;     //  restore the selected red ball's num
	private List<Integer> blueNums;  
	
	private SensorManager manager;
	private ShakeListener listener;
	
	public PlaySSQ(Context context) {
	        super(context);
        }

	@Override
	public int getId() {
		return ConstantValue.VIEW_PLAYSSQ;
	}

	@Override
	protected void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq, null);
		
		randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
		randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);
		redContainer = (MyGridView) findViewById(R.id.ii_ssq_red_number_container);
		blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
		
		redNums = new  ArrayList<Integer>();
		blueNums = new ArrayList<Integer>();
		
		redAdapter = new PoolAdapter(context,33,redNums,R.drawable.id_redball);
		redContainer.setAdapter(redAdapter);
		
		blueAdapter = new PoolAdapter(context, 16,blueNums,R.drawable.id_blueball);
		blueContainer.setAdapter(blueAdapter);
		
//		获取传感器服务
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
	}

	@Override
	protected void setListener() {
		randomRed.setOnClickListener(this);
		randomBlue.setOnClickListener(this);
		
		redContainer.setOnActionUpListener(new OnActionUpListener() {
			
			@Override
			public void onActionUp(TextView view, int position) {
				
				if(!redNums.contains(position+1))
				{
					view.setBackgroundResource(R.drawable.id_redball);
					redNums.add(position+1);
				}
				else
				{
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					redNums.remove((Object)(position+1));
				}
				changeNotice();
			}

		});
		
		/*redContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!redNums.contains(position+1))
				{
					view.setBackgroundResource(R.drawable.id_redball);
					view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_change));
					redNums.add(position+1);
				}
				else
				{
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					redNums.remove((Object)(position+1));
				}
                        }
			
		});*/
		
		blueContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!blueNums.contains(position+1))
				{
					view.setBackgroundResource(R.drawable.id_blueball);
					view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_change));
					blueNums.add(position+1);
				}
				else
				{
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					blueNums.remove((Object)(position+1));
				}
				changeNotice();
                        }
			
		});
		
		
	}

	@Override
        public void onClick(View v) {
		Random random = new Random();
		 switch (v.getId()) {
		case R.id.ii_ssq_random_red:
			redNums.clear();
			while(redNums.size() < 6)
			{
				int num = random.nextInt(33)+1;
				
				if(redNums.contains(num))
				{
					continue;
				}
				redNums.add(num);
			}
			redAdapter.notifyDataSetChanged();
			changeNotice();
			break;
		case R.id.ii_ssq_random_blue:
			blueNums.clear();
			int num = random.nextInt(16)+1;
			blueNums.add(num);
			
			blueAdapter.notifyDataSetChanged();
			changeNotice();
			break;
		default:
			break;
		}
	        super.onClick(v);
        }
	
	/**
	 * 　修改界面的标题
	 */
	private void changeTitle()
	{
		String titleInfo="";
		//1,标题  －－－－　界面之间的数据传递
		//判断购彩大厅是否获取到期次信息
		if(bundle != null)
		{
			titleInfo = "双色球" + bundle.getString("issue") + "期";
		}
		else
		{
			titleInfo = "双色球选号";
		}
		//如果获取到：拼装标题
		//否则默认的标题展示
		 
		TitleManager.getInstance().changeTitle(titleInfo);
	}
	
	/**
	 * 改变底部导航的提示信息
	 */
	private void changeNotice()
	{
		String notice = "";
		if(redNums.size() < 6)
		{
			notice = "您还需要选择 "+(6-redNums.size())+" 个红球";
		}
		else if(blueNums.size() == 0)
		{
			notice = "您还需要选择"+1+"个篮球";
		}
		else
		{
			notice = "共 "+calc()+" 注 "+calc()*2+" 元";
		}
		
		BottomManager.getInstance().changeGameBottomNotice(notice );
	}
	
	/**
	 * 结算注数
	 * @return
	 */
	private int calc() {
		int redC = (int) (factorial(redNums.size()) / (factorial(6) * factorial(redNums.size() - 6)));
		int blueC = blueNums.size();
	        return redC * blueC;
        }
	/**
	 *结算num的阶乘　
	 * @param num
	 * @return
	 */
	private long factorial(int num)
	{
		if(num > 1)
		{
			return num * factorial(num - 1);
		}
		else if(num == 1 | num == 0)
		{
			return 1;
		}
		else
		{
			throw new IllegalArgumentException("num >= 0");
		}
	}
	

	@Override
	public void onResume() {
		changeTitle();
		changeNotice();
		clear();
		//注册传感器
		listener = new ShakeListener(context) {

			@Override
                        public void randomCure() {
	                        randomSSQ();
                        }

		};
		manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ,SensorManager.SENSOR_DELAY_FASTEST);
	        super.onResume();
	}
	
	@Override
	public void onPause() {
		//注销掉传感器
		manager.unregisterListener(listener);
	        super.onPause();
	}
	
	/**
	 * 摇晃　手机　选一注
	 */
	private void randomSSQ() {

		Random random = new Random();
		
		redNums.clear();
		blueNums.clear();
		
			while(redNums.size() < 6)
			{
				int num = random.nextInt(33)+1;
				
				if(redNums.contains(num))
				{
					continue;
				}
				redNums.add(num);
			}
		
			int num = random.nextInt(16)+1;
			blueNums.add(num);
			
			redAdapter.notifyDataSetChanged();
			blueAdapter.notifyDataSetChanged();
			changeNotice();
			
		}
	
	/**
	 * 清空处理　
	 */
	public void clear()
	{
		redNums.clear();
		blueNums.clear();
		changeNotice();
		
		redAdapter.notifyDataSetChanged();
		blueAdapter.notifyDataSetChanged();
	}

	@Override
        public void done() {
	        // 1,判断：用户是否选择了一注投注
		if(redNums.size() >= 6 && blueNums.size() >= 1)
		{
			bundle = new  Bundle();
			bundle.putString("issue", "11515期"); //模拟　
			if(bundle != null)//无法联网，模拟，实应为　！＝
			{
				//封装用户的投注信息
				Ticket ticket = new Ticket();
				DecimalFormat decimalFormat = new  DecimalFormat("00");
				StringBuffer redBuffer = new StringBuffer();
				for(Integer item : redNums)
				{
					redBuffer.append(decimalFormat.format(item)).append(" ");
				}
				ticket.setRedNum(redBuffer.substring(0, redBuffer.length()-1));
				
				StringBuffer blueBuffer = new StringBuffer();
				for(Integer item : blueNums)		
				{
					blueBuffer.append(" ").append(decimalFormat.format(item));
				}
				ticket.setBlueNum(blueBuffer.substring(1));
				
				ticket.setNum(calc());
				ShoppingCart.getInstance().getTickets().add(ticket);
				
//				ShoppingCart.getInstance().setIssue(bundle.getString("issue"));
				ShoppingCart.getInstance().setIssue("111314期");   //　模拟
				ShoppingCart.getInstance().setLotteryid(ConstantValue.SSQ);
				//跳转页面
				MiddleManager.getInstance().changeUI(Shopping.class, bundle);
			}
			else
			{
				//重新获取期次信息
				getCurrentIssueInfo();
			}
		}else
		{
			//提示信息需要选择一注
			PromptManager.showToast(context, "需要选择一注");
		}
        }
	
	
	protected  void getCurrentIssueInfo()
	{
		new MyHttpTask<Integer>() {
			
	/*		protected void onPreExecute() {
				//显示滚动条
				PromptManager.showProgressDialog(context);
			};*/

			@Override
                        protected Message doInBackground(Integer... params) {
				//获取数据－－－调用业务层
				CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);
	                        return engine.getCurrentIssueInfo(params[0]);
                        }
			
			@Override
			protected void onPostExecute(Message result) {
				PromptManager.closeProgressDialog();
			        // TODO 更新界面　
				if(result != null)
				{
					Oelement oelement = result.getBody().getOelement();
					if(ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
					{
						CurrentIssueElement element = (CurrentIssueElement) result.getBody().getElements().get(0);
						//创建Bundle
						bundle = new  Bundle();
						bundle.putString("issue", "11515期"); //模拟　
						changeTitle();
					}
					else
					{
						PromptManager.showToast(context, oelement.getErrormsg());
					}
				}else{
					PromptManager.showToast(context, "服务器忙，请稍后重试.....");
				}
			        super.onPostExecute(result);
			}
		}.executeProxy(ConstantValue.SSQ);
	}
}
	


