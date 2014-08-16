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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to mark and describe JMX bean attributes, by marking methods
 * in a class. If only the getter is marked, the attribute is read-only. If only
 * the setter is marked, the attribute write-only. if both methods are marked,
 * then the attribute is read-write enabled. Description and name can be
 * specified on either the setter or getter. The annotated method(s) has/have to
 * be public and must follow the JMX specification for attributes.
 * 
 * @author Udo Klimaschewski
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBeanAttribute {
	/**
	 * Name of this attribute, by default the standard Java bean syntax will be
	 * used to create a name out of the getter/setter name.
	 * 
	 * @return The name of the method.
	 */
	String name() default "";

	/**
	 * The description of this attribute, empty by default.
	 * 
	 * @return The attribute description.
	 */
	String description() default "";

	/**
	 * Resource bundle key that will be used to load the name from the bundle
	 * using the current <code>java.util.Locale</code>. A
	 * {@link JMXBean#resourceBundleName()} must be specified in the bean
	 * annotation for this to work.
	 * 
	 * @return The resource bundle key for the name.
	 */
	String nameKey() default "";

	/**
	 * Resource bundle key that will be used to load the description from the
	 * bundle using the current <code>java.util.Locale</code>. A
	 * {@link JMXBean#resourceBundleName()} must be specified in the bean
	 * annotation for this to work.
	 * 
	 * @return The resource bundle key for the description.
	 */
	String descriptionKey() default "";
	
	/**
	 * Optional sort value to use when the bean attributes are sorted.
	 * 
	 * @return The sort value.
	 */
	String sortValue() default "";
}
