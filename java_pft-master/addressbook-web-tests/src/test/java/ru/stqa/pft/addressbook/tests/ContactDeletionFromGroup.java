package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Iterator;

import static ru.stqa.pft.addressbook.tests.TestBase.app;

/**
 * Created by Ольга on 29.08.2016.
 */
public class ContactDeletionFromGroup extends TestBase {

  private GroupData newGroup;

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) { // если нет групп
      newGroup = new GroupData().withName("test1");
      app.gotoGroupPage();
      app.group().create(newGroup); // создаём группу
      app.contact().returnToHomePage();
      app.contact().gotoAddPage();
      ContactData newContact = new ContactData().withFirstName("name1");
      app.contact().create(newContact); // создаём контакт
      newContact.inGroup(newGroup); // добавляем новый контакт в новую группу
    }
    if (app.db().contacts().size() == 0) { //если нет контактов
      app.contact().returnToHomePage();
      app.contact().gotoAddPage();
      ContactData newContact = new ContactData().withFirstName("name1");
      app.contact().create(newContact); //создаём контакт
      newContact.inGroup(app.db().groups().iterator().next()); //добавляем новый контакт в какую-нибудь группу
    }
  }

  @Test
  public void testContactDeletionFromGroup() {

    Groups groups = app.db().groups();

    GroupData targetGroup = findGroupWithContacts(groups);
    if (targetGroup == null) {
      System.out.println("Нет групп с контактами");
      return;
    }
    ContactData removedContact = findContactInGroup(targetGroup);
    app.contact().removeContactFromGroup(targetGroup, removedContact);
    Assert.assertEquals(app.contact().isThereAContactInGroupUI(removedContact), false);
    Assert.assertEquals(app.db().isThereAContactInGroupDb(removedContact, targetGroup), false);

  }

  private ContactData findContactInGroup(GroupData group) {
    return group.getContacts().iterator().next();
  }

  private GroupData findGroupWithContacts(Groups groups) {
    for (GroupData findedGroup : groups) {
      if (findedGroup.getContacts().size() != 0) {
        return findedGroup;
      }
    }
    return null;
  }

}
