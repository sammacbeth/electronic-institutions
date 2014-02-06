package uk.ac.imperial.einst;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.CompositeKnowledgeBuilder;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.time.SessionPseudoClock;

/**
 * Builds and manages a Drools session governing interactions their consequences
 * in an Electronic Institution. This is the entry points for agent interaction
 * with the institution.
 * 
 * @author Sam Macbeth
 * 
 */
public class EInstSession {

	private final boolean LOG_WM = true;

	private final Logger logger = Logger.getLogger(EInstSession.class);
	protected StatefulKnowledgeSession session;
	protected KnowledgeBase kbase;
	private SessionPseudoClock clock;
	int t = 0;
	protected Set<Module> modules;

	/**
	 * Create an Electronic Institution session with the given modules. Module
	 * classes are instantiated by the class and can be retrieved via
	 * {@link #getModule(Class)}.
	 * 
	 * @param modules
	 *            Set of module classes to use for rules and state access.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @see Module
	 */
	public EInstSession(Iterable<Class<? extends Module>> modules)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		super();
		// get rule files needed for modules and create session
		Set<String> ruleFiles = new HashSet<String>();
		for (Class<? extends Module> m : modules) {
			RuleResources deps = m.getAnnotation(RuleResources.class);
			if (deps != null) {
				ruleFiles.addAll(Arrays.asList(deps.value()));
			}
		}
		this.kbase = buildKnowledgeBase(ruleFiles.toArray(new String[] {}));
		createSession(this.kbase);
		// instantiate modules
		this.modules = new HashSet<Module>();
		for (Class<? extends Module> m : modules) {
			Constructor<? extends Module> ctor = m.getConstructor();
			Module mInst = ctor.newInstance();
			mInst.initialise(this, this.session);
			this.modules.add(mInst);
		}

	}

	/**
	 * Copy constructor. Allows generation of a new drools session without
	 * having to rebuild the knowledge base.
	 * 
	 * @param session
	 */
	public EInstSession(EInstSession session) {
		super();
		this.kbase = session.kbase;
		this.modules = new HashSet<Module>(session.modules);
		createSession(this.kbase);
	}

	static KnowledgeBase buildKnowledgeBase(String... ruleFiles) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		CompositeKnowledgeBuilder ckbuilder = kbuilder.batch();
		for (String file : ruleFiles) {
			ckbuilder.add(ResourceFactory.newClassPathResource(file,
					EInstSession.class.getClass()), ResourceType.DRL);
		}
		ckbuilder.build();
		if (kbuilder.hasErrors()) {
			System.err.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Drools compile errors");
		}
		// drools fusion config
		KnowledgeBaseConfiguration baseConf = KnowledgeBaseFactory
				.newKnowledgeBaseConfiguration();
		baseConf.setOption(EventProcessingOption.STREAM);

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(baseConf);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	protected void createSession(KnowledgeBase kbase) {
		KnowledgeSessionConfiguration sessionConf = KnowledgeBaseFactory
				.newKnowledgeSessionConfiguration();
		sessionConf.setOption(ClockTypeOption.get("pseudo"));
		session = kbase.newStatefulKnowledgeSession(sessionConf, null);
		clock = session.getSessionClock();
		session.setGlobal("logger", logger);
	}

	/**
	 * Increments the session clock and triggers rules.
	 */
	public void incrementTime() {
		clock.advanceTime(1, TimeUnit.SECONDS);
		session.fireAllRules();
		t++;

		if (LOG_WM) {
			logger.info("Session:");
			SortedSet<Object> wm = new TreeSet<Object>(
					new Comparator<Object>() {
						@Override
						public int compare(Object o1, Object o2) {
							return o1.toString().compareTo(o2.toString());
						}
					});
			wm.addAll(session.getObjects());
			for (Object o : wm) {
				logger.info(o.toString());
			}
			logger.info("-----");
		}
	}

	/**
	 * Inserts a new fact into the session.
	 * 
	 * @param o
	 */
	public void insert(Object o) {
		session.insert(o);
	}

	/**
	 * Inserts an institutional action into the session.
	 * 
	 * @param a
	 */
	public void insert(Action a) {
		a.setT(t);
		session.insert(a);
	}

	/**
	 * Get a module for access to internal session state.
	 * 
	 * @param moduleClass
	 * @return
	 * @throws UnavailableModuleException
	 */
	@SuppressWarnings("unchecked")
	public <M extends Module> M getModule(Class<M> moduleClass)
			throws UnavailableModuleException {
		for (Module m : this.modules) {
			if (moduleClass.isInstance(m)) {
				return (M) m;
			}
		}
		throw new UnavailableModuleException(moduleClass);
	}

}
