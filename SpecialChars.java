/*
 * Author: Mihailscu Maria-Elena
 * Faculty of Automatic Control and Computer Science,
 * University POLITHENICA of Bucharest
 */

import java.util.HashMap;
import java.util.Map;

public class SpecialChars {
	private static Map<String, String> chars;
	public static final String regex_keys;
	static {
		chars = new HashMap<String , String>();
		chars.put("ă", "\\\\u{a}");
		chars.put("Ă", "\\\\u{A}");
		chars.put("â", "\\\\^{a}");
		chars.put("Ă", "\\\\^{A}");
		chars.put("î", "\\\\^{i}");
		chars.put("Î", "\\\\^{I}");
		chars.put("ş", "\\\\c{s}");
		chars.put("ș", "\\\\c{s}");
		chars.put("Ş", "\\\\c{S}");
		chars.put("ţ", "\\\\c{t}");
		chars.put("ț", "\\\\c{t}");
		chars.put("Ţ", "\\\\c{T}");
		chars.put("–", "-");
		regex_keys = "[" + chars.keySet().toString().replaceAll("[\\]\\[, ]", "") + "]";
	}

	public static String replaceSpecialChars(String str) {
		for(String key: chars.keySet()){
			str = str.replaceAll(key, chars.get(key));
        }
        return str;
	}

	public static String replaceLaTeXSpecialChars(String str) {

		str = str.replaceAll("(?<!\\\\)_", "\\\\_");
		str = str.replaceAll("(?<!\\\\)\\$", "\\\\\\$");
		str = str.replaceAll("(?<!\\\\)&", "\\\\&");
		str = str.replaceAll("(?<!\\\\)#", "\\\\#");
		str = str.replaceAll("(?<!\\\\)%", "\\\\%");

		return str;
	}	
}
