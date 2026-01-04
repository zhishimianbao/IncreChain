package com.hjx.cp.abe.MyPlan;

import com.hjx.cp.abe.attribute.Attribute;
import com.hjx.cp.abe.engine.CpAneEngine;
import com.hjx.cp.abe.parameter.*;
import com.hjx.cp.abe.parameter.PublicKey;
import com.hjx.cp.abe.structure.AccessTree;
import com.hjx.cp.abe.structure.AccessTreeBuildModel;
import com.hjx.cp.abe.text.CipherText;
import com.hjx.cp.abe.text.PlainText;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class Mytest {

    // 假设全局单例
    private static CpAneEngine cpAneEngine = new CpAneEngine();
    private static SystemKey systemKey;
    private static UserPrivateKey userPrivateKey;

    private static final List<String> allDepartments = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"
    );

    public static void main(String[] args) throws Exception {
        // 初始化系统密钥和用户私钥（用户属性包含所有部门）
        systemKey = SystemKey.build();

        // 这里先用所有部门作为用户属性，确保能成功解密
        List<Attribute> userAttributes = new ArrayList<>();
        for (String dep : allDepartments) {
            userAttributes.add(new Attribute(dep, systemKey.getPublicKey()));
        }
        userPrivateKey = cpAneEngine.keyGen(systemKey.getMasterPrivateKey(), userAttributes);

        runExperimentAndWriteExcel();
    }

    public static void runExperimentAndWriteExcel() throws Exception {
        String fileName = "Mytest.xlsx";
        Workbook workbook;
        Sheet sheet;

        File file = new File(fileName);
        if (file.exists()) {
            // 文件存在则读取
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            }
            // 获取已有Sheet，如果不存在则创建
            sheet = workbook.getSheet("ABE实验结果");
            if (sheet == null) {
                sheet = workbook.createSheet("ABE实验结果");
                createHeader(sheet);
            }
        } else {
            // 文件不存在，创建新的Workbook和Sheet
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("ABE实验结果");
            createHeader(sheet);
        }

        // 找到最后一行索引，追加写入
        int rowIndex = sheet.getLastRowNum() + 1;

        // 需要测试的部门数量
        int[] departmentCounts = {5};

        for (int depCount : departmentCounts) {
            List<String> departments = allDepartments.subList(0, depCount);

            // 1. 生成主密钥 kall
            String kall = generateMasterKey();

            // 2. 为每个部门生成标签
            Map<String, String> departmentTagMap = new HashMap<>();
            for (String d : departments) {
                departmentTagMap.put(d, generateTag());
            }

            // 3. 构造访问树（包含所有部门）
            AccessTree accessTree = getAccessTree(systemKey.getPublicKey(), departments);

            // 4. ABE加密主密钥
            PlainText plainText = new PlainText(kall, systemKey.getPublicKey());
            long abeEncStart = System.currentTimeMillis();
            CipherText cipherText = cpAneEngine.encrypt(systemKey.getPublicKey(), plainText, accessTree);
            long abeEncEnd = System.currentTimeMillis();

            // 5. ABE解密主密钥
            long abeDecStart = System.currentTimeMillis();
            String decryptedKall = cpAneEngine.decryptToStr(systemKey.getPublicKey(), userPrivateKey, cipherText);
            long abeDecEnd = System.currentTimeMillis();

            if (!kall.equals(decryptedKall)) {
                System.err.println("ABE解密失败，解密内容与加密不一致");
                System.err.println("原文: " + kall);
                System.err.println("解密: " + decryptedKall);
            } else {
                System.out.println("ABE解密成功，主密钥匹配");
            }

            // 6. 文件批量AES加密（基于部门标签派生的AES密钥）
            long fileEncStart = System.currentTimeMillis();
            batchEncryptFiles(kall, departmentTagMap);
            long fileEncEnd = System.currentTimeMillis();

            // 7. 文件批量AES解密（计时）
            long fileDecStart = System.currentTimeMillis();
            batchDecryptFiles(kall, departmentTagMap);
            long fileDecEnd = System.currentTimeMillis();

            // 8. 统计耗时
            long abeEncTime = abeEncEnd - abeEncStart;
            long abeDecTime = abeDecEnd - abeDecStart;
            long fileEncTime = fileEncEnd - fileEncStart;
            long fileDecTime = fileDecEnd - fileDecStart;
            long totalTime = abeEncTime + abeDecTime + fileEncTime + fileDecTime;

            // 9. 写入Excel
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(depCount);
            row.createCell(1).setCellValue(abeEncTime);
            row.createCell(2).setCellValue(abeDecTime);
            row.createCell(3).setCellValue(fileEncTime);
            row.createCell(4).setCellValue(fileDecTime);
            row.createCell(5).setCellValue(totalTime);

            System.out.printf("部门数: %d，ABE加密: %d ms，ABE解密: %d ms，文件加密: %d ms，文件解密: %d ms，合计: %d ms\n",
                    depCount, abeEncTime, abeDecTime, fileEncTime, fileDecTime, totalTime);
        }

        // 自动调整列宽
        for (int i = 0; i <= 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写回文件（覆盖）
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            workbook.write(fos);
        }
        workbook.close();

        System.out.println("实验完成，结果已追加写入 '" + fileName + "' 文件。");
    }

    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"部门数", "ABE加密(ms)", "ABE解密(ms)", "文件加密(ms)", "文件解密(ms)", "总耗时(ms)"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    // 构造访问树（所有部门均为叶子节点，根节点1个）
    private static AccessTree getAccessTree(PublicKey publicKey, List<String> departments) {
        int size = departments.size() + 1;
        AccessTreeBuildModel[] models = new AccessTreeBuildModel[size];
        models[0] = AccessTreeBuildModel.innerAccessTreeBuildModel(1, 1, 1, -1); // 根节点

        for (int i = 0; i < departments.size(); i++) {
            models[i + 1] = AccessTreeBuildModel.leafAccessTreeBuildModel(i + 2, 1, departments.get(i), 1);
        }
        return AccessTree.build(publicKey, models);
    }

    // 生成主密钥
    private static String generateMasterKey() {
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    // 生成随机标签
    private static String generateTag() {
        byte[] tagBytes = new byte[16];
        new SecureRandom().nextBytes(tagBytes);
        return Base64.getEncoder().encodeToString(tagBytes);
    }

    // 从 masterKey 和 tag 派生AES密钥
    private static String deriveKey(String masterKey, String tag) throws Exception {
        String input = masterKey + tag;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    // 批量加密 input 文件夹里的文件，使用部门标签派生AES密钥
    private static void batchEncryptFiles(String masterKey, Map<String, String> departmentTagMap) throws Exception {
        File inputDir = new File("input");
        File encryptedDir = new File("encrypted");
        if (!encryptedDir.exists()) encryptedDir.mkdirs();

        File[] files = inputDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("input 文件夹为空，跳过文件加密");
            return;
        }

        for (File file : files) {
            if (!file.isFile()) continue;

            // 约定文件名前缀为部门字母，如 A_, B_, ...
            String fileName = file.getName();
            String departmentPrefix = fileName.substring(0, 1);
            String tag = departmentTagMap.get(departmentPrefix);
            if (tag == null) {
                System.out.println("跳过未匹配部门文件：" + fileName);
                continue;
            }

            String aesKeyBase64 = deriveKey(masterKey, tag);
            SecretKey aesKey = new SecretKeySpec(Base64.getDecoder().decode(aesKeyBase64), "AES");

            File encryptedFile = new File(encryptedDir, fileName + ".bin");
            encryptFile(file, encryptedFile, aesKey);

            System.out.println("加密文件：" + fileName);
        }
    }

    // AES加密文件
    private static void encryptFile(File inputFile, File outputFile, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, len);
                if (output != null) fos.write(output);
            }
            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) fos.write(finalBytes);
        }
    }

    // 批量解密 encrypted 文件夹里的文件
    private static void batchDecryptFiles(String masterKey, Map<String, String> departmentTagMap) throws Exception {
        File encryptedDir = new File("encrypted");
        File decryptedDir = new File("decrypted");
        if (!decryptedDir.exists()) decryptedDir.mkdirs();

        File[] files = encryptedDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("encrypted 文件夹为空，跳过文件解密");
            return;
        }

        for (File encFile : files) {
            if (!encFile.isFile()) continue;

            String fileName = encFile.getName();
            // 去除后缀 .bin，取第一个字符判断部门
            String origName = fileName.endsWith(".bin") ? fileName.substring(0, fileName.length() - 4) : fileName;
            String departmentPrefix = origName.substring(0, 1);
            String tag = departmentTagMap.get(departmentPrefix);
            if (tag == null) {
                System.out.println("跳过未匹配部门文件：" + fileName);
                continue;
            }

            String aesKeyBase64 = deriveKey(masterKey, tag);
            SecretKey aesKey = new SecretKeySpec(Base64.getDecoder().decode(aesKeyBase64), "AES");

            File decryptedFile = new File(decryptedDir, origName);
            decryptFile(encFile, decryptedFile, aesKey);

            System.out.println("解密文件：" + origName);
        }
    }

    // AES解密文件
    private static void decryptFile(File encryptedFile, File decryptedFile, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        try (FileInputStream fis = new FileInputStream(encryptedFile);
             FileOutputStream fos = new FileOutputStream(decryptedFile)) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, len);
                if (output != null) fos.write(output);
            }
            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) fos.write(finalBytes);
        }
    }
}
