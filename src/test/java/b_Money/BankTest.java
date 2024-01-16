package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		assertThrows(AccountExistsException.class, () -> SweBank.openAccount("Ulrika"));
		try {
			SweBank.openAccount("Alice");
		} catch (AccountExistsException e) {
			fail("Expected no exception, but AccountExistsException was thrown");
		}
		assertThrows(AccountExistsException.class, () -> SweBank.openAccount("Alice"));

		// test account created
		Account expectedAliceAccount = new Account("Alice", SweBank.getCurrency());
		assertEquals(expectedAliceAccount.getBalance().getAmount(), SweBank.getBalance("Alice"));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		Money depositAmount = new Money(5000, SEK);
		SweBank.deposit("Ulrika", depositAmount);
		assertEquals((Integer) 5000, SweBank.getBalance("Ulrika"));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		// fill account to withdraw from
		SweBank.deposit("Ulrika", new Money(5000, SEK));

		SweBank.withdraw("Ulrika", new Money(2000, SEK));
		assertEquals((Integer) 3000, SweBank.getBalance("Ulrika"));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(10000, SEK));
		assertEquals((Integer) 10000, SweBank.getBalance("Ulrika"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(10000, SEK));
		SweBank.transfer("Ulrika", "Bob", new Money(5000, SEK));
		assertEquals((Integer) 5000, SweBank.getBalance("Ulrika"));
		assertEquals((Integer) 5000, SweBank.getBalance("Bob"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(10000, SEK));
		SweBank.addTimedPayment("Ulrika", "payment1", 5, 2, new Money(1000, SEK), Nordea, "Bob");
		SweBank.tick();
		SweBank.tick();
		assertEquals((Integer) 9000, SweBank.getBalance("Ulrika"));
		assertEquals((Integer) 1000, Nordea.getBalance("Bob"));
	}
}
