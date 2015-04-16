package com.common.context;

import com.common.enums.SecurityType;
import com.common.util.security.SecurityUtil;
import com.common.util.string.StringUtil;

import java.util.Locale;

public final class CommonConstant {
    public static int CACHE_DURATION_MINUTES = 10;
    public static final String SPLIT_SYMBOL = ";";
    public static final String SYSTEM_USER_ID = "system";

    public static final class DeployConst {
	public static final String DEPLOY_PROPERTY_FILENAME = "properties/deploy.properties";
	public static final String DEPLOY_ENV_KEY = "deploy.env";
	public static final String DEPLOY_VERSION_KEY = "deploy.version";
	public static final String DEPLOY_TIME_KEY = "deploy.time";
	public static final String DEPLOY_DEBUG_KEY = "deploy.debug";
	public static volatile String FEATURES_FILE_NAME = "features.%s.%s.htm";
    }

    public static final class EmailConfig {
	public static final String SMTP_HOST_KEY = "SMTP_HOST";
	public static final String SMTP_PORT_KEY = "SMTP_PORT";
	public static final String SSL_ENABLED_KEY = "SSL_ENABLED";
	public static final String MAIL_SENDER_KEY = "MAIL_SENDER";
	public static final String MAIL_SENDER_ENABLED_KEY = "MAIL_SENDER_ENABLED";
	public static final String MAIL_AUTH_KEY = "MAIL_AUTH";
	public static final String MAIL_SENDER_PASSWORD_KEY = "MAIL_SENDER_PASSWORD";
	public static final String MAIL_RECEIVER_CC_KEY = "MAIL_RECEIVER_CC";
	public static final String MAIL_RECEIVER_BCC_KEY = "MAIL_RECEIVER_BCC";
	public static final String MAIL_QUEUE_INTERVAL_SECONDS_KEY = "MAIL_QUEUE_INTERVAL_SECONDS";
	public static final String MAIL_SEND_MAX_RETRY_TIMES = "MAIL_SEND_MAX_RETRY_TIMES";
	public static final String MAIL_DECLARE_KEY = "MAIL_DECLARE";

	public static String getPropetyFile() {
	    return "config/mail.properties";
	}
    }

    public static final class FaxConfig {
	public static final String HOST_KEY = "fax.host";
	public static final String PORT_KEY = "fax.port";
	public static final String USER_KEY = "fax.user";
	public static final String PASSWORD_KEY = "fax.password";
	public static final String MAX_DIALS_KEY = "fax.maxDials";
	public static final String KILL_TIME_KEY = "fax.killtime";
	public static final String TRIES_ATTEMPTED_KEY = "fax.triesAttempted";
	public static final String LOCAL_COUNTRY_KEY = "fax.local.country";
	public static final String LOCAL_COUNTRY_PREFIX_KEY = "fax.local.country.prefix";
	public static final String LOCAL_AREA_KEY = "fax.local.area";
	public static final String LOCAL_AREA_PREFIX_KEY = "fax.local.area.prefix";

	public static String getPropetyFile(String deployEnv) {
	    return StringUtil.isNullOrEmpty(deployEnv) ? "properties/env/fax.properties"
		    : String.format("properties/env/fax.%s.properties",
			    new Object[] { deployEnv });
	}
    }

    public static final class SuiteConfig {
	public static final String ENV_PROD_KEY = "env.prod";
	public static final String SESSION_TIMEOUT_KEY = "session.timeout";

	public static String getPropetyFile(String deployEnv) {
	    return StringUtil.isNullOrEmpty(deployEnv) ? "properties/env/suite.properties"
		    : String.format("properties/env/suite.%s.properties",
			    new Object[] { deployEnv });
	}
    }

    public static final class LogConst {
	public static final String AUDITTRAIL = "AuditTrail";
	public static final String DEBUG = "DEBUG";
	public static final String LOG_LEVEL_DEBUG = " D ";
	public static final String LOG_LEVEL_ERROR = " E ";
	public static final String LOG_LEVEL_FATAL = " F ";
	public static final String LOG_LEVEL_INFO = " I ";
	public static final String LOG_LEVEL_WARN = " W ";
	public static final String LOG_LEVEL_TRACE = " T ";
	public static final String LOG_LEVEL_ALARM = " A ";
	public static final String LOG_PROP_FILE_NAME = String.format(
		"properties/messageResource_%s.properties",
		new Object[] { Locale.US.toString() });
	public static final String LOGLEVEL = "LOGLEVEL";
	public static final String OPERLOG = "OperationTrail";
    }

    public static final class SessionConstant {
	public static final String SESSION_SERVICE_CALL_UUID = "ServiceCallUuid";
	public static final String SESSION_TOKEN = "Token";
	public static final String SESSION_COOKIE = "Cookie";
	public static final String SESSION_SIGNATURE = "Signature";
	public static final String SESSION_USERID = "UserId";
	public static final String SESSION_TIMESTAMP = "Timestamp";
	public static final String SESSION_HOSTID = "HostId";
	public static final String SESSION_SERVICE_NAMESPACE = "ServiceNamespace";
	public static volatile int SESSION_TIME_OUT_MINIUTES = 120;
    }

    public static final String SYSTEM_USER_PASSWORD = SecurityUtil.sign(
	    "System~098", SecurityType.MD5);
    public static final String ANONYMOUS_USER_ID = "anonymous";
    public static final String EB_SYSTEM_USER_ID = "ebsystem";
    public static final long POOLED_KEY_INIT_VALUE = 1L;
    public static volatile int DEFAULT_ROW_MAX = 5000;
    public static volatile int DEFAULT_PAGE_SIZE = 100;
    public static volatile int TABLE_PK_LENGTH = 32;
    public static final String COMET_RESPONSE_SEPARATOR = "<!--COMET_SEPARATOR-Version 1.0-->";

    public static final class ConfigurationConst {
	public static final String SUBSYSTEM_CONFIG = "configurations/subsystem.xml";
	public static final String FUNCTION_CONFIG = "configurations/function_%s.xml";
    }

    public static final class DataOrderType {
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
    }

    public static final class DateFormat {
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
	public static final String COMPLEX_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    public static final class ExcelGeneratorConst {
	public static final String DEFAULT_HEADER_STRING = "";
	public static final String DEFAULT_SHEET_NAME = "Sheet1";
	public static final short HEADER_FONT_SIZE = 10;
	public static final String DATE_FORMAT = "m/d/yy h:mm";
	public static final String BUFFER_FILENAME_KEY = "ExcelBufferFile";
	public static final int BUFFER_FILENAME_DIGIT = 9;
    }

    public static final class Expression {
	public static final String AND = "AND";
	public static final String OR = "OR";
    }

    public static final class PropertyConst {
	public static final String APPLICATION_RESOURCE_PROPERTIES = "properties/messageResource_%s.properties";
	public static final String FRAMEWORK_RESOURCE_PROPERTIES = "properties/messageFrm_%s.properties";
    }

    public static final class SchedulerConst {
	public static final String DATA_EXTRACTION_DATAMAP_SCHEDULERNUMBER = "SchedulerNumber";
	public static final String SCHEDULER_JOB_GROUP = "AppJob";
	public static final String SCHEDULER_TRIGGER_GROUP = "AppTrigger";
    }

    public static final class WeekDay {
	public static final int FRIDAY = 6;
	public static final int MONDAY = 2;
	public static final int SATURDAY = 7;
	public static final int SUNDAY = 1;
	public static final int THURSDAY = 5;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
    }
}
