package cn.yangfuxing.lottery.bean;

import java.util.ArrayList;
import java.util.List;

import cn.yangfuxing.lottery.GlobalParams;

/**
 * 彩票购物车  单例模式　
 * @author flystar
 *
 */
public class ShoppingCart {
	private ShoppingCart(){}
	
	private static  ShoppingCart instance = new ShoppingCart();
	
	
	
	public static ShoppingCart getInstance() {
		return instance;
	}

	private Integer lotteryid;
	private String issue;
	private List<Ticket> tickets = new ArrayList<Ticket>();
	private Integer lotterynumber; //注数
	private Integer lotteryvalue;
	
	private Integer appnumbers = 1;//倍数
	private Integer issuesnumbers = 1;// 追期
	
	
	
	public Integer getAppnumbers() {
		return appnumbers;
	}
	public Integer getIssuesnumbers() {
		return issuesnumbers;
	}
	public Integer getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public List<Ticket> getTickets() {
		return tickets;
	}
	public Integer getLotterynumber() {
		lotterynumber = 0;
		for(Ticket item : tickets)
		{
			lotterynumber += item.getNum();
		}
		return lotterynumber;
	}
	public Integer getLotteryvalue() {
		lotteryvalue = 2*getLotterynumber()*appnumbers*issuesnumbers;
		return lotteryvalue;
	}
	
	/**
	 * 操作倍数　
	 */
	public boolean addAppnumbers(boolean isAdd)
	{
		if(isAdd)
		{
			appnumbers++;
			if(appnumbers > 99)
			{
				appnumbers--;
				return false;
			}
			
			if(getLotteryvalue() > GlobalParams.MONEY)
			{
				appnumbers--;
				return false;
			}
		}
		else
		{
			appnumbers--;
			if(appnumbers == 0)
			{
				appnumbers++;
				return false;
			}
		}
		return true;
	}
	

	/**
	 * 操作追期　
	 */
	public boolean addIssuesnumbers(boolean isAdd)
	{
		if(isAdd)
		{
			issuesnumbers++;
			if(issuesnumbers > 99)
			{
				issuesnumbers--;
				return false;
			}
			
			if(getLotteryvalue() > GlobalParams.MONEY)
			{
				issuesnumbers--;
				return false;
			}
		}
		else
		{
			issuesnumbers--;
			if(issuesnumbers == 0)
			{
				issuesnumbers++;
				return false;
			}
		}
		return true;
	}
	public void clear() {
	        tickets.clear();
	        lotterynumber = 0;
	        lotteryvalue = 0;
	        
	        appnumbers = 1;
	        issuesnumbers = 1;
        }
	
}
