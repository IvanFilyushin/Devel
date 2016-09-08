package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Ольга on 15.08.2016.
 */
public class ContactPhoneEmailAddressTests extends TestBase {

  @Test
  public void testContactPhonesEmailsAddress() {

    app.contact().returnToHomePage();
    ContactData contact = app.contact().all().iterator().next();
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

    // проверяем поле адреса
    assertThat(contact.getAddress(),equalTo(contactInfoFromEditForm.getAddress()));
    // проверяем поля телефонов
    assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
    // проверяем поля е-мейлов
    assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactInfoFromEditForm)));

  }

  private String mergeEmails(ContactData contact) {
    return Arrays.asList(contact.getEmail1(), contact.getEmail2(), contact.getEmail3())
            .stream().filter((s) -> ! s.equals("")).collect(Collectors.joining("\n"));

  }

  private String mergePhones(ContactData contact) {
    return Arrays.asList(contact.getPhone1(), contact.getPhone2(), contact.getPhone3())
            .stream().filter((s) -> ! s.equals("")).map(ContactPhoneEmailAddressTests::clened)
            .collect(Collectors.joining("\n"));
  }

  public static String clened(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }

}
