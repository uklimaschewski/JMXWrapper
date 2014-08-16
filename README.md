A public Maven repository for my projects
================================

To use an artifact from this repository in your Maven project, add the following repository location to your pom.xml:

    <repositories>
        <repository>
            <id>uklimaschewski-maven-repo</id>
            <name>Maven repository from uklimaschewski on GitHub</name>
            <url>https://raw.github.com/uklimaschewski/maven-repo/master</url>
            <layout>default</layout>
        </repository>
    </repositories>

Then you can add dependencies to the individual artifacts from this repository in your pom.xml:
(Check the actual artifact versions, here I only give an example entry using versions 1.2)

* **[JmxWrapper] (https://github.com/uklimaschewski/JMXWrapper)**
    JMXWrapper is a wrapper that allows creation of dynamic JMX MBeans through annotations

        <dependencies>
            <dependency>
                <groupId>com.udojava</groupId>
                <artifactId>jmxwrapper</artifactId>
                <version>1.2</version>
            </dependency>
        </dependencies>
 