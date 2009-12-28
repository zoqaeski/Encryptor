/*
 * Class Name:		KeyGen
 *
 * Author:			Robbie Smith (zoqaeski@gmail.com)
 * Creation Date:	167.2008 16:07:38
 * Last Modified:	180.2008 19:54:56
 *
 * Class Description: 
 * Generates a Key for use in Encryptor. This key is more of a character set which Encryptor uses to perform character-by-character substitution. The key is vital in Encryptor's functioning: as well as being the basis for a character substitution table, it is also used to generate a numerical substitution table which
 *
 */

package zoqaeski.encryptor.utils;

import java.util.*;
// import zoqaeski.encryptor.*;

public class KeyGen {
	public static final Scanner kb = new Scanner(System.in);
	private static char[] keyChrArray;

	// These are the constants which define various character classes
	private static String BASIC_LATIN_LC = "abcdefghijklmnopqrstuvwxyz "; // llc
	private static String BASIC_LATIN_UC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "; // luc
	private static String NUMERALS = "0123456789 "; // num

	private static String ASCII_PUNCTUATION = " !\"#$%&\'()*+.,-/:;<=>?@[\\]^_`{|}~ "; // apn
	private static String TYPO_PUNCTUATION = "–—‘’“”„«»¡¿°·÷× "; // tpn

	private static String QEVESA_XTD = "ÁÉËÍŇÓÖŐŠÚÜŰáéëíňóöőšúüű "; // qxt

	// Greek (basic letters only)
	private static String GREEK_LC = "αβγδεζηθικλμνξοπρςστυφχψωάέήίόύώ "; // glc
	private static String GREEK_UC = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΆΈΉΊΌΎΏ "; // guc

	// Cyrillic (basic letters only)
	private static String CYRILLIC_LC = "абвгдежзийклмнопрстуфхцчшщъыьэюя "; // clc
	private static String CYRILLIC_UC = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ "; // cuc

	// More to be added (when I can be bothered to)

	// No-argument constructor	
	public KeyGen() {
	}

	/**
	 * Generates a key for Encryptor based on user selections.
	 * @return The generated key for use in Encryptor.
	 */
	public static String generate() {
		// Display introduction
		System.out.println("\nSelect the character classes to be included in the key. These are as follows: ");
		System.out.println("    llc    Lower case basic Latin [a-z]");
		System.out.println("    luc    Upper case basic Latin [A-Z]");
		System.out.println("    num    Numerals [0-9]");
		System.out.println("    apn    ASCII punctuation");
		System.out.println("    tpn    Typographically correct punctuation");
		System.out.println("    qxt    Extended characters for Qevesa");
		System.out.println("    extra  Extra letters you wish to include");

		// Grab selections
		StringBuilder keyStr = new StringBuilder();
		String[] selections = kb.nextLine().toLowerCase().split(" ");
		for(int c = 0; c < selections.length; c++) {
			if(selections[c].equals("llc")) keyStr = keyStr.append(BASIC_LATIN_LC);
			if(selections[c].equals("luc")) keyStr = keyStr.append(BASIC_LATIN_UC);
			if(selections[c].equals("num")) keyStr = keyStr.append(NUMERALS);
			if(selections[c].equals("apn")) keyStr = keyStr.append(ASCII_PUNCTUATION);
			if(selections[c].equals("tpn")) keyStr = keyStr.append(TYPO_PUNCTUATION);
			if(selections[c].equals("qxt")) keyStr = keyStr.append(QEVESA_XTD);
			if(selections[c].equals("extra")) {
				System.out.print("\nPlease enter any extra characters you want included in your key: ");
				keyStr = keyStr.append(kb.nextLine());
			}
		}
		
		return generate(keyStr.toString());	
	}

	public static String generate(String base) {
		// Append a space so that the generation works correctly:
		base = new String(base + " ");
		// Remove duplicate entries from the base text.
		StringBuilder btchars = new StringBuilder();
		for(int c = 0; c < base.length(); c++) {
			// Find character in array
			String charInST = ((Character) base.charAt(c)).toString();
			int positionOfChar = btchars.indexOf(charInST);
			// If the character isn't found, add it to the next empty element in the array.
			if(positionOfChar == -1) {
				btchars = btchars.append(charInST);
			}
		}

		String gkey = btchars.toString();
		char[] keyArr = gkey.toCharArray();
		char[] keyArr2 = new char[keyArr.length];

		long seed = 16061985;
		boolean useThisKey = false;
		while(useThisKey == false) {
			System.out.print("\nEnter a seed to shuffle the key: ");
			seed = kb.nextLong();
			kb.nextLine();
			keyArr2 = KeyGen.permute(keyArr, seed);
			gkey = String.valueOf(keyArr2);
			System.out.print("\nI generated this key:\n" + gkey + "\n\nWould you like to use it? ");
			useThisKey = usersAnswer();
		}

		return gkey;
	}

	public static String generate(String base, long seed) {
		char[] keyArr = base.toCharArray();
		char[] keyArr2 = new char[keyArr.length];
		keyArr2 = KeyGen.permute(keyArr, seed);
		String gkey = String.valueOf(keyArr2);

		return gkey;
	}

	// This is an implementation of the Fisher-Yates shuffle I found on Wikipedia (http://en.wikipedia.org/w/index.php?title=Fisher-Yates_shuffle&oldid=218853629); the algorithm was changed to allow seeding and shuffle arrays of characters instead.
	private static char[] permute(char[] original, long seed) {
		Random rng = new Random(seed);
		int n = original.length;
		char[] shuffled = new char[n];
		
		for(int c = 0; c < shuffled.length; c++) {
			shuffled[c] = original[c];
		}

		while (n > 1) {
			int k = rng.nextInt(n);
			--n;
			char tmp = shuffled[n];
            shuffled[n] = shuffled[k];
            shuffled[k] = tmp;
		}
		return shuffled;
	}

	private static boolean usersAnswer() {
		char a = kb.nextLine().toLowerCase().trim().charAt(0);
		if(a == 'y') return true;
		return false;
	}


/* =================== *
 *   THE MAIN METHOD   *
 * =================== */
	public static void main(String[] args){
	    System.out.println(KeyGen.generate());
	}
}

