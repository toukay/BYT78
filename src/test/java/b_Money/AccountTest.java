package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("payment1", 30, 10, new Money(50000, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("payment1"));

		testAccount.removeTimedPayment("payment1");
		assertFalse(testAccount.timedPaymentExists("payment1"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		Money paymentAmount = new Money(1000, SEK);
		int interval = 3;
		int next = 1;
		testAccount.addTimedPayment("payment1", interval, next, paymentAmount, SweBank, "Alice");

		// after next
		testAccount.tick();

		// Check amount transferred to Alice correctly
		Integer expectedAliceBalance = 1001000; // 1000000 initial + 1 payments of 1000
		Integer aliceBalance = SweBank.getBalance("Alice");
		assertEquals(expectedAliceBalance, aliceBalance);

		// Check amount deducted from testAccount correctly
		Integer expectedTestAccountBalance = 9999000; // 10000000 initial - 1 payments of 1000
		Integer testAccountBalance = testAccount.getBalance().getAmount();
		assertEquals(expectedTestAccountBalance, testAccountBalance);

		// after interval
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();

		// Check amount transferred to Alice correctly
		expectedAliceBalance = 1002000; // 1000000 initial + 2 payments of 1000 each
		aliceBalance = SweBank.getBalance("Alice");
		assertEquals(expectedAliceBalance, aliceBalance);

		// Check amount deducted from testAccount correctly
		expectedTestAccountBalance = 9998000; // 10000000 initial - 2 payments of 1000 each
		testAccountBalance = testAccount.getBalance().getAmount();
		assertEquals(expectedTestAccountBalance, testAccountBalance);
	}

	@Test
	public void testDepositWithdraw() {
		Money initialBalance = testAccount.getBalance();
		Money depositAmount = new Money(500000, SEK);
		Money withdrawAmount = new Money(500000, SEK);

		// deposit
		testAccount.deposit(depositAmount);
		Money newExpectedDepositBalance = initialBalance.add(depositAmount);

		assertTrue(newExpectedDepositBalance.equals(testAccount.getBalance()));

		// withdraw
		testAccount.withdraw(withdrawAmount);
		Money newExpectedWithdrawBalance = newExpectedDepositBalance.sub(withdrawAmount);
		assertTrue(newExpectedWithdrawBalance.equals(testAccount.getBalance()));
	}
	
	@Test
	public void testGetBalance() {
		Money MoneyAmount = new Money(10000000, SEK);

		assertTrue(MoneyAmount.equals(testAccount.getBalance()));
	}
}
