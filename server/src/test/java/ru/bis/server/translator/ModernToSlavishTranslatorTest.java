package ru.bis.server.translator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Java6Assertions.assertThat;


class ModernToSlavishTranslatorTest {
    private static ModernToSlavishTranslator translator;

    @BeforeAll
    private static void init() {
        translator = new ModernToSlavishTranslator();
    }

    @Test
    void addingErLetter() {
        String result = translator.translate("Офицер!\nТекст для перевода и в нужный формат!");
        PrintWriter printWriter = new PrintWriter(System.out,true, StandardCharsets.UTF_8);
        printWriter.println(result);
        assertThat(result).isEqualTo("Офицеръ!\n" +
                "Текстъ для перевода и въ нужный форматъ!");
    }

    @Test
    void changingToILetter() {
        String result = translator.translate("Линия. Другие. Приехал. Синий");
        assertThat(result).isEqualTo("Линiя. Другiе. Прiехалъ. Синiй");
    }

    @Test
    void changingToFita() {
        String result = translator.translate("Агафья, Анфим, Афанасий, Афина, Варфоломей, Голіаф," +
                " Евфимій, Марфа, Матфей, Мефодій, Нафанаил, Парфенон, Пифагор," +
                " Руф, Саваоф, Тимофей, Эсфир, Іудиф, Фаддей, Фекла," +
                " Фемида, Фемистокл, Феодор, Фёдор, Федя, Феодосій, Федосій," +
                " Феодосій, Феодот, Федот, Феофан, Феофил, Ферапонт, Фома, Фоминична");
        PrintWriter printWriter = new PrintWriter(System.out,true, StandardCharsets.UTF_8);
        printWriter.println(result);

    }
}
