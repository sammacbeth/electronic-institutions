package uk.ac.imperial.einst.micropay;

public class Account {

	final Object holder;
	double balance = 0;
	double minValue = 0;
	boolean frozen = false;

	public Account(Object holder, double balance) {
		super();
		this.holder = holder;
		this.balance = balance;
	}

	public Account(Object holder, double balance, double borrowLimit) {
		this(holder, balance);
		this.minValue = -borrowLimit;
	}

	public Object getHolder() {
		return holder;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getMinValue() {
		return minValue;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	@Override
	public String toString() {
		return "account(" + holder + ", " + balance + ")";
	}

}
