package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

/**
 * Created by Ольга on 28.07.2016.
 */
public class AllContactDeletionTests extends TestBase {
  @BeforeMethod
  public void ensurePreconditions() {
    app.contact().returnToHomePage();
    if (app.db().contacts().size() == 0) {
      app.contact().gotoAddPage();
      app.contact().create(new ContactData().withFirstName("name1").withLastName("name2")
              .withNickName("name3").withTitle("title").withCompany("company").withAddress("address")
              .withPhone1("phone1").withPhone2("phone2").withPhone3("phone3"));
    }
  }
  @Test
  public void testAllContactDeletionTest() {

    app.contact().allContacts();
    app.contact().deleteContacts();
    app.contact().returnToHomePage();
    Assert.assertEquals(app.contact().count(), 0);
  }

}
