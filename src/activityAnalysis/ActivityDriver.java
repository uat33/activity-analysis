package activityAnalysis;


/**
 *
 * The purpose of the program is to take a text file of the user's choice which contains running data for an entire year.
 * The program allows the user to do a variety of things such as search through them, see stats by month,
 * see the tough runs, see the toughest running year etc.
 *
 * @author Uday
 **/


//import stuff
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;


public class ActivityDriver {

	// main method. throws IOexception because we're using files
	public static void main(String[] args) throws FileNotFoundException {

		// tell the user what the program does.
		System.out.println("""
				Welcome to the Running Stats Program!
				The purpose of this program is to allow the user to perform a variety of tasks on their running data.
				The user can do many things based on their input.
				You can generate stats such as KM and time run in the year, or see that plus average calories and pace for a month of your choice.
				You can also search through all your runs in the year by name, or a distance range
				In addition to that, you can see which of your runs are considered tough and which one is the toughest
				And finally you can see which of your years was the toughest.""");


		Scanner in = new Scanner(System.in);  //  so you can take inputs.
		System.out.println(); // spacing

		System.out.println("Which year?"); // ask the user which year they want to see data for

		Scanner inFile = new Scanner(new File(in.nextLine() + ".txt")); // the year plus ".txt" is the file name, so just create the inFile based off that

		ArrayList<Activity> filled = fill(inFile); // fill the array with objects using the fill method and passing in the inFile.

		while(true) { // this method is the menu. it runs until the user exits.
			System.out.println(); // space out different iterations of the loop so it's easier to read
			System.out.println("""
					Activities Main Menu
					1. Generate Stats
					2. Search Activities
					3. Toughest year
					4. Exit"""); // ask what they want to do

			int choice = in.nextInt(); // and take their choice into an int.


			if(choice == 1) { // if they entered one they are looking for the stats

				System.out.println("Stats Submenu\n" + // show them the submenu
						"1. Output total km\n2. Output total time\n3. Totals for month");
				int subchoice = in.nextInt(); // and put that into subchoice
				if (subchoice == 1) { // if that is equal to one
					// they want the total km run this year.
					// but we need it rounded to two decimal places so shift two decimal places to the right, round and then shift two decimal places to the left.
					System.out.println((Math.round(Activity.getTotalKm() * 100.0) / 100.0) +" KM run this year."); // output that plus tell the user that it is the km run this year.
				}
				else if (subchoice == 2) {// if subchoice is equal to two
					System.out.println((Math.round(Activity.getTotalTime() * 100.0) / 100.0) +" minutes run this year."); //  essentially the same thing as the previous if statement. but with time instead of distance. get the time, shift two decimals to the right, round and shift back. output it and tell them what it is.
				}
				else if (subchoice == 3) { // if that is equal to three
					// they want the totals for a specific month.
					// ask them which month (1-12)
					System.out.println("Enter the month number from 1-12. (January is 1, February is 2 etc.)");
					double[] monthTotals = monthTotals(filled, in.nextInt()); // call the method that does the work for this and returns the four values we're looking for in an array of doubles. takes the filled array and the monthnumber as params. see method to see what it does.
					// not quite done. that array should contain four things.
					// 1. time run in the month. only alteration necessary is rounding to two decimals which is done in the method.
					// 2. distance run in the month. only alteration necessary is rounding to two decimals which is done in the method.
					// 3. Average calories burned per run. the method takes care of making this an average.
					// 4. the average pace for that month.  the method takes care of making this an average. but it needs to be converted from an int to a string so we can see it as a time because a minute has 60 seconds not 100.

					// so all we need to do here is turn the pace from say, 5.5 to 5:30 as an example.
					// just use the one in the activity class.
					// now output the values in the array.
					// all on different lines to for usability purposes.
					// and put what they are and the correct units for usability purposes.
					System.out.println("Here are the stats for that month:\nTotal Time: " + monthTotals[0] + " Minutes" + "\nTotal Distance: " + monthTotals[1] + " KM" + "\nAverage Calories: " + (int)(monthTotals[2]+.5) + " Calories"+ "\nAverage Pace: " + Activity.backToString(monthTotals[3]));

				}
				else System.out.println("Invalid input."); // if subchoice isn't one of those values, tell the user they entered an invalid input.

			}
			else if (choice == 2) { // if choice is two, they want to search
				// ask them which search parameter they want.
				System.out.println("""
						Choose your Search Parameter
						1. Search by Name
						2. Search by range of distance
						3. Output tough activities
						4. Output toughest activity""");
				int subchoice = in.nextInt(); // put their input into subchoice
				if (subchoice == 1) { // if it's one, they want to search with name of the run.
					// ask them for that
					System.out.println("Enter the name of the run.");

					// the method searchRuns() takes the filled array and this string that contains the name and returns an arraylist
					// this array list will only have the runs that contain what the user entered in its title. see method for more info
					for(Activity act : searchRuns(filled, in.next())) { // loop through each one in this new arraylist
						System.out.println(act); // output it
					}

				}
				else if (subchoice == 2) { // if subchoice is 2 then they want to search with a range.
					// we need a minimum and max value.
					System.out.println("Enter the minimum distance you want to be counted."); // ask for min
					int min = in.nextInt(); // put it into min variable
					System.out.println("Enter the maximum distance you want to be counted."); // ask for max
					int max = in.nextInt(); // put it into max variable
					for(Activity act : searchRuns(filled, min, max)) { // call a method also called searchRuns but with different parameters. pass in min and max so it knows which method to use.
						System.out.println(act); // loop through each one in the returned arraylist which only contains runs in this range (see the method) and output them.
						System.out.println(); // skip lines so it's easier to read.

					}
				}
				else if (subchoice == 3) { // if subchoice is 4 then they want all the tough activities.
					System.out.println("Here are all the tough activities:"); // output this so they know what they're looking at.
					for(Activity act : toughRuns(filled)) { // toughRuns method returns arraylist with only tough ones. see method.
						// loop through each one
						System.out.println(act); // output it.
						System.out.println(); // skip lines so it's easier to read.
					}
				}
				// if subchoice is 4, they want the toughest activity in the year
				// use the method toughestRun() passing in arraylist of all runs to get single toughest.
				// see method for how this is done.
				// output ti and tell them what they're looking at.
				else if (subchoice == 4) System.out.println("The toughest activity for this year is:\n" + toughestRun(filled));

				else System.out.println("Invalid input."); // if subchoice is none of those tell them their input is invalid
			}
			else if (choice == 3) { // if choice is 3 they are looking for the toughest year.

				// we need to be able to access text files for all four years.
				// so create them.
				// toughestYear() takes all the textfiles as params, and return an int which is the toughest year
				// so put the text files in the method
				// see the method for how this is done.
				System.out.println("The toughest year is: " + toughestYear(new Scanner(new File("2018.txt")),  new Scanner(new File("2019.txt")), new Scanner(new File("2020.txt")), new Scanner(new File("2021.txt"))));

			}
			else if (choice == 4)break; // if choice is 4 they want to exit the menu loop so break.

			else System.out.println("Invalid input.");// if choice is none of those, it is an invalid input so tell them that.
		}
		in.close(); // job done. close the text file.
	}




