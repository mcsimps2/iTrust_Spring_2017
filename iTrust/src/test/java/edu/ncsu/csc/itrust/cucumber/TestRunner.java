package edu.ncsu.csc.itrust.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/edu/ncsu/csc/itrust/cucumber/ChildbirthVisit.feature")
public class TestRunner {
	
}
