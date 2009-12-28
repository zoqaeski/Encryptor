/*
 * Class Name:		EncryptorMain
 *
 * Author:			Robbie Smith (zoqaeski@gmail.com)
 * Creation Date:	123.2008 15:50:33
 * Last Modified:	180.2008 21:28:42
 *
 */

package zoqaeski.encryptor;

import java.util.*;
import java.io.*;

import zoqaeski.utils.*;
import zoqaeski.encryptor.*;
import zoqaeski.encryptor.utils.*;

public class EncryptorMain {
	public static final Scanner kb = new Scanner(System.in);

	private static String input;
	private static String output;
	private static String key;
	private static String letterFreqI;
	private static String letterFreqO;

	private static boolean textWasModified = false;

	// File paths
	private static String pifpath;
	private static String pofpath;

/* ---------------- *
 * THE CORE METHODS *
 * ---------------- */
// The main method simply calls a whole lot of other methods to guide the user through Encypting something.
	public static void main(String[] args){
		System.out.println("*******************\n*                 *\n*    ENCRYPTOR    *\n*                 *\n*******************\n");
		printMenu(mainMenu(), "ddd-dd");
		char mode = kb.nextLine().toLowerCase().charAt(0);
		while(mode != 'q') {
			switch(mode) {
				case 'm':
					miMode();
					break;
				case 'f':
					fMode();
					break;
				case 'p':
					repeat();
					break;
				case 'b':
					batchKeyGen();
					break;
				default:
					System.out.println("Unknown mode!");
			}
			printMenu(mainMenu(), "dddddd");
			mode = kb.nextLine().toLowerCase().charAt(0);
		}

		System.exit(0);
	}

	// Menu loaders
	
	private static String[] mainMenu() {
		String[] menu = new String[6];

		menu[0] = "\n---------\nMain Menu\n---------\nSelect what mode you would like me to use: ";
		menu[1] = "\n    [M]anual input";
		menu[2] = "\n    [F]ile IO";
		menu[3] = "\n    [P]revious IO";
		menu[4] = "\n    [B]atch generation of keys";
		menu[5] = "\n    [Q]uit";

		return menu;	
	}

	private static String[] keyMenu() {
		String[] menu = new String[7];

		menu[0] = "\nHow do you want to generate your key?";
		menu[1] = "\n    [W]alkthrough";
		menu[2] = "\n    [M]anual input";
		menu[3] = "\n    [F]ile input";
		menu[4] = "\n    [T]ext input (from input text)";
		menu[5] = "\n    [P]revious key";
		menu[6] = "\n    [N]o thanks.";

		return menu;	
	}

	/**
	 This is a menu loader that selects various items of a main menu.
	 @param menu The array containing the menu items.
	 @param settings A string representing which items are to be displayed. It takes the form (d|-)*, where d indicates display. Settings must be at least the length of the menu array.
	 */
	private static void printMenu(String[] menu, String settings) {
		char[] csettings = settings.toCharArray();

		for(int c = 0; c < menu.length; c++) {
			if(csettings[c] == 'd') {
				System.out.print(menu[c]);
			}
		}
		System.out.println();
	}

	// Manual input mode
	private static void miMode() {
		System.out.println("\nI will perform my functions based on standard input. Please enter your text that I should play with: ");
		input = kb.nextLine().trim();

		key = generateKey();
		output = encMode(input);

		System.out.println("\nAnd my output is:");
		System.out.println(output);

		lfaMain();
	}

	// Repeat last input/output
	private static void repeat() {
		System.out.println("\nI will perform my functions based on the previous [I]nput/[O]utput.");
		if(kb.hasNextLine()) {
			char io = kb.nextLine().toLowerCase().charAt(0);
			if(io == 'i') {
				input = new String(input);
			} else if(io == 'o') {
				input = new String(output);
			}
		}
		input = input;
		System.out.println("\nI am using the input:\n" + input);

		key = generateKey();
		output = encMode(input);

		System.out.println("\nAnd my output is:");
		System.out.println(output);
		
		lfaMain();
	}
	
