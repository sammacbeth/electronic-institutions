package uk.ac.imperial.einst;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.imperial.einst.access.Apply;

public class ActionTest {

	Actor a = new StubActor("A");
	Actor b = new StubActor("B");
	Institution i1 = new StubInstitution("I1");
	Institution i2 = new StubInstitution("I2");

	@Test
	public void testEqualsSelf() {
		Action app1 = new Apply(a, i1, "test");
		assertTrue(app1.equalsIgnoreT(app1));
	}

	@Test
	public void testEqualsSame() {
		Action app1 = new Apply(a, i1, "test");
		Action app2 = new Apply(a, i1, "test");
		assertTrue(app1.equalsIgnoreT(app2));
		assertTrue(app2.equalsIgnoreT(app1));
	}

	@Test
	public void testNotEquals() {
		Action app1 = new Apply(a, i1, "test");
		Action app2 = new Apply(a, i1, "test2");
		Action app3 = new Apply(b, i1, "test");
		Action app4 = new Apply(a, i2, "test");
		Action app5 = new Apply(b, i2, "test2");
		assertFalse(app1.equalsIgnoreT(app2));
		assertFalse(app2.equalsIgnoreT(app1));
		assertFalse(app1.equalsIgnoreT(app3));
		assertFalse(app3.equalsIgnoreT(app1));
		assertFalse(app1.equalsIgnoreT(app4));
		assertFalse(app4.equalsIgnoreT(app1));
		assertFalse(app1.equalsIgnoreT(app5));
		assertFalse(app5.equalsIgnoreT(app1));
	}

	@Test
	public void testEqualsWildcard() {
		Action app1 = new Apply(a, i1, "test");
		Action app2 = new Apply(a, i1, null);
		Action app3 = new Apply(null, i1, "test");
		assertTrue(app1.equalsIgnoreT(app2));
		assertTrue(app2.equalsIgnoreT(app1));
		assertTrue(app1.equalsIgnoreT(app3));
		assertTrue(app3.equalsIgnoreT(app1));
	}

}
