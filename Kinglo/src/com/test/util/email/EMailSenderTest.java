package com.test.util.email;

import com.coscon.shipsuite.common.util.email.EMailBody;
import com.coscon.shipsuite.common.util.email.EMailColor;
import com.coscon.shipsuite.common.util.email.EMailField;
import com.coscon.shipsuite.common.util.email.EMailSender;
import com.coscon.shipsuite.common.util.hardware.SystemUtil;

import java.io.File;
import java.util.LinkedHashMap;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EMailSenderTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void sendMail() {
	String[] xRecipient = { "temp_zoujh@coscon.com" };
	String xSubject = "Test Mail中文内容";

	StringBuilder sb = new StringBuilder();

	sb.append("<form>");
	sb.append("<table align=\"right\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" height=\"100%\" border=\"0\">");
	sb.append("<tr> ");
	sb.append("<td align=\"center\" width=\"100%\"><strong><font color=\"blue\" size=\"10\">System Admin_中文名字, 欢迎您进入portfolio系统. </font></strong></td>");

	sb.append("</tr>");

	sb.append("<tr>");
	sb.append("<td align=\"center\" valign=\"top\"><font color=\"red\" size=\"6\"><strong>");

	sb.append("中文内容");

	sb.append("</strong></font>");
	sb.append("</td>");
	sb.append("</tr>");
	sb.append("</table>");
	sb.append("</form>");

	String xContent = sb.toString();

	File[] xAttachment = { new File("D:/test.txt") };
	try {
	    EMailSender.getInstance("172.32.255.35", "25", "simp", "abcd,123")
		    .sendMail(xRecipient, null, null, xSubject, xContent,
			    xAttachment);
	} catch (AddressException e) {
	    e.printStackTrace();
	} catch (MessagingException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void sendMailWithMailBody() {
	String[] recipient = { "duyj@coscon.com" };
	String subject = "Test Mail中文内容";

	EMailField labelField = new EMailField("   Request Type:",
		EMailColor.red, Integer.valueOf(4));
	EMailField textField = new EMailField("Issue", EMailColor.blue,
		Integer.valueOf(4));

	EMailField labelField2 = new EMailField("  Request No.:",
		EMailColor.red, Integer.valueOf(4));
	EMailField textField2 = new EMailField("11111199", EMailColor.blue,
		Integer.valueOf(4));
	LinkedHashMap<EMailField, EMailField> contentMap = new LinkedHashMap();
	contentMap.put(labelField, textField);
	contentMap.put(labelField2, textField2);

	EMailField prefixField = new EMailField("尊敬的客户：<br>您好, 您的订单信息如下：");
	EMailField suffixField = new EMailField(
		"<br> <p>这是系统自动发出来的邮件，请不要回复，谢谢！</p>");
	EMailBody body = new EMailBody();
	body.setPrefixField(prefixField);
	body.setContentTableMap(contentMap);
	body.setSuffixField(suffixField);

	File[] attachment = { new File("c:/suite.log") };
	try {
	    EMailSender.getInstance("172.32.255.35", "25", "simp", "abcd,123")
		    .sendMail(recipient, new String[] { "duyj@coscon.com" },
			    null, subject, body.toHtml(), attachment);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    @Test
    public void checkSystemStatus(){
	SystemUtil.checkSystemStatus();
    }
}
