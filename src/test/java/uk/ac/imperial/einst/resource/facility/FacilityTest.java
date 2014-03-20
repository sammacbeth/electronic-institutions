package uk.ac.imperial.einst.resource.facility;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.SpecificationTest;
import uk.ac.imperial.einst.StubActor;
import uk.ac.imperial.einst.StubInstitution;
import uk.ac.imperial.einst.micropay.Account;
import uk.ac.imperial.einst.micropay.MicroPayments;
import uk.ac.imperial.einst.resource.ArtifactTypeMatcher;
import uk.ac.imperial.einst.resource.Pool;

public class FacilityTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(Facilities.class);
		mods.add(MicroPayments.class);
		return mods;
	}

	@Test
	public void test() {
		session.LOG_WM = true;

		Institution i = new StubInstitution("i");
		Pool pool = new Pool(i, Collections.<String> emptySet(),
				Collections.<String> emptySet(),
				Collections.<String> emptySet(), new ArtifactTypeMatcher(
						Object.class));
		Set<Pool> pools = new HashSet<Pool>();
		pools.add(pool);
		Facility fac = new Facility(i, pools, 10, 5, 0.1, 0.1);
		session.insert(pool);
		session.insert(fac);
		session.insert(new Account(i, 0));
		session.insert(new Action(new StubActor("a"), i) {
		});
		session.incrementTime();

		session.insert(new Action(new StubActor("a"), i) {
		});
		session.incrementTime();

		// TODO proper checking of invoices.
	}

}
