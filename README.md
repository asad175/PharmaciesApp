# PharmaciesApp
Language: JAVA
Architectural Design Pattern: MVP

External APIs/SDK:
1. Google Maps SDK 
2. Google Volley SDK for network calls
3. Google Places search API is called through Volley
4. Google Directions API is called through Volley

But I didn't use any other third party SDK or libraries, except taking a small code snippet from stackoverflow to parse Polyline points provided by the Google Directions API


Details:

This App only contains 1 Activity named "MapHomeActivity". It has its presenter named "MapHomePresenter" in which business logic is implemented. Presenter updates Activity component through the Interface named "IMapHomeActivity".

Instead of these, some other Helper classes are created, like "LocationHelper" to observe user location and send updates to presenter through an interface. Same is the case with another Helper class named "PermissionHelper".

Another Utility class "Parser "is used to parse JSON objects into required formatted objects.

All API calls are being done through "ApiManager" class, which works as a network layer.
