# Manuális kód átvizsgálás és Statikus analízis
## Manuális kód átvizsgálás
A kódot átnéztük, megvizsgáltuk errorokat szerencsére nem találtunk és az alkalmazás is futott, de találtunk pár helyen kisebb hibákat, például a nem platform specifikus elnevezések (camelCase-ek) használatát egyből észrevettük.

## Statikus analízis
A statikus analízis elvégézésre SonarCloudot használtunk amit a már meglévő github actions-höz adtunk hozzá a gyakorlaton ismertetett módon.

![](../doc/img/ActionsWithSonar.png)


A projekthez való hozzádása után a SonarCloud ezeket az értékeket mutatta a játékról:
![](../doc/img/RégiSonar.png)

22 bug-ot és 18 Security Hotspot-ot talált a kiértékelő ezeket mind kijavítottuk és pár darab code smell-t is ezeken felül.
Azért javíttuk inkább ezeket, mert ezeknek nagyobb jelentőségük volt a projekt szempontjából, lényegesebb hibák voltak.
### Kijavított bugok
1. "Random"-okkal kapcsolatos bugok:
   * sok helyen hoztunk létre új random objektumot, ezeket lecseréltük egy központira, a felhasználásnál pedig erre hívunk next-et, ezzel javítva a működést 
2. "Interrupted Exception" bug :
   * lekezeltük az ignorált Interrupted Exeption-öket, hogy semmilyen esetbe se következzen be a megszakításból adatvesztés
3. "Double Casting" bug :
   * néhány műveletnél nem voltak egyes változók megfelelően castolva, a stabilabb és pontosabb (megjelenítésnél) működés érdekében javítottuk ezeket
4. "Remove method" bug : 
   * mivel tudjuk, hogy az alkalmazásban az itt haszált map.values() függvényére mindig vissza kell kapnunk egy listát, amin értelmezhető a remove metódus, így arra jutottunk, hogy ezt megjelöljük "Won't Fix"-nek SonarCloudon

   
### Kijavitott SecurityHotspot-ok : 
1. "Random"-okkal kapcsolatos Security Hotspot-ok:
   * mérlegeltük azt, hogy az alkalmazás megfelelő működéséhez szükséges-e egy biztonságosabb Random és arra jutottunk, hogy ennek az javítása nem szükséges, nem befolyásolja a működést 
2. "printStackTrace()"-el kapcsolatos Security Hotspot-ok: 
   * azért javítottuk ki, hogy a felhasználó számára véletlenül se jelenjen meg kódrészlet, ami érzékeny információkat tartalmazhat


### Kijavított - átnézett code-smellek :
1. Collection.size() == 0 feltételt Collection.isEmpty() - re cseréltük, mivel, egyfelöl olvashatóbb lett így a kódunk
másfelől meg a komplexitása az isEmpty() metodusnak O(1) míg a size()-nak lehet O(n) is.
2. Nagyon sok code-smell (kb 50-100 db) a nem megfelelő platform specifikus elnevezések miatt volt ezeket nem javítottuk csak megjegyeztük, hogy a jővőben erre jobban oda figyelünk majd.


## Javitás utáni kép SonarCloudról:
![](../doc/img/UjSonar.png)

