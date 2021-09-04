import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main (String[] args) throws TelegramApiException {
        final Logger mainLogger = LoggerFactory.getLogger(Main.class);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        Bot bot = new Bot();

        try {
            telegramBotsApi.registerBot(bot);
            mainLogger.info("Bot registered");
        } catch (TelegramApiRequestException e) {
            mainLogger.error("Error: {}", e.toString());
        }
    }
}
