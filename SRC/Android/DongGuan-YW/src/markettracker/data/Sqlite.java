package markettracker.data;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Tool;
import android.content.Context;

public class Sqlite {

	public static DB mDB;

	private static DB getDB(Context context) {
		if (mDB == null)
			mDB = new DB(context);
		return mDB;
	}

	public synchronized static void execSqlList(Context context, List<String> SqlList) {

		try {
			getDB(context).execSqlList(SqlList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static long setPlan(Context context, SObject rpt) {
		long index;
		try {
			index = getDB(context).setPlan(rpt);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return index;
	}
	
	public synchronized static long savePlan(Context context, SObject rpt)
	{
		long index;
		try
		{
			index = getDB(context).savePlan(rpt);
		}
		catch (Exception ex)
		{
			return -1;
		}
		return index;
	}

	public synchronized static String addDate(Context context, String date, String days) {
		String data;
		try {
			data = getDB(context).addDate(date, days);
		} catch (Exception e) {
			e.printStackTrace();
			return Tool.getCurrDate();
		}
		return data;
	}

	public synchronized static FieldsList getReportList(Context context, Fields condition) {
		FieldsList list;
		try {
			list = getDB(context).getReportList(condition);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * 查询未下载的信息公告和未读的事项提醒
	 * @param context
	 * @param type	2=信息公告 3=事项提醒
	 * @return
	 */
	public synchronized static int getUnReadMsgCount(Context context, int type) {
		int count = 0;
		try {
			count = getDB(context).getUnReadMsgCount(type);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return count;
	}

	public synchronized static int getUnSubmitRptCount(Context context) {
		int count = 0;
		try {
			count = getDB(context).getUnSubmitRptCount();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return count;
	}

	public synchronized static List<String> getTemplateIdList(Context context, String code, String key) {
		List<String> list = new ArrayList<String>();
		try {
			list = getDB(context).getTemplateIdList(code, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized static List<Fields> getProductIdList(Context context) {
		List<Fields> list = new ArrayList<Fields>();
		try {
			list = getDB(context).getProductIdList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized static String getDate(Context context, String where) {
		String data;
		try {
			data = getDB(context).getDate(where);
		} catch (Exception e) {
			e.printStackTrace();
			return Tool.getCurrDate();
		}
		return data;
	}

	public synchronized static boolean upsertData(Context context, List<SObject> list, String type) {
		try {
			if (list == null || list.size() <= 0)
				return true;
			if (getDB(context).upsertData(list, type) < 0)
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public synchronized static FieldsList getFieldsList(Context context, int type, String code) {
		FieldsList list = new FieldsList();
		try {
			list = getDB(context).getFieldsList(type, code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized static FieldsList getFieldsList(Context context, int type, Fields data) {
		FieldsList list = new FieldsList();
		try {
			list = getDB(context).getFieldsList(type, data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized static Template getTemplate(Context context, String type) {
		Template template = new Template();
		try {
			template = getDB(context).getTemplate(type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return template;
	}

	public synchronized static SObject getReport(Context context, Template temp, String code, int type, String key) {
		SObject object;
		try {
			object = getDB(context).getReport(temp, code, type, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return object;
	}

	public synchronized static SObject getDPReport(Context context, Template temp, String code) {
		SObject object;
		try {
			object = getDB(context).getDPReport(temp, code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return object;
	}

	public synchronized static SObject getSurveyRpt(Context context, Template temp, String code) {
		SObject object;
		try {
			object = getDB(context).getSurveyRpt(temp, code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return object;
	}

	public synchronized static List<SObject> getSubmitObject(Context context) {
		List<SObject> list;
		try {
			list = getDB(context).getSubmitObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized static List<SObject> getReadMsgId(Context context) {
		List<SObject> list;
		try {
			list = getDB(context).getReadMsgId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public synchronized static long saveReport(Context context, SObject rpt) {
		long index;
		try {
			index = getDB(context).saveReport(rpt);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return index;
	}

	public synchronized static long saveDP(Context context, SObject rpt) {
		long index;
		try {
			index = getDB(context).saveDP(rpt);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return index;
	}

	public static void execSingleSql(Context context, String sql) {
		try {
			getDB(context).execSingleSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<DicData> getDictDataList(Context context, String mainDictId, String detaildicId) {
		try {
			return getDB(context).getDicList(mainDictId, detaildicId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<DicData> getAnswerList(Context context, String questionId) {
		try {
			return getDB(context).getAnswerList(questionId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<DicData> getDictDataList(Context context, String mainDictId) {
		try {
			return getDB(context).getDicList(mainDictId, "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 电子资料
	 * @param context
	 * @return
	 */
	public synchronized static List<FieldsList> getElectornicGroups(Context context) {
		List<FieldsList> list = new ArrayList<FieldsList>();
		try {
			list = getDB(context).getFieldsListList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
