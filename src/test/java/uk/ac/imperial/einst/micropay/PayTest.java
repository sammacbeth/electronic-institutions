package uk.ac.imperial.einst.micropay;

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

public class PayTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(MicroPayments.class);
		return mods;
	}
	
	@Test
	public void test() throws UnavailableModuleException {
		session.LOG_WM = true;
		MicroPayments pay = session.getModule(MicroPayments.class);
		
		Actor a = new StubActor("a");
		Institution i = new StubInstitution("i");
		
		Account aa = new Account(a, 10);
		Account ai = new Account(i, 0);
		Transfer t1 = new Transfer(a, i, 5);
		
		session.insert(aa);
		session.insert(ai);
		session.insert(t1);
		
		session.incrementTime();
		
		assertTrue(pay.isCleared(t1));
		assertEquals(5, aa.getBalance(), 0.0001);
		assertEquals(5, ai.getBalance(), 0.0001);
	}

}
