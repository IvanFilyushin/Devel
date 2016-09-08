package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ольга on 26.07.2016.
 */
public class ContactHelper extends HelperBase {


  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void submitContactCreation() {
    click(By.name("submit"));
  }

  public void returnToHomePage() {

    if (isElementPresent(By.id("maintable")))
      return;
    click(By.linkText("home"));
  }

  public void fillForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstname());
    type(By.name("lastname"), contactData.getLastname());
    type(By.name("nickname"), contactData.getNickname());
    type(By.name("title"), contactData.getTitle());
    type(By.name("company"), contactData.getCompany());
    type(By.name("address"), contactData.getAddress());
    type(By.name("home"), contactData.getPhone1());
    type(By.name("mobile"), contactData.getPhone2());
    type(By.name("work"), contactData.getPhone3());
//    attach(By.name("photo"), contactData.getPhoto());

    if (creation)
      if (contactData.getGroups().size() > 0) {
        Assert.assertTrue(contactData.getGroups().size() == 1);
        new Select(wd.findElement(By.name("new_group")))
                .selectByVisibleText(contactData.getGroups().iterator().next().getName());
      } else {
        Assert.assertFalse(isElementPresent(By.name("new_group")));
      }
  }

  public void selectContact(int index) {

    wd.findElements(By.name("selected[]")).get(index).click();

  }

  public void selectContactById(int id) {

    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();

  }

  public void delete() {
    click(By.xpath("//div/div[4]/form[2]/div[2]/input"));
    wd.switchTo().alert().accept();
  }

  public void submitModification() {
    click(By.name("update"));
  }

  public void gotoModification(int id) {

//    List<WebElement> elements = wd.findElements(By.cssSelector("tr[name=entry]"));
//    for (WebElement element : elements) {
//      int idd = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
//      if (idd == id) {
//        element.findElement(By.xpath("./td[8]/a/img")).click();
//        return;
//      }
//    }

    WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='%s']", id)));
    WebElement row = checkbox.findElement(By.xpath("./../.."));
    List<WebElement> cells = row.findElements(By.tagName("td"));
    cells.get(7).findElement(By.tagName("a")).click();

