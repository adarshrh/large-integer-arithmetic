/**
 * IDSA Long Project 1
 * Group members:
 * Adarsh Raghupati   axh190002
 * Akash Akki         apa190001
 * Keerti Keerti      kxk190012
 * Stewart cannon     sjc160330
 */
package axh190002;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Num implements Comparable<Num> {

    static long defaultBase = 1000000000;
    long base = defaultBase;
    long[] arr;                        // array to store arbitrarily large integers
    boolean isNegative;                    // boolean flag to represent negative numbers
    int len;                        // actual number of elements of array that are used; number is stored in arr[0..len-1]
    static int numLimit = 9;

    /**
     * Takes a arbitrarily long number as a String and converts it into Num object
     * by converting string into long array with each long value less than or equal
     * to numLimit(9 by default).
     *
     * @param s
     */
    public Num(String s) {
        if (s.charAt(0) == '-') {
            this.isNegative = true;
            s = s.substring(1);
        }
        Num number = new Num();
        int len = s.length();
        number.base = defaultBase;
        int index = 0;
        number.arr = new long[(len / numLimit) + 1];

        while (len >= numLimit) {
            number.arr[index++] = Long.parseLong(s.substring(len - numLimit, len));
            len -= numLimit;
        }
        // If there are any digits left
        if (len > 0) {
            number.arr[index] = Long.parseLong(s.substring(0, len));
        }
        this.arr = number.arr;
        this.base = number.base;
        this.setNumLen();
    }


    /**
     * Constructor to create Num object using long parameter
     *
     * @param x
     */
    public Num(long x) {
        if (x == 0) {
            this.arr = new long[1];
            this.arr[0] = 0;
        } else {
            if (x < 0) {
                this.isNegative = true;
                x = Math.abs(x);
            }

            int index = 0;
            long temp = x;
            // find required length for array
            while (temp > 0) {
                temp /= this.base;
                index++;
            }
            this.arr = new long[index];
            int i = 0;
            // Convert given number to the required base and store in array
            while (x > 0) {
                this.arr[i++] = x % this.base;
                x = x / this.base;
            }
        }
        this.setNumLen();
    }

    public Num() {
    }

    /**
     * Calculates ans = a+b
     * Where a and b are arbitrarily large integers
     * @param a
     * @param b
     * @return
     */
    public static Num add(Num a, Num b) {
        boolean isA_Negative = a.isNegative;
        boolean isB_Negative = b.isNegative;
        Num ans = new Num();
        // if a is negative and b is positive: perform b-a
        if (isA_Negative && !isB_Negative) {
            a.isNegative = !a.isNegative;
            ans = subtract(b, a);
            a.isNegative = isA_Negative;
            return ans;
        }
        // if b is negative and a is positive: perform a-b
        else if (!isA_Negative && isB_Negative) {
            b.isNegative = !b.isNegative;
            ans = subtract(a, b);
            b.isNegative = isB_Negative;
            return ans;
        }

        ans.isNegative = a.isNegative;
        int lenMax, lenMin, j;
        long max[] = null;
        if (a.len > b.len) {
            max = a.arr;
            lenMax = a.len;
            lenMin = b.len;
        } else {
            max = b.arr;
            lenMax = b.len;
            lenMin = a.len;
        }
        ans.arr = new long[lenMax + 1];
        ans.base = a.base;
        int i, carry = 0;
        for (i = 0; i < lenMin; i++) {
            ans.arr[i] = a.arr[i] + b.arr[i] + carry;
            // if addition of two long exceeds more than number of digits in base
            if (ans.arr[i] >= a.base) {
                carry = (int) (ans.arr[i] / a.base);
                ans.arr[i] = ans.arr[i] % a.base;
            } else
                carry = 0;
        }
        // If there are elements left
        for (j = i; j < lenMax; j++) {
            ans.arr[j] = max[j] + carry;
            if (ans.arr[j] >= a.base) {
                carry = (int) (ans.arr[j] / a.base);
                ans.arr[j] = ans.arr[j] % a.base;
            } else
                carry = 0;
        }
        if (carry != 0)
            ans.arr[j++] = carry;
        ans.len = j;
        ans.setNumLen();
        return ans;
    }

    /**All array indices may not be used.
     * This function sets the length of the array on basis of used indices
     */
    private void setNumLen() {
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 0 && i == 0)
                this.len = 1;
            if (arr[i] > 0) {
                this.len = i + 1;
                break;
            }
        }
    }

    /**
     *Calculates ans = a-b
     * Where a and b are arbitrarily large integers
     * @param a
     * @param b
     * @return
     */
    public static Num subtract(Num a, Num b) {
        Num ans = new Num();
        ans.base = a.base;
        boolean isA_Negative = a.isNegative;
        boolean isB_Negative = b.isNegative;
        int i;
        long gt[] = null;
        long lt[] = null;
        int gtLen = 0, ltlen = 0;

        // If two numbers does not have same symbol then add two numbers
        if (isA_Negative != isB_Negative) {
            b.isNegative = !b.isNegative;
            ans = add(a, b);
            b.isNegative = isB_Negative;
            return ans;
        }

        a.isNegative = false;
        b.isNegative = false;
        int comp = a.compareTo(b);
        // If a is greater
        if (comp == 1) {
            gt = a.arr;
            lt = b.arr;
            ans.isNegative = isA_Negative;
            gtLen = a.len;
            ltlen = b.len;
        }
        // If b is greater
        else if (comp == -1) {
            gt = b.arr;
            lt = a.arr;
            ans.isNegative = !isB_Negative;
            gtLen = b.len;
            ltlen = a.len;
        }
        // if a and b are equal
        else {
            ans = new Num(0);
            ans.isNegative = false;
            ans.setNumLen();
            a.isNegative = isA_Negative;
            b.isNegative = isB_Negative;
            return ans;
        }
        // Perform subtraction of each long elements one by one
        ans.arr = new long[Math.max(a.len + 1, b.len + 1)];
        for (i = 0; i < ltlen; i++) {
            ans.arr[i] = gt[i] - lt[i];
            // To handle carry if gt[i] value is less than lt[i]
            if (ans.arr[i] < 0) {
                ans.arr[i] += ans.base;
                gt[i + 1] -= 1;
            }
        }
        int carry = 0;
        if (i < gtLen) {
            if (gt[i] < 0)
                carry = -1;
        }
        while (i < gtLen) {
            if (carry == -1) {
                if (gt[i] <= 0)
                    ans.arr[i] = defaultBase - 1;
                else {
                    ans.arr[i] = gt[i] - 1;
                    carry = 0;
                }
                ;

                i++;
            } else {
                ans.arr[i] = gt[i];
                i++;
            }
        }
        ans.setNumLen();
        a.isNegative = isA_Negative;
        b.isNegative = isB_Negative;
        return ans;
    }

    /**
     * Calculates: ans = a*b
     * Where a and b are arbitrarily large integers
     * This method expects that a and b to have the same base
     * @param a: first Num object
     * @param b: second Num object
     * @return a new Num that equals a*b
     */
    public static Num product(Num a, Num b) {
        return productIterative(a, b);
    }

    /**
     * Iterative method to calculate a*b.
     * Performs multiplication by standard method of multiplying number a by each digit of b and then adding the result
     * @param a
     * @param b
     * @return
     */
    public static Num productIterative(Num a, Num b) {
        Num ans = new Num();
        int i, j;
        if (a.isNegative == b.isNegative)
            ans.isNegative = false;
        else
            ans.isNegative = true;
        ans.base = a.base;
        ans.arr = new long[a.len + b.len + 1];
        int carry = 0;
        long val = 0;
//Standard multiplication: take one element from a.arr and multiply with each element of b.arr
        for (i = 0; i < a.len; i++) {
            for (j = 0; j < b.len; j++) {
                val = ans.arr[i + j] + (a.arr[i] * b.arr[j]) + carry;
                if (val >= ans.base) {
                    carry = (int) (val / ans.base);
                    val = val % ans.base;
                } else
                    carry = 0;

                ans.arr[j + i] = val;
            }
            if (carry != 0) {
                ans.arr[j + i] = carry;
                carry = 0;
            }

        }

        ans.setNumLen();
        return ans;
    }


    /**
     * generates a^n by calculating a^(n/2), then doing a^(n/2) * a^(n/2), then
     * multiplied by a again if n was odd
     *
     * @param a: Num to multiply
     * @param n: number of times to multiply a with itself
     * @return a^n
     */
    public static Num power(Num a, long n) {
        if (n == 1) {
            return a;
        }
        if (n == 0) {
            return new Num(1);
        }
        Num temp = power(a, n / 2);
        temp = product(temp, temp);
        if (n % 2 == 1) {
            temp = product(temp, a);
        }
        return temp;
    }

    /**
     * Calculates ans = a/b by the method similar to binary search
     * @param a numerator in the expression
     * @param b denominator in the expression
     * @return quotient
     */
    public static Num divide(Num a, Num b) {
        int comp = a.compareTo(b);
        Num gt = null;
        Num lt = null;
        if (comp == 1) {
            gt = a;
            lt = b;
        } else if (comp == -1) {
            return new Num(0);
        } else {
            Num ans = new Num(1);
            return ans;
        }
        Num high = gt;
        Num low = new Num(1);
        Num temp = new Num(0);
        Num mid = add(high, low).by2();
        Num prod = product(mid, lt);
        int prodComp = prod.compareTo(gt);
        while (prodComp != 0) {
            if (prodComp == 1)
                high = mid;
            else if (prodComp == -1)
                low = mid;
            temp = mid;
            mid = (add(high, low)).by2();
            if (temp.compareTo(mid) == 0)
                return mid;
            prod = product(mid, lt);
            prodComp = prod.compareTo(gt);
        }
        mid.setNumLen();
        return mid;
    }


    /**
     * Calculates: Remainder = a%b
     * @param a
     * @param b
     * @return remainder
     */
    public static Num mod(Num a, Num b) {
        Num remainder = new Num();
        int comp = a.compareTo(b);
        if (comp == -1) {
            // if the value to be divided is smaller than the divisor then that will be the remainder ex: 2/1200 : mod is 2
            return a;
        } else if (comp == 0) {
            // if both the number are equal then mod = 0
            return new Num(0);
        }
        remainder = subtract(a, product(divide(a, b), b));
        remainder.setNumLen();
        return remainder;
    }

    /**
     * Calculates square root of arbitrarily large number using binary search method
     * If the input is not a perfect square then truncated square root is returned
     * @param a
     * @return
     */
    public static Num squareRoot(Num a) {

        Num end = a;
        Num start = new Num("0");
        Num mid = add(add(start, end).by2(), new Num("1"));
        Num ans = null;
        while (start.compareTo(end) <= 0) {
            mid = add(start, end).by2();

            if (power(mid, 2).compareTo(a) == 0) {
                ans = mid;
                break;
            } else if (power(mid, 2).compareTo(a) < 0) {
                start = add(mid, new Num("1"));
                ans = mid;
            } else {
                end = add(mid, new Num(-1));

            }

        }

        return ans;

    }

    /**
     * Compares one Num object to other.
     * @param other
     * @return 0 if two numbers are equal. 1 if current Num is greater than the parameter other. -1 if current Num is less than parameter other
     */
    public int compareTo(Num other) {
        if (this.isNegative != other.isNegative) {
            if (this.isNegative == true)
                return -1;
            else
                return 1;
        } else {
            if (this.len > other.len) {
                if (this.isNegative == true)
                    return -1;
                else
                    return 1;
            } else if (this.len < other.len) {
                if (this.isNegative == true)
                    return 1;
                else
                    return -1;
            }
            for (int i = this.len - 1; i >= 0; i--) {
                if (this.arr[i] > other.arr[i])
                    return 1;
                else if (this.arr[i] < other.arr[i])
                    return -1;
            }
        }
        return 0;
    }

    /**
     * Output using the format "base: elements of list ..."
     * For example, if base=100, and the number stored corresponds to 10965,
     * then the output is "100: 65 9 1"
     */
    public void printList() {
        System.out.print(this.base + ": ");
        if (this.isNegative)
            System.out.print("-");
        for (int i = 0; i < this.len; i++) {
            System.out.print(" " + this.arr[i]);
        }
        System.out.println("");
    }

    /**
     * Return number to a string in base 10
      */
    public String toString() {
        if (this.base == defaultBase) {
            StringBuilder number = new StringBuilder();
            if (this.isNegative) {
                number.append('-');
            }
            for (int i = this.len - 1; i >= 0; i--) {
                if (i == this.len - 1) {
                    number.append(this.arr[i]);
                } else {
                    // while storing in long array leading zeros are not stored. Hence to restore
                    // that zeros make each long number to have numLimit digits.
                    number.append(String.format("%" + String.valueOf(numLimit) + "s", String.valueOf(this.arr[i])).replace(' ', '0'));
                }
            }
            return number.toString();
        } else {
            StringBuilder builder = new StringBuilder();
            Num num=this.convertBase(10);
            if(num.isNegative)
                builder.append('-');
            for(int i=num.len-1; i>=0; i--)
                builder.append(num.arr[i]);
            num.len=builder.length();
            return builder.toString();
        }

    }


    public long base() {
        return base;
    }

    /**
     * Returns number equal to "this" number, in base=newBase
     * @param newBase
     * @return
     */
    public Num convertBase(int newBase) {
        Num prev = convertSingleValue(this.base, newBase);
        prev.setNumLen();
        Num temp = new Num(0);
        temp.base = newBase;
        Num singleValue = new Num();
        for (int i = this.len - 1; i >= 0; i--) {
            temp = product(temp, prev);
            singleValue = convertSingleValue(this.arr[i], newBase);
            temp = add(temp, singleValue);
        }
        temp.isNegative = this.isNegative;

        return temp;

    }

    /**
     * Converts single long number to given base
     * @param val
     * @param newBase
     * @return
     */
    public Num convertSingleValue(long val, int newBase) {
        Num valInNewBase = new Num();
        int index = 0;
        valInNewBase.base = newBase;
        long temp = val;
        long x1 = temp;
        while (x1 > 0) {
            x1 /= 10;
            index++;
        }
        valInNewBase.arr = new long[index];
        int i = 0;
        while (temp > 0) {
            valInNewBase.arr[i] = temp % newBase;
            i++;
            temp = temp / newBase;
        }
        valInNewBase.setNumLen();
        return valInNewBase;
    }

    /**
     * Helper method for divide and squareRoot operations. Divides "this" number by 2
     * @return
     */
    public Num by2() {
        int i = this.len - 1;
        long v = 0, rem = 0;
        Num z = new Num();
        z.arr = new long[this.len];
        while (i >= 0) {
            v = this.arr[i];
            if (rem == 1)
                v += base;
            z.arr[i--] = v / 2;
            rem = v % 2;
        }
        z.setNumLen();
        return z;
    }

    /**
     * Evaluate an expression in postfix and return resulting number
     * Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
     *  a number: [1-9][0-9]*. There is no unary minus operator.
     * @param expr
     * @return
     */
    public static Num evaluatePostfix(String[] expr) {
        Stack<Num> stack = new Stack<Num>();

        HashSet<String> operatorSet = new HashSet<String>();

        operatorSet.add("+");
        operatorSet.add("-");
        operatorSet.add("*");
        operatorSet.add("/");
        operatorSet.add("%");
        operatorSet.add("^");
        for (int i = 0; i < expr.length; i++) {

            if (!operatorSet.contains(expr[i]))
                stack.push(new Num(expr[i]));
            else
                stack.push(compute(expr[i], stack.pop(), stack.pop()));
        }
        return stack.pop();
    }

    /**
     * Helper method to evaluate each operations of postfix
     * @param s
     * @param val1
     * @param val2
     * @return
     */
    private static Num compute(String s, Num val1, Num val2) {
        Num result = null;
        switch (s) {
            case "+":
                result = add(val2, val1);
                break;

            case "-":
                result = subtract(val2, val1);
                break;

            case "/":
                result = divide(val2, val1);
                break;

            case "*":
                result = product(val2, val1);
                break;
            case "^":
                long n = Long.parseLong(val1.toString());
                result = power(val2, n);
                break;
        }
        return result;
    }

    /**
     * Parse/evaluates an expression in infix and returns resulting number
     * Parser for following grammar is implemented:
     * E -> T {addopT}*
     * T -> F {multopF}*
     * F -> (E) | num
     * addop-> +|-
     * multop-> *|/
     * num-> digit num| digit
     * digit -> 0|1|...|9
     * @param expr
     * @return
     */
    public static Num evaluateExp(String expr) {
        Queue<String> qt = tokenize(expr);
        for (String s : qt)
            System.out.print(s);
        System.out.println();
        Num ans = evalE(qt);
        return ans;
    }

    /**
     * Read the infix expression and convert to tokens
     * @param expr
     * @return
     */
    public static Queue<String> tokenize(String expr) {
        Queue<String> queue = new LinkedList<>();
        char[] tokens = expr.toCharArray();
        int i = 0;
        int len = tokens.length;
        StringBuilder builder = new StringBuilder();
        while (i < len) {
            if (tokens[i] == ' ') {
                i++;
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '(' || tokens[i] == ')') {
                queue.offer(Character.toString(tokens[i]));
                i++;
            } else if (Character.isDigit(tokens[i])) {
                builder = new StringBuilder();
                while (i < len && Character.isDigit(tokens[i])) {
                    builder.append(tokens[i]);
                    i++;
                }
                queue.offer(builder.toString());
            }
        }
        return queue;
    }

    /**
     * Implements E -> T {addopT}* part of the grammar
     * @param qt
     * @return
     */
    private static Num evalE(Queue<String> qt) {

        Num val1 = evalT(qt);
        while (!qt.isEmpty() && ((qt.peek().equals("+")) || qt.peek().equals("-"))) {
            String oper = qt.remove();
            Num val2 = evalT(qt);
            if (oper.equals("+")) {
                val1 = add(val1, val2);
            } else
                val1 = subtract(val1, val2);
        }
        return val1;
    }

    /**
     * Implements T -> F {multopF}* part of the grammar
     * @param qt
     * @return
     */
    private static Num evalT(Queue<String> qt) {

        Num val1 = evalF(qt);

        //   System.out.print("queue peek "+qt.peek());
        while (!qt.isEmpty() && (qt.peek().equals("*") || qt.peek().equals("/"))) {
            String oper = qt.remove();
            Num val2 = evalF(qt);
            if (oper.equals("*")) {
                val1 = product(val1, val2);
            } else val1 = divide(val1, val2);
        }

        return val1;
    }

    /**
     * Implements below parts of the grammar
     * F -> (E) | num
     * addop-> +|-
     * multop-> *|/
     * num-> digit num| digit
     * digit -> 0|1|...|9
     *
     * @param qt
     * @return
     */
    private static Num evalF(Queue<String> qt) {

        Num val;
        if (qt.peek().equals("(")) {
            String oper = qt.remove();
            val = evalE(qt);
            oper = qt.remove();
        } else {
            String num = qt.remove();
            val = new Num(num);
        }
        return val;
    }

    public static void main(String[] args) {

    }
}
