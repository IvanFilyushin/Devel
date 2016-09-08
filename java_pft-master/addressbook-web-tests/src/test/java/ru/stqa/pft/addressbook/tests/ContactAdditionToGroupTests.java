package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Iterator;

/**
 * Created by Ольга on 28.08.2016.
 */
public class ContactAdditionToGroupTests extends TestBase {

  @Test
  public void testContactAdditionToGroup() {
//    Contacts before = app.db().contacts();
//    ContactData addedContact = before.iterator().next();
//    app.contact().returnToHomePage();
//    app.contact().selectFromAllGroups();
//    app.contact().addToGroup(addedContact);
//    Contacts uiContacts = app.contact().all();
//   Contacts dbContacts =

//    app.contact().returnToHomePage();
//    GroupData selectedGroup = app.contact().selectTargetGroup();
//    System.out.println(selectedGroup);
//    ContactData addedContact = app.contact().selectRandomContact();

    Contacts contacts = app.db().contacts();
    Groups groups = app.db().groups();

    if (groups.size() == 0) {
      System.out.println("Групп нет. Создаём...");
      app.gotoGroupPage();
      app.group().create(new GroupData().withName("test"));
      app.contact().returnToHomePage();
      groups = app.db().groups();
    }
    ContactData addedContact = contacts.iterator().next();
    Iterator<GroupData> iterator = groups.iterator();
    GroupData targetGroup = iterator.next();
    int i = 0;
    while (i < groups.size()) {
      if (!app.db().isThereAContactInGroupDb(addedContact, targetGroup)) {
        i = groups.size() + 1;
      } else {
        if (iterator.hasNext()) {
          targetGroup = iterator.next();
        }
        i++;
      }
    }
    if (i == groups.size()) {
      System.out.println("Контакт есть во всех группах. Создаём новую...");
      app.gotoGroupPage();
      targetGroup = new GroupData().withName("test" + i);
      app.group().create(targetGroup);
      app.contact().returnToHomePage();
    }
    app.contact().addToGroup(addedContact, targetGroup);
    Assert.assertEquals(app.contact().isThereAContactInGroupUI(addedContact), true);
    Assert.assertEquals(app.db().isThereAContactInGroupDb(addedContact, targetGroup), true);
  }


}
