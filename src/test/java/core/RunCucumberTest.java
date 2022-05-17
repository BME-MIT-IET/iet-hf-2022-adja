/*
package core;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

//import static core.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
//import static core.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("core")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "core")
public class RunCucumberTest {
    int i= 0;
    //System.out.printLine("hapci");
}

 */



package core;

        import io.cucumber.junit.Cucumber;
        import io.cucumber.junit.CucumberOptions;
        import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions()
public class RunCucumberTest {
}



