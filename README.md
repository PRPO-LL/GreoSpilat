### TODO

- [x] Create a database
- [x] Implement entity and persistance modules for userEvents
- [ ] DTO for transfering User via user_id to the database
- [ ] Communication via API with the Event microservice
- [ ] test

## Greo spilat ##

*Luka Kalšek, Luka Bele*, Luka^2
*skupina 10*

Aplikacija, ki omogoča mreženje med uporabniki z namenom skupnega športanja. 
Uporabna za timske športe kot so košarka, nogomet, odbojka, pa tudi individualne kot so tek, plavanje, plezanje,... saj je vse slajše v družbi. 

Vsak uporabnik ima svoj profil, preko katerega lahko objavlja pozive za športanje in izrazi pripravljenost za udeležbo ob določenih terminih, na kar se lahko odzovejo drugi. Poleg tega lahko ustvarja tudi športne dogodke, na katere se drugi uporabniki prijavijo in pridružijo, kar omogoča enostavno organizacijo skupnih aktivnosti.

**Primeri uporabe**

- **Ustvarjanje športnih dogodkov**: Uporabniki lahko ustvarijo dogodke, kot so "3-na-3 košarka" ali "pohod na Donačko goro," na katere se lahko prijavijo drugi člani.

- **Pozivi za športanje**: Uporabniki lahko objavijo svoje termine za športanje, da jih drugi povabijo v ekipo ali na dogodek.

- **Zemljevid dogodkov**: Aplikacija ponuja zemljevid, kjer so prikazani vsi prihajajoči športni dogodki v bližini, kar omogoča lažje iskanje lokalnih aktivnosti.

- **Nastavitve športnih preferenc**: Uporabniki lahko nastavijo svoje športne preference in določijo, za katere športe želijo prejemati povabila ter obvestila, kar zagotavlja bolj personalizirano izkušnjo.

Aplikacija poenostavi organizacijo in povezovanje športnih navdušencev ter ustvarja skupnost, kjer se lahko uporabniki srečujejo in rekreirajo skupaj.

**Mikrostoritve**
- Uporabniški račun
- Usvarjanje dogodkov / lastnih pobud za športanje
- Zemljevid športnih dogodkov
- Obveščanje o novih dogodkih
- MojProfil
- Nastavitve
- Admin panel

##### Opis mikrostoritev #####
###### Uporabniški račun ######

Glavna opravila te mikrostoritve so kreacija, vračanje, posadabljanje in brisanje uporabniških računov. Prav tako ima storitev svojo podatkovno bazo kjer hrani vse atribute. Preko internega APIja komunicira s drugimi storitvami kot so; zemljevid športnih dogodkov, moj profil, admin panel,...

Mikrostoritev bo preko Kafke ali RabbitMQ omogočala sporočanje drugim mikrostoritvam, ko je uporabnik "online" in pripravljen za športanje. Viden bo tudi na zemljevidu.

###### Usvarjanje dogodkov ######

Mikrostoritev skrbi za uporabniško kreirane dogodke, jih dodaja, spreminja in briše. Sodeluje tesno z mikrostoritvijo za obveščanje o dogodkih in povabilih ter zemljevidom športnih dogodkov. Možna je tudi izvedba ponavljajočih se dogodkov (nedeljska košarka). Storitev ima prav tako svojo podatkovno bazo interni API preko katerega komunicira s drugimi mikrostoritvami. 

###### Zemljevid športnih dogodkov ######

Zemljevid športnih dogodkov je zadolžena za prostorsko in lokacijsko prikazovanje dogodkov. Uporabnikom omogoča da dogodke najdejo in z njimi interaktirajo. Integrirala bo mapping API (GoogleMaps, OpenStreetMaps) in skrbela za querije in filtre iskanja ter vizualizacijo. 

###### Obveščanje o novih dogodkih in povabila ######

Mikrostoritev ki skrbi za obveščanje in povabila bo zadolžena še za odgovore na povabila, reagirala bo na nove evente, updejte ali uporabniške vnose. Push notifikacije v mobilni verziji*.
Omogočala bo tudi časovno razporejanje obvestil in sledenje komu so bila obvestila poslana in njihov odziv na njih. 

###### MojProfil ######

Mikrostoritev mojProfil bo zadolžena ze upravljanje z uporabniškim profilom znotraj sistema. Osredotočala se bo na spreminjanje obstoječega profila, nastavljanje preferenc, radiusa zaznavanja,...
Komunicirala bo z isto bazo kot mikrostoritev za uporabniške račune.

###### Admin panel ######

Admin panel administratorjem omogoča upravljanje in nadzor sistema, moderiranje objav in profilov. Namenjena je dostopu do ostalih mikrostoritev za potrebe moderiranja in spreminjanja. 


###### Nastavitve ######

Kakor admin panel upravlja sistem na ravni administratorja, ga nastavite na ravni uporabnika. Vsak si lahko konfigurira svojo uporabniško izkušnjo s nastavitvami jezika, obvestil, teme, privatnosti,... 

#### Orodja ######
Nekatera so navedena že v shemi
- Meaven
- IntelliJ
- jetty
- JAX-RS
- Kubernetes 
- KumuluzEE
- postgreSQL
- Postman
- Kafke ali RabbitMQ


#### Arhitektura #####
![arhitektura](arhitektura_spil.png)

