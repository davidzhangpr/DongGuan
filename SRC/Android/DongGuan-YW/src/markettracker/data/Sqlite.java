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
		} catch (Exception ex) {
		}
	}

	public synchronized static long setPlan(Context context, SObject rpt) {
		long index;
		try {
			index = getDB(context).setPlan(rpt);
		} catch (Exception ex) {
			return -1;
		}
		return index;
	}

	public synchronized static String addDate(Context context, String date, String days) {
		String data;
		try {
			data = getDB(context).addDate(date, days);
		} catch (Exception ex) {
			return Tool.getCurrDate();
		}
		return data;
	}

	public synchronized static FieldsList getReportList(Context context, Fields condition) {
		FieldsList list;
		try {
			list = getDB(context).getReportList(condition);
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static int getUnReadMsgCount(Context context) {
		int count = 0;
		try {
			count = getDB(context).getUnReadMsgCount();
		} catch (Exception ex) {
			return -1;
		}
		return count;
	}

	public synchronized static int getUnSubmitRptCount(Context context) {
		int count = 0;
		try {
			count = getDB(context).getUnSubmitRptCount();
		} catch (Exception ex) {
			return -1;
		}
		return count;
	}

	public synchronized static List<String> getTemplateIdList(Context context, String code, String key) {
		List<String> list = new ArrayList<String>();
		try {
			list = getDB(context).getTemplateIdList(code, key);
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static List<Fields> getProductIdList(Context context) {
		List<Fields> list = new ArrayList<Fields>();
		try {
			list = getDB(context).getProductIdList();
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static String getDate(Context context, String where) {
		String data;
		try {
			data = getDB(context).getDate(where);
		} catch (Exception ex) {
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
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public synchronized static FieldsList getFieldsList(Context context, int type, String code) {
		FieldsList list = new FieldsList();
		try {
			list = getDB(context).getFieldsList(type, code);
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static FieldsList getFieldsList(Context context, int type, Fields data) {
		FieldsList list = new FieldsList();
		try {
			list = getDB(context).getFieldsList(type, data);
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static Template getTemplate(Context context, String type) {
		Template template = new Template();
		try {
			template = getDB(context).getTemplate(type);
		} catch (Exception ex) {
			return null;
		}
		return template;
	}

	// public synchronized static TemGroupList getTemplateGroupList(Context
	// context) {
	// TemGroupList list = new TemGroupList();
	// try {
	// list = getDB(context).getTemplateGroupList();
	// } catch (Exception ex) {
	// return null;
	// }
	// return list;
	// }
	//
	// public synchronized static Template getTemplate(Context context, String
	// type) {
	// Template template = new Template();
	// try {
	// template = getDB(context).getTemplate(type);
	// } catch (Exception ex) {
	// return null;
	// }
	// return template;
	// }

	public synchronized static SObject getReport(Context context, Template temp, String code, int type, String key) {
		SObject object;
		try {
			object = getDB(context).getReport(temp, code, type, key);
		} catch (Exception ex) {
			return null;
		}
		return object;
	}

	public synchronized static SObject getDPReport(Context context, Template temp, String code) {
		SObject object;
		try {
			object = getDB(context).getDPReport(temp, code);
		} catch (Exception ex) {
			return null;
		}
		return object;
	}

	public synchronized static SObject getSurveyRpt(Context context, Template temp, String code) {
		SObject object;
		try {
			object = getDB(context).getSurveyRpt(temp, code);
		} catch (Exception ex) {
			return null;
		}
		return object;
	}

	public synchronized static List<SObject> getSubmitObject(Context context) {
		List<SObject> list;
		try {
			list = getDB(context).getSubmitObject();
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static List<SObject> getReadMsgId(Context context) {
		List<SObject> list;
		try {
			list = getDB(context).getReadMsgId();
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

	public synchronized static long saveReport(Context context, SObject rpt) {
		long index;
		try {
			index = getDB(context).saveReport(rpt);
		} catch (Exception ex) {
			return -1;
		}
		return index;
	}

	public synchronized static long saveDP(Context context, SObject rpt) {
		long index;
		try {
			index = getDB(context).saveDP(rpt);
		} catch (Exception ex) {
			return -1;
		}
		return index;
	}

	// public synchronized static boolean creatReport(Context context, Report
	// rpt) {
	// try {
	// if (rpt == null)
	// return true;
	// // if (!((String) rpt.mData.get(-1)).equals(""))
	//
	// getDB(context).createReport(rpt, Rms.getUserId(context));
	//
	// return true;
	// } catch (Exception ex) {
	// return false;
	// }
	// }

	// public synchronized static boolean deletReport(Context context, String
	// rptId) {
	// try {
	// getDB(context).openDatabase();
	// // if (!((String) rpt.mData.get(-1)).equals(""))
	//
	// // getDB(context).deletPhoto(Integer.parseInt(rptId));
	// // getDB(context).deletRptDetail(Integer.parseInt(rptId));
	// getDB(context).deletRpt(Integer.parseInt(rptId));
	//
	// return true;
	// } catch (Exception ex) {
	// return false;
	// } finally {
	// getDB(context).closeDatabase();
	// }
	// }

	public static void execSingleSql(Context context, String sql) {
		try {
			getDB(context).execSingleSql(sql);
		} catch (Exception ex) {
		}
	}

	public static List<DicData> getDictDataList(Context context, String mainDictId, String detaildicId) {
		try {
			return getDB(context).getDicList(mainDictId, detaildicId);

		} catch (Exception ex) {
			return null;
		}
	}

	public static List<DicData> getAnswerList(Context context, String questionId) {
		try {
			return getDB(context).getAnswerList(questionId);

		} catch (Exception ex) {
			return null;
		}
	}

	public static List<DicData> getDictDataList(Context context, String mainDictId) {
		try {
			return getDB(context).getDicList(mainDictId, "");

		} catch (Exception ex) {
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
		} catch (Exception ex) {
			return null;
		}
		return list;
	}

}
