# Manuális kód átvizsgálás és Statikus analízis
## Manuális kód átvizsgálás
A kódot átnéztük , megvizsgáltuk és találtunk pár helyen hibát, például a nem platform specifikus elnevezések (camelCase) egyből feltüntek nekünk.

## Statikus analízis
A statikus analízis elvégézésre SonarCloudot használtunk amit amár meglévő github actionshöz adtunk hozzá a gyakorlaton ismertett módon.

A projekthez való hozzádása után ezeket az értékeket mutatta a játékról:
![](https://github.com/BME-MIT-IET/iet-hf-2022-adja/tree/main/doc/img/RégiSonar.png)

22 bug-ot és 18 Security Hotspot-ot talált a kiértékelő ezeket mind kijavítottuk.
Azért javíttuk inkább ezeket, mert ezeknek nagyobb jelentőségük volt a projekt szempontjából, lényegesebb hibák voltak.
### Kijavított bugok
1. "Random"-okkal kapcsolatos bugok:
   * 
2. "Interrupted Exception" bug :
   *
3. "Double Casting" bug :
   * 
4. "Remove method" bug : 
   * 




### Kijavitott SecurityHotspot-ok : 
1. "Random"-okkal kapcsolatos Security Hotspot-ok:
2. 



