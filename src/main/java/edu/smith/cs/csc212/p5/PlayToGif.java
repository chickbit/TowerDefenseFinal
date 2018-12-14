package edu.smith.cs.csc212.p5;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.jjfoley.gfx.GFX;

public class PlayToGif {
	public static void main(String[] args) throws IOException {
		GFX app = new TDGame();
		int numSeconds = 10;
		// make new files each time this is run - I don't want to overwrite old gifs
		String fileName = "TDGame_" + getCurrentTimeUsingCalendar() + ".gif";
		System.out.println(fileName);
		app.playToGIF(new File(fileName), 50 * numSeconds);
	}

	/**
	 * Sourced from https://dzone.com/articles/getting-current-date-time-in-java, accessed 12/1/18
	 * 
	 * @return a String that list the year, month, date, and time
	 */
	public static String getCurrentTimeUsingCalendar() {

		Calendar cal = Calendar.getInstance();

		Date date = cal.getTime();

		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_h.mm_a_z");

		String formattedDate = dateFormat.format(date);

		// System.out.println(formattedDate);
		return formattedDate;
	}
}
