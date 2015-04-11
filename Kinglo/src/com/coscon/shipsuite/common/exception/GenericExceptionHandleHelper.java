package com.coscon.shipsuite.common.exception;

import com.caucho.hessian.client.HessianConnectionException;
import com.coscon.shipsuite.common.util.string.IMessageHelper;
import com.coscon.shipsuite.common.util.string.StringUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.util.WeakHashMap;
import javax.mail.MessagingException;
import org.hibernate.OptimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.transaction.CannotCreateTransactionException;

public class GenericExceptionHandleHelper {
    private IMessageHelper messageHelper;
    private static WeakHashMap<IMessageHelper, GenericExceptionHandleHelper> instanceMap = new WeakHashMap<IMessageHelper, GenericExceptionHandleHelper>();

    protected GenericExceptionHandleHelper(IMessageHelper messageHelper) {
	this.messageHelper = messageHelper;
    }

    public static GenericExceptionHandleHelper getInstance(
	    IMessageHelper messageHelper) {
	if (messageHelper == null) {
	    return null;
	}
	synchronized (messageHelper) {
	    GenericExceptionHandleHelper instance = (GenericExceptionHandleHelper) instanceMap
		    .get(messageHelper);
	    if (instance == null) {
		instance = new GenericExceptionHandleHelper(messageHelper);
	    }
	    return instance;
	}
    }

    public String convertThrowable(Throwable ex) {
	if ((ex instanceof UndeclaredThrowableException)) {
	    ex = ((UndeclaredThrowableException) ex).getUndeclaredThrowable();
	}
	if (((ex instanceof MessagingException))
		&& (ex.getMessage().contains("Could not connect to SMTP host"))) {
	    return this.messageHelper.getMessageString(
		    "exception.connect.mailserver", new String[0]);
	}
	if ((!(ex instanceof ConnectException))
		&& (!(ex instanceof NoRouteToHostException))
		&& (!(ex instanceof SocketException))) {
	    if (ex.getMessage() != null) {
		if (!ex.getMessage().contains("java.net.ConnectException")) {
		}
	    }
	} else {
	    return this.messageHelper.getMessageString(
		    "exception.connect.appserver", new String[0]);
	}
	if (((ex instanceof CannotCreateTransactionException))
		&& (ex.getMessage()
			.startsWith("Could not open Hibernate Session for transaction"))) {
	    return this.messageHelper.getMessageString(
		    "exception.connect.dbserver", new String[0]);
	}
	if (((ex instanceof HessianConnectionException))
		&& ((ex.getCause() instanceof FileNotFoundException))) {
	    if (ex.getMessage().startsWith("HessianProxy cannot connect to ")) {
		return this.messageHelper.getMessageString(
			"exception.connect.service", new String[] { ex
				.getCause().getMessage() });
	    }
	}
	if ((((ex.getCause() instanceof StaleObjectStateException)) && (ex
		.getMessage()
		.contains("Row was updated or deleted by another transaction")))
		|| (((ex.getCause() instanceof OptimisticLockException)) && (ex
			.getMessage().startsWith("Newer version")))) {
	    return this.messageHelper.getMessageString(
		    "exception.optimisticLock.failed", new String[0]);
	}
	if (((ex.getCause() instanceof ConstraintViolationException))
		&& (ex.getCause().getCause().getMessage().contains("ORA-02292"))) {
	    return this.messageHelper.getMessageString(
		    "exception.database.oracle.constraint.refrenced",
		    new String[0]);
	}
	if (((ex.getCause() instanceof ConstraintViolationException))
		&& (ex.getCause().getCause().getMessage().contains("ORA-01400"))) {
	    return this.messageHelper.getMessageString(
		    "exception.database.oracle.constraint.null", new String[0]);
	}
	if (((ex.getCause() instanceof ConstraintViolationException))
		&& (ex.getCause().getCause().getMessage().contains("ORA-00001"))) {
	    return this.messageHelper.getMessageString(
		    "exception.database.oracle.constraint.unique",
		    new String[0]);
	}
	if (((ex.getCause() instanceof DataException))
		&& (ex.getCause().getCause().getMessage().contains("ORA-01438"))) {
	    return this.messageHelper.getMessageString(
		    "exception.database.oracle.data.tooGreater", new String[0]);
	}
	if (((ex.getMessage() != null) && (ex.getMessage()
		.contains("org.hibernate.QueryTimeoutException")))
		|| (((ex.getCause() instanceof QueryTimeoutException)) && (((ex
			.getMessage() != null) && (ex.getMessage()
			.contains("ORA-12899"))) || ((ex.getMessage() != null) && (ex
			.getMessage().contains("error code [12899]")))))) {
	    return this.messageHelper.getMessageString(
		    "exception.database.oracle.lengthTooLong", new String[0]);
	}
	if (ex.getMessage() != null) {
	    if (ex.getMessage()
		    .contains(
			    "Hibernate operation: could not execute query; bad SQL grammar")) {
		return this.messageHelper.getMessageString(
			"exception.hibernate.badsql", new String[0]);
	    }
	}
	if (ex.getMessage() != null) {
	    if (ex.getMessage().contains(
		    "Batch update returned unexpected row count from update")) {
		return this.messageHelper.getMessageString(
			"exception.hibernate.update.failed", new String[0]);
	    }
	}
	if (((ex.getCause() instanceof IOException))
		&& (ex.getMessage()
			.contains("Server returned HTTP response code"))) {
	    return this.messageHelper.getMessageString(
		    "exception.connect.appserver", new String[0]);
	}
	if ((ex instanceof ShipSuiteApplicationException)) {
	    return this.messageHelper.getMessageString(
		    ((ShipSuiteApplicationException) ex).getMsgKey(),
		    ((ShipSuiteApplicationException) ex).getMsgArgs());
	}
	if ((ex instanceof ShipSuiteRuntimeException)) {
	    return this.messageHelper.getMessageString(
		    ((ShipSuiteRuntimeException) ex).getMsgKey(),
		    ((ShipSuiteRuntimeException) ex).getMsgArgs());
	}
	StringBuffer sb = new StringBuffer();
	String errorInfo = ex.getMessage();
	if ((StringUtil.isNullOrEmpty(errorInfo)) && (ex.getCause() != null)) {
	    errorInfo = ex.getCause().getMessage();
	}
	return this.messageHelper.getMessageString("exception.nodefined",
		new String[] { errorInfo });
    }
}
