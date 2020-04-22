package com.shiffler.AcmeTestingCenter.cucumber.web.controller;

import com.shiffler.AcmeTestingCenter.AcmeTestingCenterApplication;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = {

        "src/test/resources/features/retrievemedicaltestbyid.feature",
        "src/test/resources/features/retrievemedicaltestlist.feature"},
        glue = "com.shiffler.AcmeTestingCenter.cucumber.web.controller.steps",
        plugin = {"pretty", "html:target/cucumber/"})
//@SpringBootTest
//@ContextConfiguration(classes = AcmeTestingCenterApplication.class)
public class RetrieveMedicalTestByIdCukeCfgTest {
}
