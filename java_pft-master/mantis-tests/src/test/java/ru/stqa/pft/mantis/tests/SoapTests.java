package ru.stqa.pft.mantis.tests;


import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.models.Issue;
import ru.stqa.pft.mantis.models.Project;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Created by DELL on 08.09.16.
 */
public class SoapTests extends TestBase{

    @Test
    public void testGetProjects() throws MalformedURLException, ServiceException, RemoteException {
        Set<Project> projects = app.soap().getProjects();
        for (Project project:projects) {
            System.out.println(project.getName());
        }
    }

    @Test
    public void testCreateProject() throws RemoteException, ServiceException, MalformedURLException {
        Set<Project> projects = app.soap().getProjects();
        Issue issue = new Issue().withSummary("Test issue")
                .withDescription("Test issue description").withProject(projects.iterator().next());
        Issue created = app.soap().addIssue(issue);
        assertEquals(issue.getSummary(),created.getSummary());
    }
}
