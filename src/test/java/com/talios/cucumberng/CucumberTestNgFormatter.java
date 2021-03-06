package com.talios.cucumberng;

import gherkin.formatter.Formatter;
import gherkin.formatter.NiceAppendable;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import org.testng.ITestResult;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: amrk
* Date: 28/02/12
* Time: 10:27 PM
* To change this template use File | Settings | File Templates.
*/
public class CucumberTestNgFormatter implements Formatter, Reporter {

    private final NiceAppendable out;

    private LinkedList<Step> steps = new LinkedList<Step>();

    public CucumberTestNgFormatter(Appendable appendable) {
        out = new NiceAppendable(appendable);
    }

    public void setCurrentTestNgResult(ITestResult tr) {
        org.testng.Reporter.setCurrentTestResult(tr);
    }

    @Override
    public void uri(String uri) {
        org.testng.Reporter.log("<div class=\"featureFile\">Feature File: " + uri + "</div>");
    }

    @Override
    public void feature(Feature feature) {
        org.testng.Reporter.log("<div class=\"feature\">Feature: " + feature.getName() + "</div>");
    }

    @Override
    public void background(Background background) {
    }

    @Override
    public void scenario(Scenario scenario) {
        org.testng.Reporter.log("<div class=\"scenario\">Scenario: " + scenario.getName() + "</div>");
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        org.testng.Reporter.log("<div class=\"scenario\">Scenario Outline: " + scenarioOutline.getName() + "</div>");
    }

    @Override
    public void examples(Examples examples) {
    }

    @Override
    public void step(Step step) {
        steps.add(step); //CHEATING…cache the steps
    }

    @Override
    public void eof() {
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, int line) {
    }

    @Override
    public void done() {
        steps.clear();
    }

    @Override
    public void close() {
        out.close();
    }

    @Override
    public void result(Result result) {
        //addTestNgAttribute(RESULTS, result);
        String timing = result.getDuration() != null ?
                " : " + (Math.round(result.getDuration() / 1000000000f * 100f) / 100f) + "s" : "";
        Step step;
        if (steps.isEmpty()) {
            step = new Step(null, "MISMATCH BETWEEN STEPS AND RESULTS", "", 0, null, null);
        } else {
            step = steps.pop();
        }
        org.testng.Reporter.log("<div class=\"result\">" + step.getKeyword() + " " + step.getName() +
                " (" + result.getStatus() + timing + ")</div>");

        if (Result.FAILED.equals(result)) {
            ITestResult tr = org.testng.Reporter.getCurrentTestResult();
            tr.setThrowable(result.getError());
            tr.setStatus(ITestResult.FAILURE);
        }
        if (Result.SKIPPED.equals(result)) {
            ITestResult tr = org.testng.Reporter.getCurrentTestResult();
            tr.setThrowable(result.getError());
            tr.setStatus(ITestResult.SKIP);
        }
        if (Result.UNDEFINED.equals(result)) {
            ITestResult tr = org.testng.Reporter.getCurrentTestResult();
            tr.setThrowable(result.getError());
            tr.setStatus(ITestResult.FAILURE);
        }
    }

    @Override
    public void embedding(String s, InputStream inputStream) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void write(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void match(Match match) {
    }

}
