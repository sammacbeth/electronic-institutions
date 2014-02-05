package uk.ac.imperial.einst;

public class UnavailableModuleException extends Exception {

	public UnavailableModuleException(Class<? extends Module> moduleClass) {
		super("Module class '" + moduleClass.getCanonicalName()
				+ "' not available.");
	}

	private static final long serialVersionUID = 1584124989303508261L;

}
