package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;

	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		NOK = new Currency("NOK", 0.35);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("NOK", NOK.getName());
		assertEquals("EUR", EUR.getName());
	}

	@Test
	public void testGetRate() {
		assertEquals(0.15, SEK.getRate(), 0.001);
		assertEquals(0.20, DKK.getRate(), 0.001);
		assertEquals(0.35, NOK.getRate(), 0.001);
		assertEquals(1.5, EUR.getRate(), 0.001);
	}

	@Test
	public void testSetRate() {
		SEK.setRate(0.16);
		DKK.setRate(3.22);
		NOK.setRate(0.94);
		EUR.setRate(0.23);
		assertEquals(0.16, SEK.getRate(), 0.001);
		assertEquals(3.22, DKK.getRate(), 0.001);
		assertEquals(0.94, NOK.getRate(), 0.001);
		assertEquals(0.23, EUR.getRate(), 0.001);
	}

	@Test
	public void testGlobalValue() {
		assertEquals(150, (int)SEK.universalValue(1000)); // e.g. (0.15 * 1000) = 150
		assertEquals(200, (int)DKK.universalValue(1000));
		assertEquals(350, (int)NOK.universalValue(1000));
		assertEquals(1500, (int)EUR.universalValue(1000));
	}

	@Test
	public void testValueInThisCurrency() {
		assertEquals(10000, (int)SEK.valueInThisCurrency(1000, EUR)); // e.g. (1.5 * 1000) / 0.15 = 10000 (1.5 is EUR rate, and 0.15 is SEK rate)
		assertEquals(7500, (int)DKK.valueInThisCurrency(1000, EUR));
		assertEquals(4285, (int)NOK.valueInThisCurrency(1000, EUR));
		assertEquals(100, (int)EUR.valueInThisCurrency(1000, SEK));
	}

}
