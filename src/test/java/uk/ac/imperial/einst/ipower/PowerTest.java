package uk.ac.imperial.einst.ipower;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.SpecificationTest;
import uk.ac.imperial.einst.StubActor;
import uk.ac.imperial.einst.StubInstitution;
import uk.ac.imperial.einst.UnavailableModuleException;
import uk.ac.imperial.einst.resource.Appropriate;
import uk.ac.imperial.einst.resource.ArtifactTypeMatcher;
import uk.ac.imperial.einst.resource.Provision;

public class PowerTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(IPower.class);
		return mods;
	}

	@Test
	public void testBasicPow() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);
		final Actor actor = new StubActor("a");
		final Actor otherActor = new StubActor("b");
		final Institution inst = new StubInstitution("i");
		final Institution otherInst = new StubInstitution("i2");
		final Action action = new Provision(actor, inst, inst);
		final Action otherAction = new Provision(actor, otherInst, inst);

		assertFalse(ipow.pow(actor, action));
		assertFalse(ipow.pow(otherActor, action));
		assertFalse(ipow.pow(actor, otherAction));
		assertEquals(0, ipow.getPowers(actor).size());

		session.insert(new Pow(actor, action));
		session.incrementTime();

		// pow to provision an object matching the specified action
		assertTrue(ipow.pow(actor, action));
		assertTrue(ipow.pow(actor, new Provision(actor, inst, inst)));
		// this method only allows the specified object in the provision
		assertFalse(ipow.pow(actor, new Provision(actor, inst, otherInst)));
		// but nulls are ignored
		assertTrue(ipow.pow(actor, new Provision(actor, inst, null)));

		assertFalse(ipow.pow(otherActor, action));
		assertFalse(ipow.pow(actor, otherAction));
		assertEquals(1, ipow.getPowers(actor).size());
	}

	@Test
	public void testWildCardPow() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);
		final Actor actor = new StubActor("a");
		final Actor otherActor = new StubActor("b");
		final Institution inst = new StubInstitution("i");
		final Institution otherInst = new StubInstitution("i2");
		final Action action = new Provision(actor, inst, null);

		session.insert(new Pow(actor, action));
		session.incrementTime();

		assertTrue(ipow.pow(actor, action));
		assertEquals(1, ipow.getPowers(actor).size());
		// pow true for any provisioned artifact
		assertTrue(ipow.pow(actor, new Provision(actor, inst, new Object())));
		assertTrue(ipow.pow(actor, new Provision(actor, inst, null)));
		// but false for different inst
		assertFalse(ipow.pow(actor, new Provision(actor, otherInst,
				new Object())));
		assertFalse(ipow.pow(actor, new Provision(actor, otherInst, null)));
		// and for different action types
		assertFalse(ipow.pow(actor, new Appropriate(actor, inst, new Object())));

		// no powers for other actor
		assertEquals(0, ipow.getPowers(otherActor).size());
		assertFalse(ipow.pow(otherActor, action));
		assertFalse(ipow.pow(otherActor, new Provision(otherActor, inst,
				new Object())));
		assertFalse(ipow.pow(otherActor, new Provision(actor, inst,
				new Object())));
	}

	@Test
	public void testClassMatching() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);
		final Actor actor = new StubActor("a");
		final Institution inst = new StubInstitution("i");
		final Action action = new Provision(actor, inst,
				new ArtifactTypeMatcher(Institution.class));

		session.insert(new Pow(actor, action));
		session.incrementTime();

		assertEquals(1, ipow.getPowers(actor).size());
		// cannot provision with different artifact types
		assertFalse(ipow.pow(actor, action));
		assertFalse(ipow.pow(actor, new Provision(actor, inst, new Object())));
		// only subclasses of Institution or of type Class<Institution>.
		assertTrue(ipow.pow(actor, new Provision(actor, inst, inst)));
		assertTrue(ipow.pow(actor,
				new Provision(actor, inst, Institution.class)));
	}

}
