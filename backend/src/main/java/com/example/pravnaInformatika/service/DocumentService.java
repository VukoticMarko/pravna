package com.example.pravnaInformatika.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class DocumentService {

    public String getText(String text_path) throws IOException {
        File file = new File(text_path);
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    public String getCaseNumber(String verdict_text) {
        Pattern pattern = Pattern.compile("^[kK]\\.\\s?([Bb]r\\.\\s?)?[0-9]{2}/[0-9]{1,4}\\s");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = " ";
        if (matcher.find()) {
            ret = matcher.group();
        } else {
            Pattern pattern2 = Pattern.compile("\\s[kK]\\.\\s?([Bb]r\\.\\s?)?[0-9]{2}/[0-9]{1,4}\\s");
            Matcher matcher2 = pattern2.matcher(verdict_text);
            if (matcher2.find()) {
                ret = matcher2.group();
            }
        }
        return ret.trim();
    }


    public String getSud(String verdict_text) {
        Pattern pattern = Pattern.compile("\\sU IME CRNE GORE\\s*[A-ZŽĐŠČĆa-zžđšćčć]+ ((SUD U)|(sud u)) [A-ZŽĐŠČĆa-zžđšćčć]+");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group();
            Pattern pattern2 = Pattern.compile("\\s[A-ZŽĐŠČĆa-zžđšćčć]+ ((SUD U)|(sud u)) [A-ZŽĐŠČĆa-zžđšćčć]+");
            Matcher matcher2 = pattern2.matcher(ret);
            if (matcher2.find()) {
                ret = matcher2.group();
            }
            else {

            }
        }
        return ret.trim();
    }

    public String getDefendantInitials(String verdict_text) {
        Pattern pattern = Pattern.compile("\\s((PRESUDU)|(P R E S U D U))\\s*((Okrivljen[ai])|(OKRIVLJEN[AI])|(Optužen[ia])):?\\s*[A-ZŽĐŠČĆ]{1,2}([., ]) ?[A-ZŽĐŠČĆ]{1,2}([., ])");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group();
            if (ret.contains("P R E S U D U"))
                ret = ret.split("P R E S U D U")[1];
            Pattern pattern2 = Pattern.compile("\\s[A-ZŽĐŠČĆ]{1,2}([., ]) ?[A-ZŽĐŠČĆ]{1,2}([., ])");
            Matcher matcher2 = pattern2.matcher(ret);
            if (matcher2.find()) {
                ret = matcher2.group();
            }
            else {

            }
        }
        return ret.replace(",", ".").replace(". ", ".").replace(".", ". ").trim();
    }

    public String getJudge(String verdict_text) throws IOException {
        Pattern pattern = Pattern.compile("\\s((ZAPISNIČAR(KA)?)|(Zapisničar(ka)?))(,|\\s)?\\s*((SUDIJA)|(S U D I J A)|(SUTKINJA)|(S U T K I N J A)),?\\s+[A-ZŽĐŠČĆa-zžđšćčć]+ [A-ZŽĐŠČĆa-zžđšćčć]+((,\\s?s.r.)|,|\\s)\\s?[A-ZŽĐŠČĆa-zžđšćčć]+ [A-ZŽĐŠČĆa-zžđšćčć]+,?(\\s?s.r.)?");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group();
            String[] lines = ret.split("\\r?\\n");
            if (lines.length > 2) {
                ret = lines[2].trim();
                String[] names = ret.split(",");
                ret = names.length > 1 ? names[1].replaceAll("s\\.r\\.", "").trim() : ret;
            } else {
                // Handle case where there are not enough lines
                // This could be logging the error, throwing a custom exception, etc.
            }
        } else {
            Pattern pattern2 = Pattern.compile("\\s((Sudija)|(SUDIJA)|(S U D I J A)|(Sutkinja)|(SUTKINJA)|(S U T K I N J A))(:|,)?\\s+(Mr )?[A-ZŽĐŠČĆa-zžđšćčć]+ [A-ZŽĐŠČĆa-zžđšćčć]+");
            Matcher matcher2 = pattern2.matcher(verdict_text);
            if (matcher2.find()) {
                ret = matcher2.group();

                String lines[] = ret.split("\\r?\\n");
                int i = 0;
                do {
                    i++;
                    ret = lines[i].replace("s.r.", "").trim();

                } while (lines[i].matches("^\\s*$"));
            }
        }
        return ret;
    }

    public String getClerk(String verdict_text) throws IOException {
        Pattern pattern = Pattern.compile("\\s((ZAPISNIČAR(KA)?)|(Zapisničar(ka)?))(,|\\s)?\\s*((SUDIJA)|(S U D I J A)|(SUTKINJA)|(S U T K I N J A)),?\\s+[A-ZŽĐŠČĆa-zžđšćčć]+ [A-ZŽĐŠČĆa-zžđšćčć]+");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group();
            String[] lines = ret.split("\\r?\\n");
            if (lines.length > 2) { // Make sure there are at least three lines
                ret = lines[2].trim();
            } else if (lines.length > 0) { // Fallback if there are fewer lines
                ret = lines[lines.length - 1].trim(); // Use the last available line
            }
            return ret.replace("s.r.", "").trim();
        }
        Pattern pattern2 = Pattern.compile("\\sZTO(:|-)\\s?[A-ZŽĐŠČĆa-zžđšćčć]+ [A-ZŽĐŠČĆa-zžđšćčć]+");
        Matcher matcher2 = pattern2.matcher(verdict_text);

        //Pattern pattern3 = Pattern.compile("\\s((uz\\sučešće(\\snamještenika\\ssuda)?)|(sa\\szapisničarom))\\s+[A-ZŽĐŠČĆa-zžđšćčć]+ [A-ZŽĐŠČĆa-zžđšćčć]+");
        if (matcher2.find())
            return matcher2.group().replace("ZTO:", "").replace("ZTO-", "").trim();
        Pattern pattern3 = Pattern.compile("\\s((uz učešće\\s([a-zžđšćčć]+\\s){0,5})|(sa zapisničarom\\s))\\s*[A-ZŽĐŠČĆ]{1,2}[a-zžđšćčć]+ [A-ZŽĐŠČĆ]{1,2}[a-zžđšćčć]+");
        Matcher matcher3 = pattern3.matcher(verdict_text);
        if (matcher3.find()) {
            ret = matcher3.group().replace(",", "").replace("\r\n", " ").replace("\n", " ").trim();
        }
        return ret;
    }

    public List<String> getFelony(String verdict_text) throws IOException {
        Pattern pattern = Pattern.compile("\\s+zbog\\s+krivičnog\\s+djela\\s+[A-ZŽĐŠČĆa-zžđšćčć0-9.,\\s]*?\\s+Krivičnog\\s+zakonika\\s+Crne\\s+Gore");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = "unknown";
        List<String> results = new ArrayList<>();
        if (matcher.find()) {
            ret = matcher.group();
            results.add(ret);
        }
        else {
            Pattern pattern2 = Pattern.compile("(\\szbog)?\\skrivičnog\\sdjela\\s[A-ZŽĐŠČĆa-zžđšćčć0-9.,\\s]*?\\sKrivičnog\\szakonika\\sCrne\\sGore");
            Matcher matcher2 = pattern2.matcher(verdict_text);
            while (matcher2.find()) {
                if (matcher2.groupCount() > 0) {
                    results.add(matcher2.group(1));
                } else {
                    results.add(matcher2.group());
                }
            }
        }
        results = results.stream().map(val -> {
            if (val == null) return "";
            return val.replace("\r\n", " ").replace("\n", " ").trim();
        }).toList();
        return results;
    }

    public String getVerdict(String verdict_text) throws IOException {
        Pattern pattern = Pattern.compile("\\s((U ?S ?L ?O ?V ?N ?U ? O ?S ?U ?D ?U)|(O ?S ?U ?Đ ?U ?J ?E))\\s+[\\S\\s]*?\\.\r?\n");
        Matcher matcher = pattern.matcher(verdict_text);
        String ret = "unknown";
        if (matcher.find()) {
            ret = matcher.group();
        }
        return ret.replace("\r\n", " ").replace("\n", " ").trim();
    }

    public List<String> getMetadata(String file) throws IOException {
        String verdict_text = getText(file);
        List<String> metadata = new ArrayList<>();

        String fileNameWithExtension = new File(file).getName();
        String fileNameOnly = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
        metadata.add(fileNameOnly);

        metadata.add(getJudge(verdict_text));
        metadata.add(getClerk(verdict_text));
        metadata.add(getDefendantInitials(verdict_text));

        metadata = metadata.stream()
                .map(data -> data.replaceAll("\\r?\\n", " "))
                .collect(Collectors.toList());

        metadata.add(getSud(verdict_text));
        metadata.add(getVerdict(verdict_text));

        List<String> felonies = getFelony(verdict_text);
        if (!felonies.isEmpty()) {
            metadata.addAll(felonies);
        }

        System.out.println(metadata);
        return metadata;
    }


    public void generateMetadata() {
        Path dirPath = Paths.get("src/main/resources/judgements_pdf"); // Path to the directory containing PDFs

        // Try-with-resources statement to ensure FileWriter and Stream<Path> are closed automatically
        try (FileWriter myWriter = new FileWriter("src/main/resources/metadata.txt");
             Stream<Path> paths = Files.walk(dirPath)) {

            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    List<String> metadata = getMetadata(file.toString());
                    for (String data : metadata) {
                        myWriter.write(data + ";");
                    }
                    myWriter.write(System.lineSeparator()); // Add a newline after each file's metadata
                } catch (IOException e) {
                    // Handle exceptions
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            // Handle exceptions, possibly logging them or notifying the user
            e.printStackTrace();
        }
    }

    public void generateXMLfromMetadata() {
        String inputFile = "metadata.txt"; // Your metadata file path
        Path outputDirectory = Paths.get("Z:\\Faks\\Pravna Informatika\\pravna\\backend\\src\\main\\resources\\judgements_xml");
        //Path outputDirectory = Paths.get("/src/main/resources/judgements_xml/"); // Output directory for XML files
        if (!Files.exists(outputDirectory)) {
            try {
                Files.createDirectories(outputDirectory);
                System.out.println("Created new folder for xml's.");
            } catch (IOException e) {
                e.printStackTrace();
                return; // Directory creation failed, exit the method
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] metadata = line.split(";");
                if (metadata.length < 3) {
                    System.out.println("Skipping line due to incorrect format: " + line);
                    continue;
                }
                Path filePath = Paths.get(metadata[0]);
                String fileNameOnly = filePath.getFileName().toString();
                String xmlFileName = fileNameOnly + ".xml";
                String xmlContent = generateXmlContent(metadata);
                Path xmlFilePath = outputDirectory.resolve(xmlFileName);
                Files.write(xmlFilePath, xmlContent.getBytes());
                try {
                    // Write XML content to file
                    Path writtenFilePath = Files.write(xmlFilePath, xmlContent.getBytes(), StandardOpenOption.CREATE);
                    System.out.println("Written file path: " + writtenFilePath.toAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to write XML file: " + xmlFilePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateXmlContent(String[] metadata) {
        try{
            return String.format("<akomaNtoso xmlns=\"http://docs.oasis-open.org/legaldocml/ns/akn/3.0\" "
                            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                            + "<judgement>"
                            + "<meta>"
                            + "<identification source=\"#court\">"
                            + "<FRBRWork>"
                            + "<FRBRauthor>%s</FRBRauthor>"
                            + "<FRBRdate date=\"%s\">%s</FRBRdate>"
                            + "<FRBRtitle>%s</FRBRtitle>"
                            + "<FRBRcountry>CG</FRBRcountry>"
                            + "</FRBRWork>"
                            + "</identification>"
                            // ... Add more elements based on your metadata and XML structure
                            + "</judgement></akomaNtoso>",
                    metadata[1], // Author
                    metadata[2], // Date
                    metadata[2], // Date repeated
                    metadata[0]  // Title (case number)

            );
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Wrong format in line! Skipping line...");
        }
        return "Faulty metadata.";
    }
}
