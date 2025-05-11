public class TextCrypt {

    private String text;
    private String hash;

    //Even numbers shift up, Odd numbers shift down
    //The int value is the ammount shifted
    private char[] CharacterArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public TextCrypt(String text, String hash) {
	this.text = text;
	this.hash = hash;
    }

    public String encode() {
		String encodedStr = "";
		for(int i=0; i < this.hash.length(); i++) {
			char c = this.hash.charAt(i);
			
			int shift = getShift(c);
			System.out.println("Shift: " + shift);
			int type;

			if(shift % 2 == 1) {
			type = -1;
			}
			else {
			type = 1;
			}
			System.out.println("Change type: " + type);

			for(int j=0; j < this.text.length(); j++) {
			
			char textChar = this.text.charAt(j);
			System.out.println("Starting character: " + textChar);
			
			//The value of the char as an int
				int value = textChar;

			//Is going to be a negative or positive number
			int change = shift * type;

			int changedChar;
			
			if(value + change > 126) {
				int ammount = (value + change) - 126;
				changedChar = ammount;
			}
			else if(value + change < 32) {
				changedChar = 126 + (value + change);
			}
			else {
				//The new char value;
				changedChar = value + change;
			}
			System.out.println("Ending character: " + (char)changedChar + "\n\n");
			encodedStr += (char)changedChar;
			}
			this.text = encodedStr;
			encodedStr = "";
			System.out.println("-----------------------------------");
		}
		System.out.println(this.text);
		return this.text;
    }

    public int getShift(char c) {
	if(c == 'a') return 10;
	if(c == 'b') return 11;
	if(c == 'c') return 12;
	if(c == 'd') return 13;
	if(c == 'e') return 14;
	if(c == 'f') return 15;
	else return Integer.parseInt(String.valueOf(c));
    }

    public String decode(String encodedText, String hash) {
		System.out.println("Starting: " + hash);
		String decodedStr = "";
		for(int i=0; i < hash.length(); i++) {
			char c = hash.charAt(i);
			
			int shift = getShift(c);
			System.out.println("Shift: " + shift);
			int type;

			if(shift % 2 == 1) {
			type = 1;
			}
			else {
			type = -1;
			}
			System.out.println("Change type: " + type);

			for(int j=0; j < encodedText.length(); j++) {
			
			char textChar = encodedText.charAt(j);
			System.out.println("Starting character: " + textChar);
			
			//The value of the char as an int
				int value = textChar;

			//Is going to be a negative or positive number
			int change = shift * type;

			int changedChar;
			
			if(value + change > 126) {
				int ammount = (value + change) - 126;
				changedChar = ammount;
			}
			else if(value + change < 32) {
				changedChar = 126 + (value + change);
			}
			else {
				//The new char value;
				changedChar = value + change;
			}
			System.out.println("Ending character: " + (char)changedChar + "\n\n");
			decodedStr += (char)changedChar;
			}
			encodedText = decodedStr;
			decodedStr = "";
			System.out.println("-----------------------------------");
		}
		System.out.println(encodedText);
		return encodedText;
    }

}
