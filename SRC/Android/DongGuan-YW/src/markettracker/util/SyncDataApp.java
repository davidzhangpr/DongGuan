package markettracker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreConnectionPNames;

import markettracker.comm.ExceptionResponse;
import markettracker.comm.MarketRequestFactory;
import markettracker.comm.MarketResponseFactory;
import markettracker.comm.MarketTracker;
import markettracker.comm.QuerySoapResponse;
import markettracker.comm.Request;
import markettracker.comm.Response;
import markettracker.comm.ResponseListener;
import markettracker.comm.UpsertSoapResponse;
import markettracker.comm.getServerTimeSoapResponse;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.QueryResult;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.UpsertResult;

public class SyncDataApp extends Application
{
	
	private Context context;
	private boolean startThread = true;
	// private boolean start = false;
	private static final String ANDROID = "Android";
	private static final String SOAP_RESPONSE = "response";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String RESPONSE_TYPE = "responseType";
	private List<SObject> sObjectsSub;
	
	List<Activity> list = new ArrayList<Activity>();
	
	@Override
	public void onCreate()
	{
		init();
	}
	
	private void init()
	{
		context = this;
		MarketTracker.init(context);
	}
	
	/**
	 * 压栈（）添加Activity到集合中
	 */
	public void pushActivity(Activity activity) {
		list.add(activity);
	}

	/**
	 * 出栈（）删除集合中的Activity
	 */
	public void pullActivity(Activity activity) {
		list.remove(activity);
	}

	/**
	 * 清空栈中所有的activity
	 */
	public void exit() {
		if(list.size() > 0){
			for (int i = list.size() - 1; i >=0; i--) {
				list.get(i).finish();
			}
			list.clear();
		}
	}
	
	public void Login(LoginConfig config, Handler handler)
	{
		MarketTracker.login(config, new ResponseListener(context, handler));
	}
	
	public void Query(QueryConfig config, Handler handler, Activity activity)
	{
		MarketTracker.query(config, new ResponseListener(handler, context, config, activity));
	}
	
	public void GetServerTime(Handler handler, Activity activity)
	{
		MarketTracker.getServerTime(new ResponseListener(handler, context, activity));
	}
	
	public void Upload(List<SObject> list, Handler handler)
	{
		MarketTracker.upload(list, new ResponseListener(list, context, handler));
	}
	
	public synchronized void startSyncData()
	{
		startThread = true;
	}
	
	public synchronized void stopSyncData()
	{
		startThread = false;
	}
	
	public void stopGetMsg() {
		start = false;
	}
	
	private QueryConfig getQueryConfig()
	{
		QueryConfig config;
		config = new QueryConfig();
		config.setUserId(Rms.getUserId(context));
		config.setIsAll("0");
		config.setStartRow("0");
		config.setLastTime(Tool.getCurrDate());
		config.setType("GetMessageList");
		return config;
	}
	
