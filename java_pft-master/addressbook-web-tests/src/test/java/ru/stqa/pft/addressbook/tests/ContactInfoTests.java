package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Ольга on 15.08.2016.
 */
public class ContactInfoTests extends TestBase {

  @Test
  public void testContactInfo() {
    app.contact().returnToHomePage();
    ContactData contact = app.contact().all().iterator().next();
    String sInfo = app.contact().infoFromInfoPage(contact).getInfo().replace("\n", "").replace(" ", "")
            .replaceAll("Memberof:\\w*", "").replaceAll("\\(www[.\\w*]{1,}\\)", "");
    String sEdit = mergeInfo(app.contact().infoFromEditForm(contact)).replace("\n", "").replace(" ", "");
    assertThat(sInfo, equalTo(sEdit));
  }

  private String mergeInfo(ContactData contact) {
    String s = contact.getFirstname() + contact.getLastname() + contact.getAddress();
    if (!contact.getPhone1().equals("")) {
      s = s + "H:" + contact.getPhone1();
    }
    if (!contact.getPhone2().equals("")) {
      s = s + "M:" + contact.getPhone2();
    }
    if (!contact.getPhone3().equals("")) {
      s = s + "W:" + contact.getPhone3();
    }
    s = s + contact.getEmail1() + contact.getEmail2() + contact.getEmail3();
    return s;
  }
}