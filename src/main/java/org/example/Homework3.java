package org.example;

import java.io.*;
import java.util.*;

public class Homework3 {

    public static List<String[]> readFile(File file) {
        List<String[]> lst = new ArrayList<>();
        try (FileReader fr = new FileReader(file);
             BufferedReader bf = new BufferedReader(fr)) {
            String line;
            while ((line = bf.readLine()) != null) {
                lst.add(line.split(" "));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + file.getName());
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return lst;
    }

    public static void writeFile(List<String[]> lst, File file, User user) {
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bf = new BufferedWriter(fw)) {
            for (String[] item : lst) {
                bf.write(Arrays.toString(item)
                        .replace('[', ' ')
                        .replace(']', ' ')
                        .replaceAll(", ", " ")
                        .trim());
                bf.newLine();
            }
            bf.write(user.toString());
            bf.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static boolean isNumeric(String val) {
        try {
            Long.parseLong(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void enterFIO(User user, int count) {
        System.out.println("Введите ФИО " + (count + 1) + "-го пользователя (через пробел):");
        try {
            Scanner userInfoScan = new Scanner(System.in);
            String fio = userInfoScan.nextLine();
            String[] userFio = fio.split(" ");
            if (userFio.length < 3) {
                throw new RuntimeException("Введено недостаточно данных");
            }
            user.setSurname(userFio[0]);
            user.setName(userFio[1]);
            user.setPatronymic(userFio[2]);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + ", Повторите ввод");
            enterFIO(user, count);
        }
    }
    public static void enterBirthDate(User user, int count) {
        System.out.println("Введите дату рождения " + (count + 1) + "-го пользователя (в формате dd.mm.yyyy):");
        try {
            Scanner userInfoScan = new Scanner(System.in);
            String birthFullDate = userInfoScan.nextLine();
            String[] userBirthdate = birthFullDate.split("\\.");
            if (userBirthdate.length != 3 || !isNumeric(userBirthdate[0]) || !isNumeric(userBirthdate[1]) || !isNumeric(userBirthdate[2])) {
                throw new RuntimeException("Некорректный формат даты");
            }
            int day = Integer.parseInt(userBirthdate[0]);
            int month = Integer.parseInt(userBirthdate[1]);
            int year = Integer.parseInt(userBirthdate[2]);
            if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2023) {
                throw new RuntimeException("Выход за пределы возможных значений в дате");
            }
            user.setBirthDate(userBirthdate[0]);
            user.setBirthMonth(userBirthdate[1]);
            user.setBirthYear(userBirthdate[2]);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + ", Повторите ввод");
            enterBirthDate(user, count);
        }
    }

    public static void enterPhoneNumber(User user, int count) {
        System.out.println("Введите номер телефона " + (count + 1) + "-го пользователя:");
        try {
            Scanner userInfoScan = new Scanner(System.in);
            Long phoneNumber = userInfoScan.nextLong();
            if (phoneNumber <= 0) {
                throw new InputMismatchException("Номер телефона должен быть положительным числом");
            }
            user.setPhoneNumber(phoneNumber);
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage() + ", Повторите ввод");
            enterPhoneNumber(user, count);
        }
    }

    public static void enterGender(User user, int count) {
        System.out.println("Введите пол " + (count + 1) + "-го пользователя (Ж или М):");
        try {
            Scanner userInfoScan = new Scanner(System.in);
            String gender = userInfoScan.nextLine();
            if (!gender.equals("Ж") && !gender.equals("М")) {
                throw new RuntimeException("Пол указан некорректно");
            }
            user.setGender(gender);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + ", Повторите ввод");
            enterGender(user, count);
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String num;
        do {
            System.out.println("Введите количество пользователей (в числовом формате):");
            num = scan.nextLine();
        } while (!isNumeric(num));

        int numberOfUsers = Integer.parseInt(num);
        List<User> usersList = new ArrayList<>();

        int count = 0;
        while (count < numberOfUsers) {
            Integer[] requestVariants = {1, 2, 3, 4};
            List<Integer> requestOrder = Arrays.asList(requestVariants);
            Collections.shuffle(requestOrder);

            User user = new User();

            for (int variant : requestOrder) {
                switch (variant) {
                    case 1:
                        enterFIO(user, count);
                        break;
                    case 2:
                        enterBirthDate(user, count);
                        break;
                    case 3:
                        enterPhoneNumber(user, count);
                        break;
                    default:
                        enterGender(user, count);
                        break;
                }
            }

            usersList.add(user);
            count++;
        }

        for (User nextUser : usersList) {
            File file = new File(nextUser.getSurname());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.err.println("Ошибка при создании файла: " + e.getMessage());
                }
            }
            List<String[]> fileContent = readFile(file);
            writeFile(fileContent, file, nextUser);
        }
    }
}