	public void startSync()
	{
		new Thread()
		{
			public void run()
			{
				while (true)
				{
					try
					{
						Thread.sleep(1000 * 60 * 10);
						startSyncData();
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	private boolean getServerTime = false;
	
	public void syncRpt(final Handler handler)
	{
		new Thread()
		{
			String type = null;
			Object object = null;
			
			public void run()
			{
				
				while (true)
				{
					while (startThread)
					{
						if (Tool.isConnect(context))
						{
							sObjectsSub = null;
							if (getServerTime)
							{
								type = Constants.SyncType.GETSERVERTIME;
								object = null;
							}
							
							List<SObject> slist = Sqlite.getSubmitObject(context);
							if (slist != null && slist.size() > 0)
							{
								type = Constants.SyncType.UPLOAD;
								object = slist;
								sObjectsSub = slist;
								
							}
							else
							{
								List<SObject> slist1 = Sqlite.getReadMsgId(context);
								if (slist1 != null && slist1.size() > 0)
								{
									type = Constants.SyncType.UPLOADMSG;
									object = slist1;
									sObjectsSub = slist1;
								}
								else
								{
									stopSyncData();
									// startThread = false;
									type = Constants.SyncType.QUERY;
									object = getQueryConfig();
								}
							}
							// listener = new RequestListener(type);
							
							AndroidHttpClient httpClient = AndroidHttpClient.newInstance(ANDROID);
							httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20 * 1000);
							httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
							
							Response qr = null;
							Request request = MarketRequestFactory.getSoapRequest(type, object);
							String SOAPRequestXML = request.getRequest();
							try
							{
								HttpPost httppost;
								httppost = new HttpPost(MarketTracker.getMarket().getServerURL(type));
								HttpEntity entity = new StringEntity(SOAPRequestXML);
								
								httppost.setEntity(entity);
								httppost.setHeader(CONTENT_TYPE, "text/xml; charset=utf-8");
								httppost.setHeader("Accept-Encoding", "gzip");
								
								HttpResponse response = httpClient.execute(httppost);
								
								InputStream is = response.getEntity().getContent();
								
								Header contentEncoding = response.getFirstHeader("Content-Encoding");
								if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))
								{
									is = new GZIPInputStream(is);
								}
								
								String soapResponseString = null;
								BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));
								StringBuffer sb = new StringBuffer();
								String line;
								while ((line = br.readLine()) != null)
								{
									sb.append(line).append("\n");
								}
								soapResponseString = sb.toString();
								
								Bundle bundle = new Bundle();
								if (response.getStatusLine().getStatusCode() == 200)
								{
									bundle.putString(RESPONSE_TYPE, type);
								}
								else
								{
									bundle.putString(RESPONSE_TYPE, "Exception");
									bundle.putString(SOAP_RESPONSE, "网络异常！");
								}
								bundle.putString(SOAP_RESPONSE, soapResponseString);
								qr = MarketResponseFactory.getSoapResponse(bundle);
								
							}
							catch (ClientProtocolException e)
							{
								Bundle bundle = new Bundle();
								bundle.putString(RESPONSE_TYPE, "Exception");
								bundle.putString(SOAP_RESPONSE, "网络异常！" + e.getMessage());
								qr = MarketResponseFactory.getSoapResponse(bundle);
							}
							catch (IOException ioe)
							{
								Bundle bundle = new Bundle();
								bundle.putString(RESPONSE_TYPE, "Exception");
								bundle.putString(SOAP_RESPONSE, "网络异常！" + ioe.getMessage());
								qr = MarketResponseFactory.getSoapResponse(bundle);
							}
							catch (Exception e)
							{
								Bundle bundle = new Bundle();
								bundle.putString(RESPONSE_TYPE, "Exception");
								bundle.putString(SOAP_RESPONSE, "网络异常！" + e.getMessage());
								qr = MarketResponseFactory.getSoapResponse(bundle);
							}
							finally
							{
								if (httpClient != null)
								{
									httpClient.close();
									httpClient.getConnectionManager().shutdown();
									httpClient = null;
								}
							}
							if (qr instanceof ExceptionResponse)
							{
								try
								{
									handler.sendMessage(handler.obtainMessage(Constants.PropertyKey.ERR, ((ExceptionResponse) qr).getResult()));
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								
							}
							else
							{
								if (this.type.equals(Constants.SyncType.UPLOADMSG))
								{
									UpsertSoapResponse queryresponse = (UpsertSoapResponse) qr;
									uploadMsgResult((ArrayList<UpsertResult>) queryresponse.getResult());
								}
								else if (this.type.equals(Constants.SyncType.UPLOAD))
								{
									try
									{
										UpsertSoapResponse queryresponse = (UpsertSoapResponse) qr;
										uploadResult((ArrayList<UpsertResult>) queryresponse.getResult());
										handler.sendMessage(handler.obtainMessage(Constants.PropertyKey.UPLOAD, queryresponse));
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
									
								}
								else if (this.type.equals(Constants.SyncType.QUERY))
								{
									QuerySoapResponse queryresponse = (QuerySoapResponse) qr;
									queryResult((QueryResult) queryresponse.getResult());
									handler.sendMessage(handler.obtainMessage(Constants.PropertyKey.QUERY, queryresponse));
								}
								else if (this.type.equals(Constants.SyncType.GETSERVERTIME))
								{
									getServerTimeSoapResponse queryresponse = null;
									queryresponse = (getServerTimeSoapResponse) qr;
									Tool.setServerTimes(queryresponse.getResult());
									
									handler.sendMessage(handler.obtainMessage(Constants.PropertyKey.GETSERVERTIME, ""));
								}
							}
						}
					}
					try
					{
						Thread.sleep(1000 * 30);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void getMyTime()
	{
		new Thread()
		{
			public void run()
			{
				while (true)
				{
					Tool.addServerTimes();
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			
		}.start();
	}
	
	private void queryResult(Object sObjects)
	{
		try
		{
			final QueryResult qresult = (QueryResult) sObjects;
			if (qresult.isSuccess() == 1)
			{
				
				List<SObject> sObjectList = qresult.getClientTable();
				if (Sqlite.upsertData(context, sObjectList, qresult.getType()))
				{
					
				}
			}
		}
		catch (Exception ex)
		{
		}
	}
	
	@SuppressWarnings("unchecked")
	private void uploadMsgResult(Object sObjects)
	{
		try
		{
			List<UpsertResult> list = (List<UpsertResult>) sObjects;
			UpsertResult reslut = null;
			SObject rpt;
			List<String> sqlList = new ArrayList<String>();
			for (int i = 0; i < sObjectsSub.size(); i++)
			{
				rpt = sObjectsSub.get(i);
				reslut = list.get(0);
				if (reslut.isSuccess() == 1)
				{
					sqlList.add("update t_message_detail set issubmit = 1" + " where serverid =" + rpt.getField("MsgId"));
				}
				else if (reslut.isSuccess() == -1)
				{
					sqlList.add("update t_message_detail set issubmit = 0, errmsg='" + reslut.getErrorMsg() + "' where serverid =" + rpt.getField("MsgId"));
				}
			}
			if (sqlList.size() > 0)
				Sqlite.execSqlList(context, sqlList);
			// handler.sendMessage(handler.obtainMessage(PropertyKey.UPLOADMSG,
			// list));
		}
		catch (Exception ex)
		{
			// sendSyncErrMsg(handler, "数据上传失败");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void uploadResult(Object sObjects)
	{
		try
		{
			List<UpsertResult> list = (List<UpsertResult>) sObjects;
			
			UpsertResult reslut = null;
			SObject rpt;
			List<String> sqlList = new ArrayList<String>();
			for (int i = 0; i < sObjectsSub.size(); i++)
			{
				rpt = sObjectsSub.get(i);
				reslut = list.get(i);
				if (reslut.isSuccess() == 1)
				{
					//促销活动接收服务器返回的报告id
					if("3".equals(sObjectsSub.get(0).getSValue("TemplateId")) || "13".equals(sObjectsSub.get(0).getSValue("TemplateId"))){
						sqlList.add("update t_data_callReport set issubmit = 1, serverid = '"+reslut.getRptId()+"'" + " where " + Constants.TableInfo.TABLE_KEY + " =" + rpt.getField(Constants.TableInfo.TABLE_KEY));
					}else{
						sqlList.add("update t_data_callReport set issubmit = 1" + " where " + Constants.TableInfo.TABLE_KEY + " =" + rpt.getField(Constants.TableInfo.TABLE_KEY));
					}

					sqlList.add("update T_Visit_Plan_Detail set str1 ='1'" + " where clientid ='" + rpt.getSValue("ClientId") + "'");
				}
				else if (reslut.isSuccess() == -1)
				{
					sqlList.add("update t_data_callReport set issubmit = 0, errmsg='" + reslut.getErrorMsg() + "'" + " where " + Constants.TableInfo.TABLE_KEY + " =" + rpt.getField(Constants.TableInfo.TABLE_KEY));
				}
			}
			if (sqlList.size() > 0)
				Sqlite.execSqlList(context, sqlList);
		}
		catch (Exception ex)
		{
		}
	}
	
	private boolean start = false;
	
	public void QueryMessage(final Handler handler, final Activity activity) {
		final QueryConfig config;
		config = new QueryConfig();
		config.setUserId(Rms.getUserId(context));
		config.setIsAll("0");
		config.setStartRow("0");
		config.setLastTime(Tool.getCurrDate());
		config.setType("GetMessageList");
		start = true;
		new Thread() {
			public void run() {
				while (start) {
					if (Tool.isConnect(context)) {
						MarketTracker.query(config, new ResponseListener(
								handler, context, config, activity));
					}
					try {
						Thread.sleep(1000 * 60 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
}
