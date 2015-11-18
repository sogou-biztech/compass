/**
 * 
 */
package com.sogou.bizdev.compass.core.preprocessor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.util.PatternMatchUtils;

/** 
 * @Description: 根据方法名确定当前方法是去主库还是从库
 * @author zjc
 * @since 1.0.0 
 */
public class NameMatchMasterSlaveAttributeSource implements
		MasterSlaveAttributeSource {

	/** Keys are method names; values are MasterSlaveAttribute */
	private Map<String, MasterSlaveAttribute> nameMap = new HashMap<String, MasterSlaveAttribute>();
	
	
	/**
	 * Parses the given properties into a name/attribute map.
	 * Expects method names as keys and String attributes definitions as values,
	 * parsable into TransactionAttribute 
	 */
	/**
	 * @param masterSlaveAttributes
	 */
	public void setProperties(Properties masterSlaveAttributes) {
		for (Iterator<Object> it = masterSlaveAttributes.keySet().iterator(); it.hasNext(); ) {
			String methodName = (String) it.next();
			String value = masterSlaveAttributes.getProperty(methodName);
			MasterSlaveAttribute ms = Enum.valueOf(MasterSlaveAttribute.class, value.toUpperCase());
			nameMap.put(methodName, ms);
		}
	}
	
	
	@Override
	public MasterSlaveAttribute getMasterSlaveAttribute(Method method,Class<?> targetClass)
	{
		// look for direct name match
		String methodName = method.getName();
		MasterSlaveAttribute attr = (MasterSlaveAttribute) this.nameMap
				.get(methodName);

		if (attr == null) {
			// Look for most specific name match.
			String bestNameMatch = null;
			for (Iterator<String> it = this.nameMap.keySet().iterator(); it.hasNext();) {
				String mappedName =  it.next();
				if (isMatch(methodName, mappedName)
						&& (bestNameMatch == null || bestNameMatch.length() <= mappedName
								.length())) {
					attr = (MasterSlaveAttribute) this.nameMap.get(mappedName);
					bestNameMatch = mappedName;
				}
			}
		}

		return attr;
	}
	
	/**
	 * Return if the given method name matches the mapped name.
	 * <p>The default implementation checks for "xxx*", "*xxx" and "*xxx*" matches,
	 * as well as direct equality. Can be overridden in subclasses.
	 * @param methodName the method name of the class
	 * @param mappedName the name in the descriptor
	 * @return if the names match
	 * @see org.springframework.util.PatternMatchUtils#simpleMatch(String, String)
	 */
	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}

}
