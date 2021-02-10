package telegram_bot;

import java.io.File;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyTelegramBot extends TelegramLongPollingCommandBot {

    public MyTelegramBot() {

        // Регистрируем команду "/solve"
        register(new SolveCommand());

        // Регистрируем действие по-умолчанию на остальные команды
        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = SendMessage
                    .builder()
                    .chatId(message.getChatId().toString())
                    .text("Команда " + message.getText() + " неизвестна этому боту.")
                    .build();
            try {
                absSender.execute(commandUnknownMessage);
                absSender.execute(getDefaultMessage(message.getChatId().toString()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        // Действие на обновления (сообщения), которые не являются командой
        if (update.hasMessage()) {
            Message message = update.getMessage();

            try {
                execute(getDefaultMessage(message.getChatId().toString()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static SendPhoto getDefaultMessage(String chatId) {
        return SendPhoto
                .builder()
                .chatId(chatId)
                .photo(new InputFile(new File("example.png").getAbsoluteFile()))
                .parseMode(ParseMode.MARKDOWN)
                .caption("Чтобы решить этот пример пришлите команду `/solve`, где "
                        + "первый аргумент это `x`, а второй это `a` или `b` "
                        + "(в зависимости от значения `x`).\n\n"
                        + "Например, если `x` = 6, то `b` = 8: `/solve 6 8`")
                .build();
    }

    @Override
    public String getBotUsername() {
        return "tetetetetststst_bot";
    }

    @Override
    public String getBotToken() {
        return "123456789:QWEQwEqweqweqwEqEQEQWasde5asdasd324";
    }

}
