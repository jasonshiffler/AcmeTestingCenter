package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManagementPageController {

    MedicalTestService medicalTestService;

    @Autowired
    ManagementPageController(MedicalTestService medicalTestService){
        this.medicalTestService = medicalTestService;
    }

    @GetMapping("/mgmt")
    public String mainPage(Model model){
        List<MedicalTest> medicalTestList = medicalTestService.getAllMedicalTestsAsList();
        model.addAttribute(medicalTestList);
        return "index";
    }

}
