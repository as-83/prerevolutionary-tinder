ALTER SEQUENCE IF EXISTS hibernate_sequence START with 12;

UPDATE tinder.usr
SET search_preferences = 2;

INSERT INTO tinder.usr(user_id, name, description, sex, search_preferences, telegram_id)
values(11, 'Сулейман', 'Стажер Лиги Цмфровых Технологий', 0, 2, 736511947);

UPDATE tinder.usr
SET description = 'Образованная барышня ищет мужа миллионера,  непременно пожилого, во избежание неверности'
where user_id=10;