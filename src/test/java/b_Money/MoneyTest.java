package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(10000, (int)SEK100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK100.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("100.0 SEK", SEK100.toString());
	}

	@Test
	public void testUniversalValue() {
		assertEquals(1500, (int)SEK100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		assertTrue(EUR10.equals(new Money(1000, EUR)));
		assertFalse(EUR10.equals(new Money(1000, SEK)));
	}

	@Test
	public void testAdd() {
		assertEquals(20000, (int)SEK100.add(EUR10).getAmount());
	}

	@Test
	public void testSub() {
		assertEquals(10000, (int)SEK200.sub(EUR10).getAmount());
	}

	@Test
	public void testIsZero() {
		assertTrue(SEK0.isZero());
		assertFalse(SEK100.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals(-10000, (int)SEK100.negate().getAmount());
	}

	@Test
	public void testCompareTo() {
		assertTrue(SEK100.compareTo(new Money(10000, SEK)) == 0);
		assertTrue(SEK100.compareTo(SEK200) < 0);
		assertTrue(EUR20.compareTo(EUR10) > 0);
	}
}
