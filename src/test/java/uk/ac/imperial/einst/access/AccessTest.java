package uk.ac.imperial.einst.access;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.SpecificationTest;
import uk.ac.imperial.einst.StubActor;
import uk.ac.imperial.einst.StubInstitution;
import uk.ac.imperial.einst.UnavailableModuleException;
import uk.ac.imperial.einst.ipower.IPower;
import uk.ac.imperial.einst.ipower.Obl;

public class AccessTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(AccessControl.class);
		mods.add(IPower.class);
		return mods;
	}

	@Test
	public void testValidApply() {
		Actor a = new StubActor("A");
		Institution i = new StubInstitution("I");

		Apply app = new Apply(a, i, "test");
		session.insert(app);

		session.incrementTime();
		assertTrue(app.isValid());
	}

	@Test
	public void testInValidApply() {
		Actor a = new StubActor("A");
		Institution i = new StubInstitution("I");
		RoleOf r = new RoleOf(a, i, "test");

		session.insert(r);

		session.incrementTime();

		Apply app = new Apply(a, i, "test");
		session.insert(app);

		session.incrementTime();
		assertFalse(app.isValid());
	}

	@Test
	public void testDiscretionaryAssignment() throws UnavailableModuleException {
		Actor a = new StubActor("A");
		Actor g = new StubActor("G");
		Institution i = new StubInstitution("I");
		RoleOf r = new RoleOf(g, i, AccessControl.GATEKEEPER);
		ACMethod ac = new ACMethod(i, "test", ACMethod.DISCRETIONARY);
		AccessControl acmod = session.getModule(AccessControl.class);

		session.insert(r);
		session.insert(ac);
		session.incrementTime();

		Apply app = new Apply(a, i, "test");
		session.insert(app);

		session.incrementTime();
		assertTrue(app.isValid());

		Assign ass = new Assign(g, a, i, "test");
		session.insert(ass);
		session.incrementTime();

		Set<String> roles = acmod.getRoles(a, i);
		assertTrue(roles.size() == 1);
		assertTrue(roles.contains("test"));
	}

	@Test
	public void testAssignBeforeApplyFail() throws UnavailableModuleException {
		Actor a = new StubActor("A");
		Actor g = new StubActor("G");
		Institution i = new StubInstitution("I");
		RoleOf r = new RoleOf(g, i, "gatekeeper");
		ACMethod ac = new ACMethod(i, "test", ACMethod.DISCRETIONARY);
		AccessControl acmod = session.getModule(AccessControl.class);

		session.insert(r);
		session.insert(ac);
		session.incrementTime();

		Assign ass = new Assign(g, a, i, "test");
		session.insert(ass);
		session.incrementTime();

		Apply app = new Apply(a, i, "test");
		session.insert(app);

		session.incrementTime();
		assertTrue(app.isValid());

		Set<String> roles = acmod.getRoles(a, i);
		assertTrue(roles.size() == 0);
	}

	@Test
	public void testNoneAssignment() throws UnavailableModuleException {
		Actor a = new StubActor("A");
		Actor g = new StubActor("G");
		Institution i = new StubInstitution("I");
		RoleOf r = new RoleOf(g, i, AccessControl.GATEKEEPER);
		ACMethod ac = new ACMethod(i, "test", ACMethod.NONE);
		AccessControl acmod = session.getModule(AccessControl.class);
		IPower ipow = session.getModule(IPower.class);

		session.insert(r);
		session.insert(ac);
		session.incrementTime();

		Apply app = new Apply(a, i, "test");
		session.insert(app);

		session.incrementTime();
		assertTrue(app.isValid());

		List<Obl> obls = ipow.getObligations(g);
		assertTrue(obls.size() == 1);

		Assign ass = new Assign(g, a, i, "test");
		assertTrue(obls.get(0).matches(ass));
		session.insert(ass);
		session.incrementTime();

		Set<String> roles = acmod.getRoles(a, i);
		assertTrue(roles.size() == 1);
		assertTrue(roles.contains("test"));
	}

	@Test
	public void testResign() throws UnavailableModuleException {
		Actor a = new StubActor("A");
		Actor g = new StubActor("G");
		Institution i = new StubInstitution("I");
		RoleOf r = new RoleOf(g, i, AccessControl.GATEKEEPER);
		ACMethod ac = new ACMethod(i, "test", ACMethod.NONE);
		AccessControl acmod = session.getModule(AccessControl.class);

		session.insert(r);
		session.insert(ac);
		session.incrementTime();

		Apply app = new Apply(a, i, "test");
		session.insert(app);

		session.incrementTime();
		assertTrue(app.isValid());

		Assign ass = new Assign(g, a, i, "test");
		session.insert(ass);
		session.incrementTime();

		Set<String> roles = acmod.getRoles(a, i);
		assertTrue(roles.size() == 1);
		assertTrue(roles.contains("test"));

		Resign res = new Resign(a, i, "test");
		session.insert(res);
		session.incrementTime();

		assertTrue(res.isValid());
		roles = acmod.getRoles(a, i);
		assertEquals(0, roles.size());
	}

}
