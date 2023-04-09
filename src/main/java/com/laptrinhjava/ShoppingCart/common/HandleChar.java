package com.laptrinhjava.ShoppingCart.common;

public class HandleChar {
    // viết hoa chữ cái đầu
    public static String upperFirstString(String string){
        String firstLetter = string.substring(0, 1);

        // chuỗi remainingLetters chứa phần còn lại của name
        String remainingLetters = string.substring(1, string.length());

        // sử dụng phương thức toUpperCase() để chuyển đổi firstLetter thành chữ in hoa
        firstLetter = firstLetter.toUpperCase();

        // sau khi chuyển đổi thì gộp chúng lại
        string = firstLetter + remainingLetters;

        return string;
    }
}
