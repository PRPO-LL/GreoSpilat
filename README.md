
# GreoSpilat App 

*Luka Kalšek, Luka Bele*, Luka^2
*skupina 10*

👦🏼: "A maš še koga za basket?"

👦🏽: "Nimam, lahko pa pogledava na GreoSpilat."

👦🏼: "GreoSpilat?"

GreoSpilat. Aplikacija, ki druženje iz socialnih omrežij vrne pred bloke, na vaška igrišča ali na mestne tekaške steze. Povezuje ljudi, ki jim do svoje športne aktivnosti manjka nekaj igralcev, žoga ali pa motivacija družbe. Namenjena rekreaciji, skupnim treningom ali pa spoznavanju novih ljudi. Preprosto ustvari dogodek, za katerega ti manjka še kakšen igralec, ali pa poglej kaj je na voljo in se pridruži zanimivi aktivnosti. Ko se dogodek zapolni z udeleženci boš o tem obveščen/a in že si lahko na poti na svoj prvi padel! Ne čakaj, GreoSpilat!

## Kazalo

## Uvod

Namen projkta je ustvariti aplikacijo, ki vključuje sodobne tehnologije in prakse v razvoju programske opreme s poudarkom na mikrostoritveni arhitekturi, izpostavljanju RESTful APIjev, pakiranje slik mikrostoritev v vsebnike in stroke, uporabi orodja za orkestracijo in nameščanju le tega v oblak. Cilj je, da se pri razvoju spoznava z novimi tehnologijami in praksami in da končni izdelek (aplikacija) podpira najpogostejše primere uporabe za tip aplikacije, ki jo razvijava. Bodoči uporabnik bo skozi uporabniški vmesnik uporabljal aplikacijo, ki bo z uporabo prej omenjenenih tehnologij in skaliranja zagotavljala prijetno uporabniško izkušnjo. Motivacija za temo je predstavljna na začetku tega dokumenta in je zelo intuitivna, saj sva pri ideji izhajala iz najinih lastnih potreb. Ta aplikacija "rešuje problem", ki je na trenutnem trgu še precej nerešen, oziroma za to ne obstaja zelo očiten ponudnik kot na marsikaterem drugem področju. Primerna je za tako stare in mlade, bolj ali manj aktivne, hkrati pa spodbuja pravo socializacijo in fizkulturo.

## Arhitektura aplikacije

Spodnja slika prikazuje glavne komponente in interakcije med njimi.

- Vue.js komponenta predstavlja uporabniški vmesnik s pogledi kateri je vstopna točka za komunikacijo z zalednim delom.

- Zaledni del predstavljajo mikrostoritve, ki se naprej delijo na API, poslovno logijo, in persistenčno plast za komunikacijo s persistenčnim nivojem.

- Persistenčno plast predstavlja PostgreSQL podatkovna baza z relacijami za vsako od mikrostoritev, ki so med sabo smiselno povezane

- 🟧 Oranžne povezave: Predstavljajo HTTP klice iz uporabniškega vmesnika na REST vire mikrostoritev

- 🟩 Zelene povezave: Predstavljajo HTTP klice iz mikrostoritve na API druge mikrostoritve

- 🟦 Modre povezave: predstavljajo 

![arhitektura](slike/shema.png)

### Uporabljene tehnologije

#### Maven

Orodje za avtomatizacijo gradnje javanskih projektov. Skrbi za odvisnosti definirane v parent pom.xml, ki je v glanem direktoriju projekta in jih iz Maven Central repozitorija prenese v naš lokalni repozitorij in jih oporabi pri gradnji projekta. pom.xml datoteko ima tudi vsaka mikrostoritev v svojem direktoriju, prav tako pa vsaka njena komponenta (api, bizLogic, persistance). Maven uporablja enotne sheme projekta, ki da delijo na vire src/ in target/ kej konča zgrajena verzija. Primer strukture za MS user:

    .
    ├── Dockerfile  

    ├── pom.xml   

    ├── users-api  

    │   ├── pom.xml  

    │   ├── src  

    │   │   └── main  

    │   └── target  
    
            └── users-api-1.0.0-SNAPSHOT.jar

    ├── users-bizLogic  

    │   ├── pom.xml  

    │   ├── src  

    │   │   └── main  

    │   └── target  

            └── users-bizLogic-1.0.0-SNAPSHOT.jar

    └── users-persistance 

        ├── pom.xml  
    
        ├── src  
    
        │   └── main  
    
        └── target  
    
            └── users-persistance-1.0.0-SNAPSHOT.jar
    

Maven binary prenesemo iz https://maven.apache.org/download.cgi in sledimo namestitvi. Po spremembah projekt zgradimo z  ``mvn clean install``.

#### KumuluzEE

