/*
 * Copyright 2012 Udo Klimaschewski
 * 
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.udojava.jmx.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.ImmutableDescriptor;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

/**
 * This is a wrapper class that wraps a normal Java object into a JMX dynamic
 * bean by using Java annotations. It supports free naming and description of
 * the bean, bean attributes, operations and operation parameters. Names and
 * descriptions can also be loaded through a Java {@link ResourceBundle}. This
 * allows localization of names and descriptions at bean creation time.
 * 
 * To create a dynamic JMX bean, simply annotate your normal Java class and pass
 * an instance of it to the constructor. This wrapper class will act as a proxy
 * between the JMX system and your logic. Simply register the wrapped bean with
 * JMX and you can access it through JCOnsole, JVisualVM and other JMX clients.
 * 
 * Four annotation types are used to describe the bean:
 * <ul>
 * <li>{@link JMXBean} : Marks and describes a class to be used as a dynamic JMX
 * bean.</li>
 * <li>{@link JMXBeanAttribute} : Marks and describes methods (setter/getter) in
 * a JMXBean to be used as a JMX attribute.</li>
 * <li>{@link JMXBeanOperation} : Marks and describes a method to be used as a
 * JMX operation.</li>
 * <li>{@link JMXBeanParameter} : Describes a method parameter for JMX operation
 * parameters</li>
 * </ul>
 * 
 * Here is an example of an annotated class:
 * 
 * <pre>
 * &#064;JMXBean(description = &quot;My first JMX bean test&quot;)
 * public class MyBean {
 * 	int level = 0;
 * 
 * 	&#064;JMXBeanAttribute(name = &quot;Floor Level&quot;, description = &quot;The current floor level&quot;)
 * 	public int getLevel() {
 * 		return level;
 * 	}
 * 
 * 	&#064;JMXBeanAttribute
 * 	public void setLevel(int newLevel) {
 * 		level = newLevel;
 * 	}
 * 
 * 	&#064;JMXBeanOperation(name = &quot;Echo Test&quot;, description = &quot;Echoes the parameter back to you&quot;)
 * 	public String myMethod(
 * 			&#064;JMXBeanParameter(name = &quot;Input&quot;, description = &quot;String of what to echo&quot;) String param) {
 * 		return &quot;You said &quot; + param;
 * 	}
 * }
 * </pre>
 * 
 * <br>
 * <em><strong>Sorting the attributes and operations</strong></em><br>
 * <br>
 * To sort attributes and operations in a certain way, you have to mark the bean
 * as sorted.<br>
 * By default, attributes and operations will be sorted by their name. You can specify
 * a <code>sortValue</code> for the operations and attributes to override the
 * default value.
 * 
 * <pre>
 *       &#064;JMXBean(sorted=true)
 *       public class MyBean {
 *           int level = 0;
 *   
 *           &#064;JMXBeanAttribute(nameKey="level", descriptionKey="levelDescription", sortKey="1")
 *           public int getLevel() {
 *               return level;
 *           }
 *           
 *           &#064;JMXBeanOperation(sortValue="2")
 *           public String methodX(String p1) {
 *               return p1;
 *           }
 *       }
 * </pre>
 * 
 * <em><strong>How to use resource bundles for bean description:</strong></em><br>
 * <br>
 * To use a resource bundle, the resource bundles name has to be specified in
 * the bean annotation. In that case, the <code>nameKey</code> and
 * <code>descriptionKey</code> annotation attributes will be used to search the
 * bundle for the keys specified therein. The current default locale will be
 * used to find localized versions of names and descriptions. If no entry is
 * found in the bundle(s), the default names and descriptions will be used:
 * 
 * <pre>
 *       &#064;JMXBean(resourceBundleName="com.example.my.package.BundleName")
 *       public class MyBean {
 *           int level = 0;
 *   
 *           &#064;JMXBeanAttribute(nameKey="level", descriptionKey="levelDescription")
 *           public int getLevel() {
 *               return level;
 *           }
 * </pre>
 * 
 * <em><strong>How to use:</strong></em><br>
 * <br>
 * 
 * <pre>
 * MyBean bean = new MyBean();
 * JMXBeanWrapper wrappedBean = new JMXBeanWrapper(bean);
 * MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
 * mbs.registerMBean(wrappedBean, new Objectname(com.example.my.package:type=TestBean,name=My Bean));
 * </pre>
 * 
 * @author Udo Klimaschewski
 * 
 */
public class JMXBeanWrapper implements DynamicMBean {

	public static final String BEAN_OPERATION_SORT = "com.udojava.jmx.wrapper.BEAN_OPERATION_SORT";

	/**
	 * The generated bean info.
	 */
	private MBeanInfo beanInfo = null;

