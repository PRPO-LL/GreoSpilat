
[Projekt](Sportapp) pri predmetu.

### 18.10.###

uporabljal intelliJ,
prvo hello world projekt, pol samo en flat projekts z enim reosursom, pol pa dodajaj,...
POL pa narei prvi modul 

v Javi naredimo en Application class in za vsak api Resource class
pomembni metodi @Path in @Get/Post/...

ogrodje za unit teste (JUnit)
openTelemetry knjižnica za spremljanje izvajanja mikrostoritve

**Pakiranje**
.jar je zip datoteka s predpisano strukturo
v .jar bomo zapakirali celoten runtime

zapakiramo kodo + vse dodatke zraven JRE ki jih storitev nudi za obratovanje

kaj rabimo za REST? komunikacijo preko HTTPS

ce uporablamo KumuluzE v .jar zapakiramo jetty HTTP

**Apache Maven** orodje za buildanje
pom.xml wizard sam zgenerira, vsebuje deklaracijo
	GroupID, ArtefactID, verzija -> zelo pomembne 3
	si.fri, ime projekta, 1.0.0



v javi dva tipa **miktostoritvenih ogrodij**
- **Microprofile** v katrega spadajo Quarkus, IBMov WSL, in slovensko KumuluzEE (koda med njimi prenosljiva, pom.xml se malo popravi)
- **Spring** obstaja tudi mikrostoritveno orodje imenovano boot

Skupna točka obeh orodij je *Maven* 

Skoz Maven dependencies povemo katere dele mikrostoritvenega orodja povemo kaj potrebujemo, da nea lih vsega damo not v projekt

Najprej vključimo BillsOfMaterial v dependenciesManagement
pod Dependencies vedno potrebujemo kumuluz core, pol npr. jetty, postgress (driver za bazo), Jax-RS za restAPI, ...
^ to najdemo v dokumentaciji da vemo kak vključimo dependencies, copy paste

ko bos prvic delal rest storitve naj vrača konstane vrednosti, nea se zezaj z implementacijo baze

če hočemo met .jar vključemo cumukuzuv maven plugin ki dela repackage

**Gradnja miktostoritve**
- iz API, storitve, entitete, to so 3je različni moduli


API ma svoj src, target in pom.xml, tako tudi storitev in entiteta
vsaka modul ma svoj pom.xml ki se deduje od krovnega pom.xml

`mvn package` spakira celoten program v .jar


interni repozitorij artefactov,  lasten maven repozitorij kamor zlagamo dependencies (Nexus repozitorij, tut za Docker image)


Postman storitev za testiranje APIjev
_____________________________
public class NakupovalniSeznamDto{
sledi Json fajlu
naredimo beanse, in pol getterje in setterje, to vse intelliJ zgenerira
}

za vsak objekt ki ga sprejemate kot parameter si treba nareit ustrezen java Bean v ozadju


response nosi http status, http header pa generik??

________
v meta direktorij mores dodati beans datoteko
kumuluzEE zeli config.yaml