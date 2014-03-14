package uk.ac.imperial.einst.micropay;

public class Invoice {

	final Object payee;
	final Object payer;
	final String ref;
	final double amount;
	final int t;

	public Invoice(Object payee, Object payer, String ref, double amount, int t) {
		super();
		this.payee = payee;
		this.payer = payer;
		this.ref = ref;
		this.amount = amount;
		this.t = t;
	}

	public Object getPayee() {
		return payee;
	}

	public Object getPayer() {
		return payer;
	}

	public String getRef() {
		return ref;
	}

	public double getAmount() {
		return amount;
	}

	public int getT() {
		return t;
	}

	@Override
	public String toString() {
		return "invoice(" + payee + ", " + payer + ", " + ref + ", " + amount
				+ ", " + t + ")";
	}

}
