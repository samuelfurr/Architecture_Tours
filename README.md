# Architecture_Tours
App to provide automated architecture tours

The app is still in beta, and currently does not provide architecture tours -- what it does do is pull information about architecturally significant buildings near the user and displays that information.  The app pulls pictures of buildings and their names from the Google Places API, and then pulls the descriptions of those buildings from Wikipedia.  It displays a list of those buildings to the user, allowing them to select items in the list for more information about the buildings.  It also allows the user to view the buildings on a map (using the Google Mpas API), and to get directions to the buildings using the Google Maps app.

Necessary Updates (In order of importance):

  - Currently the App doesn't check for location permission from the user - this means the app won't work after install unless the user manually allows the app location permissions.  This is an easily remidied issue, but in interests of time it hasn't yet been implemented.
 
  - The app needs a better way to query Wikipedia for information about places.  Currently the app can only display results that have Wikipedia aticles whose names exaclty match the place names delivered by Google.  This means the app doesn't display as many results as is ideal.
 
  - The app needs to be faster.  It currently takes a long time to recive all the data about buildings: their pictures, their location, their descriptions, and parse all the data.  This process needs to be sped up.
  
  
  - The app needs to save building data so that it can provide tours to the user.
  
  - The app needs to actually provide the user with an automated tour route!  This would preferably involve some sort of machine learning in order to decide what route the user would like best.
  
  - Various UX/UI updates.  Though the app pretty much follows material design at this point, it is relatively barebones.  It would be nice for the user to have the ability to favorite places, and the app just needs a bit more polish.
  
Thats pretty much it!
