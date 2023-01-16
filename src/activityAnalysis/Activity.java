package activityAnalysis;
// import this for later
import java.util.Comparator;

public class Activity {

	// these are all private as we don't want them to be accessible unless it is through an object. 

	// declare the fields. use the appropriate data types 
	private final int[] date;
	private final String title;
	private final double distance;
	private final int calories;
	private final double time;
	private final int avgHR;
	private final int maxHR;
	private final double avgPace;

	// these two are static so that they aren't redeclared every time a new run is created. as a result we can actually get the total instead of redeclaring it every time. 
	private static int totalTime; private static int totalKm;

	// these are all public so they can be accessed without an object.

	// the actual constructor // 
	// recieves all the fields as the appropriate data type. 
	public Activity(int[] date, String title, double distance, int calories, double time, int avgHR, int maxHR, double avgPace) {
		// set this objects attribute equal to what was passed in.
		this.date = date;
		this.title = title;
		this.distance = distance;
		this.calories = calories;
		this.time = time;
		this.avgHR = avgHR;
		this.maxHR = maxHR;
		this.avgPace = avgPace;
		
		// for these ones, we're just trying to keep track of the totals. so add the values for this object to the total
		totalTime += time; totalKm += distance;
	}


	// the methods that get the values for us.
	// no setters required because once declared, they are never changed.
	public int[] getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public double getDistance() {
		return distance;
	}

	public int getCalories() {
		return calories;
	}

	public double getTime() {
		return time;
	}

	public int getAvgHR() {
		return avgHR;
	}

	public int getMaxHR() {
		return maxHR;
	}

	public double getAvgPace() {
		return avgPace;
	}

	public static int getTotalTime() {
		return totalTime;
	}

	public static int getTotalKm() {
		return totalKm;
	}


	// the ones that are static are that so they can be accessed from the other class. like to parse and stuff. 

	/** // the toString(). Decides how we output the actual object. 
	 * 
	 * @author Uday
	 *
	 */
	@Override
	public String toString() { 

		// choose out output format. 
		// some of them have modifications so here's a quick overview
		// string.format() so we get 2019-06-03 and not 2019-6-3
		// the distance is rounded to two decimal places. commented in like two other places how this is done. 
		// the time and pace are put into a method called backToString(). see the method for how and why this is done
		return "Date: " + date[0] + "-" + String.format("%02d", date[1]) + '-' + String.format("%02d", date[2]) + ", Title: " + title + ", Distance: " + Math.round(distance * 100.0) / 100.0 + " KM" + ", Calories: " + calories + ", Time: " + backToString(time) + ", Average Heart Rate: " + avgHR + ", Max Heart Rate: " + maxHR + ", Average Pace: " + backToString(avgPace);
	}


	/** parsing method. Takes a timestring like 5:30 and converts it into an integer like 5.5. always returns in minutes
	 * 
	 * @author Uday
	 *
	 */
	public static double parseTime(String time) {		
		// take what's before the first colon and multiply by 60 as that is the hours and we want minutes.  
		// add that with what is after the first colon but before the second colon as that is minutes. 
		// turn the last one which is the seconds into minutes by dividing by 60. 
		// add that
		// return it
		return Integer.parseInt(time.substring(0, time.indexOf(':')))*60 + Integer.parseInt(time.substring(time.indexOf(':')+1, time.lastIndexOf(':'))) + (Double.parseDouble(time.substring(time.lastIndexOf(':')+1))/60);

	}



	/** parsing method. Takes a date string and makes an array of ints out of it.  
	 * 
	 * @author Uday
	 *
	 */
	public static int[] parseDate(String date) {
		// take a substring from the beginning to the first -, parse t an int and throw it in the first slot as that is the year. 
		// take what's after the first - and before the second - parse into an int and throw it into the second slot as it is the month
		// take what's after the second - parse it into an int and throw it into the third slot as it is the day
		return new int[]{Integer.parseInt(date.substring(0, date.indexOf('-'))), Integer.parseInt(date.substring(date.indexOf('-')+1, date.lastIndexOf('-'))), Integer.parseInt(date.substring(date.lastIndexOf('-') + 1, date.indexOf(" ")))}; // return it.

	}

