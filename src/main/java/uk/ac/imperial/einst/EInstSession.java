package uk.ac.imperial.einst;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

public class EInstSession {

	private final Logger logger = Logger.getLogger(SpecificationTest.class);
	protected StatefulKnowledgeSession session;
	protected KnowledgeBase kbase;
	private SessionPseudoClock clock;
	int t = 0;
	protected Set<Module> modules;

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

	public void incrementTime() {
		clock.advanceTime(1, TimeUnit.SECONDS);
		session.fireAllRules();
		t++;

		logger.info("Session:");
		for (Object o : session.getObjects()) {
			logger.info(o.toString());
		}
		logger.info("-----");
	}

	public void insert(Object o) {
		session.insert(o);
	}

	public void insert(Action a) {
		a.setT(t);
		session.insert(a);
	}

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
