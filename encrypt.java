import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.io.ByteArrayOutputStream;

// For CPU time reading, excluding OS functions such as context switching and possible I/O time
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

// For parallelization
import java.util.concurrent.*;
import java.util.stream.IntStream;



/**
 * For proper decoding, the file types must all stay the same throughout the encoding and decoding process!!
 */
public class encrypt {

	private final static int MAX = 255;
	private final static int MIN = 0;

	private final static int SEL_INPUT = 0;
	private final static int SEL_OUTPUT = 1;
	private final static int SEL_PASS = 2;

	private final static int INPUT = 3;
	private final static int OUTPUT = 4;
	private final static int PASS = 5;


	//  0: -e null         encode/encrypt
	//  1: -d null         decode/decrypt
	//  2: -p "string"     password
	//  3: -i "string"     input file
	//  4: -o "string"     output file
	//  5: -a "string"     append contents
	//  6: -v null         view contents in standard output stream
	private static Boolean encode = false;
	private static Boolean decode = false;

	private static String password  = null;
	private static String input = null;
	private static String output = null;

	private static String append = null;
	private static Boolean view = false;

	private static boolean keep = true;
	private static boolean silent = false;

	private static boolean showhash = false;
    
    public static void main(String[] args) {

		String input = args[0];
		if(input.equals("--help")) {
			encrypt.keep = false;
			System.out.println( " _.........._\n"+
								"| |        | |\n"+
								"| |        | |"+ "\tScramble\n"+
								"| |        | |"+ "\tVersion 0.0.2\n" +
								"| |________| |"+ "\tIsaac L. Kenyon\n" +
								"|   ______   |\n"+
								"|  |    | |  |\n"+
								"|__|____|_|__|\n"
			);
			System.out.println(printHelp());
			return;
		}

		//  0: -e  ->  null       ->  encode/encrypt
		//  1: -d  ->  null       ->  decode/decrypt
		//  2: -p  ->  "string"   ->  password
		//  3: -i  ->  "string"   ->  input file
		//  4: -o  ->  "string"   ->  output file
		//  5: -a  ->  "string"   ->  append contents after decoding
		//  6: -A  ->  "string"   ->  append contents before decoding
		//  7: -v  ->  null       ->  view contents in standard output stream
		//  8: -s  ->  null       ->  silent mode, no standard output

		try {
			char lastChanged = ' ';
			for(int i=0; i < args.length; i++) {
				if(args[i].charAt(0) == '-' && args[i].length() == 2) {
					char c = args[i].charAt(1);
					switch(c) {
						case('e')->{ encrypt.encode = true; } 
						case('d')->{ encrypt.decode = true; } 
						case('p')->{ lastChanged = 'p'; } 
						case('i')->{ lastChanged = 'i'; } 
						case('o')->{ lastChanged = 'o'; } 
						case('a')->{ lastChanged = 'a'; } 
						case('v')->{ encrypt.view = true; }
						case('s')->{ encrypt.silent = true; }
						case('h')->{ encrypt.showhash = true; }
						default  ->{ throw new Exception("Argument type: '" + args[i].charAt(1) + "' not accepted."); }
					}
				}
				else {
					switch(lastChanged) {
						case('p') -> { encrypt.password = args[i]; }
						case('i') -> { encrypt.input = args[i]; }
						case('o') -> { encrypt.output = args[i]; }
						case('a') -> { encrypt.append = args[i]; }
						default   -> { throw new Exception("Argument input error!"); }
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		if(encrypt.silent == false) {
			System.out.println( " _.........._\n"+
								"| |        | |\n"+
								"| |        | |"+ "\tScramble\n"+
								"| |        | |"+ "\tVersion 0.0.2\n" +
								"| |________| |"+ "\tIsaac L. Kenyon\n" +
								"|   ______   |\n"+
								"|  |    | |  |\n"+
								"|__|____|_|__|"
			);
		}

        if(encrypt.keep == true) {

			try {
				if(encrypt.password == null) {
					throw new Exception("No password was provided!");
				}
				else if(encrypt.input == null) {
					throw new Exception("No input file provided!");
				}
				else if(encrypt.encode == true) {
					encode(encrypt.input, encrypt.output, getHash(encrypt.password), false);
					//System.out.println("encode");
				}
				else if(encrypt.decode == true) {
					decode(encrypt.input, encrypt.output, getHash(encrypt.password), false);
					//System.out.println("decode");
				}
				else if(encrypt.append != null) {
					append(encrypt.input, encrypt.output, getHash(encrypt.password), encrypt.append, false);
					//System.out.println("append");
				}

				if(encrypt.view == true) {
					decode(encrypt.input, null, getHash(encrypt.password), encrypt.view);
					System.out.println("view");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			

			// String InputFile = args[1];

			// String outputFile = args[2];

			// String decodedFile = args[3];
			// get an instance of the SHA-256 message digest algorithm
			//try {

				/**long start = System.currentTimeMillis();
				System.out.println("  Encoding...");
				encode(InputFile, outputFile, hexString.toString());
				System.out.println("  Done!");

				System.out.println("  Decoding...");
				decode(outputFile, decodedFile, hexString.toString());
				System.out.println("  Done!");
				long finish = System.currentTimeMillis();
				long time = finish - start;
				double time_s = (double)time / 1000.0;
				System.out.println("\nTotal time: " + time + "ms -> " + time_s + "s");**/


				// TextCrypt text = new TextCrypt("Test", hexString.toString());
				// String newText = text.encode();	
				// System.out.println("Encoded String:" + newText + ":");

				// TextCrypt text2 = new TextCrypt(newText, hexString.toString());
				// String decodedText = text2.decode(newText, hexString.toString());
				// System.out.println("Decoded String:" + decodedText + ":");
			// } catch(NoSuchAlgorithmException e) {
			// 	e.printStackTrace();
			// }
		}


		// Scanner scnr = new Scanner(System.in);

		// System.out.print("Enter Password: ");
		// String input = scnr.nextLine();
		
		// System.out.println();
		// Hash hash = new Hash("data.text", input);


		// String MyHash = hash.getFinalhash();
    }

	public static String getHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// compute the hash of the input string
			byte[] hash = md.digest(password.getBytes());

			// convert the hash to a hexadecimal string
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}

			// print the hash
			if(encrypt.showhash == true) {
				System.out.println("\nHash: " + hexString.toString() + "\n");
			}
			

			// return the hash
			return hexString.toString();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String printHelp() {
		return "Help:\n\nThe file extension must be same for input and output\n\n"+
		"  -e    Encrypt file\n"+
		"  -d    Decrypt file\n"+
		"  -i    Input file with extension\n"+  
		"  -o    Output file with extension\n" +
		"  -p    Password input\n"+
		"  -a    Append string onto file\n"+
		"  -v    View the file in standard output\n";
	}

	public static void encode(String inputFile, String outputFile, String hash, boolean view) {
		try {
			Path path = Paths.get(inputFile);
			byte[] fileContents = Files.readAllBytes(path);

			int totalBytes = fileContents.length;
			// long start = System.nanoTime();

			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

			// Make sure we have access to CPU time
			if (!threadMXBean.isThreadCpuTimeSupported()) {
				System.out.println("CPU time measurement is not supported.");
				return;
			}

			// Start measuring CPU time (in nanoseconds)
			long startCpuTime = threadMXBean.getCurrentThreadCpuTime();

			byte[] encodedBytes = scramble(fileContents, hash);
			// byte[] encodedBytes = compressed_scramble(fileContents, hash);
			// byte[] encodedBytes = compressed_PARALLEL_scramble(fileContents, hash);
			// byte[] encodedBytes = PARALLEL_scramble(fileContents, hash);
			// byte[] encodedBytes = compressed_PARALLEL_scramble_TWO(fileContents, hash);
			

			//System.out.println("\nLength: " + fileContents.length + "\nFirst Byte: " + fileContents[0]++);

			// long end = System.nanoTime();
			// long durationInNs = end - start;
			// double durationInSeconds = durationInNs / 1_000_000_000.0;

			long endCpuTime = threadMXBean.getCurrentThreadCpuTime();

			// Calculate the CPU time taken by the program in nanoseconds
			long cpuTimeTaken = endCpuTime - startCpuTime;

			// Convert the CPU time to milliseconds for easier reading
			double cpuTimeInSeconds = cpuTimeTaken / 1000000000.0;

			// System.out.println("\nEncoding " + totalBytes + " Bytes took " + cpuTimeInSeconds + " seconds\n");
			System.out.printf("\nEncoding %d Bytes took %.9f seconds\n", totalBytes, cpuTimeInSeconds);

			if(outputFile != null) {
				OutputStream os = new FileOutputStream(new File(outputFile));
            	os.write(fileContents);
			}
			if(view == true) {
				OutputStream os = System.out;
				os.write(encodedBytes);
				os.flush();
				os.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}




	}

	public static void decode(String inputFile, String outputFile, String hash, boolean view) {
		try {
			Path path = Paths.get(inputFile);
			byte[] fileContents = Files.readAllBytes(path);

			int totalBytes = fileContents.length;
			// long start = System.nanoTime();

			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

			// Make sure we have access to CPU time
			if (!threadMXBean.isThreadCpuTimeSupported()) {
				System.out.println("CPU time measurement is not supported.");
				return;
			}

			// Start measuring CPU time (in nanoseconds)
			long startCpuTime = threadMXBean.getCurrentThreadCpuTime();

			byte[] encodedBytes = IcanRead(fileContents, hash);
			//System.out.println("\nLength: " + fileContents.length + "\nFirst Byte: " + fileContents[0]++);

			// long end = System.nanoTime();
			// long durationInNs = end - start;
			// double durationInSeconds = durationInNs / 1_000_000_000.0;

			long endCpuTime = threadMXBean.getCurrentThreadCpuTime();

			// Calculate the CPU time taken by the program in nanoseconds
			long cpuTimeTaken = endCpuTime - startCpuTime;

			// Convert the CPU time to milliseconds for easier reading
			double cpuTimeInSeconds = cpuTimeTaken / 1000000000.0;

			// System.out.println("\nDecoding " + totalBytes + " Bytes took " + cpuTimeInSeconds + " seconds\n");
			System.out.printf("\nDecoding %d Bytes took %.9f seconds\n", totalBytes, cpuTimeInSeconds);

			if(outputFile != null) {
				OutputStream os = new FileOutputStream(new File(outputFile));
            	os.write(fileContents);
			}
			if(view == true) {
				OutputStream os = System.out;
				os.write(encodedBytes);
				//System.out.println("TEST");
				os.flush();
				os.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	//
	//  When appending, the file is decoded, appended, then encoded once again.
	//  This causes the files to have the exact same encoding, all the way until the new bytes are added.
	//  You just see the encoded value of those new bytes.
	//
	//  This could cause a problem, as someone who has access to past versions, and low-length appending can use analysis to 
	//  determine what the value appended is.
	//
	//  OR maybe NOT?!?!
	//  
	//  I still need to keep testing things to see how they work out, and what one can discover from it.
	//
	//
	//  IT IS APPENDING A STRING FOR SOME REASON
	public static void append(String inputFile, String outputFile, String hash, String value, boolean view) {
		try {
			Path path = Paths.get(inputFile);

			
			byte[] fileContents = Files.readAllBytes(path);

			byte[] ByteValue = value.getBytes();
			byte[] encodedBytes = IcanRead(fileContents, hash);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(encodedBytes);
			outputStream.write(ByteValue);

			byte c[] = outputStream.toByteArray();

			byte[] new_encodedBytes = scramble(c, hash);

			if(outputFile != null) {
				OutputStream os = new FileOutputStream(new File(outputFile));
            	os.write(new_encodedBytes);
			}
			if(view == true) {
				OutputStream os = System.out;
				os.write(new_encodedBytes);
				os.flush();
				os.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//**********************************************DOWNSIDES***************************************************
	//
	// This scramble will be the same in all cases up until the first character is different
	//
	// EXAMPLE:
	//    
	//    "first" and "firdt"
	//    
	// On both of these, the encode is the same until it reaches index 3
	//
	//    "fir|st"     "fir|dt"
	//
	//    3/5 of these encodings would be the exact same
	//
	// Though on a side note, anything that comes after it is mathmatically so rare to ever be the same it is basically impossible
	//
	// Even if the strings were 
	//
	//    "Tiis is a test and it is very hard" and "This is a test and it is very hard"
	//      ^
	//      |
	//   Even though just this letter is different, anything after it is not the same at all to the second  
	//
	//
	//
	public static byte[] scramble(byte[] array, String hash) {

		long startTime = System.nanoTime();

		for(int i=0; i < hash.length(); i++) {
			char c = hash.charAt(i);
			int shift = getShift(c);
			int dir = getDir(shift);
			shift = shift * dir;

			//System.out.println(c);
			//System.out.println("Shift(" + c + "): " + shift);
			//System.out.println("Byte int: " + (int)array[0]);

			// long loopTime = System.nanoTime();

			for(int j=0; j < array.length; j++) {
				int change = 0;
				int ammount = 0;

				if(j != 0) {
					ammount = (int)array[j] + shift + (int)array[j-1]; //+ offset;
				}
				else {
					// ammount = (int)array[j] + shift + (int)array[j+1]; //+ offset;
					ammount = (int)array[j] + shift + (int)array[j]; //+ offset;
				}

				while(ammount > encrypt.MAX) {
					int edit = ammount - encrypt.MAX;
					ammount = encrypt.MIN - 1 + edit;
				}
				while(ammount < encrypt.MIN) {
					int test = encrypt.MIN - ammount;
					ammount = encrypt.MAX + 1 - test;
				}

				array[j] = (byte)ammount;

				int newByte = change;
			}
			// long loopEndTime = System.nanoTime();
			// long duration = (loopEndTime - loopTime); // duration in nanoseconds
			// System.out.println("Inner Loop #" + i + " Time: " + (duration / 1_000_000_000.0) + " seconds");

		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // duration in nanoseconds
		System.out.println("Wall-Clock Elapsed Time: " + (duration / 1_000_000_000.0) + " seconds");

		return array;
	}

	public static byte[] compressed_scramble(byte[] array, String hash) {

		long startTime = System.nanoTime();

		// compressHash(hash);

		// for(int i=0; i < hash.length(); i++) {
			// char c = hash.charAt(i);
			int shift = compressHash(hash);
			int dir = getDir(shift);
			shift = shift * dir;

			//System.out.println(c);
			//System.out.println("Shift(" + c + "): " + shift);
			//System.out.println("Byte int: " + (int)array[0]);

			for(int j=0; j < array.length; j++) {
				int change = 0;
				int ammount = 0;

				if(j != 0) {
					ammount = (int)array[j] + shift + (int)array[j-1]; //+ offset;
				}
				else {
					// ammount = (int)array[j] + shift + (int)array[j+1]; //+ offset;
					ammount = (int)array[j] + shift + (int)array[j]; //+ offset;
				}

				while(ammount > encrypt.MAX) {
					int edit = ammount - encrypt.MAX;
					ammount = encrypt.MIN - 1 + edit;
				}
				while(ammount < encrypt.MIN) {
					int test = encrypt.MIN - ammount;
					ammount = encrypt.MAX + 1 - test;
				}

				array[j] = (byte)ammount;

				int newByte = change;
			}

		// }
		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // duration in nanoseconds
		System.out.println("Wall-Clock Elapsed Time: " + (duration / 1_000_000_000.0) + " seconds");

		return array;
	}

	public static byte[] PARALLEL_scramble(byte[] array, String hash) {
		// Create a thread pool to parallelize the inner loop
		ForkJoinPool forkJoinPool = new ForkJoinPool();

		long startTime = System.nanoTime();

		for (int i = 0; i < hash.length(); i++) {
			char c = hash.charAt(i);
			int shift = getShift(c);
			int dir = getDir(shift);
			final int updated_shift = shift * dir;

			// System.out.println("Starting new index on hash");

			// Use the ForkJoinPool to process the array in parallel
			forkJoinPool.submit(() -> {
				for (int j = 0; j < array.length; j++) {
					// Step 1: Calculate initial value (ammount)
					int ammount = (int) array[j] + shift;

					// Step 2: Handle range constraints using modulo arithmetic
					// We are assuming encrypt.MIN and encrypt.MAX are constants, and that encrypt.MAX > encrypt.MIN
					int range = encrypt.MAX - encrypt.MIN + 1; // The range size
					if (ammount > encrypt.MAX) {
						// Apply modulo for wrapping around
						ammount = encrypt.MIN + ((ammount - encrypt.MIN) % range);
					} else if (ammount < encrypt.MIN) {
						// Handle underflow case (wrap around to MAX)
						ammount = encrypt.MAX - ((encrypt.MIN - ammount - 1) % range);
					}

					// Step 3: Update the array
					array[j] = (byte) ammount;
				}

			}).join(); // Wait for this task to complete before moving to the next iteration of the hash
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // duration in nanoseconds
		System.out.println("Wall-Clock Elapsed Time: " + (duration / 1_000_000_000.0) + " seconds");

		forkJoinPool.shutdown(); // Shutdown the pool after all tasks are done
		return array;
	}

	public static byte[] compressed_PARALLEL_scramble(byte[] array, String hash) {
		// Create a thread pool to parallelize the inner loop
		ForkJoinPool forkJoinPool = new ForkJoinPool();

		long startTime = System.nanoTime();

		// for (int i = 0; i < hash.length(); i++) {
			// char c = hash.charAt(i);
			int shift = compressHash(hash);
			int dir = getDir(shift);
			final int updated_shift = shift * dir;

			// System.out.println("Starting new index on hash");

			int range = encrypt.MAX - encrypt.MIN + 1;

			IntStream.range(0, array.length).parallel().forEach(j -> {
				int ammount = (int) array[j] + shift;

				if (ammount > encrypt.MAX) {
					ammount = encrypt.MIN + ((ammount - encrypt.MIN) % range);
				} else if (ammount < encrypt.MIN) {
					ammount = encrypt.MAX - ((encrypt.MIN - ammount - 1) % range);
				}

				array[j] = (byte) ammount;
			});
		// }

		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // duration in nanoseconds
		System.out.println("Wall-Clock Elapsed Time: " + (duration / 1_000_000_000.0) + " seconds");

		forkJoinPool.shutdown(); // Shutdown the pool after all tasks are done
		return array;
	}

	public static byte[] compressed_PARALLEL_scramble_TWO(byte[] array, String hash) {
		// Create a thread pool to parallelize the inner loop
		ForkJoinPool forkJoinPool = new ForkJoinPool();

		long startTime = System.nanoTime();

		for (int i = 0; i < hash.length()-63; i++) {
			char c = hash.charAt(i);
			int shift = getShift(c);
			int dir = getDir(shift);
			final int updated_shift = shift * dir;

			// System.out.println("Starting new index on hash");

			int range = encrypt.MAX - encrypt.MIN + 1 - updated_shift;

			IntStream.range(0, array.length).parallel().forEach(j -> {
				int ammount = (int) array[j] + updated_shift;

				if (ammount > encrypt.MAX) {
					ammount = encrypt.MIN + ((ammount - encrypt.MIN) % range);
				} else if (ammount < encrypt.MIN) {
					ammount = encrypt.MAX - ((encrypt.MIN - ammount - 1) % range);
				}

				array[j] = (byte) ammount;
			});
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // duration in nanoseconds
		System.out.println("Wall-Clock Elapsed Time: " + (duration / 1_000_000_000.0) + " seconds");

		forkJoinPool.shutdown(); // Shutdown the pool after all tasks are done
		return array;
	}

	public static byte[] IcanRead(byte[] array, String hash) {
		for(int i=hash.length()-1; i >= 0; i--) {
			char c = hash.charAt(i);
			int shift = getShift(c);
			int dir = getDir(shift) * -1; //-1 to swap directions for the decoder
			shift = shift * dir;

			for(int j=array.length-1; j >= 0; j--) {
				int change = 0;
				int ammount = 0;

				if(j != 0) {
					ammount = (int)array[j] + shift - (int)array[j-1]; //+ offset;
				}
				else {
					// ammount = (int)array[j] + shift - (int)array[j+1]; //+ offset;
					ammount = (int)array[j] + shift - (int)array[j]; //+ offset;
				}

				while(ammount > encrypt.MAX) {
					int edit = ammount - encrypt.MAX;
					ammount = encrypt.MIN - 1 + edit;
				}
				while(ammount < encrypt.MIN) {
					int test = encrypt.MIN - ammount;
					ammount = encrypt.MAX + 1 - test;
				}

				//System.out.println("Shift: " + shift);
				//System.out.println("Before: " + array[j] + "\nAfter: " + ammount + "\n");
				array[j] = (byte)ammount;

				int newByte = change;
			}
		}
		return array;
	}

	public static byte[] PARALLEL_IcanRead(byte[] array, String hash) {
		for(int i=hash.length()-1; i >= 0; i--) {
			char c = hash.charAt(i);
			int shift = getShift(c);
			int dir = getDir(shift) * -1; //-1 to swap directions for the decoder
			shift = shift * dir;

			for(int j=array.length-1; j >= 0; j--) {
				int change = 0;
				int ammount = 0;

				if(j != 0) {
					ammount = (int)array[j] + shift; //+ offset;
				}
				else {
					// ammount = (int)array[j] + shift - (int)array[j+1]; //+ offset;
					ammount = (int)array[j] + shift; //+ offset;
				}

				while(ammount > encrypt.MAX) {
					int edit = ammount - encrypt.MAX;
					ammount = encrypt.MIN - 1 + edit;
				}
				while(ammount < encrypt.MIN) {
					int test = encrypt.MIN - ammount;
					ammount = encrypt.MAX + 1 - test;
				}

				//System.out.println("Shift: " + shift);
				//System.out.println("Before: " + array[j] + "\nAfter: " + ammount + "\n");
				array[j] = (byte)ammount;

				int newByte = change;
			}
		}
		return array;
	}

	public static int compressHash(String hash) {
		int value = 0;
		for (int i = 0; i < hash.length(); i++) {
			value += getShift(hash.charAt(i));
		}
		int cutoff_value = value % 16;

		System.out.println("Compessed Hash: " + cutoff_value);

		// Will return a value 0-15
		return value % 16;
	}

	public static int getShift(char c) {
		if(c == 'a') return 10;
		if(c == 'b') return 11;
		if(c == 'c') return 12;
		if(c == 'd') return 13;
		if(c == 'e') return 14;
		if(c == 'f') return 15;
		else return Integer.parseInt(String.valueOf(c));
    }

	public static int getDir(int shift) {
		if(shift % 2 == 1) {
			return -1;
		}
		return 1;
	}

	public static void getStats() {
		Properties p = System.getProperties();   
    	p.list(System.out); 
    	System.out.print("Total CPU:");
    	System.out.println(Runtime.getRuntime().availableProcessors());
    	System.out.println("Max Memory:" + Runtime.getRuntime().maxMemory() + "\n" + "available Memory:" + Runtime.getRuntime().freeMemory());
    	System.out.println("os.name=" + System.getProperty("os.name"));
	}
          
}