//  wd.findElement(By.xpath(String.format("input[value='%s']/../../td[8]/a",id))).click();
//  wd.findElement(By.xpath(String.format("tr[.//input[value='%s']]/td[8]/a",id))).click();
//  wd.findElement(By.cssSelector(String.format("a[href='edit?id=%s']",id))).click();


  }

  public void allContacts() {
    click(By.id("MassCB"));
  }

  public void deleteFromModification() {
    click(By.xpath("//div[@id='content']/form[2]/input[2]"));
  }

  public void create(ContactData contact) {
    fillForm(contact, true);
    submitContactCreation();
    contactCache = null;
    returnToHomePage();
  }


  public void deleteContact(int index) {
    selectContact(index);
    delete();
    returnToHomePage();
  }

  public void deleteContact(ContactData contact) {
    selectContactById(contact.getId());
    delete();
    contactCache = null;
    returnToHomePage();
  }

  public void deleteContacts() {
    delete();
    contactCache = null;
  }

  public void modify(ContactData contact) {
    gotoModification(contact.getId());
    fillForm(contact, false);
    submitModification();
    contactCache = null;
    returnToHomePage();
  }


  public void deleteFromEdit(ContactData deletedContact) {
    gotoModification(deletedContact.getId());
    deleteFromModification();
    contactCache = null;
    returnToHomePage();
  }

  public void gotoAddPage() {
    if (isElementPresent(By.tagName("h1"))
            && wd.findElement(By.tagName("h1")).getText().equals("Edit / add address book entry")) {
      return;
    }
    click(By.linkText("add new"));
  }

  public boolean isThereAContact() {

    return isElementPresent(By.name("selected[]"));
  }

  public int count() {

    return wd.findElements(By.name("selected[]")).size();
  }

  public List<ContactData> list() {
    List<ContactData> contacts = new ArrayList<ContactData>();
    List<WebElement> elements = wd.findElements(By.name("entry"));
    for (WebElement element : elements) {
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      String lastname = element.findElement(By.xpath("./td[2]")).getText();
      String firstname = element.findElement(By.xpath("./td[3]")).getText();
      ContactData contact = new ContactData().withId(id).withFirstName(firstname).withLastName(lastname);
      contacts.add(contact);
    }
    return contacts;
  }

  private Contacts contactCache = null;

  public Contacts all() {
    if (contactCache != null) {
      return new Contacts(contactCache);
    }
    contactCache = new Contacts();
    List<WebElement> rows = wd.findElements(By.name("entry"));
    for (WebElement row : rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value"));
      String lastname = cells.get(1).getText();
      String firstname = cells.get(2).getText();
      String address = cells.get(3).getText();
      String allEmails = cells.get(4).getText();
      String allPhones = cells.get(5).getText();
      ContactData contact = new ContactData().withId(id).withFirstName(firstname).withLastName(lastname)
              .withAllPhones(allPhones).withAddress(address).withAllEmails(allEmails);
      contactCache.add(contact);
    }
    return new Contacts(contactCache);
  }

  public ContactData infoFromEditForm(ContactData contact) {

    gotoModification(contact.getId());
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String address = wd.findElement(By.name("address")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    String email1 = wd.findElement(By.name("email")).getAttribute("value");
    String email2 = wd.findElement(By.name("email2")).getAttribute("value");
    String email3 = wd.findElement(By.name("email3")).getAttribute("value");
    wd.navigate().back();
    return new ContactData().withFirstName(firstname).withLastName(lastname).withPhone1(home).withPhone2(mobile)
            .withPhone3(work).withEmail1(email1).withEmail2(email2).withEmail3(email3).withAddress(address);

  }

  public ContactData infoFromInfoPage(ContactData contact) {
    gotoContactInfoPage(contact.getId());
    String info = wd.findElement(By.id("content")).getText();
    wd.navigate().back();
    return new ContactData().withInfo(info);
  }

  private void gotoContactInfoPage(int id) {

    wd.findElement(By.cssSelector(String.format("a[href='view.php?id=%s']", id))).click();
  }

  public void addToGroup(ContactData contact, GroupData group) {
    selectContactById(contact.getId());
    if (group.getId() != Integer.MAX_VALUE){
      selectTargetGroup(group.getId());
    } else {
      selectTargetGroup(group.getName());
    }
      submitAddToGroup();
    gotoGroupPage();
  }

  private void gotoGroupPage() {
//    click(By.xpath("//div/div[4]/div/i/a"));
    wd.findElement(By.partialLinkText("group page")).click();
  }

  private void submitAddToGroup() {
    click(By.name("add"));
  }

  public void selectTargetGroup(int id) {
    WebElement g = wd.findElement(By.name("to_group"));
    g.findElement(By.cssSelector(String.format("option[value='%s']", id))).click();

/*    List<WebElement> options = listToGroup.findElements(By.tagName("option"));
    int i = (int) (Math.random() * ((options.size())));
    int id = Integer.parseInt(options.get(i).getAttribute("value"));
    String s1 = options.get(i).getText();
    options.get(i).click();*/

  }
  public void selectTargetGroup(String name) {
    WebElement g = wd.findElement(By.name("to_group"));
//  g.findElement(By.xpath(String.format("//*[.='%s']",name))).click();
//    g.findElement(By.xpath(String.format("/*[.='%s']",name))).click();
    List<WebElement> ss = g.findElements(By.tagName("option"));
    for (WebElement s:ss){
      String sss=s.getText();
      if(sss.equals(name)){
        s.click();
        return;
      }
    }
    System.out.println("Не найдено");
  }
  public void selectFromAllGroups() {
    WebElement g = wd.findElement(By.name("group"));
    g.findElement(By.cssSelector("option[value='']")).click();
  }

  public ContactData selectRandomContact() {
    List<WebElement> contacts = wd.findElements(By.name("selected[]"));
    int i = (int) (Math.random() * ((contacts.size())));
    contacts.get(i).click();
    return new ContactData();
  }

  public boolean isThereAContactInGroupUI(ContactData contact) {
    int id = contact.getId();
 //   return isElementPresent(By.cssSelector(String.format("selected [][value='%s']", id)));
//    WebElement d = wd.findElement(By.xpath(String.format("input[value='%s']",id)));
    return isElementPresent(By.cssSelector(String.format("input[value='%s']",id)));
  }


  public void removeContactFromGroup(GroupData group, ContactData contact) {
    selectGroupToModify(group.getId());
    selectContactById(contact.getId());
    submitDeletionFromGroup();
    gotoGroupPage();

  }

  private void submitDeletionFromGroup() {
    wd.findElement(By.name("remove")).click();
  }

  private void selectGroupToModify(int id) {
    WebElement g = wd.findElement(By.name("group"));
    g.findElement(By.cssSelector(String.format("option[value='%s']",id))).click();
  }


}
