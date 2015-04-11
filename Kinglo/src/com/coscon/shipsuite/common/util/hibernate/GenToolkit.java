package com.coscon.shipsuite.common.util.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Table;

/**
 * 解析指定数据库的某张表，自动根据其表定义生成对应的Hibernate封装类，输出到控制台 请根据实际需要，手工修改数据库连接、用户名、密码、表名
 * 
 * 本文件通过SVN更新到本地，使用后请勿提交
 * 
 * @author lijy
 * 
 */
public class GenToolkit {

	// ==================================================================
	// 以下四个字段需要根据实际需要修改，请勿提交此类更改到SVN
//	private static String URL = "jdbc:oracle:thin:@//172.22.12.207:1521/orcl";
//	private static String DB_USER = "CBS_TEST"; // Your name
//	private static String DB_PASS = "TEST"; // Your pass
	
	private static String URL = "jdbc:oracle:thin:@//172.32.231.217:1521/CBS";
	private static String DB_USER = "cbs_test"; // Your name
	private static String DB_PASS = "CBS_TEST_PS"; // Your pass
	private static String TABLE_NAME = "WFL_WORKFLOW_PROCEDURE"; // Target table name
	private static int    TABLE_PREFIX = 3;
	private static boolean USE_CONVERSION = true;
	// ==================================================================

	private static String LINE = "\r\n";
	private static String TAB = "	";

	private static Map<String, String> nameConversionMap = new HashMap<String, String>();
	private static Map<String, String> typeConversionMap = new HashMap<String, String>();

