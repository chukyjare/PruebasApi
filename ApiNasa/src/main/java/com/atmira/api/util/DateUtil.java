package com.atmira.api.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class DateUtil.
 */
public class DateUtil {

	/**
	 * Gets the dates.
	 *
	 * @param days the days
	 * @return the dates
	 */
	public static List<String> getDates(byte days) {
		List<String> listDates = new ArrayList<String>();
		LocalDateTime localDateTimeNow = LocalDateTime.now();
		LocalDate localDateNow = localDateTimeNow.toLocalDate();
		String stringDateNow = localDateNow.toString();
		listDates.add(stringDateNow);
		for (int i = 1; i < days; i++) {
			LocalDateTime localDateTime = localDateTimeNow.plusDays(i);
			LocalDate localDate = localDateTime.toLocalDate();
			String stringDate = localDate.toString();
			listDates.add(stringDate);
		}
		return listDates;
	}
}
