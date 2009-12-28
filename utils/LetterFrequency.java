/*
 * Class Name:		LetterFrequency
 *
 * Author:			Robbie Smith (zoqaeski@gmail.com)
 * Creation Date:	168.2008 17:11:30
 * Last Modified:	189.2008 17:08:15
 *
 * Class Description: 
 * This class will generate a letter frequency chart.
 *
 */

package zoqaeski.encryptor.utils;

import java.util.*;
//import zoqaeski.encryptor.utils.*;

public class LetterFrequency {
	/**
	 Generates a letter frequency chart.
	 @param sampleText The text to analyse.
	 @param graphUnit The character to use as a graph marker.
	 */
	public static String createLFChart(String sampleText) {
		//		lfdp = accuracy;

		// Get length!
		int stlen = sampleText.length();
		if(stlen == 0) {
			System.out.println("I cannot do a letter frequency chart on an empty string!");
			System.exit(1);
		}
		
		// Initialise arrays[] to be bigger than they will ever need to be: it is unlikely that a string will contain more different characters than its own length. At most, a string may never repeat a single character.
		char[] stletters = new char[stlen];
		int[] stlcount = new int[stlen];
		
		// This is a counter; as we're populating the array, it starts at zero.
		int nextEmptyElement = 0;

		for(int c = 0; c < stlen; c++) {
			// Find character in array
			char charInST = sampleText.charAt(c);
			int positionOfChar = -1;
			int p = 0;
			while(p < stletters.length) {
				if(stletters[p] == charInST) {
					positionOfChar = p;
					break;
				} else {
					p++;
				}
			}

			// If the character isn't found, add it to the next empty element in the array.
			if(positionOfChar == -1) {
				stletters[nextEmptyElement] = charInST;
				stlcount[nextEmptyElement] = 1;
				nextEmptyElement += 1;

			} else {
				// Otherwise, increment the parallel counting array.
				stlcount[positionOfChar] += 1;
			}

		}

		stletters = dropEmptyElements(stletters);
		stlcount = dropEmptyElements(stlcount);

		String[] combinedList = new String[stletters.length];
		for(int q = 0; q < combinedList.length; q++) {
			StringBuffer cList = new StringBuffer(2);
			cList = cList.append(stlcount[q]).append(stletters[q]);
			combinedList[q] = cList.toString();
		}

		combinedList = mergeSort(combinedList);
		for(int r = 0; r < combinedList.length; r++) {
			stlcount[r] = Integer.decode(combinedList[r].substring(0, combinedList[r].length() - 1)).intValue();
			stletters[r] = combinedList[r].charAt(combinedList[r].length() - 1);
		}

//		System.out.println();
		
		// Put it all back together
		StringBuilder output = new StringBuilder("");
		for(int i = 0; i < stletters.length; i++){
			// Do maths
			double percentageOfTotal = stlcount[i] / (double) stlen * 100;
//			output = output.append("  ");
			String chr = "\"" + stletters[i] + "\"";
			output = output.append(String.format("\n  %-6s\t", chr));
			output = output.append(String.format("%-24s", String.format("%8.3f",percentageOfTotal) + "%  (" + stlcount[i] + ")"));


			// Make graph
			int numberOfBars = (int)(Math.ceil(percentageOfTotal));
			for(int nb = 0; nb < numberOfBars; nb++) {
				output = output.append("*");
			}
/*
			for(int s = numberOfBars; s <= 100; s++) {
				output = output.append(" ");
			}
*/
		}
		return output.toString();
	}

//	private static double roundNum(double originalNumber, int precision) {
/*//	This is the original Ruby code I used to round a number to an arbitrary precision
		def roundnum(number, dps)
			dpsi = dps.to_i
			noninthalf = number.to_s.split(".")[1]
			inthalf = number.to_s.split(".")[0]

			dl = noninthalf.slice(0,dpsi).to_i
			de = noninthalf.slice(dpsi,1).to_i
			if de >= 5 : dl += 1
			end

			dstr = dl.to_s

			if dstr.length < dpsi
				dpadlen = dpsi - dstr.length
				dpad = "0"*dpadlen
				dstr = dl.to_s + dpad
			end

			newnum = inthalf + "." + dstr

			return newnum

		end
*/
/*		String[] number = String.valueOf(originalNumber).split(".");
		StringBuilder wholePart = new StringBuilder(number[0]);
		StringBuilder fractionalPart = new StringBuilder(number[1]);
		int nextDigit = (int)(fractionalPart.charAt(precision + 1));
		int lastDigit = (int)(fractionalPart.charAt(precision)) + 1;

		if(nextDigit >= 5) {
			fractionalPart.setCharAt(precision, 
		}


	} */

