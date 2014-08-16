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
 * Annotation used to describe a JMX bean operation parameter. The annotated
 * method parameter has to follow the JMX specification for operation
 * parameters. All parameters of a method will be used for the operation, even
 * if they are not annotated. Annotation of parameters is used only to further
 * describe the parameter and set a more meaningful name.
 * 
 * @author Udo Klimaschewski
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBeanParameter {
	/**
	 * Name of this parameter, <i>param1, param2, param3 etc.</i> by default.
	 * 
	 * @return The name of the parameter.
	 */
	String name() default "";

	/**
	 * The description of this parameter, empty by default.
	 * 
	 * @return The parameter description.
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
}