  static {
    if (USE_CONVERSION) {

      typeConversionMap.put("VARCHAR2", "String");
      typeConversionMap.put("VARCHAR", "String");
      typeConversionMap.put("NUMBER", "BigDecimal");
      typeConversionMap.put("NCHAR", "String");
      typeConversionMap.put("NVARCHAR2", "String");
      typeConversionMap.put("TIMESTAMP", "Timestamp");
      typeConversionMap.put("RAW", "byte[]");
      typeConversionMap.put("BFILE", "byte[]");
      typeConversionMap.put("DATE", "Date");
      typeConversionMap.put("CHAR", "String");
      typeConversionMap.put("CLOB", "String");
      typeConversionMap.put("BLOB", "Blob");

      nameConversionMap.put("ABS", "Absolute");
      nameConversionMap.put("ACCS", "Accessible");
      nameConversionMap.put("ACCT", "Account");
      nameConversionMap.put("ACQ", "Acquisition");
      nameConversionMap.put("ACT", "Actual");
      nameConversionMap.put("ACTVY", "Activity");
      nameConversionMap.put("ADDR", "Address");
      nameConversionMap.put("ADJ", "Adjustment");
      nameConversionMap.put("ADMIN", "Administration");
      nameConversionMap.put("AE", "AfterEfficiency");
      nameConversionMap.put("AGG", "Aggregate");
      nameConversionMap.put("AGMT", "Agreement");
      nameConversionMap.put("ALT", "Alert");
      nameConversionMap.put("ALTN", "Alternative");
      nameConversionMap.put("AMT", "Amount");
      nameConversionMap.put("ARR", "Arrival");
      nameConversionMap.put("ASSN", "Association");
      nameConversionMap.put("AU", "Authority");
      nameConversionMap.put("AVL", "Available");
      nameConversionMap.put("BE", "BeforeEfficiency");
      nameConversionMap.put("BGN", "Begin");
      nameConversionMap.put("BI", "BusinessIndex");
      nameConversionMap.put("BIZ", "Business");
      nameConversionMap.put("BKG", "Booking");
      nameConversionMap.put("BL", "Bill");
      nameConversionMap.put("BO", "BusinessObject");
      nameConversionMap.put("BOP", "BusinessOperation");
      nameConversionMap.put("BPM_A", "BPM Analysis");
      nameConversionMap.put("BPM_D", "ActualDetail");
      nameConversionMap.put("BPM_P", "Planning");
      nameConversionMap.put("BPM_S", "ActualSummary");
      nameConversionMap.put("BRK", "Brokerage");
      nameConversionMap.put("BSC", "BalancedScorecard");
      nameConversionMap.put("BTW", "Between");
      nameConversionMap.put("BU", "BusinessUnit");
      nameConversionMap.put("C", "Config");
      nameConversionMap.put("CALC", "Calculation");
      nameConversionMap.put("CCTR", "CostCenter");
      nameConversionMap.put("CCY", "Currency");
      nameConversionMap.put("CDE", "Code");
      nameConversionMap.put("CGO", "Cargo");
      nameConversionMap.put("CHK", "Check");
      nameConversionMap.put("CHKLIST", "CheckList");
      nameConversionMap.put("CHNG", "Changed");
      nameConversionMap.put("CHRG", "Charge");
      nameConversionMap.put("CLSMTH", "CloseMonth");
      nameConversionMap.put("CMCNTR", "CMContainer");
      nameConversionMap.put("CMP", "Comparision");
      nameConversionMap.put("CMPL", "Complete");
      nameConversionMap.put("CMPL", "Completion");
      nameConversionMap.put("CMPORT", "CMPort");
      nameConversionMap.put("CNEE", "Consignee");
      nameConversionMap.put("CNT", "Count");
      nameConversionMap.put("CNTR", "Container");
      nameConversionMap.put("CNTRI", "Contribution");
      nameConversionMap.put("COL", "Column");
      nameConversionMap.put("COLL", "Collection");
      nameConversionMap.put("COM", "Common");
      nameConversionMap.put("COMM", "Commission");
      nameConversionMap.put("COMMIT", "Commitment");
      nameConversionMap.put("COND", "Condition");
      nameConversionMap.put("CONF", "Configuration");
      nameConversionMap.put("CONST", "Constant");
      nameConversionMap.put("CONSUMP", "Consumption");
      nameConversionMap.put("CRSPND", "Corresponding");
      nameConversionMap.put("CTGRY", "Category");
      nameConversionMap.put("CTRCT", "Contract");
      nameConversionMap.put("CTRL", "Control");
      nameConversionMap.put("CURR", "Current");
      nameConversionMap.put("CUST", "Customization");
      nameConversionMap.put("CUST", "Customer");
      nameConversionMap.put("DEF", "Defination");
      nameConversionMap.put("DEFLT", "Default");
      nameConversionMap.put("DEL", "Delete");
      nameConversionMap.put("DEP", "Departure");
      nameConversionMap.put("DEPT", "Department");
      nameConversionMap.put("DESC", "Description");
      nameConversionMap.put("DEST", "Destination");
      nameConversionMap.put("DHBRD", "Dashboard");
      nameConversionMap.put("DIFF", "Difference");
      nameConversionMap.put("DIM", "Dimension");
      nameConversionMap.put("DIR", "Direction");
      nameConversionMap.put("DISCNT", "Discount");
      nameConversionMap.put("DISP", "Display");
      nameConversionMap.put("DOC", "Document");
      nameConversionMap.put("DSCH", "Discharge");
      nameConversionMap.put("DT", "Datetime");
      nameConversionMap.put("DTE", "Date");
      nameConversionMap.put("DTL", "Detail");
      nameConversionMap.put("DYN", "Dynamic");
      nameConversionMap.put("ECNTR", "EmptyContainer");
      nameConversionMap.put("EFF", "Effective");
      nameConversionMap.put("EFFCY", "Efficiency");
      nameConversionMap.put("EOV", "EndofVoyage");
      nameConversionMap.put("EQMT", "Equipment");
      nameConversionMap.put("ERR", "Error");
      nameConversionMap.put("ESTM", "Estimate");
      nameConversionMap.put("EX", "Exclude");
      nameConversionMap.put("EXCHG", "Exchange");
      nameConversionMap.put("EXPSN", "Expansion");
      nameConversionMap.put("F", "First");
      nameConversionMap.put("F_BASE", "FirstBase");
      nameConversionMap.put("F_POD", "FirstPOD");
      nameConversionMap.put("F_POL", "FirstPOL");
      nameConversionMap.put("FCIL", "Facility");
      nameConversionMap.put("FIN", "Finance");
      nameConversionMap.put("FLW", "Flow");
      nameConversionMap.put("FM", "From");
      nameConversionMap.put("FRML", "Formular");
      nameConversionMap.put("FRQ", "Frequency");
      nameConversionMap.put("FRT", "Freight");
      nameConversionMap.put("FWDER", "Forwarder");
      nameConversionMap.put("GEN", "Generate");
      nameConversionMap.put("GRP", "Group");
      nameConversionMap.put("HIS", "History");
      nameConversionMap.put("HRCHY", "Hierarchy");
      nameConversionMap.put("IB", "Inbound");
      nameConversionMap.put("IDX", "Index");
      nameConversionMap.put("ILD", "Inland");
      nameConversionMap.put("IMP", "Import");
      nameConversionMap.put("IND", "Indicator");
      nameConversionMap.put("INFR", "Information");
      nameConversionMap.put("INIT", "Initial");
      nameConversionMap.put("INITV", "Initiative");
      nameConversionMap.put("INTER", "Interchange");
      nameConversionMap.put("INTERNAL", "Internal");
      nameConversionMap.put("INV", "Invoice");
      nameConversionMap.put("L", "Last");
      nameConversionMap.put("L_BASE", "LastBase");
      nameConversionMap.put("L_POD", "LastPOD");
      nameConversionMap.put("LANG", "Language");
      nameConversionMap.put("LCNTR", "LadenContainer");
      nameConversionMap.put("LDD", "Loading");
      nameConversionMap.put("LOC", "Local");
      nameConversionMap.put("LUBRI", "Lubrication");
      nameConversionMap.put("LVL", "Level");
      nameConversionMap.put("LY", "LastYear");
      nameConversionMap.put("MDE", "Mode");
      nameConversionMap.put("MGT", "Management");
      nameConversionMap.put("MOD", "Modification");
      nameConversionMap.put("MSG", "Message");
      nameConversionMap.put("MTHD", "Method");
      nameConversionMap.put("MUL", "Multiply");
      nameConversionMap.put("MUTL", "Mutual");
      nameConversionMap.put("NME", "Name");
      nameConversionMap.put("NND", "MMD");
      nameConversionMap.put("NTFI", "Notification");
      nameConversionMap.put("NUM", "Number");
      nameConversionMap.put("OB", "OutBound");
      nameConversionMap.put("OBJ", "Object");
      nameConversionMap.put("OFCE", "Office");
      nameConversionMap.put("OP", "Operator");
      nameConversionMap.put("OPND", "Operand");
      nameConversionMap.put("OPR", "Operation");
      nameConversionMap.put("ORIG", "Original");
      nameConversionMap.put("PACKG", "Package");
      nameConversionMap.put("PARM", "Parameter");
      nameConversionMap.put("PCT", "Percent");
      nameConversionMap.put("PECT", "Percentage");
      nameConversionMap.put("PER", "Period");
      nameConversionMap.put("PN", "Personalization");
      nameConversionMap.put("PRE", "Prepare");
      nameConversionMap.put("PREF", "Preference");
      nameConversionMap.put("PROG", "Program");
      nameConversionMap.put("PROJ", "Project");
      nameConversionMap.put("PROP", "Property");
      nameConversionMap.put("PRV", "Previous");
      nameConversionMap.put("PYMT", "Payment");
      nameConversionMap.put("QTY", "Quantity");
      nameConversionMap.put("RC", "RegionCenter");
      nameConversionMap.put("REC", "Record");
      nameConversionMap.put("RECALC", "Recalculation");
      nameConversionMap.put("REF", "Reference");
      nameConversionMap.put("REG", "Register");
      nameConversionMap.put("REL", "Reliability");
      nameConversionMap.put("RESP", "Response");
      nameConversionMap.put("REV", "Revenue");
      nameConversionMap.put("RPT", "Report");
      nameConversionMap.put("RTE", "Route");
      nameConversionMap.put("SALCNTR", "SalesContainer");
      nameConversionMap.put("SCH", "Schedule");
      nameConversionMap.put("SEG", "Segment");
      nameConversionMap.put("SEQ", "Sequence");
      nameConversionMap.put("SET", "Settlement");
      nameConversionMap.put("SHMT", "Shipment");
      nameConversionMap.put("SHPR", "Shipper");
      nameConversionMap.put("SIZE", "Size");
      nameConversionMap.put("SND", "Second");
      nameConversionMap.put("SO", "Shipowner");
      nameConversionMap.put("STAT", "Statistics");
      nameConversionMap.put("STL", "Settled");
      nameConversionMap.put("SUBSCRPT", "Subscription");
      nameConversionMap.put("SVC", "Service");
      nameConversionMap.put("SYS", "System");
      nameConversionMap.put("TERR", "Territory");
      nameConversionMap.put("THD", "Threshold");
      nameConversionMap.put("THRESH", "Threshhold");
      nameConversionMap.put("TL_DIR", "TradeLaneByDirection");
      nameConversionMap.put("TML", "Terminal");
      nameConversionMap.put("TRANS", "Transfer");
      nameConversionMap.put("TRCKG", "Tracking");
      nameConversionMap.put("TRG", "Trigger");
      nameConversionMap.put("TRN", "Transformed");
      nameConversionMap.put("TTL", "Total");
      nameConversionMap.put("TXT", "Text");
      nameConversionMap.put("TYPE", "Type");
      nameConversionMap.put("UPD", "Update");
      nameConversionMap.put("USR", "User");
      nameConversionMap.put("UTIL", "Utilization");
      nameConversionMap.put("VAL", "Value");
      nameConversionMap.put("VER", "Version");
      nameConversionMap.put("VERF", "Verification");
      nameConversionMap.put("VNDR", "Vendor");
      nameConversionMap.put("VOY", "Voyage");
      nameConversionMap.put("VSL", "Vessel");
    }
  }

	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;
		try {
			conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);

