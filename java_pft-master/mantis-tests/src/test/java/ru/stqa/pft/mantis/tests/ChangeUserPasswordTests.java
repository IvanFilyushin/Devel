package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.models.MailMessage;
import ru.stqa.pft.mantis.models.UserData;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Created by DELL on 07.09.16.
 */
public class ChangeUserPasswordTests extends TestBase {

    @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }

    @Test
    public void testChangeUserPassword() throws SQLException, IOException {

        // выбираем пользователя из БД
        UserData user = app.useDb().getUser();
        //  новый пароль = имя пользователя
        String newPassword = user.getUserName();
        // сбрасываем пароль пользователя
        app.changePassword().resetPassword(user);
        // получаем ссылку на страницу ввода нового пароля
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 100000);
        String confirmationLink = findConfirmationLink(mailMessages, user.getEmail());
        // меняем пароль
        app.changePassword().enterNewPassword(confirmationLink, newPassword);
        // проверяем вход пользователя с новым паролем
        assertTrue(app.newSession().login(user.getUserName(), newPassword));
    }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();

        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
