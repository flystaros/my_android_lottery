package cn.yangfuxing.lottery.test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.net.NetUtils;
import cn.yangfuxing.lottery.net.protocal.Element;
import cn.yangfuxing.lottery.net.protocal.Message;
import cn.yangfuxing.lottery.net.protocal.element.CurrentIssueElement;
import android.test.AndroidTestCase;
import android.util.Log;
import android.util.Xml;

public class XmlTest extends AndroidTestCase 
{
	public void testCreateXml()
	{
		
		/*Element element = new Element();
		element.getLotteryid().tagvalue = "118";*/
		
		CurrentIssueElement element = new CurrentIssueElement();
		element.getLotteryid().tagvalue = "118";
		Message message = new Message();
		message.getXML(element);   //获取某个请求的XML文件　
		
		Log.i("FLY",message.getXML(element));

	}
	public void testNet()
	{
		NetUtils.checkNet(getContext());
	}	
}
