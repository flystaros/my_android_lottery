package cn.yangfuxing.lottery.bean;
/**
 *用户投注信息的封装
 * @author flystar
 *
 */
public class Ticket 
{
	private String redNum;
	private String blueNum;
	
	private  int num;// 注数

	public String getRedNum() {
		return redNum;
	}

	public void setRedNum(String redNum) {
		this.redNum = redNum;
	}

	public String getBlueNum() {
		return blueNum;
	}

	public void setBlueNum(String blueNum) {
		this.blueNum = blueNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
}
