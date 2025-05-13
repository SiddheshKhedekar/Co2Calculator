package com.siddhesh.co2calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Co2CalculatorApplicationTests {

	// This test checks if the Spring application context loads correctly.
	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodRuns() {
		// Explicitly call main to increase code coverage
		Co2CalculatorApplication.main(new String[] {});
	}

}
