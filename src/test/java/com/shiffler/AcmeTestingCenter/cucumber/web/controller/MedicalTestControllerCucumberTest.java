package com.shiffler.AcmeTestingCenter.cucumber.web.controller;

import com.shiffler.AcmeTestingCenter.AcmeTestingCenterApplication;
import com.shiffler.AcmeTestingCenter.web.controller.MedicalTestController;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        plugin = {"pretty", "html:target/cucumber/"})
@SpringBootTest
@ContextConfiguration(classes = AcmeTestingCenterApplication.class)
public class MedicalTestControllerCucumberTest {
}