	/**	Method that searches finds the year with toughest runs, by whichever year had the most runs in the top 20 toughest runs.
	 *
	 * @author Uday
	 *
	 */
	public static int toughestYear(Scanner inFile1, Scanner inFile2, Scanner inFile3, Scanner inFile4) { // seperate scanner needed as for each text file
		ArrayList<Activity> all = fill(inFile1); // create the arraylist that will have all the runs. for now, just fill it with the first file's.

		// add all the runs for all the other files
		all.addAll(fill(inFile2)); all.addAll(fill(inFile3)); all.addAll(fill(inFile4));

		// sort it. use collections.sort. but with a custom sorter. see the Activity class for more info.
		Collections.sort(all, Activity.compareToughness);
		// we need to count the frequency of each year. but we need to have the frequency attached with the year so we know which year to return.
		// we could use a 2d array, but then we'd have to go through it at the end to find the largest and then its year.
		// we could use a bunch of if statements and variables and stuff but i like this way more
		// So lets try a tree map.
		// when the type is integers, it naturally sorts it from least to greatest so that's done for us.
		// there are some complications though. but that's a little later.

		// create a treemap containing two integers called yearFrequency, because the first integer is to represent the year, and the second its frequency.
		// it has to be in that order, because we need to change the frequency. the first one, the key, can't be changed. but the second one, the value, can.
		TreeMap<Integer, Integer> yearFrequency = new TreeMap<Integer, Integer>();
		// add the four years, with the frequency which all start at 0.
		yearFrequency.put(2018, 0); yearFrequency.put(2019, 0); yearFrequency.put(2020, 0); yearFrequency.put(2021, 0);
		// now go through that arraylist with all the runs we sorted earlier
		// but only the last 20 because those are the 20 toughest.
		for(int i = all.size()-1; i >= all.size()-20;i--) {
			int year = all.get(i).getDate()[0]; // get the year by taking the object at i, getting the date attribute as an array, and then taking the first element.
			// now to update the frequency
			//.replace() takes two parameters. the first is the key and the second is what we're updating it to.
			// the key is just the year which we just found so that's easy
			// we need to add one to what was already there.
			// to access what was already their, use .get(key), the key being the year.
			// add one to that and use it as the second parameter.
			yearFrequency.replace(year, yearFrequency.get(year)+1);
		}
		// at the end of that loop, we have a tree map with the years and their frequency.

		// unfortunately the ascending order of the tree map only applies with the key not the value, which is what we need to find the most frequent year

		// so create another tree map of the same type, frequencyYear, not to be confused with yearFrequency.
		// this one will have the frequency as the key since we don't need to change that anymore.
		TreeMap<Integer, Integer> frequencyYear = new TreeMap<Integer, Integer>();

		// we basically need to swap the values of the other treemap.
		// this is actually straightforward because the years are always the same.
		// so just use .get() with each year as the key to get the value, putting it in the key slot.
		// put the year in the value slot
		// do that four times
		frequencyYear.put(yearFrequency.get(2018), 2018); frequencyYear.put(yearFrequency.get(2019), 2019);
		frequencyYear.put(yearFrequency.get(2020), 2020); frequencyYear.put(yearFrequency.get(2021), 2021);
		// this new tree map is now sorted based on frequency so now the last slot having the most common year.

		return frequencyYear.get(frequencyYear.lastKey()); // use .lastKey to get the lastfrequency, and put that in .get to get the value associated with that frequency.
		// which is the year. return it.
	}


