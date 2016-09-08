package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import ru.stqa.pft.mantis.models.UserData;

/**
 * Created by DELL on 07.09.16.
 */
public class ChangePasswordHelper extends HelperBase {

    public ChangePasswordHelper(ApplicationManager app) {
        super(app);
    }

    public void resetPassword(UserData user)  {
        loginAsAdmin();
        click(By.className("manage-menu-link"));
        click(By.partialLinkText("Управление пользователями"));
        click(By.partialLinkText(user.getUserName()));
        click(By.cssSelector("input[value='Сбросить пароль']"));
        logoutAsAdmin();
    }

    private void loginAsAdmin() {
        wd.get(app.getProperty("web.baseUrl") + "/login_page.php");
        type(By.name("username"), "administrator");
        type(By.name("password"), "root");
        click(By.cssSelector("input[value='Войти']"));
    }

    private void logoutAsAdmin() {
        click(By.id("logout-link"));
    }

    public void enterNewPassword(String confirmationLink, String password) {
        wd.get(confirmationLink);
        type(By.id("password"), password);
        type(By.id("password-confirm"), password);
        click(By.cssSelector("input[value='Изменить учетную запись']"));
    }
}
