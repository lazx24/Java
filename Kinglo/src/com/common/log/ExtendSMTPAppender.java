package com.common.log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.w3c.dom.Element;

public final class ExtendSMTPAppender extends AppenderSkeleton implements
	UnrecognizedElementHandler {
    private String to;
    private String cc;
    private String bcc;
    private String from;
    private String replyTo;
    private String subject;
    private String smtpHost;
    private String smtpUsername;
    private String smtpPassword;
    private String smtpProtocol;
    private int smtpPort = -1;
    private boolean smtpDebug = false;
    private int bufferSize = 512;
    private boolean locationInfo = false;
    private boolean sendOnClose = false;
    private boolean sslEnabled = false;
    protected CyclicBuffer cb = new CyclicBuffer(this.bufferSize);
    protected Message msg;
    protected TriggeringEventEvaluator evaluator;

    public ExtendSMTPAppender() {
	this(new DefaultEvaluator());
    }

    public ExtendSMTPAppender(TriggeringEventEvaluator evaluator) {
	this.evaluator = evaluator;
    }

    public void activateOptions() {
	Session session = createSession();
	this.msg = new MimeMessage(session);
	try {
	    addressMessage(this.msg);
	    if (this.subject != null) {
		try {
		    this.msg.setSubject(MimeUtility.encodeText(this.subject,
			    "UTF-8", null));
		} catch (UnsupportedEncodingException ex) {
		    LogLog.error("Unable to encode SMTP subject", ex);
		}
	    }
	    if (!(this.evaluator instanceof OptionHandler)) {
		return;
	    }
	} catch (MessagingException e) {
	    LogLog.error("Could not activate SMTPAppender options.", e);
	}
	((OptionHandler) this.evaluator).activateOptions();
    }

    protected void addressMessage(Message msg) throws MessagingException {
	if (this.from != null) {
	    msg.setFrom(getAddress(this.from));
	} else {
	    msg.setFrom();
	}
	if ((this.replyTo != null) && (this.replyTo.length() > 0)) {
	    msg.setReplyTo(parseAddress(this.replyTo));
	}
	if ((this.to != null) && (this.to.length() > 0)) {
	    msg.setRecipients(Message.RecipientType.TO, parseAddress(this.to));
	}
	if ((this.cc != null) && (this.cc.length() > 0)) {
	    msg.setRecipients(Message.RecipientType.CC, parseAddress(this.cc));
	}
	if ((this.bcc != null) && (this.bcc.length() > 0)) {
	    msg.setRecipients(Message.RecipientType.BCC, parseAddress(this.bcc));
	}
    }

    public void append(LoggingEvent event) {
	if (!checkEntryConditions()) {
	    return;
	}
	event.getThreadName();
	event.getNDC();
	event.getMDCCopy();
	if (this.locationInfo) {
	    event.getLocationInformation();
	}
	event.getRenderedMessage();
	event.getThrowableStrRep();
	this.cb.add(event);
	if (this.evaluator.isTriggeringEvent(event)) {
	    sendBuffer();
	}
    }

    protected boolean checkEntryConditions() {
	if (this.msg == null) {
	    this.errorHandler.error("Message object not configured.");
	    return false;
	}
	if (this.evaluator == null) {
	    this.errorHandler
		    .error("No TriggeringEventEvaluator is set for appender ["
			    + this.name + "].");
	    return false;
	}
	if (this.layout == null) {
	    this.errorHandler.error("No layout set for appender named ["
		    + this.name + "].");
	    return false;
	}
	return true;
    }

    public synchronized void close() {
	this.closed = true;
	if ((this.sendOnClose) && (this.cb.length() > 0)) {
	    sendBuffer();
	}
    }

    protected Session createSession() {
	Properties props = null;
	try {
	    props = new Properties(System.getProperties());
	} catch (SecurityException ex) {
	    props = new Properties();
	}
	String prefix = "mail.smtp";
	if (this.smtpProtocol != null) {
	    props.put("mail.transport.protocol", this.smtpProtocol);
	    prefix = "mail." + this.smtpProtocol;
	}
	if (this.smtpHost != null) {
	    props.put(prefix + ".host", this.smtpHost);
	}
	if (this.smtpPort > 0) {
	    props.put(prefix + ".port", String.valueOf(this.smtpPort));
	}
	Authenticator auth = null;
	if ((this.smtpPassword != null) && (this.smtpUsername != null)) {
	    props.put(prefix + ".auth", "true");
	    auth = new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(
			    ExtendSMTPAppender.this.smtpUsername,
			    ExtendSMTPAppender.this.smtpPassword);
		}
	    };
	}
	props.put("mail.smtp.starttls.enable", Boolean.valueOf(this.sslEnabled));
	if (this.sslEnabled) {
	    props.setProperty("mail.smtp.socketFactory.fallback", "false");
	    props.setProperty("mail.smtp.socketFactory.port",
		    String.valueOf(this.smtpPort));
	}
	Session session = Session.getInstance(props, auth);
	if (this.smtpProtocol != null) {
	    session.setProtocolForAddress("rfc822", this.smtpProtocol);
	}
	if (this.smtpDebug) {
	    session.setDebug(this.smtpDebug);
	}
	return session;
    }

    protected String formatBody() {
	StringBuffer sbuf = new StringBuffer();
	String t = this.layout.getHeader();
	if (t != null) {
	    sbuf.append(t);
	}
	int len = this.cb.length();
	for (int i = 0; i < len; i++) {
	    LoggingEvent event = this.cb.get();
	    sbuf.append(this.layout.format(event));
	    if (this.layout.ignoresThrowable()) {
		String[] s = event.getThrowableStrRep();
		if (s != null) {
		    for (int j = 0; j < s.length; j++) {
			sbuf.append(s[j]);
			sbuf.append(Layout.LINE_SEP);
		    }
		}
	    }
	}
	t = this.layout.getFooter();
	if (t != null) {
	    sbuf.append(t);
	}
	return sbuf.toString();
    }

    InternetAddress getAddress(String addressStr) {
	try {
	    return new InternetAddress(addressStr);
	} catch (AddressException e) {
	    this.errorHandler.error("Could not parse address [" + addressStr
		    + "].", e, 6);
	}
	return null;
    }

    public String getBcc() {
	return this.bcc;
    }

    public int getBufferSize() {
	return this.bufferSize;
    }

    public String getCc() {
	return this.cc;
    }

    public final TriggeringEventEvaluator getEvaluator() {
	return this.evaluator;
    }

    public String getEvaluatorClass() {
	return this.evaluator == null ? null : this.evaluator.getClass()
		.getName();
    }

    public String getFrom() {
	return this.from;
    }

    public boolean getLocationInfo() {
	return this.locationInfo;
    }

    public String getReplyTo() {
	return this.replyTo;
    }

    public final boolean getSendOnClose() {
	return this.sendOnClose;
    }

    public boolean getSMTPDebug() {
	return this.smtpDebug;
    }

    public String getSMTPHost() {
	return this.smtpHost;
    }

    public String getSMTPPassword() {
	return this.smtpPassword;
    }

    public final int getSMTPPort() {
	return this.smtpPort;
    }

    public final String getSMTPProtocol() {
	return this.smtpProtocol;
    }

    public String getSMTPUsername() {
	return this.smtpUsername;
    }

    public String getSubject() {
	return this.subject;
    }

    public String getTo() {
	return this.to;
    }

    public boolean isSslEnabled() {
	return this.sslEnabled;
    }

    InternetAddress[] parseAddress(String addressStr) {
	try {
	    return InternetAddress.parse(addressStr, true);
	} catch (AddressException e) {
	    this.errorHandler.error("Could not parse address [" + addressStr
		    + "].", e, 6);
	}
	return null;
    }

    public boolean parseUnrecognizedElement(Element element, Properties props)
	    throws Exception {
	if ("triggeringPolicy".equals(element.getNodeName())) {
	    Object triggerPolicy = DOMConfigurator.parseElement(element, props,
		    TriggeringEventEvaluator.class);
	    if ((triggerPolicy instanceof TriggeringEventEvaluator)) {
		setEvaluator((TriggeringEventEvaluator) triggerPolicy);
	    }
	    return true;
	}
	return false;
    }

    public boolean requiresLayout() {
	return true;
    }

    protected void sendBuffer() {
	try {
	    String s = formatBody();
	    boolean allAscii = true;
	    for (int i = 0; (i < s.length()) && (allAscii); i++) {
		allAscii = s.charAt(i) <= '';
	    }
	    MimeBodyPart part;
	    if (allAscii) {
		part = new MimeBodyPart();
		part.setContent(s, this.layout.getContentType());
	    } else {
		try {
		    ByteArrayOutputStream os = new ByteArrayOutputStream();
		    Writer writer = new OutputStreamWriter(MimeUtility.encode(
			    os, "quoted-printable"), "UTF-8");
		    writer.write(s);
		    writer.close();
		    InternetHeaders headers = new InternetHeaders();
		    headers.setHeader("Content-Type",
			    this.layout.getContentType() + "; charset=UTF-8");
		    headers.setHeader("Content-Transfer-Encoding",
			    "quoted-printable");
		    part = new MimeBodyPart(headers, os.toByteArray());
		} catch (Exception ex) {
		    StringBuffer sbuf = new StringBuffer(s);
		    for (int i = 0; i < sbuf.length(); i++) {
			if (sbuf.charAt(i) >= '') {
			    sbuf.setCharAt(i, '?');
			}
		    }
		    part = new MimeBodyPart();
		    part.setContent(sbuf.toString(),
			    this.layout.getContentType());
		}
	    }
	    Multipart mp = new MimeMultipart();
	    mp.addBodyPart(part);
	    this.msg.setContent(mp);

	    this.msg.setSentDate(new Date());
	    Transport.send(this.msg);
	} catch (MessagingException e) {
	    LogLog.error("Error occured while sending e-mail notification.", e);
	} catch (RuntimeException e) {
	    LogLog.error("Error occured while sending e-mail notification.", e);
	}
    }

    public void setBcc(String addresses) {
	this.bcc = addresses;
    }

    public void setBufferSize(int bufferSize) {
	this.bufferSize = bufferSize;
	this.cb.resize(bufferSize);
    }

    public void setCc(String addresses) {
	this.cc = addresses;
    }

    public final void setEvaluator(TriggeringEventEvaluator trigger) {
	if (trigger == null) {
	    throw new NullPointerException("trigger");
	}
	this.evaluator = trigger;
    }

    public void setEvaluatorClass(String value) {
	this.evaluator = ((TriggeringEventEvaluator) OptionConverter
		.instantiateByClassName(value, TriggeringEventEvaluator.class,
			this.evaluator));
    }

    public void setFrom(String from) {
	this.from = from;
    }

    public void setLocationInfo(boolean locationInfo) {
	this.locationInfo = locationInfo;
    }

    public void setReplyTo(String addresses) {
	this.replyTo = addresses;
    }

    public final void setSendOnClose(boolean val) {
	this.sendOnClose = val;
    }

    public void setSMTPDebug(boolean debug) {
	this.smtpDebug = debug;
    }

    public void setSMTPHost(String smtpHost) {
	this.smtpHost = smtpHost;
    }

    public void setSMTPPassword(String password) {
	this.smtpPassword = password;
    }

    public final void setSMTPPort(int val) {
	this.smtpPort = val;
    }

    public final void setSMTPProtocol(String val) {
	this.smtpProtocol = val;
    }

    public void setSMTPUsername(String username) {
	this.smtpUsername = username;
    }

    public void setSslEnabled(boolean sslEnabled) {
	this.sslEnabled = sslEnabled;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public void setTo(String to) {
	this.to = to;
    }
}
