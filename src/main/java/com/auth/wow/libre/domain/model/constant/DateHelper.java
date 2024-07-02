package com.auth.wow.libre.domain.model.constant;

import lombok.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DateHelper {

  private static final Map<String, ZoneId> ZONE_IDS = createZoneIdsMap();

  /**
   * Get Zone Id By Site
   */
  public static ZoneId getZoneIdBySite(String siteId) {
    return ZONE_IDS.get(siteId);
  }

  /**
   * Get LocalDateTime
   */
  public static LocalDateTime getLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  /**
   * Get LocalDateTimeAndSiteId
   */
  public static LocalDateTime getLocalDateTime(Date date, String siteId) {
    return LocalDateTime.ofInstant(date.toInstant(), getZoneIdBySite(siteId));
  }

  /**
   * Format Date From String
   */
  public static Date formatDateFromString(String dateToParse, ZoneId zoneId) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.parse(dateToParse, formatter);
    return Date.from(localDateTime.atZone(zoneId).toInstant());
  }

  public static Date localDateFromDate(@NonNull LocalDate localDate, @NonNull ZoneId zoneId) {
    return Date.from(localDate.atStartOfDay(zoneId).toInstant());
  }

  public static String dateToString(@NonNull Date date) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    return dateFormat.format(date);
  }

  public static Date incrementMinutes(Date baseDate, Integer minute) {
    return Optional.ofNullable(baseDate).map(date -> {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(Calendar.MINUTE, minute);
      return cal.getTime();
    }).orElse(null);
  }

  private static Map<String, ZoneId> createZoneIdsMap() {
    Map<String, ZoneId> zoneIdMap = new HashMap<>();
    zoneIdMap.put("WLM", ZoneId.of("UTC-5"));
    zoneIdMap.put("WLA", ZoneId.of("UTC-3"));
    zoneIdMap.put("WLB", ZoneId.of("UTC-3"));
    zoneIdMap.put("WLC", ZoneId.of("UTC-3"));
    zoneIdMap.put("WCO", ZoneId.of("UTC-5"));

    return Collections.unmodifiableMap(zoneIdMap);
  }

}
