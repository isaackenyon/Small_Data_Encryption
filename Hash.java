import java.util.ArrayList;

public class Hash {

    private final static int HASH_SIZE = 64;

    //File name with extension that contains the ascii text data
    private String filename;

    //Password that the user wants to encrypt this data with
    private String password;
    private int passwordLength;

    //The size of the text data when placed into an array
    private int fileLength;

    private HashStructure hash;


    public Hash(String filename, String password) {
	this.filename = filename;
	this.password = password;

	this.passwordLength = this.password.length();

	this.hash = new HashStructure(this.password, HASH_SIZE);

	this.hash.printHash();
	/*for(int i=0; i < this.passwordLength; ++i) {
	    char c = this.password.charAt(i);
	    int value = ((int)c * passwordLength) % 16;

	}*/

    }

    public int getHash(String password) {
	char c = password.charAt(0);
	int hash = ((int)c * (2^16) * passwordLength) % 1277;
	return hash;
    }

    public String getFinalhash() {
	//System.out.println("Hash Class: " + this.hash.finalHash());
	return this.hash.finalHash();
    }





    /* Hash class */
    private class HashStructure {

        private ArrayList<Character> list;

	int hashSize;
	int length;

	int[] array;

	private HashStructure(String password, int hashSize) {
	    this.list = new ArrayList<Character>();
	    this.hashSize = hashSize;
	    this.array = new int[hashSize];

	    this.length = password.length();

	    for(int i=0; i < this.length; i++) {
		this.list.add(password.charAt(i));
		this.array[i] = getHash(this.list.get(i));
	    }
	}

	private int getHash(char c) {
	    int hash = ((int)c * (2^16)) % this.hashSize;
	    return hash;
	}

	private void printHash() {
	
	    for(int i=0; i < this.length; i++) {
		//System.out.println(i + ": " + this.array[i]);
	    }
	}

	private String finalHash() {

	    String binaryNumbers = "";

	    for(int i=0; i < this.length; i++) {
		int number = this.array[i];

		String value = "" + (byte)number;
		binaryNumbers += Integer.toBinaryString((byte)number);
		//System.out.println(value + " : " + Integer.toBinaryString((byte)number));
	    }

	    int length = binaryNumbers.length();
	    //System.out.println(length);

	    for(int j=0; j < 64-length; j++) {
		if(j+1 == 64-length) {
		    binaryNumbers = "0" + binaryNumbers;
		}
		else {
		    binaryNumbers = "0" + binaryNumbers;
		}
	    }

	    while(binaryNumbers.length() > 62) {
		binaryNumbers = binaryNumbers.substring(0, binaryNumbers.length()-1);
		//System.out.println(binaryNumbers.length());
	    }
		

	    long number = Long.parseLong(binaryNumbers, 2);
	    String hexStr = Long.toString(number, 16);

	    //System.out.println("\nhexStr: "+hexStr);
	    return hexStr;
	}
    }


    /* List Structure */
    private class Node {

	private ArrayList<Integer> list;
	private int length;

	private Node() {
	    this.list = new ArrayList<Integer>();
	    this.length = 0;
	}

	private void append(int value) {
	    this.list.add(value);
	    this.length++;
	}

	private int get(int index) {
	    return this.list.get(index);
	}

	private int getSize() {
	    return length;
	}


    }

}


