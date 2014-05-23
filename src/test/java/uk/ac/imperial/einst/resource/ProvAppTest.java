package uk.ac.imperial.einst.resource;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import uk.ac.imperial.einst.access.RoleOf;
import uk.ac.imperial.einst.ipower.IPower;
import uk.ac.imperial.einst.resource.ProvisionAppropriationSystem.PoolUsage;

public class ProvAppTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(ProvisionAppropriationSystem.class);
		mods.add(IPower.class);
		return mods;
	}

	@Test
	public void testProvision() {
		Institution i = new StubInstitution("i1");
		Actor a = new StubActor("a1");
		RoleOf r = new RoleOf(a, i, "test");

		Pool pool = new Pool(i, RoleOf.roleSet("test"), RoleOf.roleSet(),
				RoleOf.roleSet(), new ArtifactTypeMatcher(Object.class));
		session.insert(pool);

		Provision p = new Provision(a, i, new Object());
		session.insert(p);
		session.incrementTime();

		assertFalse(p.isValid());

		session.insert(r);
		session.incrementTime();

		assertFalse(p.isValid());

		Provision p2 = new Provision(a, i, new Object());
		session.insert(p2);
		session.incrementTime();

		assertFalse(p.isValid());
		assertTrue(p2.isValid());

		session.retract(r);
		session.incrementTime();

		Provision p3 = new Provision(a, i, new Object());
		session.insert(p3);
		session.incrementTime();

		assertFalse(p.isValid());
		assertTrue(p2.isValid());
		assertFalse(p3.isValid());

	}

	@Test
	public void testProvisionMultiPool() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);

		Institution i = new StubInstitution("i1");
		Actor a = new StubActor("a1");
		RoleOf r = new RoleOf(a, i, "test");
		Set<String> contribRoles = RoleOf.roleSet("test");
		Pool p1 = new Pool(i, contribRoles, Collections.<String> emptySet(),
				Collections.<String> emptySet(), new ArtifactTypeMatcher(
						Institution.class));
		Pool p2 = new Pool(i, contribRoles, Collections.<String> emptySet(),
				Collections.<String> emptySet(), new ArtifactTypeMatcher(
						Actor.class));

		session.insert(r);
		session.insert(p1);
		session.insert(p2);
		session.incrementTime();

		Provision pr1 = new Provision(a, i, new Object());
		Provision pr2 = new Provision(a, i, a);
		Provision pr3 = new Provision(a, i, i);

		session.insert(pr1);
		session.insert(pr2);
		session.insert(pr3);

		session.incrementTime();

		List<Action> pows = ipow.powList(a, new Provision(a, null, null));
		assertTrue(pows.size() == 2);

		assertFalse(pr1.isValid());
		assertTrue(pr2.isValid());
		assertTrue(pr3.isValid());

		assertTrue(p1.getArtifacts().size() == 1);
		assertTrue(p1.getArtifacts().contains(i));
		assertTrue(p2.getArtifacts().size() == 1);
		assertTrue(p2.getArtifacts().contains(a));
	}

	@Test
	public void testAppropriateRequest() throws UnavailableModuleException {
		ProvisionAppropriationSystem pas = session
				.getModule(ProvisionAppropriationSystem.class);
		IPower ipow = session.getModule(IPower.class);

		Institution i = new StubInstitution("i1");
		Actor a1 = new StubActor("a1");
		RoleOf r1 = new RoleOf(a1, i, "test");
		Actor a2 = new StubActor("a2");
		RoleOf r2 = new RoleOf(a2, i, "test");
		Set<String> contribRoles = RoleOf.roleSet("test");
		Pool p1 = new Pool(i, contribRoles, contribRoles,
				Collections.<String> emptySet(), new ArtifactTypeMatcher(
						Institution.class));

		session.insert(r1);
		session.insert(r2);
		session.insert(p1);
		session.insert(new Provision(a2, i, i));
		session.incrementTime();

		// test appropriation powers
		assertTrue(ipow.pow(a1, new Appropriate(a1, i, null)));
		assertFalse(ipow.pow(a1, new Appropriate(a1, i, new Object())));
		assertTrue(ipow.pow(a1, new Appropriate(a1, i,
				new StubInstitution(null))));
		assertFalse(ipow.pow(a1, new Appropriate(a1, i, a1)));
		assertTrue(ipow.pow(a1, new Appropriate(a1, i, i)));

		Request req = new Request(a1, i, new ArtifactTypeMatcher(
				Institution.class), 5);
		session.insert(req);
		session.incrementTime();

		assertTrue(req.isValid());
		List<Object> apps = pas.getAppropriations(a1);
		assertTrue(apps.size() == 1);
		assertEquals(i, apps.get(0));

		// check we don't get this item again after another request
		apps = pas.getAppropriations(a1);
		req = new Request(a1, i, new ArtifactTypeMatcher(Institution.class), 5);
		session.insert(req);
		session.incrementTime();

		assertTrue(apps.size() == 1);

		Institution i2 = new StubInstitution("i2");
		session.insert(new Provision(a2, i, i2));
		req = new Request(a1, i, new ArtifactTypeMatcher(Institution.class), 5);
		session.insert(req);
		session.incrementTime();

		apps = pas.getAppropriations(a1);
		assertTrue(apps.size() == 2);
		assertEquals(i2, apps.get(1));

		PoolUsage use = pas.getPoolUsage(a1, p1, 0);
		assertEquals(0, use.provisions);
		assertEquals(2, use.appropriations);
	}

	@Test
	public void testRemoval() {
		Institution i = new StubInstitution("i1");
		Actor a1 = new StubActor("a1");
		RoleOf r1 = new RoleOf(a1, i, "test");
		Set<String> roles = RoleOf.roleSet("test");

		Pool p1 = new Pool(i, roles, Collections.<String> emptySet(), roles,
				new ArtifactTypeMatcher(Institution.class));

		session.insert(r1);
		session.insert(p1);
		session.insert(new Provision(a1, i, i));
		session.incrementTime();

		assertTrue(p1.getArtifacts().contains(i));

		session.insert(new Remove(a1, i, i));
		session.incrementTime();

		assertFalse(p1.getArtifacts().contains(i));
	}

	@Test
	public void testRemovalWithoutPower() {
		Institution i = new StubInstitution("i1");
		Actor a1 = new StubActor("a1");
		RoleOf r1 = new RoleOf(a1, i, "test");
		Set<String> roles = RoleOf.roleSet("test");

		Pool p1 = new Pool(i, roles, Collections.<String> emptySet(),
				Collections.<String> emptySet(), new ArtifactTypeMatcher(
						Institution.class));

		session.insert(r1);
		session.insert(p1);
		session.insert(new Provision(a1, i, i));
		session.incrementTime();

		assertTrue(p1.getArtifacts().contains(i));

		session.insert(new Remove(a1, i, i));
		session.incrementTime();

		assertTrue(p1.getArtifacts().contains(i));
	}

	@Test
	public void testPrune() {
		Institution i = new StubInstitution("i1");
		Actor a1 = new StubActor("a1");
		Actor a2 = new StubActor("a2");
		RoleOf r1 = new RoleOf(a1, i, "test");
		RoleOf r2 = new RoleOf(a2, i, "test");
		Set<String> roles = RoleOf.roleSet("test");

		Pool p1 = new Pool(i, roles, Collections.<String> emptySet(), roles,
				new ArtifactTypeMatcher(String.class));
		session.insert(r1);
		session.insert(r2);
		session.insert(p1);
		session.insert(new Provision(a1, i, "1"));
		session.insert(new Provision(a2, i, "2"));
		session.incrementTime();

		assertEquals(2, p1.getArtifacts().size());

		session.insert(new Prune(a1, i, ArtifactMatcher.ALL, 0, 0));
		session.incrementTime();

		assertEquals(0, p1.getArtifacts().size());

		session.insert(new Provision(a1, i, "3"));
		session.incrementTime();
		session.insert(new Provision(a1, i, "4"));
		session.incrementTime();
		session.insert(new Prune(a1, i, ArtifactMatcher.ALL, 3, 0));
		session.incrementTime();

		assertEquals(1, p1.getArtifacts().size());
		assertTrue(p1.getArtifacts().contains("4"));
	}
}
