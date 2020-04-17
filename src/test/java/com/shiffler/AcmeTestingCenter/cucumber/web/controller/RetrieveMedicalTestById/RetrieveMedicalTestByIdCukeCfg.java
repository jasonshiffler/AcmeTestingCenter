package com.shiffler.AcmeTestingCenter.cucumber.web.controller.RetrieveMedicalTestById;

import com.shiffler.AcmeTestingCenter.AcmeTestingCenterApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/retrievemedicaltestbyid.feature",
        plugin = {"pretty", "html:target/cucumber/"})
@SpringBootTest
@ContextConfiguration(classes = AcmeTestingCenterApplication.class)
public class RetrieveMedicalTestByIdCukeCfg {
}
