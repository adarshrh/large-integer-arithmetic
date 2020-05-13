#CS 6301: Implementation of data structures and algorithms  
#Long Project 1: Integer arithmetic with arbitrarily large numbers

##Project Description  
Task is to implement the class Num that stores and performs arithmetic operations on
arbitrarily large integers. You must use the following data structure for representing Num: Array
of long integers, where the digits are in the chosen base. In particular, do not use strings to
represent the numbers. Each entry of the list stores exactly one long integer. The base is defined
to be 10 in the starter code, but you may modify it. In the discussions below, we will use base=10. 
For base = 10, the number 4028 is represented by the list: {8,2,0,4}.

###Functions to implement:
- Num(String s): Constructor for Num class; takes a string s as parameter, with a number in
decimal, and creates the Num object representing that number in the chosen base. Note
that, the string s is in base 10, even if the chosen base is not 10. The string s can
have arbitrary length.
- Num(long x): Constructor for Num class.
- String toString(): convert the Num class object into its equivalent string (in decimal).
There should be no leading zeroes in the string.
- Num add(Num a, Num b): sum of two numbers a+b stored as Num.
- Num subtract(Num a, Num b): a-b
- Num product(Num a, Num b): product of two numbers a*b.

###Testing
TestLP1.java provides tests for different functions. Set the required test case value and run the program.