	/** parsing method. takes a string which is the pace like 5:30 and makes it 5.5
	 * 
	 * @author Uday
	 *
	 */
	public static double parsePace(String pace) {
		// the exact same as the time parse, but there will never be an hour value. so just the last 2/3rds of that. 
		// add the minutes with the seconds / 60
		// return it
		return Integer.parseInt(pace.substring(0, pace.indexOf(":"))) + (Double.parseDouble(pace.substring(pace.indexOf(":")+1)))/60;
	}


	/**  time is stored as an int like say 5.5. but outputting 5.5 minutes is useless. Much better to ouput 5:30 so they can see the seconds.
	 * make this a method because it's used a couple times for both time and pace .
	 * 
	 * @author Uday
	 *
	 */

	public static StringBuffer backToString(double time) {

		// convert time to the proper format - multiply by .6. use string.format to make sure we get like 5:10 and not 5:1
		// put it into a string buffer so we can change the . to a :. do just that. 
		StringBuffer newTimeSB = new StringBuffer(String.format("%.2f", (int)time + time%1*.6));
		newTimeSB.replace(newTimeSB.indexOf("."), newTimeSB.indexOf(".")+1, ":");

		// and return the stringBuffer
		return newTimeSB;

	}
	/** Uses the criteria in the text file to decide if a run is tough or not. 
	 * 
	 * @author Uday
	 *
	 */
	public boolean isTough() { // if the distance is more than 4 and calories more than 300 and either the max heart rate is more than 170 or the avg heart rate is more than 152 or the avg pace is less than 5.25
		return distance > 4 && calories > 300 && maxHR > 170 || avgHR > 152 || avgPace < 5.25; // it is tough. so return true.
// if true hasn't been returned it is not tough so return false.
	}

	// method used to comapre the activities.  
	public int compareTo(Activity other) { // take the one you are comparing to as a parameter. 

		// first thing to check. 
		// if one is tough and the other isn't just return the one that is tough. 
		if(isTough() && !other.isTough()) return 1;
		if (!isTough() && other.isTough()) return -1;

		// next criteria. 
		// if one run is a decent margin longer than the other, say at least 10 minutes
		// return that one
		if(this.time - 10 > other.time) return 1;
		if(other.time - 10 > this.time) return -1;

		// next criteria. 
		// if one run has a pace that is lower by at least a minute, it means that on average each kilometer was run a minute faster. 
		// which is pretty hard. so return the one that is lower by a minute. 
		if(avgPace + 1 < other.avgPace)return 1;
		if(other.avgPace + 1 < avgPace) return -1;

		// next criteria. 
		// if one run is a decent amount longer, say 3 km, say that one is tougher.
		// return the one that is at least 3 km longer
		if(distance - 3 > other.distance)return 1;
		if(other.distance - 3 > distance) return -1;

		// if none of that worked, the runs are probably not that much different.
		// so just return 0. 
		return 0;
	}	

	/** The comparator that sorts a list of activities based on the .compareTo value from above. so tougher ones go at the end, easier ones in the beginning.
	 *  Inititally, I used a modified version of the quickSort algorithm. 
	 *  But this is better
	 *  essentially using //@override we can use the collections.sort that is used for arraylists, but have it use our own method as a custom comparator.
	 *
	 */
	public static Comparator<Activity> compareToughness = new Comparator<Activity>() { // data type is activity as that is what we are comparing. 
		@Override
		public int compare(Activity one, Activity two) { // take the two objects as a parameters. 
			return one.compareTo(two); // uses the .compareTo value from the above method to return a value, which is used as the collections.sort value
			// when we use collections.sort, it now takes two parameters instead of one. 
			// the first is still the name of the arraylist we are sorting
			// the second is compareToughness which will result in it using this instead of what collections.sort normall uses (since we are calling from the other class, we have to say Activity.compareToughness)
		}
	};
}
