package uk.ac.imperial.einst.resource;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.SpecificationTest;
import uk.ac.imperial.einst.StubActor;
import uk.ac.imperial.einst.StubInstitution;
import uk.ac.imperial.einst.access.RoleOf;
import uk.ac.imperial.einst.ipower.IPower;

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

		Set<String> contribRoles = new HashSet<String>();
		contribRoles.add("test");

		Pool pool = new Pool(i, contribRoles, Collections.<String> emptySet(),
				new ArtifactTypeMatcher(Object.class));
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
	public void testProvisionMultiPool() {
		Institution i = new StubInstitution("i1");
		Actor a = new StubActor("a1");
		RoleOf r = new RoleOf(a, i, "test");
		Set<String> contribRoles = new HashSet<String>();
		contribRoles.add("test");
		Pool p1 = new Pool(i, contribRoles, Collections.<String> emptySet(),
				new ArtifactTypeMatcher(Institution.class));
		Pool p2 = new Pool(i, contribRoles, Collections.<String> emptySet(),
				new ArtifactTypeMatcher(Actor.class));

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
		
		assertFalse(pr1.isValid());
		assertTrue(pr2.isValid());
		assertTrue(pr3.isValid());
		
		assertTrue(p1.getArtifacts().size() == 1);
		assertTrue(p1.getArtifacts().contains(i));
		assertTrue(p2.getArtifacts().size() == 1);
		assertTrue(p2.getArtifacts().contains(a));
	}
}