	// File input mode
	private static void fMode() {
		System.out.println("\nPlease enter the path to the input file: ");
		String[] inputFC = FileLoader.FileContents();
		String[] outputArr = new String[inputFC.length];
		input = "";
		output = "";
		for(int l = 0; l < inputFC.length; l++) {
			input = input.concat(inputFC[l]);
		}

		key = generateKey();
		outputArr = encMode(inputFC);

System.out.println(textWasModified);

		if(textWasModified) {
			System.out.println("\nI've done my magic on the text from your input file. Where would you like me to save it? Please enter a file name: ");
			PrintWriter outputStream = FileLoader.WriteFile();
			
			for(int l = 0; l < outputArr.length; l++) {
				outputStream.println(outputArr[l]);
				output = output.concat(outputArr[l]);
			}

			outputStream.close();
		}

		lfaMain();
	}


/* ------------------------- *
 * ENCRYPTION AND DECRYPTION *
 * ------------------------- */
	private static String encMode(String input) {
		String output = "";
		System.out.print("\nDo you want to [E]ncrypt or [D]ecrypt your input? (Or do you want to [S]kip this step?) ");
		if(kb.hasNextLine()) {
			char emode = kb.nextLine().toLowerCase().charAt(0);
			if(emode == 'e') {
				output = encrypt(input);
			} else if(emode == 'd') {
				output = decrypt(input);
			}
		}

		return output;
	}

	private static String[] encMode(String[] input) {
		String output[] = new String[input.length];
		System.out.print("\nDo you want to [E]ncrypt or [D]ecrypt your input? (Or do you want to [S]kip this step?) ");
		if(kb.hasNextLine()) {
			char emode = kb.nextLine().toLowerCase().charAt(0);
			if(emode == 'e') {
				output = encrypt(input);
				textWasModified = true;
			} else if(emode == 'd') {
				output = decrypt(input);
				textWasModified = true;
			}
		}
		return output;
	}

	// Encrypt
	private static String encrypt(String input) {
		System.out.print("\nDo you want to enter a seed value for the encryption? ");
		boolean a = UserAnswer.getAnswer(false);
		if(a) {
			int sbseed = getSeed();
			return Encryptor.Encrypt(input, key, sbseed);
		}
		return Encryptor.Encrypt(input, key);
	}

	private static String[] encrypt(String[] input) {
		String[] output = new String[input.length];
		
		System.out.print("\nDo you want to use the incremental mode of encryption? ");
		boolean i = UserAnswer.getAnswer(false);
		int iv = 1;
		if(i) {
			iv = getIncrementer();
		}

		System.out.print("\nDo you want to enter a seed value for the encryption? ");
		boolean a = UserAnswer.getAnswer(false);
		int sbseed = 0;
		if(a) {
			sbseed = getSeed();
		}

		if(a && !i) {
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Encrypt(input[c], key, sbseed);
			}
		} else if(a && i) {
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Encrypt(input[c], key, sbseed);
				sbseed += iv;
			}
		} else if(!a && i) {
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Encrypt(input[c], key, sbseed);
				sbseed += iv;
			}
		} else {	
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Encrypt(input[c], key);
			}
		}
		return output;
	}

	// Decrypt
	private static String decrypt(String input) {
		System.out.print("\nDo you want to enter a seed value for the decryption? ");
		boolean a = UserAnswer.getAnswer(false);
		if(a) {
			int sbseed = getSeed();
			return Encryptor.Decrypt(input, key, sbseed);
		}
		return Encryptor.Decrypt(input, key);
	}
	
	private static String[] decrypt(String[] input) {
		String[] output = new String[input.length];
		
		System.out.print("\nDo you want to use the incremental mode of decryption? ");
		boolean i = UserAnswer.getAnswer(false);
		int iv = 1;
		if(i) {
			iv = getIncrementer();
		}

		System.out.print("\nDo you want to enter a seed value for the decryption? ");
		boolean a = UserAnswer.getAnswer(false);
		int sbseed = 0;
		if(a) {
			sbseed = getSeed();
		}
		
		if(a && !i) {
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Decrypt(input[c], key, sbseed);
			}
		} else if(a && i) {
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Decrypt(input[c], key, sbseed);
				sbseed += iv;
			}
		} else if(!a && i) {
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Decrypt(input[c], key, sbseed);
				sbseed += iv;
			}
		} else {	
			for(int c = 0; c < input.length; c++) {
				output[c] = Encryptor.Decrypt(input[c], key);
			}
		}
		return output;
	}

	// Seedy business
	private static int getSeed() {
		System.out.print("\nPlease enter a seed value: ");
		int seed = 821;
		if(kb.hasNextInt()) {
			seed = kb.nextInt();
			kb.nextLine();
		}
		return seed;
	}

	private static int getIncrementer() {
		System.out.print("\nPlease enter an incrementer value: ");
		int i = 1;
		if(kb.hasNextInt()) {
			i = kb.nextInt();
			kb.nextLine();
		}
		return i;
	}

