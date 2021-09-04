import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class News_8a {
    private final Logger newsLogger = LoggerFactory.getLogger(News_8a.class);
    private Document document;

    public News_8a(){
        connect();
    }

    private void connect(){
        try {
            document = Jsoup.connect("https://www.8a.nu").get();
        } catch (IOException e) {
            newsLogger.debug("Exception: {}", e.toString());
        }
    }

    public String title (){
        return document.title();
    }

    public void newsElements(String chatId){
        sendImageFromUrl(chatId);
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        try {
            Element header = document.select("h4 > a").first();
            Element content = document.getElementsByClass("news-content").first();

            sm.enableMarkdown(true);
            sm.setText("*" + header.text() + "*\n" + content.text() + "\n\n" + "News resource: " + title());

            try{
                new Bot().execute(sm);
            } catch (TelegramApiException e) {
                newsLogger.debug("Exception: {}", e.toString());
            }

        } catch (Exception e) {
            newsLogger.debug("Exception: {}", e.toString());
        }
    }


    public void sendImageFromUrl(String chatId) {
        SendPhoto sendPhoto = new SendPhoto();

        Element imag = document.select("picture > img").first();

        InputFile inputFile = new InputFile(imag.attr("src"));
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption("News resource: " + title());

        try {
            new Bot().execute(sendPhoto);
        } catch (TelegramApiException e) {
            newsLogger.info("Exception: {}", e.toString()); //debug
        }
    }
}

