**Description**


upload.java : 

class tirée de l'API de https://www.speedtest.net/fr qui a notamment subi des modifications afin de corriger des bugs qui n'avaient pas été corrigés par les developpeurs initiaux

download.java & upl.java :

class s'occupant respectivement du test de téléchargement et d'envoi. À la fin de l'exécution, un fichier .csv est créé contenant des valeurs choisies (normalement le temps et les données envoyées entre le temps précédent et le temps actuel).


csv_modif.java :

class qui traite les fichiers .csv obtenus : réduction de l'échelle de temps (si nécessaire), création du débit, etc.
