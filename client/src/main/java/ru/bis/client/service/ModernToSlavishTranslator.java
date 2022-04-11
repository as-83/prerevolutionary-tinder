package ru.bis.client.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ModernToSlavishTranslator {

    public static final String WORD_WITH_ER = "((\\p{L}*)([^ \nаеёиоуыэюяйь1234567890])\\b)";
    public static final String WORD_WITH_Ф = "\\b(\\p{L})*[Фф](\\p{L})*\\b";
    public static final String FITA_UPPER_LETTER = "\u0472";
    public static final String FITA_LOWER_LETTER = "\u0473";
    public static final String YAT_LOWER_LETTER_CODE = "\u0463";
    public static final String YAT_UPPER_LETTER_CODE = "\u0462";
    public static final String NAME_ENDING_WITH_VOWEL = "((\\p{L}*)([аеёиоуыэюяйь])\\b)";
    public static final String WORDS_WITH_I = "(и[аеёиоуыэюяй])";
    private static final String NAME_ROOTS_WITH_FITA = "Агафь, Анфим, Афанасi, Афин, Варфоломе, Голіаф," +
            " Евфимі, Марф, Матфе, Мефоді, Нафанаил, Парфенон, Пифагор," +
            " Руф, Саваоф, Тимофе, Эсфир, Іудиф, Фадде, Фекл," +
            " Фемид, Фемистокл, Феодор, Фёдор, Фед, Феодосі, Федосі," +
            " Феодосі, Феодот, Федот, Феофан, Феофил, Ферапонт, Фом, Фоминичн";
    private static final String WORDS_WITH_YAT_REGEX = "[еЕ][вгджзклмнрстч]";
    private Set<String> nameRootsSet;

    public ModernToSlavishTranslator() {
        this.nameRootsSet = initNameRootSet();
    }

    private String result;

    public String translate(String modern) {
        this.result = modern;
        addEr();
        changeToI();
        replaceWithFita();
        replaceWithYat();
        return result;
    }

    private void addEr() {
        Pattern pattern = Pattern.compile(WORD_WITH_ER);
        Matcher m = pattern.matcher(result);
        StringBuilder stringBuilder = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(stringBuilder, m.group(0) + "ъ");
        }
        m.appendTail(stringBuilder);
        result = stringBuilder.toString();

    }

    void changeToI() {
        Pattern pattern = Pattern.compile(WORDS_WITH_I);
        Matcher m = pattern.matcher(result);
        StringBuilder stringBuilder = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(stringBuilder, m.group(0).replace("и", "i"));
        }
        m.appendTail(stringBuilder);
        result = stringBuilder.toString();
    }

    private void replaceWithFita() {
        Set<String> nameRootsSet = initNameRootSet();
        Pattern pattern = Pattern.compile(WORD_WITH_Ф);

        Matcher m = pattern.matcher(result);
        StringBuilder stringBuilder = new StringBuilder();
        while (m.find()) {
            String gr = m.group(0);
            String key = gr;
            if (gr.matches(NAME_ENDING_WITH_VOWEL)) {
                key = gr.substring(0, gr.length() - 1);
            }
            if (nameRootsSet.contains(key)) {
                gr = gr.replace("Ф", FITA_UPPER_LETTER);
                m.appendReplacement(stringBuilder, gr.replace("ф", FITA_LOWER_LETTER));
            }
        }
        m.appendTail(stringBuilder);        // Добавить остаток строки
        result = stringBuilder.toString();
    }

    private Set<String> initNameRootSet() {
        return Arrays.stream(NAME_ROOTS_WITH_FITA.split(", "))
                .collect(Collectors.toSet());
    }

    private void replaceWithYat() {
        Pattern pattern = Pattern.compile(WORDS_WITH_YAT_REGEX);
        Matcher m = pattern.matcher(result);
        StringBuilder stringBuilder = new StringBuilder();
        while (m.find()) {
            String replaced = m.group(0).replace("е", YAT_LOWER_LETTER_CODE);
            replaced = replaced.replace("Е", YAT_UPPER_LETTER_CODE);
            m.appendReplacement(stringBuilder, replaced);
        }
        m.appendTail(stringBuilder);
        result = stringBuilder.toString();
    }

}