/* -------------- *
 * KEY GENERATION *
 * -------------- */
	private static String generateKey() {
		printMenu(keyMenu(), "ddddddd");
		if(kb.hasNextLine()) {
			char kmode = kb.nextLine().toLowerCase().charAt(0);
			switch(kmode) {
				case 'w':	
					return KeyGen.generate();
				case 'm':
					System.out.println("\nPlease enter in a key: ");
					return kb.nextLine().trim();
				case 'f':
					return keyFromFile();
				case 't':
					return KeyGen.generate(input);
				case 'p':
					return new String(key);
			}
		}
		return "";
	}

	private static void batchKeyGen() {
		printMenu(keyMenu(), "dddddd-");
		String firstKey = "";
		if(kb.hasNextLine()) {
			char kmode = kb.nextLine().toLowerCase().charAt(0);
			switch(kmode) {
				case 'w':	
					firstKey = KeyGen.generate();
					break;
				case 'm':
					System.out.println("\nPlease enter in a key: ");
					firstKey = kb.nextLine().trim();
					break;
				case 'f':
					firstKey = keyFromFile();
				case 't':
					firstKey = KeyGen.generate(input);
					break;
				case 'p':
					firstKey = new String(key);
					break;
			}
		}

		System.out.print("\nI have generated your first key. How many permutations of this key would you like me to generate? ");
		int numKeys = 0;
		boolean ft = true;

		while(numKeys < 1) {
			if(!ft) {
				System.out.print("\nYou have to enter a positive number! Try again: ");
			}
			if(kb.hasNextInt()) {
				numKeys = kb.nextInt();
				kb.nextLine();
			} else {
				System.out.println("\nSeeing as you didn't enter a number, I'm just going to generate a single key.");
				numKeys = 1;
			}
			ft = false;
		}

		String[] keys = new String[numKeys];
		keys[0] = firstKey;
		if(numKeys > 1) {
			System.out.print("\nWhat starting seed value should I use to generate these keys? ");
			long seed = 16061985;
			if(kb.hasNextLong()) {
				seed = kb.nextLong();
				kb.nextLine();
			}

			// And I said, "Let there be permutations!"
			for(int c = 1; c < keys.length; c++) {
				keys[c] = KeyGen.generate(keys[c - 1], seed);
			}
		}

		System.out.println("\nI have generated the required number of keys. Where would you like me to save them? Please enter a file name: ");
		PrintWriter outputStream = FileLoader.WriteFile();
		
		for(int k = 0; k < keys.length; k++) {
			outputStream.println(keys[k]);
		}

		outputStream.close();
	}

	private static String keyFromFile() {
		System.out.println("\nPlease enter the path to the input file: ");
		String[] keys = FileLoader.FileContents();
		String key = "";

		System.out.print("\nI detected " + keys.length + " key");
		if(keys.length != 1) {
			System.out.print("s");
		}
		System.out.print(". Which one would you like me to use? ");
		int keyNum = 0;
		boolean oor = true;
		while(oor) {
			if(kb.hasNextInt()) {
				keyNum = kb.nextInt() - 1;
				kb.nextLine();
			}
			
			if(keyNum > keys.length) {
				System.out.print("\nThe key you requested does not exist. Please pick another: ");
			} else {
				oor = false;
			}
		}

		return keys[keyNum];
	}

/* ------------------------- *
 * LETTER FREQUENCY ANALYSIS *
 * ------------------------- */
	private static void lfaMain() {
		System.out.print("\nLast but not least, would you like me to generate a letter frequency distribution graph for your input and output? ");
		boolean a = UserAnswer.getAnswer(false);
		if(a) {
			letterFreqI = generateLFC(input);
			System.out.println("\nFor your input, the relative letter frequencies are as follows\n" + letterFreqI);
			if(!output.equals("")) { 
				letterFreqO = generateLFC(output);
				System.out.println("\nFor my output, the relative letter frequencies are as follows\n" + letterFreqO);	
			}
		}
	}

	private static String generateLFC(String input) {
/*		System.out.print("\nPlease enter the character you would like me to use as a graph marker: ");
		char graphUnit = '*';
		if(kb.hasNextLine()) {
			graphUnit = kb.nextLine().charAt(0);
		}*/
		return LetterFrequency.createLFChart(input);
	}


/*	private static  inFileHandler() {
		String infilepath;
		System.out.println("\nPlease enter the path to the input file: ");
		if(kb.hasNextLine() {
			infilepath = kb.nextLine();	
		} else {
			System.out.println("You need to enter a file name!");
			System.exit(1);
		}
	}
	
	private static void outFileHandler() {
		
	}
*/


}
