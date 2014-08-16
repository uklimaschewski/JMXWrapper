package com.udojava.jmx.wrapper;

import static org.junit.Assert.*;

import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.junit.Test;


public class BeanMethodTest {
	@JMXBean
	public class TestBean1 {
		@JMXBeanOperation
		public void voidMethod() {

		}

		@JMXBeanOperation
		public String complexMethod(String name, int value) {
			return name + " Test " + value;
		}
		
		@JMXBeanOperation(name="Renamed Method")
		public String renamedMethod() {
			return "ok";
		}
		
		@JMXBeanOperation
		public String m(String p1) {
			return p1;
		}
		
		@JMXBeanOperation
		public String m(String p1, String p2) {
			return p1 + " " + p2;
		}
		
		@JMXBeanOperation
		public String m(String p1, String p2, String p3) {
			return p1 + " " + p2 + " " + p3;
		}
	}

	@Test
	public void testVoidMethod() throws IntrospectionException, SecurityException, MBeanException, ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());
		
		assertNull(bean.invoke("voidMethod", null, null));
	}
	
	@Test
	public void testComplexMethod() throws IntrospectionException, SecurityException, MBeanException, ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());
		
		assertEquals("Hello Test 2", bean.invoke("complexMethod", new Object[] {"Hello", 2}, new String[] {"java.lang.String", "int"}));
	}
	
	@Test
	public void testRenamedMethod() throws IntrospectionException, SecurityException, MBeanException, ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());
		
		assertEquals("ok", bean.invoke("Renamed Method", null, null));
	}
	
	@Test
	public void testMethodSignature() throws IntrospectionException, SecurityException, MBeanException, ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());
		
		assertEquals("Hello", bean.invoke("m",new Object[] {"Hello"}, new String[] {"java.lang.String"}));
		
		assertEquals("Hello Two", bean.invoke("m",new Object[] {"Hello", "Two"}, new String[] {"java.lang.String", "java.lang.String"}));
		
		assertEquals("Hello Two Three", bean.invoke("m",new Object[] {"Hello", "Two", "Three"}, new String[] {"java.lang.String", "java.lang.String", "java.lang.String"}));
	}
}
