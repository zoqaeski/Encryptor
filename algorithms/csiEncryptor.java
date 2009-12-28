/*
 * Class Name:   csiEncryptor
 *
 * Author:		Your Name
 * Creation Date:	206.2008 15:09:16
 * Last Modified:	210.2008 17:28:49
 *
 * Class Description:
 *
 */

//package zoqaeski.encryptor.algorithms;

import java.util.*;
//import zoqaeski.encryptor.utils.*;
//import zoqaeski.utils.*;

public class csiEncryptor {
	public static final Scanner kb = new Scanner(System.in);

	private static int[] sb0;
	private static int[] kt0;
	private static int[][] mb;

	private static String k;
	private static String it;
	private static String ot;

	public csiEncryptor() {};

/*
	private static int[] processBlock(int[] in, int round) {
		
	}

	private static int[] invProcessBlock(int[] in, int round) {
	
	}
*/

/* ------------ *
 * SUBSTITUTION *
 * ------------ */
// This is where code for the substitution part of the algorithm is located.

/* ----------- *
 * PERMUTATION *
 * ----------- */
// This is where code for the permutation part of the algorithm is located.

	/**
	 Rotates elements in an array as though it was a table. Each row in the table is rotated by its row number.
	 @param in The array to input.
	 @param rl The length of a virtual row.
	 @param d The direction to rotate, 1 being left and -1 being right.
	 */
	private static int[] rotateCols(int[] in, int rl, int d) {
		// Set the starting values
		int p = 0;
		int rn = 0;
		int s = 0;
		int[] out = new int[in.length];

		if(in.length % rl != 0) {
			System.out.println("Block cannot be formed into table with rows of " + rl + " elements.");
			System.exit(1);
		
		} else {	
			for(s = 0; s < in.length; s += rl) {
				rn = s / rl;
				for(int c = 0; c < rl; c++) {
					p = (c - (rn * d)) % rl;
					// For some reason, the modulo operator doesn't work as expected.
					if(p < 0) {
						p += rl;
					}
					out[p + s] = in[c + s];
				}
			}
		}
		
		return out;
	}

	/**
	 Rotates elements in an array as though it was a table. It works on whole virtual rows.
	 @param in The array to input.
	 @param rl The length of a virtual row.
	 @param ti The number of places to rotate the rows of the table.
	 @param d The direction to rotate, 1 being left and -1 being right.
	*/
	private static int[] rotateRows(int[] in, int rl, int ti, int d) {
		int[] tmp = new int[rl];
		int[] out = new int[in.length];
		int p = 0;
		int s = 0;
		int rn = 0;

		if(in.length % rl != 0) {
			System.out.println("Block cannot be formed into table with rows of " + rl + " elements.");
			System.exit(1);
		
		} else {	
			for(int c = 0; c < in.length; c++) {
				p = (c - (rl * ti * d)) % in.length;
				// For some reason, the modulo operator doesn't work as expected.
				if(p < 0) {
					p += in.length;
				}
				out[p] = in[c];
			}
		}
		
		return out;
	}
	
/* -------------- *
 * DRIVER METHODS *
 * -------------- */
// This is where methods used to perform the encryption / decryption are located.

	public static String Encrypt(String plaintext, String key) {
		return "Not yet implemented";
	}

	public static String Decrypt(String ciphertext, String key) {
		return "Not yet implemented";
	}

	public static void main(String[] args){
		int[] test = {
			12,	43,	8,	7,	18,
			91,	10,	4,	6,	42,
			3,	7,	13,	15,	16,
			62,	3,	44,	1,	31,
			56,	23,	47,	11,	17
		};

		for(int c = 0; c < test.length; c++) {
			System.out.printf("%6d", test[c]);
		}
		System.out.println();
		
		int[] o1 = rotateCols(test, 5, 1);
		for(int c = 0; c < o1.length; c++) {
			System.out.printf("%6d", o1[c]);
		}
		System.out.println();
		
		int[] o2 = rotateCols(o1, 5, -1);
		for(int c = 0; c < o2.length; c++) {
			System.out.printf("%6d", o2[c]);
		}
		System.out.println();

		int[] o3 = rotateRows(o2, 5, 3, 1);
		for(int c = 0; c < o3.length; c++) {
			System.out.printf("%6d", o3[c]);
		}
		System.out.println();

		int[] o4 = rotateRows(o3, 5, 3, -1);
		for(int c = 0; c < o4.length; c++) {
			System.out.printf("%6d", o4[c]);
		}

		System.out.println();
		System.exit(0);	    
	}
}

