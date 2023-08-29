Требующийся функционал: Отправка-Получение сообщений 
несколькими пользователями параллельно, Логгирование сообщений 
(пользовательских и системных) в единый файл в формате "[Время, автор:] текст",
предоставление уникального ника, подключение пользователей к серверу через настроечный файл с указанием номера порта

Реализация сервера: Сервер (вынесен в отдельный класс MyServer для облегчения тестирования и вынесения логики из Main) 
реализован через ServerSocket с бесконечно повторяющимся вызовом метода 
.accept(), каждое завершение которого создает новый объект Runnable класса SingleThreadSocket и запускает его run в 
одном из потоков ThreadPool'a. Объект SingleThreadSocket - 
обработчик каждого нового подключения (user'a), Обработка и выведение сообщений на экран пользователей разделены на два 
потока (см. комментарии в классе). При получении команды "/exit" клиентский Socket закрывается, поток, реализующий run экземпляра 
SingleThreadSocket завершается и освобождается для нового подключения. В ходе обработки пользовательских сообщений они 
записываются в файл - логгер, после чего читаются из него и воспроизводятся на экранах пользователей, подключенных к серверу.
Фактически в два разных потока разнесены потокобезопасная запись и чтение из файла-логгера. 

*Подробные комментарии находятся внутри классов

Реализация клиента: Клиент по аналогии с его "ответной частью" в виде SingleThreadSocket'a реализован через два потока, 
один из кототрых отправляет сообщения для обработки на сервер, а другой воспроизводит сообщения клиента, 
других участников и серверные на экране пользователя. Прерывание общения через команду /exit. Пользователь не видит сообщений, 
написанных до его присоединения к чату и после его отключения (реализовано через метод skip класса SingleThreadSocket).