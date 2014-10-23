package com.nextcontrols.utility;

public class ROT

{

private char topChar = '~';

private char bottomChar = '!';

private int topInt = (int) topChar;

private int bottomInt = (int) bottomChar;

public ROT(){}

public ROT(char topChar, char bottomChar)

{

this.topChar = topChar;

this.bottomChar = bottomChar;

topInt = (int) topChar;

bottomInt = (int) bottomChar;

}

public char transform(char inputChar)

{

return internalTransform(inputChar);

}

public char transform(int inputInt)

{

return internalTransform((char) inputInt);

}

public String transform(String inputString)

{

StringBuilder sb = new StringBuilder();

for (int i = 0; i < inputString.length(); i++)

{

sb.append(internalTransform(inputString.charAt(i)));

}

return sb.toString();

}

private char internalTransform(char inputChar)

{

int inputInt = (int) inputChar;

if (inputInt > bottomInt - 1 && inputInt < topInt + 1)

{

inputInt -= bottomInt;

inputInt += ((topInt - bottomInt + 1) / 2);

inputInt %= (topInt - bottomInt + 1);

inputInt += bottomInt;

}

return (char) inputInt;

}

} 
