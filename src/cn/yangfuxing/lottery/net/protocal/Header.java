package cn.yangfuxing.lottery.net.protocal;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import cn.yangfuxing.lottery.ConstantValue;

/**
 *  消息头
 * @author flystar
 *
 */

public class Header 
{
	private Leaf agenterid = new Leaf("agenterid",ConstantValue.AGENTERID);
	private Leaf source = new Leaf("source",ConstantValue.SOURCE);
	private Leaf compress = new Leaf("compress", ConstantValue.COMPERESS);
	
	
	private Leaf messengerid = new Leaf("messengerid");
	private Leaf timestamp = new Leaf("timestamp");
	private Leaf digest = new Leaf("digest");
	
	private Leaf transactiontype = new Leaf("transactiontype");
	private Leaf username = new Leaf("username");
	
	public void serializerHeader(XmlSerializer serializer, String bodyWhole)
	{
		//计算第二组叶子的值
		//时间戳
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		timestamp.tagvalue = format.format(new Date());
		//唯一标志
		Random random = new  Random();
		int num = random.nextInt(999999)+1;
		DecimalFormat decimalFormat = new DecimalFormat("000000");   //数字的格式化
		messengerid.tagvalue = timestamp.tagvalue+decimalFormat.format(num);
		
		//MD5
		String data = timestamp.tagvalue+ConstantValue.AGENTER_PASSWORD+bodyWhole;
		digest.tagvalue = DigestUtils.md5Hex(data);
		
		try {
			serializer.startTag(null, "header");
			
			agenterid.serializerLeaf(serializer);
			source.serializerLeaf(serializer);
			compress.serializerLeaf(serializer);
			messengerid.serializerLeaf(serializer);
			timestamp.serializerLeaf(serializer);
			digest.serializerLeaf(serializer);
			transactiontype.serializerLeaf(serializer);
			username.serializerLeaf(serializer);
			
			serializer.endTag(null, "header");
			
			
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}

	public Leaf getTransactiontype() {
		return transactiontype;
	}

	public Leaf getUsername() {
		return username;
	}

	public Leaf getTimestamp() {
		return timestamp;
	}

	public Leaf getDigest() {
		return digest;
	}
	
	

	
	
	
	
	
	
}
