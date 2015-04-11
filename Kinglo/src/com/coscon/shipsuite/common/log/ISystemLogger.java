package com.coscon.shipsuite.common.log;

import com.coscon.shipsuite.common.util.string.IMessageHelper;

public abstract interface ISystemLogger {
    public abstract void debug(String paramString);

    public abstract void debug(String paramString1, String paramString2);

    public abstract void error(String paramString);

    public abstract void error(String paramString1, String paramString2);

    public abstract void error(String paramString1, String paramString2,
	    Throwable paramThrowable);

    public abstract void error(String paramString, String[] paramArrayOfString);

    public abstract void error(String paramString, Throwable paramThrowable);

    public abstract void error(Throwable paramThrowable);

    public abstract void fatal(String paramString);

    public abstract void fatal(String paramString1, String paramString2);

    public abstract void fatal(String paramString1, String paramString2,
	    Throwable paramThrowable);

    public abstract void fatal(String paramString, Throwable paramThrowable);

    public abstract void fatal(Throwable paramThrowable);

    public abstract void info(String paramString);

    public abstract void info(String paramString1, String paramString2);

    public abstract boolean isDebugEnabled();

    public abstract boolean isInfoEnabled();

    public abstract void setLogHelper(IMessageHelper paramIMessageHelper);

    public abstract void trace(String paramString);

    public abstract void trace(String paramString1, String paramString2);

    public abstract void trace(String paramString, String[] paramArrayOfString);

    public abstract void warn(String paramString);

    public abstract void warn(String paramString1, String paramString2);

    public abstract void warn(String paramString, String[] paramArrayOfString);
}
