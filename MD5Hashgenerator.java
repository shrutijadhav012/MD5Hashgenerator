import java.io.*;
import java.security.*;

public class MD5HashGenerator {
    public static String[] readJsonFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String firstName = null;
        String rollNumber = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("\"first_name\"")) {
                firstName = line.split(":")[1].replace("\"", "").replace(",", "").trim().toLowerCase();
            }
            if (line.startsWith("\"roll_number\"")) {
                rollNumber = line.split(":")[1].replace("\"", "").replace(",", "").trim().toLowerCase();
            }
        }
        reader.close();

        if (firstName == null || rollNumber == null) {
            throw new IllegalArgumentException("Missing 'first_name' or 'roll_number' in JSON file.");
        }

        return new String[]{firstName, rollNumber};
    }

    public static String generateMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void saveToFile(String filePath, String output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(output);
        writer.close();
    }

    public static void main(String[] args) {
        String inputFile = "input.json";
        String outputFile = "output.txt";

        try {
            String[] values = readJsonFile(inputFile);
            String firstName = values[0];
            String rollNumber = values[1];
            String concatenatedString = firstName + rollNumber;
            String md5Hash = generateMD5(concatenatedString);
            saveToFile(outputFile, md5Hash);
            System.out.println("MD5 Hash successfully saved to " + outputFile);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
