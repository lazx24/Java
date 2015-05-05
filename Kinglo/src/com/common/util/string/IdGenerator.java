package com.common.util.string;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.common.util.date.DateUtil;

public final class IdGenerator {
    private static final AtomicLong atomicSeqContinue = new AtomicLong(0L);
    private static volatile Date lastTimestamp = new Date();
    private static final AtomicLong atomicSeqTimestamp = new AtomicLong(0L);
    private static final int MAX_SEQ_OF_MILLIS = 999999;
    private static final int SEQ_DIGIT = 6;
    
    /**
     * 生成32位的随机数
     * @return
     */
    public static String getUUID() {
	UUID uuid = UUID.randomUUID();
	return StringUtil.replaceAll(uuid.toString(), "-", "");
    }

    private static String uuid = getUUID();
    
    /**
     * 获取生成随机数的长度
     * @return
     */
    public static int getUUIDLength() {
	return uuid.length();
    }
    
    /**
     * 获取时间戳
     * @return
     */
    public static long getTimestampSeq() {
	long timestamp = System.currentTimeMillis();
	long seq = atomicSeqContinue.getAndIncrement();
	if (seq > 999999L) {
	    atomicSeqContinue.set(0L);
	    seq = atomicSeqContinue.getAndIncrement();
	}
	return Long.valueOf(
		String.format("%d%s", new Object[] { Long.valueOf(timestamp),
			StringUtil.formatNumber(seq, 6) })).longValue();
    }
    
    /**
     * 获取时间戳
     * @param seqDigit 多少位序列数
     * @return
     */
    public static synchronized String getTimestamp(Integer seqDigit) {
	Date timestamp = new Date();
	String timepstampString = DateUtil.dateFormatToString(timestamp,DateUtil.MILLIONS_DATE_FORMAT);
	if ((seqDigit != null) && (seqDigit.intValue() > 0)) {
	    if (lastTimestamp.getTime() != timestamp.getTime()) {
		lastTimestamp = timestamp;
		atomicSeqTimestamp.set(0L);
	    }
	    long seq = atomicSeqTimestamp.getAndIncrement();

	    return String.format("%s%s", new Object[] { timepstampString,
		    StringUtil.formatNumber(seq, seqDigit.intValue()) });
	}
	return timepstampString;
    }
}
