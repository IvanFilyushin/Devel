package ru.stqa.pft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.stqa.pft.mantis.models.Issue;
import ru.stqa.pft.mantis.models.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by DELL on 09.09.16.
 */
public class SoapHelper {

    private ApplicationManager app;

    public SoapHelper(ApplicationManager app) {

        this.app = app;
    }

    public Set<Project> getProjects() throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        ProjectData[] projects = mc.mc_projects_get_user_accessible("administrator", "root");

        return Arrays.asList(projects).stream()
                .map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName())).collect(Collectors.toSet());


    }

    private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
        return new MantisConnectLocator().
                    getMantisConnectPort(new URL("http://localhost/mantisbt-1.3.0/api/soap/mantisconnect.php"));
    }

    public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        IssueData issueData = new IssueData();
        String[] categories = mc.mc_project_get_categories("administrator", "root", BigInteger.valueOf(issue.getProject().getId()));
        issueData.setSummary(issue.getSummary());
        issueData.setCategory(categories[0]);
        issueData.setDescription(issue.getDescription());
        issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()),issue.getProject().getName()));
        BigInteger issueId = mc.mc_issue_add("administrator", "root", issueData);
        IssueData createdIssueData = mc.mc_issue_get("administrator", "root", issueId);
        return new Issue().withId(createdIssueData.getId().intValue()).withDescription(createdIssueData.getDescription())
                .withSummary(createdIssueData.getSummary()).withProject(new Project().
                        withId(createdIssueData.getId().intValue()).withName(createdIssueData.getProject().getName()));
    }
}
