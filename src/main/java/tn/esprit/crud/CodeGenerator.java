package tn.esprit.crud;

import java.util.Random;

public class CodeGenerator {
    public static String generateCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        int codeLength = 4;
        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }

        return code.toString();
    }

    public static void main(String[] args) {
        String generatedCode = generateCode();
        System.out.println("Generated Code: " + generatedCode);
    }
}
