package uk.ac.imperial.einst.ipower;

import uk.ac.imperial.einst.Action;

public interface PowerReactive {

	void onPower(Action act);

	void onPowerRetraction(Action pow);

}
