/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

import java.io.*;

public class Utils {

	public static String logImagesFileName = "log_images.txt";
	public static String logFileName = "log_doku2latex.txt";

	public static String removeFirstAndLastTag(String s) {
		if (s.startsWith("<") == false)
			return s;
		if (s.endsWith(">") == false)
			return s;
		int index;
		index = s.indexOf('>');
		if (index >= 0)
			s = s.substring(index + 1, s.length() - 1);
		index = s.lastIndexOf('<');
		if (index > 0)
			s = s.substring(0, index);
		return s;
	}

	public static int countCharInString(String s, char c) {
		int i = 0;
		for (char ch : s.toCharArray()) {
			if (ch == c)
				i += 1;
		}

		return i;
	}

	public static void writeInLogFile(String s) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(Utils.logFileName, true));
			output.write(s + "\n");
			output.close();
		} catch (Exception e) {}

	}
}
