package com.shiffler.AcmeTestingCenter.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {

        "src/test/resources/features/retrievemedicaltestbyid.feature",
        "src/test/resources/features/retrievemedicaltestlist.feature"},
        glue = "com.shiffler.AcmeTestingCenter.cucumber.steps",
        plugin = {"pretty", "html:target/cucumber/"})

public class CucumberRunnerTest {
}
