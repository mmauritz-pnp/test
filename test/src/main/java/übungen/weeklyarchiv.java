package übungen;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class weeklyarchiv {

    public static void main(String[] args) {

        List<String> erlaubteOrdnernamen = Arrays.asList("KSZ", "PaBi", "AHP-WoBla-AP", "Falter", "Lust aufs Land", "Neuisenburger", "Waldumschau");

        Path quellverzeichnis = Paths.get("//npv.de/fs/Fremdbelichtung/Archiv/");

        LocalDate heute = LocalDate.now();
        int kalenderwoche = heute.get(WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear());

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(quellverzeichnis)) {
            for (Path file : stream) {
                if (Files.isDirectory(file)) {
                    String ordnername = file.getFileName().toString();

                    if (erlaubteOrdnernamen.contains(ordnername)) {
                        Path ueberordnerPfad = file;

                       
                        String zielHauptordnerName = "//npv.de/fs/Fremdbelichtung/Archiv/ZurArchivierung/2025/" + ordnername + "_KW" + kalenderwoche;
                        Path zielHauptverzeichnis = Paths.get(zielHauptordnerName);

                        
                        if (!Files.exists(zielHauptverzeichnis)) {
                            Files.createDirectories(zielHauptverzeichnis);
                        }

                        try (DirectoryStream<Path> unterordner = Files.newDirectoryStream(ueberordnerPfad)) {
                            for (Path unter : unterordner) {
                                if (Files.isDirectory(unter)) {
                                    String unterordnerName = unter.getFileName().toString();

                                    if (unterordnerName.equals("In") || unterordnerName.equals("Out")) {

                                        
                                        Path zielUnterordner = zielHauptverzeichnis.resolve(unterordnerName);

                                        if (!Files.exists(zielUnterordner)) {
                                            Files.createDirectories(zielUnterordner);
                                        }

                                        
                                        try (DirectoryStream<Path> inhalte = Files.newDirectoryStream(unter)) {
                                            for (Path inhalt : inhalte) {
                                                Path zielPfad = zielUnterordner.resolve(inhalt.getFileName());
                                                Files.move(inhalt, zielPfad, StandardCopyOption.REPLACE_EXISTING);
                                                System.out.println("Verschoben: " + inhalt.getFileName() + " nach " + zielPfad);
                                            }
                                        }

                                        
                                        // Files.delete(unter);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("Fehler " + ueberordnerPfad);
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler ");
            e.printStackTrace();
        }
    }
}
