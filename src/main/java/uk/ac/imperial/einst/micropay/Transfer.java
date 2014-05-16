package uk.ac.imperial.einst.micropay;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Transfer extends Action {

	final Object payer;
	final Object payee;
	final double amount;

	public Transfer(Actor actor, Institution inst, Object payee, double amount) {
		super(actor, inst);
		this.payer = actor;
		this.payee = payee;
		this.amount = amount;
	}

	public Transfer(Actor actor, Object payee, double amount) {
		this(actor, null, payee, amount);
	}

	public Transfer(Institution payer, Object payee, double amount) {
		super(null, payer);
		this.payer = payer;
		this.payee = payee;
		this.amount = amount;
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

	@Override
	public String toString() {
		return "transfer(" + payer + ", " + payee + ", " + amount + ")"
				+ toStringSuffix();
	}

}
