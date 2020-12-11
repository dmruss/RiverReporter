# RiverReporter
A native Android app giving the user access to river locations and information

## Project Description

This project focuses on creating an Android application to connect the user with 
information about rivers in Colorado.  This includes access to river flow rates, weather 
conditions, and sampling locations.  Using information from the US Geological Survey, 
this application will be a useful resource for anglers or boaters who are looking for 
current conditions on Colorado rivers. 


   ## Application Activities: 
                  1) Mapping USGS river sampling locations using the Google Maps API 
                  2) Reporting current flow rates (cfs) of rivers in Colorado using the USGS REST 
                  web services 
                  3) Reporting current and projected weather for the sampling locations using the 
                  AccuWeather api 
                  
 
On opening the app, the first view shows a google map of Colorado with markers 
showing the 338 USGS river sampling stations in the state[2].  These points are 
generated from a csv available on the USGS website [1].  There is an action bar at the 
top with a menu option showing favorites and a map alignment button.  The user is able 
to zoom in and out of the map, pan, and search by text to find the location that they are 
looking for.  


The user will tap on the marker of their choice which will cause a bubble to pop 
up displaying the location’s name.  If this is the location the user would like information 
for, they may click on the bubble which will take them to a new view window.  This view 
window will display current flow rates in cfs for that station as well as current and 
projected weather for that station location.  


While in this station view, the user will also have the option to tap a star which 
will assign the station as a ‘favorite.’  This will save the station in the favorites menu for 
easy access to that information.  The user will be able to hit a back button to return to 
the map view. 



## Requirement Specification

1. Mapping USGS river sampling locations using the Google Maps API 


      a. Each river sampling location will be shown with a pin on a map
          
      b. The pin will be tap-able to show the name of the location and prompt the 
            user if they would like more information in a new view 
            
            
2. Reporting current flow rates (cfs) of rivers in Colorado using the USGS REST 
    web services
    
   a. This will show the current flow rate of the river at the selected sampling 
station

   b. This will be shown in the more information view 
            
            
3. Reporting current and projected weather for the sampling locations using the 
Open weather map api 

   a. This will show the current and predicted weather for the selected sampling 
station 

   b. This will be shown in the more information view 
            
            
4. Saving river sampling stations as favorites 

    a. The option to save as a favorite will be shown as a star in the more 
information view 

    b. The favorites list will persist between different sessions 
    
    
## Technical Descriptions

The main activity of River Reporter is implemented 
using the Android Google Maps api.  On opening the 
app, the user will see a familiar layout and color scheme 
of the Google Maps app.  The GoogleMaps class is used 
to create a map fragment in the main xml file, override 
the onCreate, onResume, and onMapReady functions in 
the MapsActivity, call methods for marker creation, and 
move the camera.  


The action bar was created by extending 
AppCompatActivity in the MapsActivity.  The menu was 
added using a menu view and overriding the 
onCreateOptionsMenu and onOptionsItemSelected 
methods.  

The search bar was implemented using an EditText 
field view with a setOnEditorActionListener which is 
initialized when the map is created.  The text entered into 
the search bar is then passed into Android’s geocoder 
library as a getFromLocationName search. 
The markers are placed during the onMapReady 
method using data from a local csv.  The csv file was 
downloaded from the US Geological Survey’s website 
and contains information for 337 Colorado rivers[3].  The 
csv is kept in the asset folder of the app and read into an 
River object (using the CSVReader class) then placed in 
an ArrayList.  It is then placed on the map as a marker 
using the longitude, latitude, and name. 


Once an area of interest is chosen by panning or by 
searching, the user may tap on the markers in the area to 
see the names of the rivers.  These names are the exact 
titles used by the USGS for cataloging their sampling 
data.  Because the only information stored in the 
markers are the longitude, latitude, and name; I have 
implemented a search method which takes the selected 
marker’s name and scans the full ArrayList to find the 
corresponding River object.  This River object contains 
the id which is necessary for making api calls. 
 
 
The title above the marker is programmed with an on 
click listener which will open the dialog fragment 
showing that river’s current information.  The results of 
each api call are passed into the dialog fragment 
constructor as objects.  The information is then assigned 
to each text view as the fragment is constructed.  The 
star in the top right is a toggle button which will add the 
river to the favorites list when selected and the fragment 
is closed, or remove the river from the favorites list when 
unselected and the fragment is closed.  Adding a river to 
the favorites list involves inserting it into a favorite rivers 
ArrayList and a SQLite database. 


To decrease the likelihood of returning null values for the discharge and weather, the 
api calls to the USGS api and AccuWeather api[4] are made once the user clicks on the 
marker to display the river’s name.  Volley is used to create the request and add it to the 
request queue.  Both api’s return JSON objects which are then parsed for the flowrate 
and weather forecasts.  The call to the USGS is a single api call which does not require 
an api key, only a river ID.  The AccuWeather call requires an api key as well as a two 
sequential api calls.  The first is a call using the latitude and longitude which returns a 
location key.  That location key must then be passed into the second request for the 5 
day forecast.  The calls were made synchronous to avoid returning null values for the 5 
day forecast.  The days of the week were obtained using the java.time.LocalDate and 
java.time.DayOfWeek libraries.  The LocalDate object is found using LocalDate.now() 
for the current day and 4 days in the future.  That LocalDate object is then converted 
into a day of the week using the getDayOfWeek() function which returns a day of week 
object. 


Opening the menu and selecting 
favorites will then open the Favorite Rivers 
dialog fragment.  These rivers are loaded 
each time a new river is added to the 
favorites list and each time the application 
is started.  The view is created using a 
recycler view and River adapter.  Each river 
also has an onClickListener which will 
produce its current dialog fragment when 
clicked. 


To avoid returning null values in the single river 
dialog fragment,  the api calls are made for each river 
as soon as the Favorite Rivers 
dialog fragment is opened.  The 
results of each call are stored in 
separate ArrayLists and are then 
assigned to the view once a river 
is clicked.  This will produce an 
identical dialog fragment to the 
one seen using the markers, 
except the star will be selected. 


An interesting problem I ran into while implementing the SQLite database is that 
the id field, a string of 8 - 15 digits, would be converted to scientific notation each time it 
was stored in the database.  This is an issue because many of the id’s begin with 
leading 0’s which were removed during the conversion causing their api calls to fail. 
While I was unable to find a way to stop the scientific notation conversion, I 
implemented a work around which pulls the id from the original ArrayList River object 
and uses that id to make the api call.  The downside to this workaround is that it 
requires a for loop which could slow down the program in the event that the user has 
created an extremely large favorites list. 


Sources​: 
[1] 
https://maps.waterdata.usgs.gov/mapper/nwisquery.html?URL=https://waterdata.usgs.g
ov/co/nwis/current?type=flow&group_key=basin_cd&site_no_name_select=siteno&form
at=sitefile_output&sitefile_output_format=xml&column_name=agency_cd&column_nam
e=site_no&column_name=station_nm&column_name=site_tp_cd&column_name=dec_l
at_va&column_name=dec_long_va&column_name=agency_use_cd 
[2]​https://developers.google.com/android/reference/com/google/android/gms/maps/Goo
gleMap 
[3]​https://waterservices.usgs.gov/rest/DV-Service.html 
[4]​https://developer.accuweather.com/apis 
