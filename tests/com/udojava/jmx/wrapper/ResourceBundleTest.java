package com.udojava.jmx.wrapper;

import static org.junit.Assert.*;

import java.util.Locale;

import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;

import org.junit.Test;


public class ResourceBundleTest {

	@JMXBean(resourceBundleName="com.udojava.jmx.wrapper.ResourceBundleTest", descriptionKey="beanDescriptionKey")
	public class TestBean {
		@JMXBeanAttribute(nameKey="attribute1NameKey", descriptionKey="attribute1DescriptionKey")
		public String getAttribute1() {
			return "1";
		}
		
		@JMXBeanOperation(nameKey="methodNameKey", descriptionKey="methodDescriptionKey")
		public void method(@JMXBeanParameter(nameKey="paramNameKey", descriptionKey="paramDescriptionKey") String param) {
			
		}
	}
	
	@Test
	public void testbean() throws IntrospectionException, SecurityException {
		Locale defaultLocale = Locale.getDefault();
		
		Locale.setDefault(Locale.ENGLISH);
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean());
		MBeanInfo info0 = bean.getMBeanInfo();	
		assertEquals("BeanDescription_EN", info0.getDescription());
		
		Locale.setDefault(Locale.GERMAN);
		bean = new JMXBeanWrapper(new TestBean());
		info0 = bean.getMBeanInfo();	
		assertEquals("BeanDescription_DE", info0.getDescription());
		
		Locale.setDefault(Locale.FRENCH);
		bean = new JMXBeanWrapper(new TestBean());
		info0 = bean.getMBeanInfo();	
		assertEquals("BeanDescription_default", info0.getDescription());
		
		Locale.setDefault(defaultLocale);
	}
	
	@Test
	public void testAttribute() throws IntrospectionException, SecurityException {
		Locale defaultLocale = Locale.getDefault();
		
		Locale.setDefault(Locale.ENGLISH);
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean());
		MBeanAttributeInfo info0 = bean.getMBeanInfo().getAttributes()[0];	
		assertEquals("attribute1Name_EN", info0.getName());
		assertEquals("attribute1Description_EN", info0.getDescription());
		
		Locale.setDefault(Locale.GERMAN);
		bean = new JMXBeanWrapper(new TestBean());
		info0 = bean.getMBeanInfo().getAttributes()[0];
		assertEquals("attribute1Name_DE", info0.getName());
		assertEquals("attribute1Description_DE", info0.getDescription());
		
		Locale.setDefault(Locale.FRENCH);
		bean = new JMXBeanWrapper(new TestBean());
		info0 = bean.getMBeanInfo().getAttributes()[0];
		assertEquals("attribute1Name_default", info0.getName());
		assertEquals("attribute1Description_default", info0.getDescription());
		
		Locale.setDefault(defaultLocale);
	}
	
	@Test
	public void testOperation() throws IntrospectionException, SecurityException {
		Locale defaultLocale = Locale.getDefault();
		
		Locale.setDefault(Locale.ENGLISH);
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean());
		MBeanOperationInfo info0 = bean.getMBeanInfo().getOperations()[0];
		assertEquals("methodName_EN", info0.getName());
		assertEquals("methodDescription_EN", info0.getDescription());
		assertEquals("paramName_EN", info0.getSignature()[0].getName());
		assertEquals("paramDescription_EN", info0.getSignature()[0].getDescription());
		
		Locale.setDefault(Locale.GERMAN);
		bean = new JMXBeanWrapper(new TestBean());
		info0 = bean.getMBeanInfo().getOperations()[0];
		assertEquals("methodName_DE", info0.getName());
		assertEquals("methodDescription_DE", info0.getDescription());
		assertEquals("paramName_DE", info0.getSignature()[0].getName());
		assertEquals("paramDescription_DE", info0.getSignature()[0].getDescription());
		
		Locale.setDefault(Locale.FRENCH);
		bean = new JMXBeanWrapper(new TestBean());
		info0 = bean.getMBeanInfo().getOperations()[0];
		assertEquals("methodName_default", info0.getName());
		assertEquals("methodDescription_default", info0.getDescription());
		assertEquals("paramName_default", info0.getSignature()[0].getName());
		assertEquals("paramDescription_default", info0.getSignature()[0].getDescription());
		
		Locale.setDefault(defaultLocale);
	}
}
