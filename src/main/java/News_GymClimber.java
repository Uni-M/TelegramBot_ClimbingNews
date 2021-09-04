import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class News_GymClimber {
    private final Logger newsLogger = LoggerFactory.getLogger(News_GymClimber.class);
    private Document document;

    public News_GymClimber(){
        connect();
    }

    private void connect(){
        try{
            document = Jsoup.connect("https://www.gymclimber.com/news/").get();
        } catch (Exception e) {
            newsLogger.debug("Exception: {}", e.toString());
        }
    }

    public String title() {
        return document.title();
    }

    public void newsElements(String chatId){
        sendImageFromUrl(chatId);
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        try{
            Element header = document.select("h2 > a").first();
            Element content = document.select("hgroup > div > h3").first();

            sm.enableMarkdown(true);
            sm.setText("*" + header.text() + "*\n" + content.text() + "\n\n" + "News resource: " + title());

            try{
                new Bot().execute(sm);
            }catch (TelegramApiException e) {
                newsLogger.debug("Exception: {}", e.toString());
            }

        }catch (Exception e){
            newsLogger.debug("Exception: {}", e.toString());
        }
    }

    public void sendImageFromUrl(String chatId){
        SendPhoto sendPhoto = new SendPhoto();

        Element imag = document.select("picture > source").first();

        InputFile inputFile = new InputFile(imag.attr("srcset"));
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption("News resource: " + title());

        try {
            new Bot().execute(sendPhoto);
        } catch (TelegramApiException e) {
            newsLogger.debug("Exception: {}", e.toString());
        }
    }


}
