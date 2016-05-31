#AudiPic v.1.1-SNAPSHOT

AudiPic wurde im Rahmen des HackARThons (1.Platz) an der TH Köln Campus Gummersbach innerhalb von nur wenigen Stunden entwickelt
und ermöglicht es, anhand einer Audio-Datei ein Bild Live zu erzeugen. Die erzeugten Bilder sind bei den gleichen Einstellungen
immer identisch, da beim Erzeugen keine Zufallsalgorithmen verwendet wurden. Die genutzten Parameter sind unter anderem
die verschiedenen Frequenzbereiche (Bänder) sowie deren Ausschlag in Db.

AudiPic benötigt mindestens Java 8!

#v.1.1-SNAPSHOT

- Eine neue Version des genutzten Painters wurde hinzugefügt und zum Standard gemacht. Dieser berechnet die Dezibel nun
korrekt, wodurch das entstehende Bild eine korrekte Abbildung der Höhen und Tiefen sowie Lautstärke der einzelnen
Bänder und damit eine korrekte Abbildung des Liedes darstellt.
- Die Anzahl der berechneten Frequenzbänder wurde von 8 auf 10 erhöht.
- Ein paar Label wurden eindeutiger bezeichnet, sodass der Sinn und Zweck der Eingabe etwas deutlicher wird.