	/**
	 This is the method which actually performs the sort.
	 @param unsortedList The unsorted list of integers.
	 */
	public static String[] mergeSort(String[] unsortedList) {
		String[] list = new String[unsortedList.length]; // I don't know if this is necessary, but it creates a copy of the unsorted list and works with that. Probably better to be safe than sorry, I guess...
		for(int c = 0; c < list.length; c++) {
			list[c] = unsortedList[c];
		}
		if(list.length == 1) {
			return list;
		}

		int sizeA = list.length / 2;
		int sizeB = list.length - sizeA;

		String[] listA = new String[sizeA];
		for(int a = 0; a < sizeA; a++) {
			listA[a] = list[a];
		}

		String[] listB = new String[sizeB];
		for(int b = 0; b < sizeB; b++) {
			listB[b] = list[b + sizeA];
		}
		
		listA = mergeSort(listA); // I love recursion! (As long as I don't have to do it myself...)
		listB = mergeSort(listB);

		return mergeLists(listA, listB);
	}

	/**
	 Merges two (pre-sorted) arrays together. These can be any length.
	 @param listC First list
	 @param listD Second list
	 */
	private static String[] mergeLists(String[] listC, String[] listD) {
		String[] listE = new String[listC.length + listD.length];
		int e = 0;

		while(listC.length > 0 && listD.length > 0) {
			if(Integer.decode(listC[0].substring(0, listC[0].length() - 1)).intValue() < Integer.decode(listD[0].substring(0, listD[0].length() - 1)).intValue()) {
				listE[e] = listD[0];
				listD = trimList(listD);
			} else {
				listE[e] = listC[0];
				listC = trimList(listC);
			}
			e++;
		}
		
		while(listC.length > 0) {
			listE[e] = listC[0];
			listC = trimList(listC);
			e++;
		}
		
		while(listD.length > 0) {
			listE[e] = listD[0];
			listD = trimList(listD);
			e++;
		}

/*
		for(int g = 0; g < listE.length; g++) {
			System.out.print(listE[g] + " ");
		}
		System.out.println("\n");
*/
		return listE;
	}

	private static String[] trimList(String[] originalList) {
		String[] shorterList = new String[originalList.length - 1];
		for(int z = 0; z < shorterList.length; z++) {
			shorterList[z] = originalList[z + 1];
		}

		return shorterList;
	}

	/**
	 Gets rid of empty elements in an array. An empty element is defined in this case as being one with the value 0.
	 @param originalArray The array which may have empty elements.
	 */
	private static int[] dropEmptyElements(int[] originalArray) {
		int newSize = 0;
		while(originalArray[newSize] != 0) {
			newSize++;
		}

		int[] shorterArray = new int[newSize];
		for(int y = 0; y < shorterArray.length; y++) {
			shorterArray[y] = originalArray[y];
		}

		return shorterArray;

	}
	
	/**
	 Gets rid of empty elements in an array. An empty element is defined in this case as being one with the value ''.
	 @param originalArray The array which may have empty elements.
	 */
	private static char[] dropEmptyElements(char[] originalArray) {
		int newSize = 0;
		int[] intArray = new int[originalArray.length];
		for(int w = 0; w < originalArray.length; w++) {
			intArray[w] = (int) originalArray[w];
		}
		
		intArray = dropEmptyElements(intArray);

		char[] shorterArray = new char[intArray.length];
		for(int v = 0; v < intArray.length; v++) {
			shorterArray[v] = (char) intArray[v];
		}

		return shorterArray;

	}

	// No-argument constructor
	public LetterFrequency() {}

	public static void main(String[] args){
	    
	}
}

