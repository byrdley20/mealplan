package com.meals.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	public static Date addMonths(Date date1, int months) {
		Calendar calendar = createCalendar(date1);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	public static int findDaysBetweenDates(Date date1, Date date2) {
		BigDecimal diff = new BigDecimal(findDayStart(date2).getTime() - findDayStart(date1).getTime());
		BigDecimal result = diff.divide(new BigDecimal(1000 * 60 * 60 * 24l), 0, RoundingMode.CEILING);
		return result.intValue();
	}

	public static Date findYearStartDate(Date date) {
		return DateUtil.findYearStartDate(date, null);
	}

	public static Date findYearStartDate(Date date, Integer year) {
		Calendar startDateCal = createCalendar(date);
		startDateCal.set(Calendar.MONTH, startDateCal.getActualMinimum(Calendar.MONTH));
		startDateCal.set(Calendar.DAY_OF_MONTH, 1);
		if (year != null) {
			startDateCal.set(Calendar.YEAR, year);
		}
		startDateCal.setTime(findDayStart(startDateCal.getTime()));
		return startDateCal.getTime();
	}

	public static Date findYearEndDate(Date date) {
		return DateUtil.findYearEndDate(date, null);
	}

	public static Date findYearEndDate(Date date, Integer year) {
		Calendar endDateCal = createCalendar(date);
		endDateCal.set(Calendar.MONTH, endDateCal.getActualMaximum(Calendar.MONTH));
		endDateCal.set(Calendar.DAY_OF_MONTH, endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		if (year != null) {
			endDateCal.set(Calendar.YEAR, year);
		}
		endDateCal.setTime(findDayEnd(endDateCal.getTime()));
		return endDateCal.getTime();
	}

	public static Calendar findMonthStartDate(Integer month, Integer year) {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(Calendar.DAY_OF_MONTH, 1);
		if (month != null) {
			startDateCal.set(Calendar.MONTH, month - 1);
		}
		if (year != null) {
			startDateCal.set(Calendar.YEAR, year);
		}
		startDateCal.setTime(findDayStart(startDateCal.getTime()));
		return startDateCal;
	}

	public static Calendar findMonthEndDate(Integer month, Integer year) {
		Calendar endDateCal = Calendar.getInstance();
		if (month != null) {
			endDateCal.set(Calendar.MONTH, month - 1);
		}
		if (year != null) {
			endDateCal.set(Calendar.YEAR, year);
		}
		endDateCal.set(Calendar.DAY_OF_MONTH, endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		endDateCal.setTime(findDayEnd(endDateCal.getTime()));
		return endDateCal;
	}

	public static Date findDayStart(Date date) {
		Calendar calendar = createCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
		return calendar.getTime();
	}

	public static Date findDayEnd(Date date) {
		Calendar calendar = createCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
		return calendar.getTime();
	}

	public static int getYear(Date date) {
		Calendar calendar = createCalendar(date);
		return calendar.get(Calendar.YEAR);
	}

	private static Calendar createCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
}

