package cn.yangfuxing.lottery.ui;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PSource;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.GlobalParams;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.engine.CommonInfoEngine;
import cn.yangfuxing.lottery.net.protocal.Element;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.Oelement;
import cn.yangfuxing.lottery.net.protocal.element.CurrentIssueElement;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.ui.manager.MiddleManager;
import cn.yangfuxing.lottery.util.BeanFactory;
import cn.yangfuxing.lottery.util.PromptManager;

public class Hall extends BaseUI {
	private ListView categoryList;      //彩 种的入口
	private CategoryAdapter adapter;
	
	//ViewPager配置信息　
	private ViewPager pager;
	private PagerAdapter pagerAdapter;
	private List<View> pagers;
	
	private ImageView underline;
	
	private TextView fcTitle; //福彩
	private TextView tcTitle; //体彩
	private TextView gpcTitle; //高频彩

	public Hall(Context context) {
	        super(context);
        }


	protected void init() {
		showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall, null);
		
		pager = (ViewPager) findViewById(R.id.ii_viewpager);
		pagerAdapter = new MyPagerAdapter();
		
		categoryList = new ListView(context);
		adapter = new CategoryAdapter();
		categoryList.setAdapter(adapter);
		initPager();
		//页面初始化完成后，为pager设置适配器
		pager.setAdapter(pagerAdapter);
		
		//初始化选项卡的下划线
		initTabStrip();
		
        }
	
	private void initTabStrip() {
		underline = (ImageView) findViewById(R.id.ii_category_selector);
		fcTitle = (TextView) findViewById(R.id.ii_category_fc);
		tcTitle = (TextView) findViewById(R.id.ii_category_tc);
		gpcTitle = (TextView) findViewById(R.id.ii_category_gpc);
		
		fcTitle.setTextColor(Color.RED);
	        //１，获取屏幕的宽度
		//２，小图片的宽度
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.id_category_selector);
		int offset = (GlobalParams.WINDOW_WIDTH/3 - bitmap.getWidth())/2;
		
		//设置图片的出事位置
		Matrix matrix = new  Matrix();
		matrix.postTranslate(offset, 0);
		underline.setImageMatrix(matrix );
		
        }


	private void initPager() {
	        pagers = new ArrayList<View>();
	        pagers.add(categoryList);
	        
	        TextView item = new TextView(context);
	        item.setText("第二页");
	        pagers.add(item);
	        
	        item = new  TextView(context);
	        item.setText("第三页");
	        pagers.add(item);
	        
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
	private Bundle ssqBundle;
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
	        
	        ssqBundle = new Bundle();
	        ssqBundle.putString("issue", issue);
	        
        }
	
	//记录ViewPager上一个界面的position信息
	private int lastPosition=0;
	
	@Override
        protected void setListener() {
		//为ViewPager设置监听
		pager.setOnPageChangeListener(new OnPageChangeListener() 
		{
			@Override
			public void onPageSelected(int position) {
				//滑动完成后
				//position :初始值为0
				TranslateAnimation animation = new TranslateAnimation(lastPosition*GlobalParams.WINDOW_WIDTH/3,position* GlobalParams.WINDOW_WIDTH/3, 0, 0);
				animation.setDuration(300);
				animation.setFillAfter(true);  //移动完成后停留到终点
				underline.setAnimation(animation);   //开始移动
				lastPosition = position;
				
				fcTitle.setTextColor(Color.BLACK);
				tcTitle.setTextColor(Color.BLACK);
				gpcTitle.setTextColor(Color.BLACK);
				
				switch (position) {
				case 0:
					fcTitle.setTextColor(Color.RED);
					break;
				case 1:
					tcTitle.setTextColor(Color.RED);
					break;
				case 2:
					gpcTitle.setTextColor(Color.RED);
					break;
				default:
					break;
				}
				
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		fcTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(0);
			}
		});
		
		tcTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(1);
			}
		});
		
		gpcTitle.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(2);
			}
		});
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
                public View getView(final int position, View convertView, ViewGroup parent) {
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
			
			holder.bet.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position == 0)
					{
						MiddleManager.getInstance().changeUI(PlaySSQ.class,ssqBundle);
					}
				}
			});
			
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
	
	/**
	 *  ViewPager 所用到的适配器　
	 * @author flystar
	 *
	 */
	private class MyPagerAdapter extends PagerAdapter
	{
		@Override
                public void destroyItem(ViewGroup container, int position, Object object) {
			View view = pagers.get(position);
			container.removeView(view);				
                }

		@Override
                public Object instantiateItem(ViewGroup container, int position) {
			View view = pagers.get(position);
			container.addView(view);
	                return view;
                }

		@Override
                public int getCount() {
	                return pagers.size();
                }

		@Override
                public boolean isViewFromObject(View view, Object object) {
	                return view == object;
                }
	}
	
}
