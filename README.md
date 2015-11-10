JMXWrapper
===

### Introduction

JMXWrapper is a wrapper class that allows the creation of dynamic JMX MBeans by simply annotating a normal Java class.
Names and descriptions of JMX beans, attributes, operations and operation parameters can also be localized using standard Java ResourceBundles.

### Example

````java
 @JMXBean(description = "My first JMX bean test")
 public class MyBean {
 	int level = 0;
 
 	@JMXBeanAttribute(name = "Floor Level", description = "The current floor level")
 	public int getLevel() {
 		return level;
 	}
 
 	@JMXBeanAttribute
 	public void setLevel(int newLevel) {
 		level = newLevel;
 	}
 
 	@JMXBeanOperation(name = "Echo Test", description = "Echoes the parameter back to you")
 	public String myMethod(
 			@JMXBeanParameter(name = "Input", description = "String of what to echo") String param) {
 		return "You said " + param;
 	}
 }
````
Now you can use the **JMXWrapper** to publish a bean including the annotated informations to the JMX Server:

````java 
MyBean bean = new MyBean();
JMXBeanWrapper wrappedBean = new JMXBeanWrapper(bean);
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
mbs.registerMBean(wrappedBean, new ObjectName("com.example.my.package:type=TestBean,name=My Bean"));
````
**That's all!**

### Sorting the attributes and operations

To sort attributes and operations in a certain way, you have to mark the bean as sorted.
By default, attributes and operations will be sorted by their name. You can specify a
`sortValue` for the operations and attributes to override the default value.

````java
      @JMXBean(sorted=true)
      public class MyBean {
          int level = 0;
  
          @JMXBeanAttribute(nameKey="level", descriptionKey="levelDescription", sortKey="1")
          public int getLevel() {
              return level;
          }
          
          @JMXBeanOperation(sortValue="2")
          public String methodX(String p1) {
              return p1;
          }
      }
````
### Using ResourceBundles for names and descriptions

Instead of specifying names and descriptions directly into the annotations, you can use standard Java ResourceBundles.
You just have to specify the bundle name in the **JMXBean** annotation and then annotate the bundle keys for beans, attributes, operations and parameters:

````java
@JMXBean(resourceBundleName="com.example.my.package.BundleName")
        public class MyBean {
           int level = 0;
           @JMXBeanAttribute(nameKey="level", descriptionKey="levelDescription")
           public int getLevel() {
               return level;
           }
````
### Annotation types

Four annotation types can be used:

    JMXBean          : Marks and describes a class to be used as a dynamic JMX bean. 
    JMXBeanAttribute : Marks and describes methods (setter/getter) in a JMXBean to be
                       used as a JMX attribute. 
    JMXBeanOperation : Marks and describes a method to be used as a JMX operation. 
    JMXBeanParameter : Describes a method parameter for JMX operation parameters 

### Project layout

The software was created and tested using Java 1.6.0.
You can check it out directly to an Eclipse project, the necessary files are in the repository.

    src/   The Java sources
    tests/ JUnit tests

### Download / Maven

You can download the binaries, source code and JavaDoc jars from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.udojava%22%20a%3A%22JMXWrapper%22).

The project and source code in `zip` and `tar.gz` format can also be downloaded from the projects [release area](https://github.com/uklimaschewski/JMXWrapper/releases).

To include it in your Maven project, refer to the artifact in your pom:
````xml
<dependencies>
    <dependency>
        <groupId>com.udojava</groupId>
        <artifactId>JMXWrapper</artifactId>
        <version>1.2</version>
    </dependency>
</dependencies>
````

### Author and License

Copyright 2012-2015 by Udo Klimaschewski
- [about.me](http://about.me/udo.klimaschewski)
- [UdoJava.com](http://UdoJava.com)

The software is licensed under the MIT Open Source license (see [LICENSE](https://github.com/uklimaschewski/JMXWrapper/blob/master/LICENSE) file).

