package com.mdx.framework.utility;

import java.util.Calendar;

public class UnitConver {
	public static String getlevNow(long time) {
		Calendar cal = Calendar.getInstance();
		long timel = cal.getTimeInMillis() - time;
		if (timel / 1000 < 60) {
			return "1分钟以内";
		} else if (timel / 1000 / 60 < 60) {
			return timel / 1000 / 60 + "分前";
		} else if (timel / 1000 / 60 / 60 < 24) {
			return timel / 1000 / 60 / 60 + "小时前";
		} else {
			return timel / 1000 / 60 / 60 / 24 + "天前";
		}
	}

	public static String getBytesSize(Object obj) {
		if (obj == null) {
			return "";
		}
		long sized = Long.parseLong(obj.toString());
		double size = sized;
		if (size > 1024) {
			size = size / 1024;
			if (size > 1024) {
				size = size / 1024;
				if (size > 1024) {
					size = size / 1024d;
					return Dou2Str(size, 2) + "GB";
				} else {
					return Dou2Str(size, 2) + "MB";
				}
			} else {
				return Dou2Str(size, 0) + "KB";
			}
		} else {
			return Dou2Str(size, 0) + "B";
		}
	}
	
	public static String Dou2Str(double d, int count) {
		double dou = Math.abs(d);
		int rate = 1;
		for (int i = 0; i < count; i++) {
			rate *= 10;
		}
		dou = Math.round(dou * rate);
		dou = dou / rate;
		if (dou - Math.round(dou) == 0) {
			return (d < 0 ? "-" : "") + String.valueOf((int) dou);
		}
		return (d < 0 ? "-" : "") + String.valueOf(dou);
	}
}
