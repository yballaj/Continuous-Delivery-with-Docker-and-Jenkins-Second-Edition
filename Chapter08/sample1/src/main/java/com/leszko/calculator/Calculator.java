package com.leszko.calculator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Calculator service that provides basic arithmetic operations.
 */

@Service
public class Calculator {
        final static int umlNUMBER1 = 3;

	  /**
           * Sums two integers.
           *
           * @param a the first integer
           * @param b the second integer
           * @return the sum of a and b
          */
	@Cacheable("sum")
	public int sum(int a, int b) {
		return a + b;
	}
}
