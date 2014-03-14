package uk.ac.imperial.einst.micropay;

import java.util.Iterator;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;

import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/micropayments.drl")
public class MicroPayments implements Module {

	StatefulKnowledgeSession session;

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.session = session;
	}

	public boolean isCleared(Transfer t) {
		return session.getQueryResults("cleared", t).size() > 0;
	}

	public Account getAccount(Object holder) {
		Iterator<QueryResultsRow> rowIt = session.getQueryResults("account",
				holder).iterator();
		if (rowIt.hasNext()) {
			return new WrappedAccount((Account) rowIt.next().get("account"));
		}
		return null;
	}

	public Account createAccount(Object holder, double balance, double limit) {
		Account a = new Account(holder, balance, limit);
		session.insert(a);
		return new WrappedAccount(a);
	}

	class WrappedAccount extends Account {

		FactHandle handle;
		Account delegate;

		public WrappedAccount(Account delegate) {
			super(delegate.getHolder(), delegate.getBalance(), delegate
					.getMinValue());
			this.delegate = delegate;
			this.handle = session.getFactHandle(delegate);
		}

		@Override
		public Object getHolder() {
			return delegate.getHolder();
		}

		@Override
		public double getBalance() {
			return delegate.getBalance();
		}

		@Override
		public void setBalance(double balance) {
			delegate.setBalance(balance);
			session.update(handle, delegate);
		}

		@Override
		public double getMinValue() {
			return delegate.getMinValue();
		}

	}

}
