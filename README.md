## Multiple OSs Rule for the *maven-enforcer-plugin*

This Project implements a custom enforcer rule to bypass the classic
``` xml
    <requireOS>
      <family>unix</family>
    </requireOS>
```
section of the enforcer plugin which only allow to specify a **unique** OS family by allowing to define
a range of allowed Operating Systems (*windows,linux*).

To use the *multiple-os-enforcer-rule*, you have to:

* Clone the current Git project: ```git clone https://github.com/wisebrains/multiple-os-enforcer-rule```
* Move the new project directory: ```cd multiple-os-enforcer-rule```
* Build it locally so that the below artifact get copied into your local repository: ```mvn clean install -pl :multiple-os-enforcer-rule```

``` xml
    <dependency>
      <groupId>com.github.tmarwen.maven.plugins</groupId>
      <artifactId>multiple-os-enforcer-rule</artifactId>
      <version>1.0</version>
    </dependency>
```

* Use the new ```MultipleOSRule``` within your project build section as follows:

``` xml
    <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-enforcer-plugin</artifactId>
            <version>1.3.1</version>
            <dependencies>
              <dependency>
                <groupId>com.github.tmarwen.maven.plugins</groupId>
                <artifactId>multiple-os-enforcer-rule</artifactId>
                <version>1.0</version>
              </dependency>
            </dependencies>
            <executions>
              <execution>
                <id>enforce-os</id>
                <configuration>
                  <rules>
                    <multipleOsRule implementation="com.github.tmarwen.maven.plugins.MultipleOsRule">
                      <requiredOSs>linux,windows,mac-os</requiredOSs>
                    </multipleOsRule>
                  </rules>
                </configuration>
                <goals>
                  <goal>enforce</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
```
Check the ```<requireOSs>``` (with "s") section where you can define a list of the allowed Operating Systems where build should succeed.

A glance of how the build section may be can be found under [the *showcase* module](https://github.com/wisebrains/multiple-os-enforcer-rule/blob/master/showcase/pom.xml) which emphasize the new rule power.
Just run the module build:
```
    mvn clean package -pl :showcase
```
And check the build output where you should see something informative issue from the new EnforcerRule. In my an Ubuntu Box, it looks as follows:
```
[INFO] --- maven-enforcer-plugin:1.3.1:enforce (enforce-os) @ showcase ---
[INFO] The build is running in a "linux" box. Will check if this OS is within the allowed range.
[INFO] --> linux is in the allowed OS range: [linux, windows, mac-os]
```

***NB***: The project build may fail fi you are using another OS rather than those specified in the configured range (*linux,windows,mac-os*).

More on maven enforcer rules: http://maven.apache.org/enforcer/enforcer-api/writing-a-custom-rule.html
