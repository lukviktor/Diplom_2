package randomdata;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomEmailUser {

    public int randomNumberSymbolsEmail() {
        int min = 2;
        int max = 15;
        int diff = max - min;
        Random random = new Random();
        return random.nextInt(diff + 1) + min;
    }

    public String nameEmailUser() {
        return RandomStringUtils.randomAlphabetic(randomNumberSymbolsEmail());
    }

    public String randomEmailUser() { // отправляем в email
        String userNameEmail = nameEmailUser();

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
        // можно создать случайную комбинацию имя + @Сервис:
        // строки отформатированы, чтобы название компании и сервиса выводились через @
        return String.format("%s@%s", userNameEmail, services[randomService]);
    }

}
