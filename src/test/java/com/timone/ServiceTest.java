package com.timone;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.timone.IService;
import com.timone.Service;

@RunWith(Parameterized.class)
public class ServiceTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "44.4", true, "73301", true }, { "3.4", true, "08801", true },
				{ "222", true, "90005", true }, { "12.56", true, "08801", true }, { "5.5", true, "08079", true },
				{ "3.2", true, "09300", true }, { "-3.3", false, "-0930", false }, { " ", false, " ", false },
				{ null, false, null, false }, { "44444", false, "89", false }, { "444.4654", false, "90.8", false },
				{ "1000", false, "111111", false } });
	}

	private IService service;
	@Parameterized.Parameter(0)
	public String weight;
	@Parameterized.Parameter(1)
	public boolean isValidWeight;
	@Parameterized.Parameter(2)
	public String zipCode;
	@Parameterized.Parameter(3)
	public boolean isValidZipCode;

	@Before
	public void setUp() throws Exception {
		service = new Service();
	}

	@Test
	public void isValidWeight() {
		assertSame(isValidWeight, service.isValidWeight(weight));
	}

	@Test
	public void isValidZipCode() {
		assertSame(isValidZipCode, service.isValidZipCode(zipCode));
	}

}