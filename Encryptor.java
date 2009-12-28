/*
 * Class Name:		Encryptor
 *
 * Author:			Robbie Smith (zoqaeski@gmail.com)
 * Creation Date:	123.2008 13:37:18
 * Last Modified:	206.2008 15:10:53
 * Version:			0.2
 *
 */

package zoqaeski.encryptor;

import java.util.*;
import zoqaeski.encryptor.utils.*;

/**
 Class for basic encryption/decryption functionality, using a text-based block cipher, with some stream cipher characteristics.
 */
public class Encryptor {
	// Arrays to store numerical data of message and key. 
	// m1[] stores the position of each char in the message relative to the key
	// k1[] stores a list of constants dependent on the key.
	private static int[] m1;
	private static int[] k1;
	private static String k;

	// The character sets (default keys):
	private static String BASICSET = "abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789., ";
	private static String FULLSET = "zAq1XsW2cDe3VfR4bGt5NhY6MjU7Ki8lO9p0 ZaQxSwCdEvFrBgTnHymJukILoP.,!?:;@()*[]{}<>/~`-#$+=%&\'\"\\";

	// I was advised that having a constructor was a good thing, even if it did nothing.
	public Encryptor() {
		// I have now created a constructor.
	}


	/**
	 Performs the encryption.
	 @param plaintext The message to encrypt.
	 @param key The key to use to encrypt the message.
	 @param sbseed A seed to generate a substitution table. Can be any integer between 1 and 16777215;
	 @return The encrypted text.
	 */
	public static String Encrypt(String plaintext, String key, int sbseed) {
		String message = plaintext;
		String output;
		int[] sb;
		
		k = setKey(key);
		int size = message.length();
		sb = generateSBox(sbseed, size);

		m1 = new int[message.length()];
		buildM1(message, k);

		k1 = new int[message.length()];
		buildK1(k, sb);

		// Loop for transformations
		output = "";
		for(int c = 0; c < m1.length; c++) {
			int e = substitute(c, 1);			
			String result = ((Character) k.charAt(e)).toString();
//System.out.print(result);
			output = output.concat(result);
		}

//System.out.println("\n-----------------------------------------");

		return output;
	}

	/**
	 Performs the encryption.
	 @param plaintext The message to encrypt.
	 @param key The key to use to encrypt the message.
	 @return The encrypted text.
	 */
	public static String Encrypt(String plaintext, String key) {
		return Encryptor.Encrypt(plaintext, key, plaintext.length());
	}

	/**
	 Performs the decryption.
	 @param ciphertext The message to decrypt.
	 @param key The key to use to decrypt the message.
	 @param sbseed A seed to generate a substitution table. Can be any integer between 1 and 16777215;
	 @return The decrypted text.
	 */
	public static String Decrypt(String ciphertext, String key, int sbseed) {
		String message = ciphertext;
		String output;
		int[] sb;

		k = setKey(key);
		int size = message.length();
		sb = generateSBox(sbseed, size);

		m1 = new int[message.length()];
		buildM1(message, k);

		k1 = new int[message.length()];
		buildK1(k, sb);

		// Loop for transformations
		output = "";
		for(int c = 0; c < m1.length; c++) {
			int e = substitute(c, -1);			
			String result = ((Character) k.charAt(e)).toString();
//System.out.print(result);
			output = output.concat(result);
		}

//System.out.println("\n-----------------------------------------");

		return output;
	}
	
	/**
	 Performs the decryption.
	 @param ciphertext The message to decrypt.
	 @param key The key to use to decrypt the message.
	 @return The decrypted text.
	 */
	public static String Decrypt(String ciphertext, String key) {
		return Encryptor.Decrypt(ciphertext, key, ciphertext.length());
	}

