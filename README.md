# Nom du Projet4

   - League Of Pokemons

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white) ![SQLite](https://img.shields.io/badge/SQLite-003B57?style=flat&logo=sqlite&logoColor=white) ![Markdown](https://img.shields.io/badge/Markdown-000000?style=flat&logo=markdown&logoColor=white)    

## Auteurs

- Groupe D112
- g63491 Thibault Schellekens
- g63054 Mocircioiu Prodea Marian

## Description du Projet

This project is about a Pokemon game where a player can select his favourite cards from a collection of pokemons, create a team, and battle against other trainers.
Players can scan and add their own cards to the collection, allowing them to recruite said cards.
Battles get harder and harder. With each battle vanquished, the player shall be granted the ability to recruite even MORE powerful pokemons .
Saving and loading a game is also possible.
The goal is to win battles, get stronger pokemons, and then slay the final Boss.

### Handling Collection

The collection is a set of images pre-downloaded into the game files, from which a player can pull his desired pokemons to create a team. 
Once a pokemon is chosen and the player clicks(add to team) button, first we check if this card is already in the cache, meaning it has already been OCR scanned. If it isnt -> we add it to the cache and then add the pokemon to the players team, if it has already been scanned -> just add to team.
The collection will have multiple *TIERS* and will be sorted from lowest to highest tier.

### Handling importing of cards
IF the player so desires to scan one of his OWN cards, he shall then press on the import button -> allowing the player to select a certain image from his computer to be scanned, and of course, added into the collection of pokemons. <- UNSURE/COULD JUST ADD CARD IMAGE MANUALLY TO STARTER_SET.

### Handling pokemons
Once a card scanned, a Pokemon object shall be created with that card's |TIER|TYPE|NAME|HEALTH|IMAGE| and then added to the cache.

### Handling Tiers
After every battle won up to stage 5, the player unlocks a new tier of Pokemons
- Tier 5 -> Pokemons with max 100hp
- Tier 4 -> Pokemons with max 140hp
- Tier 3 -> Pokemons with max 180hp
- Tier 2 -> Pokemons with max 220hp
- Highest Tier -> Pokemons with max 300hp

### Handling players team
The players team will be stored as a list of Pokemon objects.
ONLY TEAMS OF 3 POKEMONS ALLOWED
We assume the order of pokemons appearing in battle is the sale as they are stored in this list.


more information soon


## Diagramme de Classe

Le diagramme de classe ci-dessous illustre la structure du modèle de l'application. 

![Diagramme de classe](./images/diagramme_classe.png)

## Choix de l'Architecture

L'architecture retenue pour ce projet est _model-view-controller_. 


## Plan de Tests Fonctionnels

Les tests fonctionnels élémentaires pour le projet sont les suivants :

- ...

## Calendrier Hebdomadaire des Tâches

### Semaine 1 - 6H

| Qui       | Description  
|--         | --
|Tous       | Analyse du projet.
|Alice      | Initialisation de Git
|Bob        | Configuration de l'environnement de développement

### Semaine 2 - 6H

| Qui       | Description  
|--         | --
|Alice      | Implémentation de la partie OCR
|Bob        | Implémentation de la base de données


### Semaine 3 - 6H

...


## Installation et utilisation

Pour utiliser l'application, suivez les étape suivantes : 

1. Clonez ce repository :
   ```bash
   git clone ...
   ```

2. Démarrez le projet en exécutant la commande 
   ```bash
   mvn ...
   ```


## Problèmes connus de l'application

Lors de la réalisation des tests fonctionnels, nous avons constatés les problèmes suivants : 

- ...

## Retrospective

Nous avons constaté les différences suivantes entre le diagramme de classes imaginés au départ et l'implémentation réalisée : 

- ...


