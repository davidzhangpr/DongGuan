package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.UIItem;
import markettracker.util.Constants.ControlType;
import orient.champion.business.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimeBuilder extends Builder
{
	
	private DatePicker datePicker;
	
	private TimePicker timePicker;
	
	private int years, months, days, hours, minutes;
	
	private Fields data;
	private UIItem item;
	
	public DateTimeBuilder(Context context, Fields data, UIItem item)
	{
		super(context);
		this.data = data;
		this.item = item;
		init(context, data, item);
	}
	
	private String getResult()
	{
		return data.getStrValue(item.getDataKey());
	}
	
	private void init(Context context, Fields data, UIItem item)
	{
		if (item.getControlType() == ControlType.DATATIME)
		{
			View view = LayoutInflater.from(context).inflate(R.layout.datetime, null);
			datePicker = (DatePicker) view.findViewById(R.id.datePicker1);
			timePicker = (TimePicker) view.findViewById(R.id.timePicker1);
			String datetime = getResult();
			if (datetime.equals(""))
			{
				Time time = new Time();
				time.setToNow();
				years = time.year;
				months = time.month;
				days = time.monthDay;
				hours = time.hour;
				minutes = time.minute;
			}
			else
			{
				years = Integer.parseInt(datetime.substring(0, 4));
				months = Integer.parseInt(datetime.substring(5, 7)) - 1;
				days = Integer.parseInt(datetime.substring(8, 10));
				hours = Integer.parseInt(datetime.substring(11, 13));
				minutes = Integer.parseInt(datetime.substring(14, 16));
			}
			
			datePicker.init(years, months, days, new OnDateChangedListener()
			{
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{
					// String date=year + "-"
					// + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) :
					// (monthOfYear + 1)) + "-"
					// + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
					// if()
					
					years = year;
					months = monthOfYear;
					days = dayOfMonth;
				}
			});
			
			timePicker.setCurrentHour(hours);
			timePicker.setCurrentMinute(minutes);
			timePicker.setOnTimeChangedListener(new OnTimeChangedListener()
			{
				
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
				{
					hours = hourOfDay;
					minutes = minute;
				}
				
			});
			this.setView(view);
		}
		
		else if (item.getControlType() == ControlType.DATE)
		{
			View view = LayoutInflater.from(context).inflate(R.layout.date, null);
			datePicker = (DatePicker) view.findViewById(R.id.datePicker1);
			String datetime = getResult();
			if (datetime.equals(""))
			{
				Time time = new Time();
				time.setToNow();
				years = time.year;
				months = time.month;
				days = time.monthDay;
				
			}
			else
			{
				years = Integer.parseInt(datetime.substring(0, 4));
				months = Integer.parseInt(datetime.substring(5, 7)) - 1;
				days = Integer.parseInt(datetime.substring(8, 10));
			}
			
			datePicker.init(years, months, days, new OnDateChangedListener()
			{
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{
					years = year;
					months = monthOfYear;
					days = dayOfMonth;
				}
			});
			
			this.setView(view);
		}
		else if (item.getControlType() == ControlType.TIME)
		{
			View view = LayoutInflater.from(context).inflate(R.layout.time, null);
			timePicker = (TimePicker) view.findViewById(R.id.timePicker1);
			timePicker.setIs24HourView(true);
			String datetime = getResult();
			if (datetime.equals(""))
			{
				Time time = new Time();
				time.setToNow();
				hours = time.hour;
				minutes = time.minute;
				
			}
			else
			{
				hours = Integer.parseInt(datetime.substring(0, 2));
				minutes = Integer.parseInt(datetime.substring(3, 5));
			}
			
			timePicker.setCurrentHour(hours);
			timePicker.setCurrentMinute(minutes);
			timePicker.setOnTimeChangedListener(new OnTimeChangedListener()
			{
				
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
				{
					hours = hourOfDay;
					minutes = minute;
				}
				
			});
			
			this.setView(view);
		}
	}
	
	public String getDatetime()
	{
		String datetime = "";
		if (item.getControlType() == ControlType.DATATIME)
			datetime += years + "-" + ((months + 1) < 10 ? "0" + (months + 1) : (months + 1)) + "-" + (days < 10 ? "0" + days : days) + " " + (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
		else if (item.getControlType() == ControlType.DATE)
			datetime += years + "-" + ((months + 1) < 10 ? "0" + (months + 1) : (months + 1)) + "-" + (days < 10 ? "0" + days : days);
		
		else
			
			datetime += (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
		
		return datetime;
	}
}