	/**	Method that searches the arraylist of all runs in the year to find the single toughest one, and returns it.
	 *
	 * @author Uday
	 *
	 */
	public static Activity toughestRun(ArrayList<Activity> filled) { // take all the runs as an arraylist
		Collections.sort(filled, Activity.compareToughness); // use collections.sort. but with a custom sorter. see the Activity class for more info.

		return filled.get(filled.size()-1); // The last element will be the toughest. so return it. .size()-1 gives us the last one.
	}


	/**	Method that searches the arraylist of all runs in the year to find the tough ones and return them.
	 * @author Uday
	 *
	 */
	public static ArrayList<Activity> toughRuns(ArrayList<Activity> filled) { // take the arraylist as a parameter.

		ArrayList<Activity> acts= new ArrayList<Activity>(); // create the arraylist that we will return

		for (Activity act : filled) { // loop through each run in the arraylist
			if (act.isTough()) { // if the isTough attribute for that object is true, it is a tough run by the program's definition
				acts.add(act); // so add it
			}
		}
		return acts; // at the end of the loop return the arraylist

	}

	/**	Method that searches the arraylist of all runs in the year to find those that are in the range the user enters. And returns all in an arraylist.
	 *
	 * @author Uday
	 *
	 */
	public static ArrayList<Activity>  searchRuns(ArrayList<Activity> filled, int min, int max) { // user enters their minimum and maximum

		ArrayList<Activity> acts= new ArrayList<Activity>(); // create the arraylist of the runs matching the search parameter we will return

		for (Activity act : filled) {// loop through every object in the arraylist filled containing all runs for the year
			double distance = act.getDistance(); // the distance of the run is saved into a double
			if (min <= distance && max>= distance) { // if it is in the range
				acts.add(act); // add it
			}
		}
		return acts; // at the end of the loop return the array list
	}


