package uk.ac.imperial.einst;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Action {

	final Actor actor;
	final Institution inst;
	boolean valid = false;
	int t = 0;

	protected Action(Actor actor, Institution inst) {
		super();
		this.actor = actor;
		this.inst = inst;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public Actor getActor() {
		return actor;
	}

	public Institution getInst() {
		return inst;
	}

	private static String[] ignoredFields = new String[] { "t", "valid" };

	/**
	 * Does a field-by-field comparison between this and act. null field values
	 * are treated as wildcards.
	 * 
	 * @param act
	 * @return true iff this and act have the same fields and each field pair
	 *         have equal or null values.
	 */
	public boolean equalsIgnoreT(Action act) {
		try {
			BeanInfo b1 = Introspector.getBeanInfo(this.getClass());
			BeanInfo b2 = Introspector.getBeanInfo(act.getClass());
			// cache property descriptors
			Map<String, PropertyDescriptor> pdIdx = new HashMap<String, PropertyDescriptor>();
			for (PropertyDescriptor pd1 : b1.getPropertyDescriptors()) {
				if (isSkipField(pd1))
					continue;

				pdIdx.put(pd1.getName(), pd1);
			}
			// compare class properties.
			for (PropertyDescriptor pd2 : b2.getPropertyDescriptors()) {
				if (isSkipField(pd2))
					continue;

				PropertyDescriptor pd1 = pdIdx.get(pd2.getName());
				if (pd1 == null
						|| !equalsIgnoreNull(pd1.getReadMethod().invoke(this),
								pd2.getReadMethod().invoke(act)))
					return false;
			}
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	private boolean isSkipField(PropertyDescriptor pd) {
		for (String f : ignoredFields) {
			if (pd.getName().equals(f))
				return true;
		}
		return false;
	}

	public static boolean equalsIgnoreNull(Object o1, Object o2) {
		if (o1 == null || o2 == null)
			return true;
		else
			return o1.equals(o2);
	}

}
