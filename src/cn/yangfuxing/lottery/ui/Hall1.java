package cn.yangfuxing.lottery.ui;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.engine.CommonInfoEngine;
import cn.yangfuxing.lottery.net.protocal.Element;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.Oelement;
import cn.yangfuxing.lottery.net.protocal.element.CurrentIssueElement;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.util.BeanFactory;
import cn.yangfuxing.lottery.util.PromptManager;

public class Hall1 extends BaseUI {
	
	private  TextView ssqCurrentIssue;
	private ImageView bet;

	public Hall1(Context context) {
	        super(context);
        }

	protected void setListener() {
	        bet.setOnClickListener(this);
        }

	protected void init() {
		showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall1, null);
		//如果root=null ,那么加载View的Layout为null
		// 如果不为空,return root
		
	
		ssqCurrentIssue = (TextView)findViewById(R.id.ii_hall_ssq_summary);
		bet = (ImageView) findViewById(R.id.ii_hall_ssq_bet);
		
//		getCurrentIssueInfo();
        }
	
	@Override
	public void onResume() {
		getCurrentIssueInfo();
	        super.onResume();
	}


	@Override
	public int getId() {
		return ConstantValue.VIEW_HALL;
	}

	@Override
        public void onClick(View v) {
	        
        }
	
	/**
	 *  获取当前销售期信息
	 */
	private void getCurrentIssueInfo()
	{
//		new MyAsyncTask().execute(ConstantValue.SSQ);
		new MyHttpTask<Integer>() {

			@Override
                        protected Message doInBackground(Integer... params) {
				//获取数据－－－调用业务层
				CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);
	                        return engine.getCurrentIssueInfo(params[0]);
                        }
			
			@Override
			protected void onPostExecute(Message result) {
			        // TODO 更新界面　
				if(result != null)
				{
					Oelement oelement = result.getBody().getOelement();
					if(ConstantValue.SUCCESS.equals(oelement.getErrorcode()))
					{
						changeNotice(result.getBody().getElements().get(0));
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

	/**
	 * 将秒时间转换成日时分格式
	 * 
	 * @param lasttime
	 * @return
	 */
	public String getLasttime(String lasttime) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNumericSpace(lasttime)) {
			int time = Integer.parseInt(lasttime);
			int day = time / (24 * 60 * 60);
			result.append(day).append("天");
			if (day > 0) {
				time = time - day * 24 * 60 * 60;
			}
			int hour = time / 3600;
			result.append(hour).append("时");
			if (hour > 0) {
				time = time - hour * 60 * 60;
			}
			int minute = time / 60;
			result.append(minute).append("分");
		}
		return result.toString();
	}

	
	/**
	 * 修改界面数据
	 * @param element
	 */
	protected void changeNotice(Element element) {
		CurrentIssueElement currentIssueElement = (CurrentIssueElement) element;
		String issue = currentIssueElement.getIssue();
		String lasstime = getLasttime(currentIssueElement.getLasstime());
	        String text = context.getResources().getString(R.string.is_hall_common_summary);
	        text =  StringUtils.replaceEach(text, new String[]{"ISSUE","TIME" }, new String[]{issue,lasstime});
	        ssqCurrentIssue.setText(text);
        }
	
	/**
	 * 异步访问网络的工具
	 * @author flystar
	 * Params:传输的参数
	 * Progress:下载相关的进度提示
	 * Result:服务器返回数据的封装
	 *
	 */
	
	/*private class MyAsyncTask extends AsyncTask<Integer, Void, Message>
	{
		*//**
		 * 同run方法，　在子线程中运行
		 *//*
		@Override
                protected Message doInBackground(Integer... params) {
	                // TODO Auto-generated method stub
	                return null;
                }
		
		@Override
		protected void onPreExecute() {
		        // TODO UI Thread before doInBackground
			//显示滚动条
		        super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Message result) {
		        // TODO UI Thread Before doInBackground
			//修改界面信息
		        super.onPostExecute(result);
		}
		//无法复写父类的方法时的一个技巧
		public final AsyncTask<Integer, Void, Message> executeProxy(Integer... params) {
			if(NetUtils.checkNet(context))
			{
				return super.execute(params);
			}
			else
			{
				PromptManager.showNoNetWork(context);
			}
			return null;
		  }
		
	}
*/
}
