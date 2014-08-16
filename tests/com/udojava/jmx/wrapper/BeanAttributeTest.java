package com.udojava.jmx.wrapper;

import static org.junit.Assert.assertEquals;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.junit.Test;

public class BeanAttributeTest {
	@JMXBean
	public class TestBean1 {
		private String stringAttribute;
		private boolean booleanAttribute;

		@JMXBeanAttribute
		public boolean isBooleanAttribute() {
			return booleanAttribute;
		}

		@JMXBeanAttribute
		public void setBooleanAttribute(boolean booleanAttribute) {
			this.booleanAttribute = booleanAttribute;
		}

		@JMXBeanAttribute
		public String getStringAttribute() {
			return stringAttribute;
		}

		@JMXBeanAttribute
		public void setStringAttribute(String stringAttribute) {
			this.stringAttribute = stringAttribute;
		}
	}

	@Test(expected=AttributeNotFoundException.class)
	public void testStringSetUnknown() throws IntrospectionException,
			SecurityException, InvalidAttributeValueException, MBeanException,
			ReflectionException, AttributeNotFoundException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());

		bean.setAttribute(new Attribute("unknownAttribute", "test value"));
	}

	@Test
	public void testStringSetGet() throws IntrospectionException,
			SecurityException, AttributeNotFoundException,
			InvalidAttributeValueException, MBeanException, ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());

		bean.setAttribute(new Attribute("stringAttribute", "test value"));
		assertEquals("test value", bean.getAttribute("stringAttribute"));

		bean.setAttribute(new Attribute("booleanAttribute", true));
		assertEquals(true, bean.getAttribute("booleanAttribute"));
	}

	@Test
	public void testGetAttributes() throws IntrospectionException,
			SecurityException, AttributeNotFoundException,
			InvalidAttributeValueException, MBeanException, ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());

		bean.setAttribute(new Attribute("stringAttribute", "test value"));
		bean.setAttribute(new Attribute("booleanAttribute", true));

		AttributeList attributes = bean
				.getAttributes(new String[] { "stringAttribute" });

		assertEquals("test value", ((Attribute) attributes.get(0)).getValue());
		assertEquals(1, attributes.size());

		attributes = bean.getAttributes(new String[] { "stringAttribute",
				"booleanAttribute" });

		assertEquals("test value", ((Attribute) attributes.get(0)).getValue());
		assertEquals(true, ((Attribute) attributes.get(1)).getValue());
		assertEquals(2, attributes.size());
	}

	@Test
	public void testSetAttributes() throws IntrospectionException,
			SecurityException, AttributeNotFoundException, MBeanException,
			ReflectionException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBean1());

		AttributeList aList = new AttributeList();

		aList.add(new Attribute("stringAttribute", "a string value"));
		aList.add(new Attribute("booleanAttribute", true));

		bean.setAttributes(aList);

		assertEquals("a string value", bean.getAttribute("stringAttribute"));
		assertEquals(true, bean.getAttribute("booleanAttribute"));

	}

}
