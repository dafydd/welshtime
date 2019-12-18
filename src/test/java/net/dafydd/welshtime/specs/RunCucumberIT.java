package net.dafydd.welshtime.specs;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "json:target/cucumberreports.json", "html:target/htmlreports" },
        glue = "net/dafydd/welshtime/specs")
public class RunCucumberIT {
}
