
package core;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

//import static junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
//import static junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("core")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "core")
public class RunCucumberTest {
}




/*
package core;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber"})
public class RunCucumberTest {

}

 */



