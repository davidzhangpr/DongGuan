package markettracker.util;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.Rms;
import markettracker.data.SObject;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class SyncData
{
	
	public static void Login(LoginConfig config, Handler handler, Activity activity)
	{
		((SyncDataApp) activity.getApplication()).Login(config, handler);
	}
	
	public static void Query(QueryConfig config, Handler handler, Activity activity)
	{
		((SyncDataApp) activity.getApplication()).Query(config, handler, activity);
	}
	
	public static void GetServerTime(Handler handler, Activity activity)
	{
		((SyncDataApp) activity.getApplication()).GetServerTime(handler, activity);
	}
	
	public static void Upload(List<SObject> list, Handler handler, Activity activity)
	{
		((SyncDataApp) activity.getApplication()).Upload(list, handler);
	}
	
	public static void syncData(Handler handler, Activity activity)
	{
		((SyncDataApp) activity.getApplication()).syncRpt(handler);
	}
	
	// public static void startMyTimer(Activity activity) {
	// ((SyncDataApp) activity.getApplication()).getMyTime();
	// }
	
	public static void startSyncData(Activity activity)
	{
		((SyncDataApp) activity.getApplication()).startSyncData();
	}
	
	public static void startSync(Activity activity)
	{
		((SyncDataApp) activity.getApplication()).startSync();
	}
	
	public static void StartQueryMsg(Handler handler,
			Activity activity) {
		((SyncDataApp) activity.getApplication()).QueryMessage(handler,
				activity);
	}
	
	public static void stopSyncData(Activity activity) {
		((SyncDataApp) activity.getApplication()).stopSyncData();
	}
	
	public static void StopQueryMsg(Activity activity) {
		((SyncDataApp) activity.getApplication()).stopGetMsg();
	}
	
	public static ArrayList<SObject> getPDAVersion(Context context)
	{
		ArrayList<SObject> objs = new ArrayList<SObject>();
		SObject obj;
		
		obj = new SObject();
		obj.setType("PDAPublishVersion__c");
		obj.setField("Name", "MSRVERSION");
		
		obj.setField("PublishDate__c", "2012-06-11");
		obj.setField("Version__c", Tool.GetCurrVersion(context));
		obj.setField("Remark__c", "this is the May release for MJN MSR on android");
		
		obj.setField("UserIdVersion__c", Rms.getUserId(context) + Tool.GetCurrVersion(context));
		objs.add(obj);
		
		return objs;
	}
	
	public static void create(ArrayList<SObject> objs, Context context, Handler handler, int iType)
	{
		ArrayList<SObject> SObjects = new ArrayList<SObject>();
		int Count = objs.size(), index;
		if (Count > 180)
		{
			for (int i = 0; i < 180; i++)
			{
				index = Count - i - 1;
				SObjects.add(objs.get(index));
				objs.remove(index);
			}
		}
		else
		{
			SObjects = objs;
			objs = null;
		}
		// Salesforce.create(SObjects, new QueryResponseListener(iType, context,
		// handler, objs, SObjects));
		
	}
	
	public static void update(ArrayList<SObject> objs, Context context, Handler handler, int iType)
	{
		ArrayList<SObject> SObjects = new ArrayList<SObject>();
		int Count = objs.size(), index;
		if (Count > 180)
		{
			for (int i = 0; i < 180; i++)
			{
				index = Count - i - 1;
				SObjects.add(objs.get(index));
				objs.remove(index);
			}
		}
		else
		{
			SObjects = objs;
			objs = null;
		}
		// Salesforce.update(SObjects, new QueryResponseListener(iType, context,
		// handler, objs, SObjects));
	}
	
	public static void upsert(ArrayList<SObject> objs, Context context, Handler handler, int iType)
	{
		ArrayList<SObject> SObjects = new ArrayList<SObject>();
		int Count = objs.size(), index;
		if (Count > 180)
		{
			for (int i = 0; i < 180; i++)
			{
				index = Count - i - 1;
				SObjects.add(objs.get(index));
				objs.remove(index);
			}
		}
		else
		{
			SObjects = objs;
			objs = null;
		}
		// if (iType == Constants.PropertyKey.ANDROIDVERSION)
		// MarketTracker.upsert(Constants.ExternalId.VERSIONID, SObjects,
		// new QueryResponseListener(iType, context, handler, objs,
		// SObjects));
		// else
		// MarketTracker.upsert(Constants.ExternalId.EXTERNALID, SObjects,
		// new QueryResponseListener(iType, context, handler, objs,
		// SObjects));
	}
	
	public static void delete(ArrayList<SObject> objs, Context context, Handler handler, int iType)
	{
		ArrayList<SObject> SObjects = new ArrayList<SObject>();
		int Count = objs.size(), index;
		if (Count > 180)
		{
			for (int i = 0; i < 180; i++)
			{
				index = Count - i - 1;
				SObjects.add(objs.get(index));
				objs.remove(index);
			}
		}
		else
		{
			SObjects = objs;
			objs = null;
		}
		String[] delList = new String[SObjects.size()];
		SObject sObject;
		for (int i = 0; i < SObjects.size(); i++)
		{
			sObject = SObjects.get(i);
			delList[i] = sObject.getField("serverid");
		}
		// Salesforce.delete(delList, new QueryResponseListener(iType, context,
		// handler, objs, SObjects));
	}
	
}
