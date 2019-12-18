package net.dafydd.welshtime.specs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ExampleBasedSpec {

    @Given("the time {string}")
    public void the_time(String string) {
    }

    @When("translated")
    public void translated() {
    }

    @Then("it maps to time {string}")
    public void it_maps_to_time(String string) {
    }


}
