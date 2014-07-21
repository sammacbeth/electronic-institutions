package uk.ac.imperial.einst.micropay;

import java.util.concurrent.atomic.AtomicLong;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Transfer extends Action {

	final Object payer;
	final Object payee;
	final double amount;
	final long tid;
	double toPay;

	static AtomicLong idCounter = new AtomicLong();

	public Transfer(Actor actor, Institution inst, Object payee, double amount) {
		super(actor, inst);
		this.payer = actor;
		this.payee = payee;
		this.amount = amount;
		this.tid = idCounter.getAndIncrement();
		this.toPay = amount;
	}

	public Transfer(Actor actor, Object payee, double amount) {
		this(actor, null, payee, amount);
	}

	public Transfer(Institution payer, Object payee, double amount) {
		super(null, payer);
		this.payer = payer;
		this.payee = payee;
		this.amount = amount;
		this.tid = idCounter.getAndIncrement();
		this.toPay = amount;
	}

	public static Transfer merge(Transfer t1, Transfer t2) {
		Transfer t3;
		if (t1.getActor() == null) {
			t3 = new Transfer(t1.getInst(), t1.getPayee(), t1.getAmount()
					+ t2.getAmount());
		} else {
			t3 = new Transfer(t1.getActor(), t1.getInst(), t1.getPayee(),
					t1.getAmount() + t2.getAmount());
		}
		t3.setT(t1.getT());
		return t3;
	}

	public Object getPayer() {
		return payer;
	}

	public Object getPayee() {
		return payee;
	}

	public double getAmount() {
		return amount;
	}

	public double getToPay() {
		return toPay;
	}

	public void setToPay(double toPay) {
		this.toPay = toPay;
	}

	@Override
	public String toString() {
		return "transfer("+ tid +"," + payer + ", " + payee + ", " + amount + ")"
				+ toStringSuffix();
	}

}
