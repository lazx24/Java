package com.common.util.fulltext;

import com.common.util.string.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class FulltextUtilTest {
    public void getTermOnlyQuotes() {
	String keywords = "化工\"行业安\"全发展规<划编>制导则“测试指”南";
	for (String s : FulltextUtil.getTermOnlyQuotes(keywords)) {
	    System.out.println(s);
	}
	System.out.println("-----------------------------");
	keywords = "化工行业安全发展规划编制导则测试指南";
	for (String s : FulltextUtil.getTermOnlyQuotes(keywords)) {
	    System.out.println(s);
	}
    }

    public void getTermListByQuote() {
	String keywords = "化工\"行业安\"全\"发展规划编制\"导则'测试指'南";
	for (String s : FulltextUtil.getTermListByQuote(keywords)) {
	    System.out.println(s);
	}
	System.out.println("-----------------------------");
	keywords = "化工行业安全发展规划编制导则测试指南";
	for (String s : FulltextUtil.getTermListByQuote(keywords)) {
	    System.out.println(s);
	}
    }

    @Test
    public void getTermList() {
	String keywords = "安全生产法Success Failed to procesSS";
	for (String s : FulltextUtil.getTermList(keywords)) {
	    System.out.println(s);
	}
    }

    @Test
    public void getIKAnalyzerTermList() {
	String keywords = "中华人民共和国安全生产法 GB 273.2-124";
	System.out.println(FulltextUtil.getIKAnalyzerTermList(keywords));
    }

    @Test
    public void getMaxWordTermList() {
	String keywords = "中华 人民共和国环境保护法 （1989年发布）";
	System.out.println("getTermList:\t\t"
		+ FulltextUtil.getTermList(keywords));
	System.out.println("getMaxWordTermList:\t"
		+ FulltextUtil.getMaxWordTermList(keywords));
	System.out.println("getIKAnalyzerTermList:\t"
		+ FulltextUtil.getIKAnalyzerTermList(keywords));
	System.out.println("getIKAnalyzerTermList:\t"
		+ FulltextUtil.getIKAnalyzerTermList(keywords, false));
    }

    @Test
    public void getTableName() {
	String sql = "   insert     into   Abc values";
	String lowerSql = sql.trim().toLowerCase().replaceAll("into", "")
		.replaceAll("from", "");
	int index1 = lowerSql.indexOf(" ");
	lowerSql = lowerSql.substring(index1);
	int len = lowerSql.length();

	lowerSql = StringUtil.trimLeft(lowerSql);
	int index2 = lowerSql.indexOf(" ");
	sql = sql.trim().substring(index1 + index2 + len - lowerSql.length())
		.trim();

	int index3 = sql.indexOf(" ");
	String name = sql.substring(0, index3);
	System.out.println(name);
    }

    @Test
    public void trimRight() {
	String s = " abc 123   ";
	System.out.println(StringUtil.trimRight(s));
	s = "   ";
	System.out.println(StringUtil.trimRight(s));
    }

    private String generateCountSql(String queryString) {
	queryString = queryString.replace("\r", " ").replace("\n", " ");
	String countString = queryString.toLowerCase();

	countString = "select count(*) from ("
		+ queryString.replace(" fetch ", " ");

	int orderByIndex = countString.lastIndexOf(" order by ");
	if (orderByIndex >= 0) {
	    countString = countString.substring(0, orderByIndex);
	}
	return countString + ")";
    }

    @Test
    public void countSqlTest() {
	String sql = "select distinct bkg.bookingUuid, bkg.jobNum, bkg.bookingType, masterBkg.jobNum, bkg.bookingNum, bkg.mblNum,  bkg.hblNum, bkg.operationStatus,  bkg.hblTransTerm, bkg.hblPaymentMethod, bkg.recCreUserId, bkg.recCreDt, bkg.recUpdUserId, bkg.recUpdDt,  bkg.ioFlag, svcLoop.itsSvcLoopUuid, svcLoop.svcLoopCde, svcLoop.svcLoopNme,  vessel.itsVesselUuid, vessel.vesselCde, vessel.vesselNme, voyage.voyageNum, voyage.voyageDir,  shipper.cpfCustomerSiteUuid, shipper.cpfCustomerSiteId, shipper.cpfCustomerSiteNme,  consignee.cpfCustomerSiteUuid, consignee.cpfCustomerSiteId, consignee.cpfCustomerSiteNme,  client.cpfCustomerSiteUuid, client.cpfCustomerSiteId, client.cpfCustomerSiteNme, oceanRoute1.etd  from Booking bkg left join bkg.masterBooking as masterBkg  left join bkg.bookingPartners as shipperPartner with shipperPartner.partnerType = ?  left join bkg.bookingPartners as consigneePartner with consigneePartner.partnerType = ?  left join bkg.bookingPartners as clientPartner with clientPartner.partnerType = ?  left join shipperPartner.cpfCustomerSite as shipper  left join consigneePartner.cpfCustomerSite as consignee  left join clientPartner.cpfCustomerSite as client  left join bkg.itsServiceLoop as svcLoop left join bkg.bookingRoutes as oceanRoute1 with oceanRoute1.seq = 1 and oceanRoute1.routeType = 'OCEAN' left join oceanRoute1.itsVessel as vessel left join oceanRoute1.itsVoyage as voyage where (bkg.ioFlag = ? or bkg.ioFlag = ? ) and ( bkg.recOffice.gspOfceUuid in ( ? ) or  bkg.loadingOffice.gspOfceUuid in ( ? )) order by oceanRoute1.etd desc, bkg.jobNum";
	System.out.println(generateCountSql(sql));
    }

    @Test
    public void testIKAnalysis() {
	String str = "城市排水与污水处理条例";

	IKAnalysis(str);
    }

    public static List<String> IKAnalysis(String str) {
	List<String> keywordList = new ArrayList<String>();
	try {
	    byte[] bt = str.getBytes();
	    InputStream ip = new ByteArrayInputStream(bt);
	    Reader read = new InputStreamReader(ip);
	    IKSegmenter iks = new IKSegmenter(read, true);
	    Lexeme t;
	    while ((t = iks.next()) != null) {
		keywordList.add(t.getLexemeText());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println(keywordList);
	return keywordList;
    }
}
