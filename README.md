-----------     -----------
JUTA - JUnit-Test mit Ant
-----------     -----------

(Marcus Deininger / Elke Muthmann / Marco Przystaw / Nicole Merk)

Verwendung Version 3.0, 23.3.2016


Inhalt:

1) Vorausstetzungen

2) benötigte Dateien

3) Programmablauf/Ausgabe

4) Ant unter Eclipse

5) Fehlerbehebung

---------------------------


1) Vorausstetzungen

Eclipse mit Ant-Plugin (aktuell Eclipse Mars)
- Java Development Kit (JDK) (aktuell Version 1.8)
+ ant-contrib.jar
+ poi.jar (Apache POI, the Java API for Microsoft Documents)
+ junit-4.12.jar
+ hamcrest-core-1.3.jar

2) Setup

im Verzeichnis "02-tests":
JUnit-Testdateien für jede Übungsaufgabe
die Dateien müssen "Test<Aufgabenname>" heißen. Die Testfällen sollten
idealerweise den Code selbst mit Hilfe des SourcefileReaders untersuchen.
Alle Testfälle müssen einen Timeout vorsehen. Die Dateien müssen die Methoden
	public static String getGroup()  { return "Name der Gruppe"; }
	public static String getNumber() { return "Nr. Der Aufgabe"; }
umsetzen.

im Verzeichnis "03-tasks":
die Musterlösungen - sie werden benötigt, um die Anwendung zu übersetzen
(insofern müssen es nicht unbedingt die Musterlösungen sein.)

im Verzeichnis "04-registrations":
die Excel-Notensheets des Prüfungsamts - die Namen werden mit den Moodlenamen abgeglichen.

im Verzeichnis "05-heap":
evtl. unbenutze zip-Dateien

im Verzeichnis "06-units under test":
die von Moodle heruntergeladenen Abgaben in einem oder mehreren zip-Dateien.


3) Ausführung

main in build.xml ausführen

4) Ergebnis

die Auswertung wird im Verzeichnis 07 - results abgelegt:
- "compiled" enthält die übersetzten Klassen
- "uncompilable" enthält die nicht kompilierbaen Klassen
- "unsolicited" enthält unverlangte / falsch benannte Klassen
- "report" enthält als csv-File die Auswertung.