	/**	Method that searches the arraylist of all runs in the year to find those that contain the string the user enters. And returns all in an arraylist.
	 *
	 * @author Uday
	 *
	 */
	public static ArrayList<Activity> searchRuns(ArrayList<Activity> filled, String title) {
		ArrayList<Activity> acts= new ArrayList<Activity>(); // create the arraylist of the runs matching the search parameter we will return
		for (Activity act : filled) { // loop through every object in the arraylist filled containing all runs for the year

			if (act.getTitle().contains(title)) { // use .contains instead of .equals or else you have to enter the exact title string. so basically if that object's title has the title string parameter, add it.
				acts.add(act); // if it does contain that string add it to the arraylist
			}
		}
		return acts; // at the end of the loop return the array list
	}


	/**	Method that gets the stats for a particular month inputted by the user and returns it in an array
	 *
	 * @author Uday
	 *
	 */
	public static double[] monthTotals(ArrayList<Activity> filled, int monthnum) { // takes the array and the number of the month (from 1-12) as parameters
		double[] totals = {0, 0, 0, 0}; // initialize the array so that it can be added to. double because we want to keep some decimal places for some elements.
		int counter = 0; // set a counter to 0. this is done so that we can count how many elements are in the month as we need to find the average.
		for (Activity act : filled) { // loop through each activity in the array
			if (act.getDate()[1] == monthnum) { // get the date and take the element in the first index for monthnum. If that is equal to the monthnum inputted by the user.
				counter++; // add one to the counter
				// add the appropriate attribute of the object to the corresponding element.
				totals[0]+=act.getTime(); totals[1]+=act.getDistance(); totals[2]+=act.getCalories(); totals[3]+=act.getAvgPace();
			}
		}
		// but we need average calories and average pace, hence the counter
		// divide both those by the counter to get the counters
		// still not done. need to round.
		// the time and distance need to be rounded to two decimal places
		// the below does that. multiply by 100.0 to shift two decimal places right, math.round to round any remaining decimal places, then divide by 100.0 to shift back
		// avg for the first two, round the last two to two decimal places.
		totals[2]/= counter; totals[3]/= counter; totals[0] = Math.round(totals[0] * 100.0) /
				100.0; totals[1] = Math.round(totals[1] * 100.0) / 100.0;
		return totals; // return the array
	}

	/**	Method that takes all the runs in the year and makes the arraylist of objects.
	 *
	 * @author Uday
	 *
	 */
	public static ArrayList<Activity> fill (Scanner inFile) { // take the input file as a parameter so the runs can be accessed

		ArrayList<Activity> acts= new ArrayList<Activity>(); // create the arraylist of the correct type that we will return.
		inFile.nextLine(); // the first line contains headers that need to be skipped, so do that.
		while(inFile.hasNextLine()){ // now loop through each remaining line in the textFile
			String[] part = inFile.nextLine().split("\t"); // create an array of string splitting by the space so we can get each field seperately.
			// now to create the object. but we need the correct type for each field.
			// Use the parse methods in the activity class to do that.
			// the first string is the data, so parse that and pass it in
			// the second is the title which doesn't need to be altered so pass it in normally
			// the third needs to be a double so parse it using double.parseDouble and pass it in
			// the fourth needs to be an int, so do the same but with Integer.parseInt
			// the fifth is the time which needs to be stored as an int. but since there are 60 seconds in a minute and not a 100, we can't use double.parsedouble
			// so use the method in activities to parse it, and pass it in.
			// the sixth and seventh need to be ints, and are straightforward. Integer.parseint and send them in
			// The last is the pace. For the same reason as time it needs its own thing. So use the parsepace method in activity and pass it in.
			Activity act = new Activity(Activity.parseDate(part[0]), part[1], Double.parseDouble(part[2]),
					Integer.parseInt(part[3]), Activity.parseTime(part[4]), Integer.parseInt(part[5]), Integer.parseInt(part[6]),
					Activity.parsePace(part[7]));
			acts.add(act); // now that the object has been created, add it to the arraylist
		}
		inFile.close(); // at the end of the file close it
		return acts; // return the arraylist
	}
}
