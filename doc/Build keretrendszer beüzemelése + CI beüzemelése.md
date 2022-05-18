# Build keretrendszer beüzemelése + CI beüzemelése
## Build keretrendszer beüzemelése

Az eredeti projekt nem rendelkezett semilyen Build keretrendszerrel, így ez célszerű volt elkészíteni.
A választás a **Maven** keretrendszerre eset.

Ehhez segítségül vettük az IntelliJ IDEA egyik funkcióját, amivel meglévő projekthez tudjuk hozzáadni a keretrendszert,
ezáltal kicsit módosította a projekt struktúráját és létrejött a szükséges pom.xml fájl is.

A pom.xml fájlban később könnyedén hozzáadhattuk további projekt függőségeinket, például JaCoCo, Cucumber vagy JUnit.

![](/doc/img/structure.png)

## CI beüzemelése

CI-hoz korábbi, más projektek tapasztalata alapján a **Github Actions**-re került a választás, egyszerű és gyors konfigurálhatósága miatt.

Az alap sablon alapján indultunk ki, majd, ahogy haladtunk a következő feladatokra
szükség szerint bővitettük a megfelelő funkciókkal, például a SonarCloud ellenőrzés vagy a BDD tesztekhez
kapjunk lefedetségi értékeket is.