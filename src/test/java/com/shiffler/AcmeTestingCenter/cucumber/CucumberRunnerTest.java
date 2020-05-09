package com.shiffler.AcmeTestingCenter.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.test.context.support.WithMockUser;

@RunWith(Cucumber.class)
@PropertySource("classpath:cucumber-test.properties")
@CucumberOptions(features = {

        "src/test/resources/features/retrievemedicaltestbyid.feature",
        "src/test/resources/features/retrievemedicaltestlist.feature"},
        glue = "com.shiffler.AcmeTestingCenter.cucumber.steps",
        plugin = {"pretty", "html:target/cucumber/"})

public class CucumberRunnerTest {
}
