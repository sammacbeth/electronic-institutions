package uk.ac.imperial.einst.resource;

import uk.ac.imperial.einst.Institution;

public interface AppropriationsListener {

	void onAppropriation(Object artifact, Institution from);

}
