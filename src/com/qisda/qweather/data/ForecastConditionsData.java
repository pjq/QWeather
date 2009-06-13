package com.qisda.qweather.data;

public class ForecastConditionsData
{
	/*
	 * These variables name are named according to the XML.
	 */
	private String	day_of_week;
	private int		lowTemp;
	private int		highTemp;
	private String	icon;
	private String	condition;

	/*
	 * Setter and Getter
	 */
	public String getDayOfWeek()
	{
		return this.day_of_week;
	}

	public void setDayOfWeek(String day_of_week)
	{
		this.day_of_week = day_of_week;
	}

	public int getLowTemp()
	{
		return this.lowTemp;
	}

	public void setLowTemp(int i)
	{
		this.lowTemp = i;
	}

	public int getHighTemp()
	{
		return this.highTemp;
	}

	public void setHighTemp(int highTemp)
	{
		this.highTemp = highTemp;
	}

	public String getIcon()
	{
		return this.icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getCondition()
	{
		return this.condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

}
