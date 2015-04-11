package com.coscon.shipsuite.common.util.email;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.util.string.StringUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public final class EMailSender {
    private static Map<String, EMailSender> instanceCache = new HashMap<String,EMailSender>();

    private static InternetAddress[] buildRecipientInternetAddress(
	    String[] recipientArray) {
	if ((recipientArray == null) || (recipientArray.length == 0)) {
	    return new InternetAddress[0];
	}
	InternetAddress[] addressArray = new InternetAddress[recipientArray.length];
	for (int i = 0; i < recipientArray.length; i++) {
	    try {
		addressArray[i] = new InternetAddress(recipientArray[i]);
	    } catch (AddressException e) {
		throw new ShipSuiteRuntimeException(e);
	    }
	}
	return addressArray;
    }

    public static EMailSender getInstance(String host, String port,
	    boolean sslEnabled, boolean senderEnabled, String sender,
	    boolean isMailAuth, String senderPassword, String[] receiverCc,
	    String[] receiverBcc, String mailDeclare) {
	String cacheKey = host + ":" + port + ":" + sslEnabled + ":"
		+ senderEnabled + ":" + sender + ":" + isMailAuth + ":"
		+ StringUtil.isNull(senderPassword, "") + ":"
		+ StringUtil.isNull(StringUtil.toString(receiverCc), "") + ":"
		+ StringUtil.isNull(StringUtil.toString(receiverBcc), "") + ":"
		+ StringUtil.isNull(mailDeclare, "");
	EMailSender instance = null;
	synchronized (instanceCache) {
	    instance = (EMailSender) instanceCache.get(cacheKey);
	    if (instance == null) {
		instance = new EMailSender();

		instance.SMTP_HOST = host;
		instance.SMTP_PORT = port;
		instance.MAIL_SENDER = sender;

		instance.SSL_ENABLED = sslEnabled;

		instance.MAIL_RECEIVER_CC = receiverCc;
		instance.MAIL_RECEIVER_BCC = receiverBcc;

		instance.MAIL_SENDER_ENABLED = senderEnabled;

		instance.MAIL_AUTH = isMailAuth;

		instance.MAIL_SENDER_PASSWORD = senderPassword;

		instance.MAIL_DECLARE = mailDeclare;

		instanceCache.put(cacheKey, instance);
	    }
	}
	return instance;
    }

    public static EMailSender getInstance(String host, String port,
	    boolean sslEnabled, boolean senderEnabled, String sender,
	    String senderPassword) {
	return getInstance(host, port, sslEnabled, senderEnabled, sender, true,
		senderPassword, null, null, null);
    }

    public static EMailSender getInstance(String host, String port,
	    String sender, String senderPassword) {
	return getInstance(host, port, false, false, sender, true,
		senderPassword, null, null, null);
    }

    public static String[] getRecipients(String recipients) {
	if (StringUtil.isNullOrEmpty(recipients)) {
	    return new String[0];
	}
	String[] recipientArray = recipients.trim().split(",");
	if ((recipientArray.length == 1) && (recipientArray[0].contains(";"))) {
	    recipientArray = recipients.trim().split(";");
	}
	return recipientArray;
    }

    private int MAX_RETRY_TIMES = 10;
    private String SMTP_HOST = "";
    private String SMTP_PORT = "";
    private String MAIL_SENDER = "";
    private boolean SSL_ENABLED = false;
    private boolean MAIL_AUTH = false;
    private boolean MAIL_SENDER_ENABLED = false;
    private String MAIL_SENDER_PASSWORD = "";
    private String[] MAIL_RECEIVER_CC = new String[0];
    private String[] MAIL_RECEIVER_BCC = new String[0];
    private String[] SYMBOL_SPLIT = { ",", ";", " " };
    private String MAIL_DECLARE;

    private String buildHtmlBody(String htmlBody) {
	StringBuilder sb = new StringBuilder();
	sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");

	sb.append("<html>");
	sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
	sb.append("<body>");
	sb.append(htmlBody);
	sb.append("</body>");
	sb.append("</html>");
	if (StringUtil.isNotNullAndNotEmptyWithTrim(this.MAIL_DECLARE)) {
	    sb.append(this.MAIL_DECLARE);
	}
	return sb.toString();
    }

    private Set<String> parseEmailAddress(String[] emailAddresses,
	    String[] symbols) {
	String SYMBOL = ";";
	Set<String> addressSet = new HashSet<String>();
	if ((emailAddresses == null) || (emailAddresses.length == 0)) {
	    return addressSet;
	}
	for (String addressToSplit : emailAddresses) {
	    if (addressToSplit != null) {
		for (String s : symbols) {
		    if (!s.equals(SYMBOL)) {
			addressToSplit = addressToSplit.trim().replaceAll(s,
				SYMBOL);
		    }
		}
		String[] addressArray = addressToSplit.split(SYMBOL);
		for (String address : addressArray) {
		    if (StringUtil.isNotNullAndNotEmptyWithTrim(address)) {
			addressSet.add(address);
		    }
		}
	    }
	}
	return addressSet;
    }

    public void sendMail(String sender, String senderPassword,
	    String[] recipientTO, String subject, String content)
	    throws MessagingException, AddressException {
	sendMail(sender, senderPassword, recipientTO, null, null, subject,
		content, null);
    }

    public void sendMail(String sender, String senderPassword,
	    String[] recipientTO, String subject, String content,
	    File[] attachment) throws MessagingException, AddressException {
	sendMail(sender, senderPassword, recipientTO, null, null, subject,
		content, attachment);
    }

    public void sendMail(final String sender, final String senderPassword,
	    String[] recipientTO, String[] recipientCC, String[] recipientBCC,
	    String subject, String content, File[] attachment)
	    throws MessagingException, AddressException {
	if (((recipientTO == null) || (recipientTO.length == 0))
		&& ((recipientCC == null) || (recipientCC.length == 0))
		&& ((recipientBCC == null) || (recipientBCC.length == 0))) {
	    throw new MessagingException("Recipient cannot be blank.");
	}
	Set<String> receiverSet = null;
	if (recipientTO != null) {
	    receiverSet = parseEmailAddress(recipientTO, this.SYMBOL_SPLIT);
	}
	Properties systemProperties = System.getProperties();
	systemProperties.put("mail.smtp.host", this.SMTP_HOST);
	systemProperties.put("mail.smtp.port", this.SMTP_PORT);

	systemProperties.put("mail.smtp.starttls.enable",
		Boolean.valueOf(this.SSL_ENABLED));
	if (this.SSL_ENABLED) {
	    systemProperties.setProperty("mail.smtp.socketFactory.fallback",
		    "false");
	    systemProperties.setProperty("mail.smtp.socketFactory.port",
		    this.SMTP_PORT);
	}
	systemProperties.put("mail.smtp.auth", Boolean.valueOf(this.MAIL_AUTH));
	Session xSession;
	if (!this.MAIL_AUTH) {
	    xSession = Session.getInstance(systemProperties, null);
	} else {
	    xSession = Session.getInstance(systemProperties,
		    new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
			    if (StringUtil.isNullOrEmpty(sender)) {
				return new PasswordAuthentication(
					EMailSender.this.MAIL_SENDER,
					EMailSender.this.MAIL_SENDER_PASSWORD);
			    }
			    return new PasswordAuthentication(sender,
				    senderPassword);
			}
		    });
	}
	MimeMessage xMsg = new MimeMessage(xSession);

	String from = this.MAIL_SENDER;
	if ((this.MAIL_SENDER_ENABLED) && (!StringUtil.isNullOrEmpty(sender))) {
	    from = sender;
	}
	xMsg.setFrom(new InternetAddress(from));
	if ((receiverSet != null) && (!receiverSet.isEmpty())) {
	    xMsg.setRecipients(Message.RecipientType.TO,
		    buildRecipientInternetAddress((String[]) receiverSet
			    .toArray(new String[receiverSet.size()])));
	}
	if ((recipientCC != null) && (recipientCC.length > 0)) {
	    Set<String> receiverCCSet = parseEmailAddress(recipientCC,
		    this.SYMBOL_SPLIT);
	    if (receiverCCSet.size() > 0) {
		xMsg.setRecipients(Message.RecipientType.CC,
			buildRecipientInternetAddress((String[]) receiverCCSet
				.toArray(new String[receiverCCSet.size()])));
	    }
	} else if ((this.MAIL_RECEIVER_CC != null)
		&& (this.MAIL_RECEIVER_CC.length > 0)) {
	    xMsg.setRecipients(Message.RecipientType.CC,
		    buildRecipientInternetAddress(this.MAIL_RECEIVER_CC));
	}
	if ((recipientBCC != null) && (recipientBCC.length > 0)) {
	    Set<String> receiverBCCSet = parseEmailAddress(recipientBCC,
		    this.SYMBOL_SPLIT);
	    if (receiverBCCSet.size() > 0) {
		xMsg.setRecipients(Message.RecipientType.BCC,
			buildRecipientInternetAddress((String[]) receiverBCCSet
				.toArray(new String[receiverBCCSet.size()])));
	    }
	} else if ((this.MAIL_RECEIVER_BCC != null)
		&& (this.MAIL_RECEIVER_BCC.length > 0)) {
	    xMsg.setRecipients(Message.RecipientType.BCC,
		    buildRecipientInternetAddress(this.MAIL_RECEIVER_BCC));
	}
	if (subject != null) {
	    try {
		xMsg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"),
			"UTF-8");
	    } catch (UnsupportedEncodingException e) {
		throw new ShipSuiteRuntimeException(e);
	    }
	}
	xMsg.setSentDate(new Date());

	Multipart xMPart = new MimeMultipart();
	if (content != null) {
	    MimeBodyPart xPart1 = new MimeBodyPart();

	    xPart1.setContent(buildHtmlBody(content + "\r\n\r\n "),
		    "text/html; charset=UTF-8");
	    xMPart.addBodyPart(xPart1);
	}
	if (attachment != null) {
	    for (int i = 0; i < attachment.length; i++) {
		MimeBodyPart xPart2 = new MimeBodyPart();
		xPart2.setDataHandler(new DataHandler(new FileDataSource(
			attachment[i])));
		xPart2.setFileName(attachment[i].getName());
		xMPart.addBodyPart(xPart2);
	    }
	}
	xMsg.setContent(xMPart);

	int hasRetried = 0;
	boolean hasSent = false;
	while ((!hasSent) && (hasRetried++ < this.MAX_RETRY_TIMES)) {
	    try {
		Transport.send(xMsg);
		hasSent = true;
	    } catch (RuntimeException ex) {
		if (hasRetried >= this.MAX_RETRY_TIMES) {
		    throw ex;
		}
		try {
		    Thread.sleep(500L);
		} catch (InterruptedException localInterruptedException) {
		}
	    }
	}
	xMPart = null;
	xMsg = null;
	systemProperties = null;
    }

    public void sendMail(String[] recipientTO, String subject, String content)
	    throws MessagingException, AddressException {
	sendMail(null, null, recipientTO, null, null, subject, content, null);
    }

    public void sendMail(String[] recipientTO, String subject, String content,
	    File[] attachment) throws MessagingException, AddressException {
	sendMail(null, null, recipientTO, null, null, subject, content,
		attachment);
    }

    public void sendMail(String[] recipientTO, String[] recipientCC,
	    String[] recipientBCC, String subject, String content,
	    File[] attachment) throws MessagingException, AddressException {
	sendMail(null, null, recipientTO, recipientCC, recipientBCC, subject,
		content, attachment);
    }
}
