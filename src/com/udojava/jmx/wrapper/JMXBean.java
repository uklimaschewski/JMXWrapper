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
 */package com.udojava.jmx.wrapper;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Annotation type used to mark and describe a class as a JMX bean.
 * 
 * @author Udo Klimaschewski
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBean {
	/**
	 * The name of the class describing this bean, default is the full Java
	 * class name.
	 * 
	 * @return The class name.
	 */
	String className() default "";

	/**
	 * The description for this bean, empty by default.
	 * 
	 * @return The description
	 */
	String description() default "";

	/**
	 * Resource bundle key that will be used to load the description text from
	 * the bundle using the current {@link Locale#getDefault()}.
	 * 
	 * @return The resource bundle key.
	 */
	String descriptionKey() default "";

	/**
	 * If a resource bundle name is set, the descriptions and names of beans,
	 * attributes, operations and parameters can be placed in a resource bundle.
	 * Typically, this is a properties file for each locale or language. If a
	 * resource bundle name is specified, the the annotation attributes
	 * <code>nameKey</code> and <code>descriptionKey</code> will be used to
	 * search the bundle for translations.
	 * 
	 * @return The resource bundle name.
	 */
	String resourceBundleName() default "";
	
	/**
	 * Attributes and operations will be sorted in the result lists, if this is set to <code>true</code>.
	 * Sorting will by default be done using the attribute or operation name.
	 * A <code>sortValue</code> can be annotated for the attribute or operation. If there is one present, it will be used for sorting.
	 * 
	 * @return If the bean attributes and operations should be sorted.
	 */
	boolean sorted() default false;
}
