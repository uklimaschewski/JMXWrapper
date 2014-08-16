package com.udojava.jmx.wrapper;

import static org.junit.Assert.assertEquals;

import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;

import org.junit.Test;

public class SortingTest {

	@JMXBean(sorted=true)
	public class TestBean1 {

		@JMXBeanAttribute(sortValue="1")
		public int getA3() {
			return 1;
		}

		@JMXBeanAttribute(sortValue="2")
		public int getA4() {
			return 2;
		}

		@JMXBeanAttribute(sortValue="4")
		public int getA2() {
			return 3;
		}

		@JMXBeanAttribute(sortValue="3")
		public int getA1() {
			return 4;
		}
		
		@JMXBeanOperation(sortValue="1")
		public String m3(String p1) {
			return p1;
		}

		@JMXBeanOperation(sortValue="2")
		public String m4(String p1) {
			return p1;
		}
		@JMXBeanOperation(sortValue="4")
		public String m2(String p1) {
			return p1;
		}
		@JMXBeanOperation(sortValue="3")
		public String m1(String p1) {
			return p1;
		}
	}

	@Test
	public void test() throws IntrospectionException, SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());
		
		MBeanAttributeInfo[] atts = bean.getMBeanInfo().getAttributes();
		
		assertEquals("a3", atts[0].getName());
		assertEquals("a4", atts[1].getName());
		assertEquals("a1", atts[2].getName());
		assertEquals("a2", atts[3].getName());
		
		MBeanOperationInfo[] ops = bean.getMBeanInfo().getOperations();
		
		assertEquals("m3", ops[0].getName());
		assertEquals("m4", ops[1].getName());
		assertEquals("m1", ops[2].getName());
		assertEquals("m2", ops[3].getName());
	}

}