	/**
	 * The original object, passed in the constructor.
	 */
	private Object bean = null;

	/**
	 * A <code>Map</code> of the bean attributes, used to easily access the
	 * setter and getter methods of an attribute.
	 */
	private Map<String, BeanAttribute> beanAttributes = new HashMap<String, JMXBeanWrapper.BeanAttribute>();

	/**
	 * Map operation names to method names.
	 */
	private Map<String, String> operationMapping = new HashMap<String, String>();

	/**
	 * An optional resource bundle, if a resource bundle name is set, this
	 * bundle will be set.
	 */
	private ResourceBundle resourceBundle = null;

	/**
	 * Should the bean attributes and operations be sorted?
	 */
	private boolean sorted = false;

	/**
	 * Access to the attributes setter and getter methods.
	 * 
	 * @author Udo Klimaschewski
	 * 
	 */
	private class BeanAttribute {
		/**
		 * The getter method for this attribute.
		 */
		private Method getter;

		/**
		 * The setter method for this attribute.
		 */
		private Method setter;

		/**
		 * The description for this attribute.
		 */
		private String description;

		private String sortValue;

		/**
		 * Creates a new bean attribute.
		 * 
		 * @param getter
		 *            The getter method.
		 * @param setter
		 *            The setter method.
		 * @param description
		 *            The attribute description.
		 */
		public BeanAttribute(Method getter, Method setter, String description,
				String sortValue) {
			setGetter(getter);
			setSetter(setter);
			setDescription(description);
			setSortValue(sortValue);
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Method getGetter() {
			return getter;
		}

		public Method getSetter() {
			return setter;
		}

		public void setGetter(Method method) {
			this.getter = method;
		}

		public void setSetter(Method method) {
			this.setter = method;
		}

		public String getSortValue() {
			return sortValue;
		}

		public void setSortValue(String sortValue) {
			this.sortValue = sortValue;
		}

	}

