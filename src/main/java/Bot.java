import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

public class Bot extends TelegramLongPollingBot {
    private final Logger botLogger = LoggerFactory.getLogger(Bot.class);
    News_8a news8a = new News_8a();
    News_climberNews newsClimberNews = new News_climberNews();
    News_GymClimber newsGymClimber = new News_GymClimber();


    @Override
    public String getBotUsername() {
        return "BotUsername";
    }

    @Override
    public String getBotToken() {
        return "BotToken";
    }

    @Override
    public void onUpdateReceived(Update update) {

        botLogger.debug("New User received. User ID: {}", update.getUpdateId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        String chatId = String.valueOf(update.getMessage().getChatId());

        try {
            sendMessage.setText(userInput(chatId, update.getMessage().getText()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        sendMessage.enableMarkdown(true);

        try{
            execute(sendMessage);
        }catch (TelegramApiException e) {
            botLogger.debug("Exception: {}", e.toString());
        }
    }


    public String userInput(String chatId, String userMessage) throws TelegramApiException {
        return switch (userMessage) {
            case "/start" -> "Hi!";
            case "/help" -> "Have a question? Google it";
            case "/info" -> "it is an informational bot for climbers. In order to find out the latest news, select the button with site`s name and get it";
            case "8a.nu", "climbernews.com", "gymclimber.com" -> getNews(chatId, userMessage);
            default -> "Use one of the valid commands: /start, /help or /info";
        };

    }

    private String getNews(String chatId, String userMessage) {
        KeyBord keyBord = new KeyBord();
        keyBord.ChoiceChannel(chatId);

        if(userMessage.equals("8a.nu")){
            news8a.newsElements(chatId);
        }else if (userMessage.equals("climbernews.com")){
            newsClimberNews.newsElements(chatId);
        }else {
            newsGymClimber.newsElements(chatId);
        }

        Date date = new Date();
        return date.toString();
    }

}