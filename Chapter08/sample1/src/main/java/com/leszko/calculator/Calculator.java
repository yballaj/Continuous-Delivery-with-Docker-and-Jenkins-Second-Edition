package com.leszko.calculator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Calculator service that provides basic arithmetic operations.
 */

@Service
public class Calculator {
        final static int UML_NUMBER_1 = 3; // Renamed constant

	  /**
           * Sums two integers.
           *
           * @param a the first integer
           * @param b the second integer
           * @return the sum of a and b
          */
	@Cacheable("sum")
	public int sum(int a, int b) {
		/**
                 * This method prints the value of variable 'a' to the console.
                 * It constructs a string that includes the variable's name and its value, 
                 * then prints this information to standard output, allowing the user to see the current value of 'a'.
                */
		System.out.println("Value of a: " + a);
               
		return a + b;
	}
}