	/**
	 * Creates a new dynamic JMX bean on the basis of an annotated class.
	 * 
	 * @param bean
	 *            The bean object which acts as a proxy target.
	 * @throws SecurityException
	 * @throws IntrospectionException
	 */
	public JMXBeanWrapper(Object bean) throws SecurityException,
			IntrospectionException {
		this.bean = bean;
		Class<?> beanClass = bean.getClass();

		JMXBean jmxBean = beanClass.getAnnotation(JMXBean.class);
		if (jmxBean == null) {
			throw new IllegalArgumentException(beanClass.getName()
					+ " not a JMXBean annotated class.");
		}

		String beanName = jmxBean.className().equals("") ? beanClass.getName()
				: jmxBean.className();
		String beanDescription = jmxBean.description();

		if (!jmxBean.resourceBundleName().equals("")) {
			this.resourceBundle = ResourceBundle.getBundle(jmxBean
					.resourceBundleName());
			if (resourceBundle != null) {
				if (resourceBundle.containsKey(jmxBean.descriptionKey()))
					beanDescription = resourceBundle.getString(jmxBean
							.descriptionKey());
			}
		}

		this.sorted = jmxBean.sorted();

		List<MBeanAttributeInfo> attributes = getBeanAttributeInfos(bean);

		if (sorted) {
			Collections.sort(attributes, new Comparator<MBeanAttributeInfo>() {

				@Override
				public int compare(MBeanAttributeInfo o1, MBeanAttributeInfo o2) {
					BeanAttribute a1 = beanAttributes.get(o1.getName());
					BeanAttribute a2 = beanAttributes.get(o2.getName());
					String s1 = a1.getSortValue().equals("") ? o1.getName()
							: a1.getSortValue();
					String s2 = a2.getSortValue().equals("") ? o2.getName()
							: a2.getSortValue();
					return s1.compareTo(s2);
				}
			});
		}

		List<MBeanOperationInfo> operations = getBeanOperationInfos(bean);

		if (sorted) {
			Collections.sort(operations, new Comparator<MBeanOperationInfo>() {

				@Override
				public int compare(MBeanOperationInfo o1, MBeanOperationInfo o2) {
					String s1 = (String) o1.getDescriptor().getFieldValue(
							BEAN_OPERATION_SORT);
					String s2 = (String) o2.getDescriptor().getFieldValue(
							BEAN_OPERATION_SORT);
					if ("".equals(s1)) {
						s1 = o1.getName();
					}
					if ("".equals(s2)) {
						s2 = o2.getName();
					}
					return s1.compareTo(s2);
				}
			});
		}

		this.beanInfo = new MBeanInfo(beanName, beanDescription,
				attributes.toArray(new MBeanAttributeInfo[0]), null,
				operations.toArray(new MBeanOperationInfo[0]), null);

	}

	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		BeanAttribute att = beanAttributes.get(attribute);
		if (att == null) {
			throw new AttributeNotFoundException(attribute);
		}
		try {
			return att.getGetter().invoke(bean);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		BeanAttribute att = beanAttributes.get(attribute.getName());
		if (att == null) {
			throw new AttributeNotFoundException(attribute.getName());
		}
		try {
			att.getSetter().invoke(bean, attribute.getValue());
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		AttributeList result = new AttributeList();

		for (String name : attributes) {
			try {
				Object value = getAttribute(name);
				result.add(new Attribute(name, value));
			} catch (Exception ex) {

			}
		}
		return result;
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		AttributeList result = new AttributeList();

		Iterator<Object> iterator = attributes.iterator();
		while (iterator.hasNext()) {
			Attribute att = (Attribute) iterator.next();
			try {
				setAttribute(att);
				Attribute res = new Attribute(att.getName(),
						getAttribute(att.getName()));
				result.add(res);
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 * Check if a method signature matches.
	 * 
	 * @param signature
	 *            String array of parameter class names.
	 * @param method
	 *            The method to check for a match.
	 * @return <code>true</code> if the signature matches the method,
	 *         <code>false</code> otherwise.
	 */
	private boolean signatureMatches(String[] signature, Method method) {
		if (signature != null
				&& method.getParameterTypes().length != signature.length) {
			return false;
		}

		int i = 0;
		for (Class<?> clazz : method.getParameterTypes()) {
			if (!clazz.getName().equals(signature[i++]))
				return false;
		}
		return true;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		String methodName = operationMapping.get(actionName);
		if (methodName != null) {
			try {
				for (Method method : bean.getClass().getMethods()) {
					if (method.getName().equals(methodName)
							&& signatureMatches(signature, method))
						return method.invoke(bean, params);
				}
			} catch (Exception ex) {
				Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
				if (cause instanceof Exception) {
					throw new ReflectionException((Exception) cause, cause.getMessage());
				}
				throw new ReflectionException(ex,ex.getMessage());
			}
		}
		throw new MBeanException(new IllegalArgumentException(
				"Operation not found: " + actionName + "(" + signature + ")"));
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return beanInfo;
	}

	/**
	 * Analyzes the annotated bean and builds up all the operation informations.
	 * 
	 * @param bean
	 *            The annotated object to wrap.
	 * @return A <code>List</code> of bean operation informations, empty array
	 *         if no operations are defined.
	 */
	private List<MBeanOperationInfo> getBeanOperationInfos(Object bean) {
		Class<?> beanClass = bean.getClass();
		List<MBeanOperationInfo> operations = new ArrayList<MBeanOperationInfo>();
		for (Method method : beanClass.getMethods()) {
			JMXBeanOperation jmxBeanOperation = method
					.getAnnotation(JMXBeanOperation.class);
			/*
			 * Skip operations without annotation
			 */
			if (jmxBeanOperation == null) {
				continue;
			}
			String name = jmxBeanOperation.name();
			String description = jmxBeanOperation.description();
			String sortValue = jmxBeanOperation.sortValue();
			if (resourceBundle != null) {
				if (!jmxBeanOperation.nameKey().equals("")) {
					if (resourceBundle.containsKey(jmxBeanOperation.nameKey()))
						name = resourceBundle.getString(jmxBeanOperation
								.nameKey());
				}
				if (!jmxBeanOperation.descriptionKey().equals("")) {
					if (resourceBundle.containsKey(jmxBeanOperation
							.descriptionKey()))
						description = resourceBundle.getString(jmxBeanOperation
								.descriptionKey());
				}
			}
			if ("".equals(name)) {
				name = method.getName();
			}
			int impact = MBeanOperationInfo.UNKNOWN;
			switch (jmxBeanOperation.impactType()) {
			case INFO:
				impact = MBeanOperationInfo.INFO;
				break;
			case ACTION:
				impact = MBeanOperationInfo.ACTION;
				break;
			case ACTION_INFO:
				impact = MBeanOperationInfo.ACTION_INFO;
				break;
			default:
				impact = MBeanOperationInfo.UNKNOWN;
				break;
			}
			int counter = 0;
			ArrayList<MBeanParameterInfo> pInfos = new ArrayList<MBeanParameterInfo>();
			Class<?>[] classes = method.getParameterTypes();
			Annotation[][] paramAnnotations = method.getParameterAnnotations();
			for (Class<?> clazz : classes) {
				String paramName = "param" + ++counter;
				String paramType = clazz.getName();
				String paramDescription = "";
				if (paramAnnotations[counter - 1].length > 0) {
					for (Annotation a : paramAnnotations[counter - 1]) {
						if (a instanceof JMXBeanParameter) {
							JMXBeanParameter jmxBeanParameter = (JMXBeanParameter) a;
							paramDescription = jmxBeanParameter.description();
							if (!"".equals(jmxBeanParameter.name()))
								paramName = jmxBeanParameter.name();
							if (resourceBundle != null) {
								if (!jmxBeanParameter.nameKey().equals("")) {
									if (resourceBundle
											.containsKey(jmxBeanParameter
													.nameKey()))
										paramName = resourceBundle
												.getString(jmxBeanParameter
														.nameKey());
								}
								if (!jmxBeanParameter.descriptionKey().equals(
										"")) {
									if (resourceBundle
											.containsKey(jmxBeanParameter
													.descriptionKey()))
										paramDescription = resourceBundle
												.getString(jmxBeanParameter
														.descriptionKey());
								}
							}
							continue;
						}
					}
				}
				MBeanParameterInfo pInfo = new MBeanParameterInfo(paramName,
						paramType, paramDescription);
				pInfos.add(pInfo);
			}

			Map<String, String> descriptorValues = new HashMap<String, String>();
			descriptorValues.put(BEAN_OPERATION_SORT, sortValue);
			ImmutableDescriptor desc = new ImmutableDescriptor(descriptorValues);

			MBeanOperationInfo info = new MBeanOperationInfo(name, description,
					pInfos.toArray(new MBeanParameterInfo[0]), method
							.getReturnType().getName(), impact, desc);
			operationMapping.put(name, method.getName());
			operations.add(info);
		}
		return operations;
	}

	/**
	 * Analyzes an annotated bean for JMX attributes.
	 * 
	 * @param bean
	 *            The annotated object to wrap.
	 * @return A <code>List</code> of attribute informations, empty array if no
	 *         attributes were found.
	 * @throws IntrospectionException
	 */
	private List<MBeanAttributeInfo> getBeanAttributeInfos(Object bean)
			throws IntrospectionException {

		/*
		 * Get all setters and getters and build up the map of attributes
		 */
		Class<?> beanClass = bean.getClass();
		for (Method method : beanClass.getMethods()) {
			JMXBeanAttribute jmxBeanAttribute = method
					.getAnnotation(JMXBeanAttribute.class);
			/*
			 * Skip attributes without annotation
			 */
			if (jmxBeanAttribute == null) {
				continue;
			}
			String description = jmxBeanAttribute.description();
			String name = jmxBeanAttribute.name();
			String sortValue = jmxBeanAttribute.sortValue();
			if (resourceBundle != null) {
				if (!jmxBeanAttribute.nameKey().equals("")) {
					if (resourceBundle.containsKey(jmxBeanAttribute.nameKey()))
						name = resourceBundle.getString(jmxBeanAttribute
								.nameKey());
				}
				if (!jmxBeanAttribute.descriptionKey().equals("")) {
					if (resourceBundle.containsKey(jmxBeanAttribute
							.descriptionKey()))
						description = resourceBundle.getString(jmxBeanAttribute
								.descriptionKey());
				}
			}

			if (method.getName().startsWith("get")
					|| method.getName().startsWith("is")) {
				if ("".equals(name)) {
					if (method.getName().startsWith("get")) {
						name = method.getName().substring(3);
					} else {
						name = method.getName().substring(2);
					}
					name = Character.toLowerCase(name.charAt(0))
							+ name.substring(1);
				}
				BeanAttribute att = beanAttributes.get(name);
				if (att == null) {
					beanAttributes.put(name, new BeanAttribute(method, null,
							description, sortValue));
				} else {
					att.setGetter(method);
					if ("".equals(att.getDescription())) {
						att.setDescription(description);
					}
				}
			} else if (method.getName().startsWith("set")) {
				if ("".equals(name)) {
					name = method.getName().substring(3);
					name = Character.toLowerCase(name.charAt(0))
							+ name.substring(1);
				}
				BeanAttribute att = beanAttributes.get(name);
				if (att == null) {
					beanAttributes.put(name, new BeanAttribute(null, method,
							description, sortValue));
				} else {
					att.setSetter(method);
					if ("".equals(att.getDescription())) {
						att.setDescription(description);
					}
				}
			} else {
				continue;
			}
		}
		/*
		 * Build the list of attributes out of the map and return it
		 */
		List<MBeanAttributeInfo> attributes = new ArrayList<MBeanAttributeInfo>();
		for (Map.Entry<String, BeanAttribute> entry : beanAttributes.entrySet()) {
			MBeanAttributeInfo info = new MBeanAttributeInfo(entry.getKey(),
					entry.getValue().getDescription(), entry.getValue()
							.getGetter(), entry.getValue().getSetter());
			attributes.add(info);
		}
		return attributes;
	}

	@Override
	public String toString() {
		return bean.toString() + ":" + beanInfo.toString();
	}

}