			pstmt = conn.prepareStatement("SELECT * FROM " + TABLE_NAME
					+ " WHERE ROWNUM < 2");
			rsmd = pstmt.getMetaData();

			StringBuffer sb = new StringBuffer();
			// package
			sb.append("import java.io.Serializable;").append(LINE);			
			sb.append("import java.math.BigDecimal;").append(LINE);
			sb.append("import java.util.Date;").append(LINE);
			sb.append("import javax.persistence.Column;").append(LINE);
			sb.append("import javax.persistence.Entity;").append(LINE);
			sb.append("import javax.persistence.Table;").append(LINE);
			sb.append("import javax.persistence.GeneratedValue;").append(LINE);
			sb.append("import javax.persistence.Id;").append(LINE);
			sb.append("import org.hibernate.annotations.GenericGenerator;")
					.append(LINE);
			sb.append("import org.hibernate.annotations.OptimisticLockType;")
					.append(LINE);
			sb.append("import org.hibernate.annotations.Parameter;").append(
					LINE);
			sb.append("import org.hibernate.annotations.PolymorphismType;")
					.append(LINE).append(LINE);

			// class annotation
			//修改为Entity和Table 组合使用，@Entity(name="table_name")方式不合理(by wxy 20120817)
			sb.append("@Entity").append(LINE);
			sb.append("@Table(name = \"").append(TABLE_NAME).append("\")").append(LINE);
			sb.append(
					"@org.hibernate.annotations.Entity(selectBeforeUpdate = true, optimisticLock = OptimisticLockType.NONE, polymorphism = PolymorphismType.EXPLICIT)")
					.append(LINE);
			String name = TABLE_NAME.substring(TABLE_PREFIX); 
			// class
			sb.append("public class ").append(
					databaseNameToJavaName(name, true));
			sb.append(" implements Serializable {").append(LINE);
			sb.append(LINE);
			sb.append(TAB+"@Id").append(LINE);
			sb.append(TAB+"@GeneratedValue(generator = \"paymentableGenerator\")")
					.append(LINE);
			sb.append(TAB+
					"@GenericGenerator(name = \"paymentableGenerator\", strategy = \"sequence\", parameters = "
							+ "{ @Parameter(name = \"sequence\", value = \"SEQ_"
							+ TABLE_NAME + "\") })").append(LINE);
			// column loop
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {

				// property annotation
				sb.append(TAB);
				sb.append("@Column(name = \"").append(rsmd.getColumnName(i))
						.append("\")");

				// property
				sb.append(LINE).append(TAB);
				sb.append("private ").append(
						typeConversionMap.get(rsmd.getColumnTypeName(i)));
				sb.append(" ")
						.append(databaseNameToJavaName(rsmd.getColumnName(i),
								false)).append(";");
				sb.append(LINE).append(LINE);

			}
			System.out.println(sb.toString());
			pstmt.close();
			conn.close();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
		  try {
        if (pstmt != null && !pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static String databaseNameToJavaName(String dbName,
			boolean isTableName) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String part : dbName.split("_")) {
		  if (part.length() == 0) continue;
			String convertedPart = convertPart(part.toLowerCase());
			String keyword = convertedPart.substring(0, 1).toUpperCase()
					+ convertedPart.substring(1, convertedPart.length());
			if (first) {
				if (!isTableName) {
					keyword = convertedPart.substring(0, 1).toLowerCase()
							+ convertedPart
									.substring(1, convertedPart.length());
				}
				first = false;
			}
			sb.append(keyword);
		}
		return sb.toString();
	}

	private static String convertPart(String part) {
		String converted = nameConversionMap.get(part.toUpperCase());
		return converted == null ? part : converted;
	}

}
