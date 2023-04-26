package randomdata;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Random;

public class RandomEmailUser {

    public int randomNumberSymbolsEmail() {
        int min = 2;
        int max = 15;
        int diff = max - min;
        Random random = new Random();
        int numberSymbolsEmail = random.nextInt(diff + 1) + min;
        return numberSymbolsEmail;
    }

    public String nameEmailUser() {
        String emailUser = RandomStringUtils.randomAlphabetic(randomNumberSymbolsEmail());
        return emailUser;
    }

    public String randomEmailUser() { // отправляем в email
        String userNameEmail = new String(nameEmailUser());

        // массив сервисов
        String[] services = new String[]{"hotmail.com", "live.com", "msn.com", "yahoo.com", "ymail.com",
                "rocketmail.com", "yahoomail.com", "rocketmail.com", "yahoo-inc.com", "gmail.com",
                "googlemail.com", "aol.com", "mail.ru", "yandex.ru", "rambler.ru", "qip.ru",
                "box.az", "byke.com", "chez.com", "email.ru", "gmx.net", "mail.net", "goldmail.ru",
                "inet.ua", "loveemail.com", "bigmailbox.com", "bigmir.net", "mail.com", "mail.e1.ru",
                "mail.gala.net", "lycos.com", "tut.by", "yahoo.com", "netaddress.com", "newmail.net",
                "nicknames.com", "outlook.live.com", "outlook.com", "post.cz", "spam.lv",
                "techemail.com", "ua.fm", "webmail.aol.com", "meta.ua"};

        int randomService = new Random().nextInt(services.length); // выбор случайного сервиса из массива
        String readyEmail = String.format("%s@%s", userNameEmail, services[randomService]);
        // можно создать случайную комбинацию имя + @Сервис:
        // строки отформатированы, чтобы название компании и сервиса выводились через @
        return readyEmail;
    }
    @Test
    public void sdf(){
        System.out.println(randomEmailUser());
    }
}
