package uk.ac.imperial.einst.ipower;

import org.drools.runtime.rule.Row;
import org.drools.runtime.rule.ViewChangedEventListener;

public class ObligationListener implements ViewChangedEventListener {

	final ObligationReactive actor;

	public ObligationListener(ObligationReactive actor) {
		super();
		this.actor = actor;
	}

	@Override
	public void rowAdded(Row row) {
		actor.onObligation((Obl) row.get("obl"));
	}

	@Override
	public void rowRemoved(Row row) {
	}

	@Override
	public void rowUpdated(Row row) {
	}

}
