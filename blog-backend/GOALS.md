Ausgangslage: 
Es wird der aktuelle Stand (07.03.2025 )des Blog-Backends übernommen und erweitert. 

Aufgabenstellung: 
Ziele: 
Daten Upload
Dateien können über das Backend hochgeladen werden. Dateien werden mit Blogs assoziiert. 

Lokale Speicherung
Daten sind lokal gespeichert. Zum Einsatz kommt wahrscheinlich eine Art Storage Bucket. 

Daten Download:
Endpunkte zum bereitstellen von Dateien. Z.B. downloaden von Dateien oder anzeigen von Bildern.

Datenverwaltung
Das Backend stellt die http Endpunkte bereit, um Dateien abzurufen, hochzuladen, zu ändern oder zu löschen. 

Validierung
Hochgeladene Dateien werden überprüft und validiert. Zu validierende Punkte sind z.B. Dateityp und Dateigrösse. 

Skalierbarkeit
Das System sollte, insoferm möglich, skallierbar gestalltet werden. 


Funktionen:
Dies sind mögliche Funktionen die implementiert werden können. Eine implementierung steht jedoch offen und ist Abhängig vom Aufwand und dem Projektverlauf. 

Datei Upload: Dateien können hochgeladen werden und mit einem Blog verknüpft werden. 
Datei Download: Hochgeladene Dateien können runtergeladen werden. 
Datei Löschung: Dateien können gelöscht werden. 
Datei Metadaten: Metadaten zu hochgeladenen Dateien speichern und verwalten.
Mehrere Dateien hochladen: Mehrere Dateien können gleichzeitig hochgeladen werden. 
Datensuche: Dateien können nach Namen, Typ oder Uplaod Datum gesucht werden. 