	/* ----------- *
	 * Set the key *
	 * ----------- */
	private static String setKey(String key) {
		if(key.equals("") || key.equals("basicset")) {
			return BASICSET;
		} else if(key.equals("fullset")) {
			return FULLSET;
		}
		return key;
	}
/*
	This is not needed and deprecated. I will remove it in the revised version.
*/
	
	/* ------------------ *
	 * Generate the S-Box *
	 * ------------------ */
	private static int[] generateSBox(int seed, int size) {
		int[] s1 = new int[size];
		int s = seed * size;
		
		for(int c = 0; c < size; c++) {
			s = (s + seed) ^ size;
			s1[c] = s;
		}
		return s1;
	}
/*
	This step could be retained, as it generates a nice set of numbers. I might add salt to a revised edition. Obviously, to make the algorithm more secure, one would have to use more S-Boxes. An array, perhaps?
	How does this look:
	s = ((s + seed) >>> (c % s)) ^ size ^ salt;
	(As long as given the same starting values it generates the same set all the time, in an architecture-independent manner)
*/
	/* --------------------------------- *
	 * Create the first numerical array. *
	 * --------------------------------- */
	private static void buildM1(String message, String key) {
		for(int c = 0; c < m1.length; c++) {
			m1[c] = key.indexOf(message.charAt(c));
			// If the character isn’t found, ignore it.
			if(m1[c] == -1) {
				m1[c]++;
			}
		}
	}

/*
	This step would be simply replace each character in the message with its Unicode code point. Some form of block creation method would be required though, and while we're at it, we'll include some kind of rotation function so each block is rotated.
*/

	/* ---------------------------------- *
	 * Create the second numerical array. *
	 * ---------------------------------- */
	private static void buildK1(String key, int[] sbox) {
		for(int c = 0; c < k1.length; c++) {
			k1[c] = (key.codePointAt(c % key.length()) ^ sbox[c % sbox.length]) % k1.length;
/* 
	I think it might be possible to derive a character-set independent version of this cipher by replacing this step with
	k1[c] = key[c % key.length] ^ sbox[c % sbox.length];
 */
		}
	}

	/* --------------------------------------------- *
	 * Look up the index to use in the substitution. *
	 * --------------------------------------------- */
	private static int substitute(int position, int mode) {
		int s = m1[position] + (k1[position] * mode);
		while(s >= k.length()) {
			s -= k.length();
		}

		while(s < 0) {
			s += k.length();
		}
//System.out.print("\n" + position + "\t" + m1[position] + "\t" + k1[position] + "\t" + s + "\t");
		return s;
	}

/* =================== *
 *   THE MAIN METHOD   *
 * =================== */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long endTime;
		long totalTime = 0;
		
		Scanner kb = new Scanner(System.in);
		String plaintext = "The quick brown frog jumped over the lazy ox, 1234567890 times.";
		String ciphertext = Encryptor.Encrypt(plaintext, "", 58722109);
		String decodedtext = Encryptor.Decrypt(ciphertext, "", 58722109);
		System.out.println(plaintext + "\n" + ciphertext + "\n" + decodedtext);
		System.out.println();

		endTime = System.currentTimeMillis();
		System.out.println("My automated test took " + (endTime - startTime) + " ms.");
		totalTime += (endTime - startTime);

		System.out.println("Now it's your turn!\nEnter in a plaintext to encypt:");
		plaintext = kb.nextLine();
		
		System.out.print("\nNow give me a seed to generate your ciphertext: ");
		int seed = kb.nextInt();
		
		startTime = System.currentTimeMillis();
		ciphertext = Encryptor.Encrypt(plaintext, "", seed);
		decodedtext = Encryptor.Decrypt(ciphertext, "", seed);
		System.out.println(plaintext + "\n" + ciphertext + "\n" + decodedtext);
		System.out.println();	
		endTime = System.currentTimeMillis();
		System.out.println("My automated test (with your input) took " + (endTime - startTime) + " ms.");
		totalTime += (endTime - startTime);
	}
}

