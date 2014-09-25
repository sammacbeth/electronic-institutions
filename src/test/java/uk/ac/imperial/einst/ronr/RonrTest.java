package uk.ac.imperial.einst.ronr;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.SpecificationTest;
import uk.ac.imperial.einst.StubActor;
import uk.ac.imperial.einst.StubInstitution;
import uk.ac.imperial.einst.UnavailableModuleException;
import uk.ac.imperial.einst.access.RoleOf;
import uk.ac.imperial.einst.ipower.IPower;
import uk.ac.imperial.einst.ronr.Motion.Status;
import uk.ac.imperial.einst.ronr.Vote.Choice;

public class RonrTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(RONR.class);
		mods.add(IPower.class);
		return mods;
	}

	@Test
	public void testOpenSession() throws UnavailableModuleException {
		session.LOG_WM = true;
		RONR ronr = session.getModule(RONR.class);
		IPower ipow = session.getModule(IPower.class);

		Institution i = new StubInstitution("I");
		Actor h = new StubActor("C");

		session.insert(i);
		session.insert(new RoleOf(h, i, "chair"));
		session.incrementTime();

		OpenSession s = new OpenSession(h, i, "sesh");
		assertTrue(ipow.pow(h, s));
		session.insert(s);
		session.incrementTime();
		assertTrue(s.isValid());
		Session sesh = ronr.getSession(i, "sesh");
		assertTrue(sesh.isSitting());
		assertEquals("sesh", sesh.getName());
		assertEquals(i, sesh.getInst());

		CloseSession c = new CloseSession(h, i, "sesh");
		assertTrue(ipow.pow(h, c));
		session.insert(c);
		session.incrementTime();
		assertTrue(c.isValid());
		assertFalse(sesh.isSitting());
	}

	@Test
	public void testMotion() throws UnavailableModuleException {
		session.LOG_WM = true;
		RONR ronr = session.getModule(RONR.class);
		IPower ipow = session.getModule(IPower.class);

		Institution i = new StubInstitution("I");
		Actor c = new StubActor("C");
		Actor p = new StubActor("P");
		Actor s = new StubActor("S");

		// open session
		session.insert(i);
		session.insert(new RoleOf(c, i, "chair"));
		session.insert(new RoleOf(p, i, "proposer")); // TODO should be
														// qualifies
		session.insert(new RoleOf(s, i, "seconder")); // TODO should be
														// qualifies
		session.insert(new RoleOf(c, i, "voter"));
		session.insert(new RoleOf(p, i, "voter"));
		session.insert(new RoleOf(s, i, "voter"));
		session.incrementTime();

		OpenSession os = new OpenSession(c, i, "sesh");
		session.insert(os);
		session.incrementTime();

		Session sesh = ronr.getSession(i, "sesh");
		Propose prop = new Propose(p, new Motion(sesh, "m1"));
		assertTrue(ipow.pow(p, prop));
		assertFalse(ipow.pow(c, new Propose(c, new Motion(sesh, "m1"))));
		assertFalse(ipow.pow(s, new Propose(s, new Motion(sesh, "m1"))));
		session.insert(prop);
		session.incrementTime();

		assertTrue(prop.isValid());
		Motion m = ronr.getMotion(i, "sesh", "m1");
		assertEquals(Motion.Status.Proposed, m.getStatus());

		Second sec = new Second(s, m);
		assertTrue(ipow.pow(s, sec));
		assertFalse(ipow.pow(c, new Second(c, m)));
		assertFalse(ipow.pow(p, new Second(p, m)));
		session.insert(sec);
		session.incrementTime();
		
		assertTrue(sec.isValid());
		assertEquals(Motion.Status.Seconded, m.getStatus());
		
		OpenBallot ob = new OpenBallot(c, m);
		assertTrue(ipow.pow(c, ob));
		session.insert(ob);
		session.incrementTime();
		
		assertTrue(ob.isValid());
		assertEquals(Motion.Status.Voting, m.getStatus());
		assertEquals(4, m.getVoting());
		
		Vote v = new Vote(s,m,Choice.NAY);
		assertTrue(ipow.pow(s, v));
		assertTrue(ipow.pow(p, new Vote(p,m,Choice.AYE)));
		assertFalse(ipow.pow(c, new Vote(c,m,Choice.AYE)));
		session.insert(v);
		session.incrementTime();
		
		assertTrue(v.isValid());
		assertFalse(ipow.pow(s, v));
		CloseBallot cb = new CloseBallot(c, m);
		assertTrue(ipow.pow(c, cb));
		session.insert(cb);
		
		session.incrementTime();
		
		Declare d = new Declare(c, m, Status.NotCarried);
		assertTrue(ipow.pow(c, d));
		session.insert(d);
		
		assertEquals(1, m.nays);
		assertEquals(0, m.ayes);
		
		session.incrementTime();
	}

}
