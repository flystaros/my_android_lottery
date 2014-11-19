package cn.yangfuxing.lottery.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.GlobalParams;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.bean.ShoppingCart;
import cn.yangfuxing.lottery.bean.Ticket;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.ui.manager.MiddleManager;
import cn.yangfuxing.lottery.util.PromptManager;

public class Shopping extends BaseUI {
	
	private Button addOptional;// 添加自选
	private Button addRandom;// 添加机选

	private ListView shoppingList;// 用户选择信息列表

	private ImageButton shoppingListClear;// 清空购物车
	private TextView notice;// 提示信息
	private Button buy;// 购买

	private ShoppingAdatper adapter;

	public Shopping(Context context) {
	        super(context);
        }

	@Override
        public int getId() {
	        return ConstantValue.VIEW_SHOPPING;
        }

	@Override
        protected void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_shopping, null);

		addOptional = (Button) findViewById(R.id.ii_add_optional);
		addRandom = (Button) findViewById(R.id.ii_add_random);
		shoppingListClear = (ImageButton) findViewById(R.id.ii_shopping_list_clear);
		notice = (TextView) findViewById(R.id.ii_shopping_lottery_notice);
		buy = (Button) findViewById(R.id.ii_lottery_shopping_buy);
		shoppingList = (ListView) findViewById(R.id.ii_shopping_list);

		adapter = new ShoppingAdatper();
		shoppingList.setAdapter(adapter);
        }

	@Override
        protected void setListener() {
		addOptional.setOnClickListener(this);
		addRandom.setOnClickListener(this);
		shoppingListClear.setOnClickListener(this);
		buy.setOnClickListener(this);
	        
        }
	
	private void changeNotice()
	{
		Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
		Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();
		
		String noticeInfo  = context.getResources().getString(R.string.is_shopping_list_notice);
		noticeInfo = StringUtils.replaceEach(noticeInfo, new String[]{"NUM", "MONEY" }, new String[]{lotterynumber.toString(),lotteryvalue.toString()});
		
		notice.setText(Html.fromHtml(noticeInfo));
		
	}
	
	private class ShoppingAdatper extends BaseAdapter
	{

		@Override
                public int getCount() {
	                return ShoppingCart.getInstance().getTickets().size();
                }

		@Override
                public Object getItem(int position) {
	                return ShoppingCart.getInstance().getTickets().get(position);
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
				convertView = View.inflate(context, R.layout.il_shopping_row, null);
				
				holder.delete = (ImageButton) convertView.findViewById(R.id.ii_shopping_item_delete);
				holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
				holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
				holder.num = (TextView) convertView.findViewById(R.id.ii_shopping_item_money);
				
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			
			Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
			holder.redNum.setText(ticket.getRedNum());
			holder.blueNum.setText(ticket.getBlueNum());
			holder.num.setText(ticket.getNum() + " 注");
			
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ShoppingCart.getInstance().getTickets().remove(position);
					notifyDataSetChanged();
					//如果，layout没有涉及增减项，就不用调用notify
					changeNotice();
				}
			});
			
	                return convertView;
                }
		
		class ViewHolder
		{
			ImageButton delete;
			TextView redNum;
			TextView blueNum;
			TextView num;
		}
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.ii_add_optional:
			MiddleManager.getInstance().goBack();
			break;
		case R.id.ii_add_random:
			//添加机选
			addRandom();
			changeNotice();
			break;
		case R.id.ii_shopping_list_clear:
			ShoppingCart.getInstance().getTickets().clear();
			adapter.notifyDataSetChanged();
			changeNotice();
			break;
		case R.id.ii_lottery_shopping_buy:
			//判断：购物车中是否有投注
			if(ShoppingCart.getInstance().getTickets().size() >= 1)
			{
				//判断用户登录是否登录－被动登录
				if(GlobalParams.isLogin)
				{
					//判断用户的余额是否满足投注需求
					if(ShoppingCart.getInstance().getLotteryvalue() <= GlobalParams.MONEY)
					{
						//界面跳转：跳转到追期和倍投的设置界面
						MiddleManager.getInstance().changeUI(PreBet.class,bundle);
					}
					else
					{
						//充值
					}
				}
				else
				{
					//提示用户：登录去；　界面跳转：　用户登录界面
					PromptManager.showToast(context, "登录去");
					MiddleManager.getInstance().changeUI(UserLogin.class,bundle);
				}
			}
			else
			{
				//提示用户需要投注一注
				PromptManager.showToast(context, "需要选择一注");
			}
				
			break;

		default:
			break;
		}
	}

	private void addRandom() {
	        //机选一注　
		Random random = new Random();
		List<Integer> redNums = new ArrayList<Integer>();
		List<Integer> blueNums = new ArrayList<Integer>();
		
		//机选红球
		while(redNums.size()  <  6)
		{
			int  num  = random.nextInt(33) + 1;
			
			if(redNums.contains(num))
			{
				continue;
			}
			redNums.add(num);
		}
		//选篮球
		int  num = random.nextInt(16)+1;
		blueNums.add(num);
		
		//封装　Ticket
		Ticket ticket = new  Ticket();
		DecimalFormat decimalFormat = new DecimalFormat("00");
		StringBuffer redBuffer = new StringBuffer();
		for(Integer item : redNums)
		{
			redBuffer.append(" ").append(decimalFormat.format(item));
		}
		ticket.setRedNum(redBuffer.substring(1));
		
		StringBuffer blueBuffer = new StringBuffer();
		for(Integer item : blueNums)
		{
			blueBuffer.append(" ").append(decimalFormat.format(item));
		}
		
		ticket.setBlueNum(blueBuffer.substring(1));
		ticket.setNum(1);
		//添加到购物车
		ShoppingCart.getInstance().getTickets().add(ticket);
		//更新界面
		adapter.notifyDataSetChanged();
		
		
        }
	
	@Override
	public void onResume() {
		changeNotice();
	        super.onResume();
	}

}
