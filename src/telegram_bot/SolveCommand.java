package telegram_bot;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SolveCommand extends BotCommand {

    public SolveCommand() {
        super("solve", "решить пример");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String chatId = chat.getId().toString();
        // Если у команды есть аргументы и их кол-во = 2,
        // то пытаемся спарсить их в числа и решить пример
        if (arguments != null && arguments.length == 2) {
            SendMessage answer = new SendMessage();
            answer.setChatId(chatId);
            answer.setParseMode(ParseMode.MARKDOWN);
            try {
                StringBuilder messageTextBuilder = new StringBuilder("`x = ");
                double x = Double.parseDouble(arguments[0]);
                messageTextBuilder.append(arguments[0]).append("`\n");
                double y = Double.parseDouble(arguments[1]);
                if (x <= 4) {
                    messageTextBuilder.append("`a = ").append(arguments[1]).append("`\n\n");
                } else {
                    messageTextBuilder.append("`b = ").append(arguments[1]).append("`\n\n");
                }
                double result = solve(x, y);
                messageTextBuilder.append("*y = ").append(result).append("*");
                answer.setText(messageTextBuilder.toString());
            } catch (Exception e) {
                answer.setText("*Ошибка в введенных аргументах.*");
            }
            // Отправляем результат
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            // Если аргументов нет или их кол-во != 2, то отправляем изображение
            // примера с инструкцией по использованию бота
        } else {
            SendPhoto answer = MyTelegramBot.getDefaultMessage(chatId);
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private double solve(double x, double y) {

        double result;

        if (x <= 4) {
            result = ((y * y) / (x * x) + 6 * x);
        } else {
            result = ((y * y) * ((4 + x) * (4 + x)));
        }

        return result;
    }
}
