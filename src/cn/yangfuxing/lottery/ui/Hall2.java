package cn.yangfuxing.lottery.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class Hall2 extends BaseUI {
	private ListView categoryList;      //彩 种的入口
	private CategoryAdapter adapter;

	public Hall2(Context context) {
	        super(context);
        }


	protected void init() {
		showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall2, null);
		categoryList = (ListView) findViewById(R.id.ii_hall_lottery_list);
		
//		needUpdate = new ArrayList<View>();
		
		adapter = new CategoryAdapter();
		categoryList.setAdapter(adapter);
		
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

	/**
	 *  获取当前销售期信息
	 */
	private void getCurrentIssueInfo()
	{
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

	private String text;
//	private List<View> needUpdate;
	/**
	 * 修改界面数据
	 * @param element
	 */
	protected void changeNotice(Element element) {
		CurrentIssueElement currentIssueElement = (CurrentIssueElement) element;
		String issue = currentIssueElement.getIssue();
		String lasstime = getLasttime(currentIssueElement.getLasstime());
	        text = context.getResources().getString(R.string.is_hall_common_summary);
	        text =  StringUtils.replaceEach(text, new String[]{"ISSUE","TIME" }, new String[]{issue,lasstime});
	        //TODO　更新界面　
	        //方案一
	         adapter.notifyDataSetChanged();    //所有的item更新
	        //方案二
	        //只更新需要更新的内容,没必要刷新所有的信息　
	        //获取到需要更新的引用　
/*	       TextView view = (TextView) needUpdate.get(0);
	       view.setText(text);*/
	        //方案三：
	       TextView view = (TextView) categoryList.findViewWithTag(0);
	       if(view != null)
	       {
		       view.setText(text);
	       }
	        
	        
	        
        }


	@Override
        protected void setListener() {
	        // TODO Auto-generated method stub
	        
        }
	
	private int[] logoResIds = new int[] {R.drawable.id_ssq,R.drawable.id_3d, R.drawable.id_qlc};
	private int[] titleResIds = new int[]{R.string.is_hall_ssq_title,R.string.is_hall_3d_title,R.string.is_hall_qlc_title};
	
	private class CategoryAdapter extends BaseAdapter
	{

		@Override
                public int getCount() {
	                return 3;
                }

		@Override
                public Object getItem(int position) {
	                return position;
                }

		@Override
                public long getItemId(int position) {
	                return position;
                }

		@Override
                public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.ii_hall_lottery_item, null);
				holder.logo = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_logo);
				holder.title = (TextView) convertView.findViewById(R.id.ii_hall_lottery_title);
				holder.summary = (TextView) convertView.findViewById(R.id.ii_hall_lottery_summary);
//				needUpdate.add(holder.summary);
				holder.bet = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_bet);
				
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.logo.setImageResource(logoResIds[position]);
			holder.title.setText(titleResIds[position]);
			
			holder.summary.setTag(position);
			
			if(StringUtils.isNotBlank(text) && position == 0)
			{
				holder.summary.setText(text);
			}
	                return convertView;
                }
		//依据item的layout
		class ViewHolder
		{
			ImageView logo;
			TextView title;
			TextView summary;
			ImageView bet;
		}
	}
	
}
