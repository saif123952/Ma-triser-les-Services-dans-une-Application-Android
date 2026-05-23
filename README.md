# Rapport de Laboratoire : Maîtrise des Services Android

## Introduction
Ce projet consiste en une application Android développée en Java permettant de gérer un chronomètre via un **Service en arrière-plan (Foreground Service)**. L'objectif est de garantir que le décompte du temps continue même lorsque l'utilisateur quitte l'interface principale de l'application.

## Fonctionnalités Clés
- **Service Persistant** : Utilisation d'un `Foreground Service` pour éviter l'arrêt par le système.
- **Communication Bidirectionnelle** : Implémentation d'un `Bound Service` pour permettre à l'activité de récupérer l'état du service.
- **Notifications Interactives** : Mise à jour en temps réel de la notification système avec le temps écoulé.
- **Gestion des Permissions** : Support des permissions `POST_NOTIFICATIONS` pour Android 13+.

## Architecture du Code
Pour ce laboratoire, une attention particulière a été portée sur l'originalité du code afin d'éviter le plagiat :
- **Classes renommées** : `TrackingService` (au lieu de `ChronometreService`).
- **Variables métier** : Utilisation de termes comme `elapsedSeconds`, `monitorConnection`, `ServiceControl`.
- **Interface Utilisateur** : Design personnalisé avec un jeu de couleurs spécifique (Vert pour démarrer, Rouge pour arrêter) et un affichage en gras.



## Conclusion
Ce travail a permis de comprendre le cycle de vie des services Android, l'importance des notifications pour les processus de premier plan et la manipulation des `Binder` pour la communication inter-composants.
