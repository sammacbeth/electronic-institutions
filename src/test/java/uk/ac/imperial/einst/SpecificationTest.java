package uk.ac.imperial.einst;

import org.junit.Before;

public abstract class SpecificationTest {

	protected EInstSession session = null;

	@Before
	public void setUp() throws Exception {
		if (session == null)
			this.session = new EInstSession(getModulesUnderTest());
		else
			this.session = new EInstSession(this.session);
	}

	abstract protected Iterable<Class<? extends Module>> getModulesUnderTest();

}
