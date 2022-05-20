package com.ukrnet.vx.evaluator;

import com.ukrnet.vx.exceptions.DataFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.PatternSyntaxException;
public class WaitingTimelineEvaluator implements StringEvaluator<Integer>{
     private static final String UNI_MATCHER = "*";
     private static final String FIRST_ANSWER = "P";
     private static final String NEXT_ANSWER = "N";
     private static final String DATE_FORMAT = "d.MM.uuuu";
     private static final String ID_SPLITTER = "\\.";
     private static final String DATE_SPLITTER = "-";
     private static final String DATE_EX = "Invalid date - ";
     private static final String QUERY_DATE_EX = "Invalid query date - ";
     private static final String ID_EX = "Invalid serviceId or questionId - ";
     private static final String TYPE_EX = "Invalid response type - ";
     private static final String VALUE_EX = "Wrong waiting timeline value - ";

     private String[] serviceId;
     private String[] questionId;
     private String responseType;
     private LocalDate[] date;

     private int result;

     public WaitingTimelineEvaluator(String[] query) throws DataFormatException {
         parseQuery(query);
     }

     @Override
     public boolean evaluate(String[] s) throws DataFormatException {
         if (!responseType.equals(getResponseType(s[3]))) {
             return false;
         }
         if (!compareServiceId(getServiceOrQuestionId(s[1]))
                 || !compareQuestionId(getServiceOrQuestionId(s[2]))) {
             return false;
         }
         if(!compareDate(getDate(s[4]))) {
             return false;
         }
         try {
             result = Integer.parseInt(s[5]);
         } catch (NumberFormatException e) {
             throw new DataFormatException(VALUE_EX + s[5]);
         }
         return true;
     }

     @Override
     public Integer getValue() {
        return result;
    }

     private boolean compareDate(LocalDate sDate) {
         if (date[1] != null) {
             return !sDate.isBefore(date[0]) && !sDate.isAfter(date[1]);
         } else {
             return sDate.isEqual(date[0]);
         }

     }

     private boolean compareQuestionId(String[] text) {
         if (!questionId[0].equals(UNI_MATCHER)) {
             if (text.length < questionId.length) {
                 return false;
             } else {
                 for(int i = 0; i < questionId.length; i++) {
                     if(!questionId[i].equals(text[i])) {
                         return false;
                     }
                 }
             }
         }
         return true;
     }

     private boolean compareServiceId(String[] text) {
         if (text.length < serviceId.length) {
             return false;
         } else {
             for(int i = 0; i < serviceId.length; i++) {
                 if(!serviceId[i].equals(text[i])) {
                        return false;
                 }
             }
         }
         return true;
     }

     private String[] getServiceOrQuestionId(String text) throws DataFormatException {
         try {
             if (!text.equals(UNI_MATCHER)) {
                 return text.split(ID_SPLITTER);
             } else {
                 return new String[]{UNI_MATCHER};
             }
         } catch (PatternSyntaxException e) {
             throw new DataFormatException(ID_EX + text);
         }
     }

     private String getResponseType(String text) throws DataFormatException {
         if(text.equals(FIRST_ANSWER) || text.equals(NEXT_ANSWER)) {
             return text;
         } else {
             throw new DataFormatException(TYPE_EX + text);
         }
     }

     private LocalDate getDate(String text) throws DataFormatException {
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
         try {
             return LocalDate.parse(text, dtf);
         } catch (DateTimeParseException e) {
             throw new DataFormatException(DATE_EX + text);
         }

     }

     private void getQueryDate(String queryDate) throws DataFormatException {
         try {
             LocalDate [] dates = new LocalDate[2];
             DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
             if(queryDate.contains(DATE_SPLITTER)) {
                 String [] strDates = queryDate.split(DATE_SPLITTER);
                 LocalDate from = LocalDate.parse(strDates[0], dtf);
                 LocalDate to = LocalDate.parse(strDates[1], dtf);
                 dates[0] = from;
                 dates[1] = to;
             } else {
                 LocalDate date = LocalDate.parse(queryDate, dtf);
                 dates[0] = date;
             }
             date = dates;
         } catch (DateTimeParseException e) {
             e.printStackTrace();
             throw new DataFormatException(QUERY_DATE_EX + queryDate);
         }
     }

     private void parseQuery(String[] query) throws DataFormatException {
         serviceId = getServiceOrQuestionId(query[1]);
         questionId = getServiceOrQuestionId(query[2]);
         responseType = getResponseType(query[3]);
         getQueryDate(query[4]);
     }
}
