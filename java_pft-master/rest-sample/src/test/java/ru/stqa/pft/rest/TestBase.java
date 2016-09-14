package ru.stqa.pft.rest;

import com.jayway.restassured.RestAssured;
import org.testng.SkipException;

/**
 * Created by DELL on 10.09.16.
 */
public class TestBase {

    public boolean isIssue(int issueId) {
        return !RestAssured.get(String.format("http://demo.bugify.com/api/issues/%s.txt", issueId))
                .asString().contains("[code] => 404");
    }

    public boolean isIssueOpen(int issueId) {
        String s = RestAssured.get(String.format("http://demo.bugify.com/api/issues/%s.txt", issueId))
                .asString();
        return (s.contains("[state_name] => Open") || s.contains("[state_name] => Re-open"));
    }

    public void skipIfNotFixed(int issueId) {
        if (isIssueOpen(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }
}