Ogrodje za razvoj mikrostoritev, ki ponuja orodja za delo kot so implementacija JAX-RS (javanska REST implementacija), CDN za upravljanje z odvisnostmi med objekti in JPA za preslikave med objekti in relacijami. Prav tako omogoča preverjanje zdravja storitev, preko kumuluzee-health odvisnosti s katero lahko na ``<app-url>/health/live`` preverjamo zdravje storitev. KumuluzEE vključimo preko odvisnotsi v pom.xml konfiguracijo pa prirejamo v config.yml v posamezni mikrostoritvi. 

#### OpenAPI

Orodje za sprotno dokumentiranje specifikacije, kar v kodi. Javanske funkcije anotiramo s simboli kot so ``@APIResponse`` ali ``@Operation`` , na podlagi cesar lahko kasneje dobimo specfikacijo naši API-jev v .yaml obliki na naslovu posamezne mikrostoritve ``http://localhost:<port-mikrostoritve>/openapi``. To dokumentacijo lahko vkljlučimo v Swagger UI.

#### Swagger

Z nalaganjem yaml opisov REST virov in shem, ki nam jih generira OpenAPI dobimo vizualno reprezentacijo, ki je ena od bolj popularnih in lahkih za uporabo orodij za dokumentacijo API-jev.



**Mikrostoritve**
- Authentication
- Users
- Events
- Comments
- Join
- Notifications

#### Opis mikrostoritev #####
##### Authentication
Mikrostoritev je namenjena avtentikaciji in avtorizaciji uporabnika. Uporablja se
za registracijo uporabnika, hrani šifrirana gesla in uporabniška imena. Ob prijavi
generira tudi JWT žetone, ki jih druge mikrostoritve uporabljajo za avtorizacijo
in avtentikacijo. Vsebuje tudi konˇcne toˇcke za avtentikacijo JWT žetonov.

##### Users
Mikrostoritev je namenjena upravljanju uporabnikov. Uporabniki lahko spre-
minjajo svoj raˇcun ali pa izbrišejo profil. Povezana je z mikrostoritvijami join,
event, comments in notifications.

##### Events
Mikrostoritev je namenjena upravljanju dogodkov, ustvarjanju, spreminjanju in
brisanju dogodkov. Kakor mikrostoritev user, je tudi events povezana z vsemi
ostalimi mikrostoritvami.

##### Notifications
Naloga Notifications je ustvarjanje in opravljanje z obvestili, s klicem POST
post izpostav ustvarjamo obvestila, ki se persistirajo v podatkovno bazo za na-
men nadaljne obdelave. Poleg vsebine obvestila hranimo tudi njegov status.
GET izpostave vraˇcajo toˇcno doloˇceno obvestilo ali vsa obvestila namenjena
doloˇcenemu uporabniku.

##### Comments
S komentarji upravlja storitev Comments. Komentar je z relacijo ManyToOne
veže uporabnika in dogodek preko njunih id-jev, lahko pa tudi drug komentar
z rekurzivno referenco na comment_id, s tem je podprta funkcionalnost replik
na komentar. Poleg ustvarjanja komentarjev in replik, brisanja in vraˇcanja
komentarjev glede na uporabnika ali dogodek ena od izpostav omogoˇca tudi
všeˇckanje komentarjev.

##### Join
Join upravlja z razmerjem many-to-many med uporabniki in dogodki, ki se
preslika v podatkovno bazo joins s tujim kljuˇcem iz vsake strani. Opravlja pri-

#### Ogrodje in razvojno okolje
Zaledje aplikacije je grajeno v javanskem frameworku KumuluzEE, ki bazira
na JavaEE. Uporabila sva ga za gradnjo mikrostoritev in implementacijo REST
APIjev. Za podatkovno bazo sva uporabila PostgreSQL, kjer so shranjeni vsi
podatki aplikacije. Docker je bil uporabljen za kontejnerizacijo aplikacij, kar je
omogoˇcilo enostavno izvajanje mikrostoritev v izoliranem okolju. Uporabljen
je bil v povezavi s tehnologijo Docker Compose, za povezovanje mikrostoritev
in hkratno orkestracijo. Sprednji del aplikacije je grajen z Vue.js, ki je JavaScript
framework za gradnjo uporabniških vmesnikov. Za realizacijo Kubernetes clustra
na lokalnem okolju sma uporabila orodje kind.

#### Knjižnjice in mehanizmi
- Axios→ naslavljanje HTTP zahtevkov na zaledje.
- JWT→ uporabljen za avtentikacijo in avtorizacijo uporabnikov.
- Password4j→ uporabljen za enkripcijo in verifikacijo gesel
- JPA→ upravljanje relacijskih podatkov z objekti.
- CORS filtri→ omogoˇcanje pošiljanja HTTP zahtevkov ˇcez razliˇcne domene
  
#### Razvojna okolja in orodja
- IntelliJ IDEA→ razvojno okolje.
- Postman→ orodje za testiranje REST API zahtevkov
- GitHub→ orodje za nadzor razliˇcic in upravljanje z repozitoriji.


