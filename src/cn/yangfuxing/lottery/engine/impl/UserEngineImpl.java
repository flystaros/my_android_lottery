package cn.yangfuxing.lottery.engine.impl;

import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.bean.ShoppingCart;
import cn.yangfuxing.lottery.bean.Ticket;
import cn.yangfuxing.lottery.bean.User;
import cn.yangfuxing.lottery.engine.BaseEngine;
import cn.yangfuxing.lottery.engine.UserEngine;
import cn.yangfuxing.lottery.net.HttpUtils;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.element.BalanceElement;
import cn.yangfuxing.lottery.net.protocal.element.BetElement;
import cn.yangfuxing.lottery.net.protocal.element.UserLoginElement;
import cn.yangfuxing.lottery.util.DES;

public class UserEngineImpl  extends BaseEngine implements UserEngine
{
	
	public Message login(User user)
	{
		UserLoginElement element = new UserLoginElement();
		element.getActpassword().tagvalue = user.password;
		
		Message message = new Message();
		message.getHeader().getUsername().tagvalue = user.username;
		String xml = message.getXML(element);
		Message result = super.getResult(xml);
		if(result != null)
		{
			DES des = new DES();
			String wholeBody = "<body>"+des.authcode(result.getBody().serviceBodyInsideDes, "ENCODE", ConstantValue.DES_PASSWORD)+"</body>";  //解密数据
			//解析返回的Body
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new StringReader(wholeBody));
				int eventType = parser.getEventType();
				String name = "";
				while(eventType != XmlPullParser.END_DOCUMENT)
				{
					switch (eventType)
					{
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if("errorcode".equals(name))
						{
							result.getBody().oelement.errorcode = parser.nextText();
						}
						if("errormsg".equals(name))
						{
							result.getBody().oelement.errormsg = parser.nextText();
						}
						break;

					default:
						break;
					}
					eventType = parser.next();
				}
				return result;
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Message login1(User user)
	{
		UserLoginElement element = new UserLoginElement();
		element.getActpassword().tagvalue = user.password;
		
		Message message = new Message();
		message.getHeader().getUsername().tagvalue = user.username;
		String xml = message.getXML(element);
		HttpUtils util = new HttpUtils();
		InputStream is = util.sendXML(ConstantValue.LOTTERY_URI, xml);
		if(is != null)
		{
			Message result = new Message();
			//解析返回的XML数据
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(is, ConstantValue.ENCODING);
				int eventtype = parser.getEventType();
				String name = "";
				while(eventtype != XmlPullParser.END_DOCUMENT)
				{
					switch (eventtype) 
					{
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if("timestamp".equals(name))
						{
							result.getHeader().getTimestamp().tagvalue = parser.nextText();
						}
						if("body".equals(name))
						{
							result.getBody().serviceBodyInsideDes = parser.nextText();
						}
						if("digest".equals(name))
						{
							result.getHeader().getDigest().tagvalue = parser.nextText();
						}
						break;
					default:
						break;
					}
					eventtype = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//把body数据拼接　
			DES des = new DES();
			String wholeBody = "<body>"+des.authcode(result.getBody().serviceBodyInsideDes, "ENCODE", ConstantValue.DES_PASSWORD)+"</body>";  //解密数据
			//拼接ＭＤ５的原始数据
			String md5Data = result.getHeader().getTimestamp().tagvalue + ConstantValue.AGENTER_PASSWORD+wholeBody;
			String md5Mobile = DigestUtils.md5Hex(md5Data);
			if(md5Mobile.equals(result.getHeader().getDigest().tagvalue))
			{
				//解析返回的Body
				parser = Xml.newPullParser();
				try {
					parser.setInput(new StringReader(wholeBody));
					int eventType = parser.getEventType();
					String name = "";
					while(eventType != XmlPullParser.END_DOCUMENT)
					{
						switch (eventType)
						{
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if("errorcode".equals(name))
							{
								result.getBody().oelement.errorcode = parser.nextText();
							}
							if("errormsg".equals(name))
							{
								result.getBody().oelement.errormsg = parser.nextText();
							}
							break;
						default:
							break;
						}
						eventType = parser.next();
					}
					return result;
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
        public Message balance(User user) {
	        // TODO Auto-generated method stub
	        return null;
        }

	@Override
        public Message getBalance(User user) {
		BalanceElement element = new BalanceElement();
		Message message = new  Message();
		message.getHeader().getUsername().setTagvalue(user.getUsername());
		String xml = message.getXML(element);
		
		Message result = super.getResult(xml);
		
		if(result != null)
		{
			DES des = new DES();
			String wholeBody = "<body>"+des.authcode(result.getBody().serviceBodyInsideDes, "ENCODE", ConstantValue.DES_PASSWORD)+"</body>";  //解密数据
			//解析返回的Body
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new StringReader(wholeBody));
				int eventType = parser.getEventType();
				String name = "";
				BalanceElement resultElement = null;
				while(eventType != XmlPullParser.END_DOCUMENT)
				{
					switch (eventType)
					{
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if("errorcode".equals(name))
						{
							result.getBody().oelement.errorcode = parser.nextText();
						}
						if("errormsg".equals(name))
						{
							result.getBody().oelement.errormsg = parser.nextText();
						}
						if("element".equals(name))
						{
							resultElement = new BalanceElement();
							result.getBody().getElements().add(resultElement);
						}
						if("investvalues".equals(name))
						{
							if(resultElement != null)
							{
								resultElement.setInvestvalues(parser.nextText());
							}
						}
						
						break;

					default:
						break;
					}
					eventType = parser.next();
				}
				return result;
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	        return null;
        }

	@Override
	public Message bet(User user) {
		BetElement element = new BetElement();
		element.getLotteryid().setTagvalue(ShoppingCart.getInstance().getLotteryid().toString());

		// 彩票的业务里面：
		// ①关于注数的计算
		// ②关于投注信息封装（用户投注号码）

		// 010203040506|01^01020304050607|01

		StringBuffer codeBuffer = new StringBuffer();
		for (Ticket item : ShoppingCart.getInstance().getTickets()) {
			codeBuffer.append("^").append(item.getRedNum().replaceAll(" ", "")).append("|").append(item.getBlueNum().replaceAll(" ", ""));
		}

		element.getLotterycode().setTagvalue(codeBuffer.substring(1));

		element.getIssue().setTagvalue(ShoppingCart.getInstance().getIssue());
		element.getLotteryvalue().setTagvalue((ShoppingCart.getInstance().getLotteryvalue() * 100) + "");

		element.getLotterynumber().setTagvalue(ShoppingCart.getInstance().getLotterynumber().toString());
		element.getAppnumbers().setTagvalue(ShoppingCart.getInstance().getAppnumbers().toString());
		element.getIssuesnumbers().setTagvalue(ShoppingCart.getInstance().getIssuesnumbers().toString());

		element.getIssueflag().setTagvalue(ShoppingCart.getInstance().getIssuesnumbers() > 1 ? "1" : "0");

		Message message = new Message();
		message.getHeader().getUsername().setTagvalue(user.getUsername());

		String xml = message.getXML(element);

		Message result = super.getResult(xml);

		if (result != null) {

			// 第四步：请求结果的数据处理
			// body部分的第二次解析，解析的是明文内容

			XmlPullParser parser = Xml.newPullParser();
			try {

				DES des = new DES();
				String body = "<body>" + des.authcode(result.getBody().getBodyInsideDes(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

				parser.setInput(new StringReader(body));

				int eventType = parser.getEventType();
				String name;

				BetElement resultElement = null;

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if ("errorcode".equals(name)) {
							result.getBody().getOelement().setErrorcode(parser.nextText());
						}
						if ("errormsg".equals(name)) {
							result.getBody().getOelement().setErrormsg(parser.nextText());
						}

						// 正对于当前请求
						if ("element".equals(name)) {
							resultElement = new BetElement();
							result.getBody().getElements().add(resultElement);
						}

						if ("actvalue".equals(name)) {
							if (resultElement != null) {
								resultElement.setActvalue(parser.nextText());
							}
						}

						break;
					}
					eventType = parser.next();
				}

				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}
}
