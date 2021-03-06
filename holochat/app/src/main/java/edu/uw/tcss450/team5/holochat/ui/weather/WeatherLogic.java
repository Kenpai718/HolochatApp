package edu.uw.tcss450.team5.holochat.ui.weather;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is unused in the current implementation
 *
 * My starting point for this class is the BlogPost class featured in lab two
 * as I would like to add a card view to WeatherFragment in a similar way
 * to what was done for the blog posts, in order to show a multiple day forecast.
 *
 */
public class WeatherLogic implements Serializable {


    /**
     * gets the days to be used in the forecast (current date+1 - current date+3)
     * Not sure if this will be usable just felt like writing down some ideas
     */
    public String[] getForecastSpan() {
        //value that corresponds to the current day of the week
        int dayOfTheWeekVal;

        //gets current date
        Date now = new Date();

        //convert date to a string of the day of the week
        SimpleDateFormat dayOfTheWeekAsDate = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = dayOfTheWeekAsDate.format(now);

        //take current date and assign an integer value
        switch (dayOfTheWeek){
            case "Monday":
                dayOfTheWeekVal = 0;
            case "Tuesday":
                dayOfTheWeekVal = 1;
            case "Wednesday":
                dayOfTheWeekVal = 2;
            case "Thursday":
                dayOfTheWeekVal = 3;
            case "Friday":
                dayOfTheWeekVal = 4;
            case "Saturday":
                dayOfTheWeekVal = 5;
            case "Sunday":
                dayOfTheWeekVal = 6;
            default:
                dayOfTheWeekVal = 0;
        }

        String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        //strings array to hold the upcoming days of the week
        String[] upcomingDays = new String[4];

        for(int i = 1; i < 4; i++){
            if (dayOfTheWeekVal + i > 6){
                int temp = 6 % dayOfTheWeekVal;
                upcomingDays[i-1] = daysOfTheWeek[0+temp];
            } else {
                upcomingDays[i-1] = daysOfTheWeek[dayOfTheWeekVal + i];
            }
        }
        //return current date+1, current date+2, current date+3
        return upcomingDays;
    }


}
