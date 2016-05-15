package com.udojava.jmx.wrapper;

import static org.junit.Assert.*;

import javax.management.IntrospectionException;
import javax.management.MBeanOperationInfo;

import org.junit.Test;

import com.udojava.jmx.wrapper.BeanAnnotationTest.TestBeanFullyDescribed;

public class InheritanceTest {

	public InheritanceTest() {
	}

	@JMXBean
	public class TestBean {
		@JMXBeanOperation
		public String aMethod() {
			return "Hello";
		}
	}
	
	public class InheritedBean extends TestBean {
		@JMXBeanOperation
		public String aSecondMethod() {
			return "Hello 2";
		}
	}

	@Test
	public void test() throws IntrospectionException, SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new InheritedBean());
		
		int operationCount = bean.getMBeanInfo().getOperations().length;
		
		assertEquals(2, operationCount);
	}

}
