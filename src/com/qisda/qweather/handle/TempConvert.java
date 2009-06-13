package com.qisda.qweather.handle;

import com.qisda.qweather.QWeather;

/*
 * To convert the temperature style from F to C,or from C to F.
 * You may need to edit here if you find the displaying temperature is wrong.
 * */
public class TempConvert
{

	public static int FToC(int F)
	{

		if (QWeather.bAtOffice)
		{
			return F;
		} else
		{
			return (int) ((5.0f / 9.0f) * (F - 32));
		}
	}

	public static int CToF(int C)
	{
		if (QWeather.bAtOffice)
		{
			return C;
		} else
		{
			return (int) ((9.0f / 5.0f) * C + 32);
		}
	}

}
