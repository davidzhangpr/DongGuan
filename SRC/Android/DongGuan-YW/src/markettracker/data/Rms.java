package markettracker.data;

import markettracker.util.Tool;
import android.content.Context;
import android.content.SharedPreferences;

public class Rms
{
	private static final String PARROWTECH = "parrowtech";
	private static final String M_MOBILE = "m_moblie";
	private static final String USER_ID = "userId";
	private static final String USER_NAME = "username";
	private static final String USER_PWD = "password";
	private static final String LOGIN_FIRST = "first";
	private static final String LOGIN_DATE = "logindate";
	private static final String UPDATE_DATE = "time";
	
	private static final String SAVEPWD = "savepwd";
	
	private static final String STARTTIME = "starttime";
	private static final String ENDTIME = "endtime";
	private static final String RUSER_NAME = "rusername";
	
	private static final String EmpName = "empName";
	private static final String BrandID = "brandID";
	
	private static final String DeclareType = "declareType";
	
	private static final String IsChange = "isChange";

	private static final String CHECKPRODUCT = "checkProduct";
	
	private static final String ISCONNENT = "isConnent";
	
	private static SharedPreferences mRms;
	
	private static SharedPreferences getRms(Context context)
	{
		if (mRms == null)
			mRms = context.getSharedPreferences(PARROWTECH, Context.MODE_PRIVATE);
		return mRms;
	}
	
	private static void putBoolean(Context context, String key, boolean value)
	{
		getRms(context).edit().putBoolean(key.toLowerCase(), value).commit();
	}
	
	private static void putString(Context context, String key, String value)
	{
		getRms(context).edit().putString(key.toLowerCase(), value).commit();
	}
	
	private static String getString(Context context, String key, String defaultValue)
	{
		return getRms(context).getString(key.toLowerCase(), defaultValue);
	}
	
	private static String getString(Context context, String key)
	{
		return getRms(context).getString(key.toLowerCase(), "");
	}
	
	private static boolean getBoolean(Context context, String key, boolean defaultValue)
	{
		return getRms(context).getBoolean(key.toLowerCase(), defaultValue);
	}
	
	@SuppressWarnings("unused")
	private static boolean getBoolean(Context context, String key)
	{
		return getRms(context).getBoolean(key.toLowerCase(), false);
	}
	
	public static void setUserId(Context context, String userId)
	{
		putString(context, USER_ID, userId);
	}
	
	public static String getUserId(Context context)
	{
		return getString(context, USER_ID);
	}
	
	public static void setManagerMobile(Context context, String managerMobile)
	{
		putString(context, M_MOBILE, managerMobile);
	}
	
	public static String getManagerMobile(Context context)
	{
		return getString(context, M_MOBILE);
	}
	
	public static void setUserName(Context context, String userName)
	{
		putString(context, USER_NAME, userName);
	}
	
	public static String getUserName(Context context)
	{
		
		return getString(context, USER_NAME);
	}
	
	public static void setRUserName(Context context, String userName)
	{
		putString(context, RUSER_NAME, userName);
	}
	
	public static String getRUserName(Context context)
	{
		
		return getString(context, RUSER_NAME);
	}
	
	public static void setPwd(Context context, String userPwd)
	{
		
		putString(context, USER_PWD, userPwd);
	}
	
	public static String getPwd(Context context)
	{
		
		return getString(context, USER_PWD);
	}
	
	public static void setSavePwd(Context context, boolean isSavePwd)
	{
		putBoolean(context, SAVEPWD, isSavePwd);
	}
	
	public static boolean isSavePwd(Context context)
	{
		return getBoolean(context, SAVEPWD, false);
	}
	
	public static void setFirst(Context context, boolean isFirst)
	{
		putBoolean(context, LOGIN_FIRST, isFirst);
	}
	
	public static boolean getFirst(Context context)
	{
		return getBoolean(context, LOGIN_FIRST, true);
	}
	
	public static void setLoginDate(Context context, String date)
	{
		putString(context, LOGIN_DATE, date);
	}
	
	public static String getLoginDate(Context context)
	{
		
		return getString(context, LOGIN_DATE);
	}
	
	public static void setUpdateTime(Context context, String time)
	{
		putString(context, UPDATE_DATE, time);
	}
	
	public static String getUpdateTime(Context context)
	{
		return getString(context, UPDATE_DATE, Tool.getCurrDateTime());
	}
	
	public static void setStartTime(Context context, String starttime)
	{
		putString(context, STARTTIME, starttime);
	}
	
	public static String getStartTime(Context context)
	{
		return getString(context, STARTTIME);
	}
	
	public static void setEndTime(Context context, String endtime)
	{
		putString(context, ENDTIME, endtime);
	}
	
	public static String getEndTime(Context context)
	{
		return getString(context, ENDTIME);
	}
	
	public static void setEmpName(Context context, String empName) {
		putString(context, EmpName, empName);
	}
	
	public static String getEmpName(Context context) {
		return getString(context,EmpName);
	}
	
	public static void setBrandID(Context context, String brandID) {
		putString(context, BrandID, brandID);
	}
	
	public static String getBrandID(Context context) {
		return getString(context,BrandID);
	}
	
	public static void setDeclareType(Context context, String declareType) {
		putString(context, DeclareType, declareType);
	}
	
	public static String getDeclareType(Context context) {
		return getString(context,DeclareType);
	}
	
	public static void setIsChange(Context context, Boolean isChange) {
		putBoolean(context, IsChange, isChange);
	}
	
	public static Boolean getIsChange(Context context) {
		return	getBoolean(context, IsChange);
	}

	public static void setCheckProduct(Context context, Boolean checkproduct) {
		putBoolean(context, CHECKPRODUCT, checkproduct);
	}
	
	public static Boolean getCheckProduct(Context context) {
		return	getBoolean(context, CHECKPRODUCT , false);
	}
	
	public static void setIsConnent(Context context, boolean isConnent) {
		putBoolean(context,ISCONNENT, isConnent);
	}
	
	public static boolean getIsConnent(Context context) {
		return getBoolean(context,ISCONNENT, false);	
	}
	
}
