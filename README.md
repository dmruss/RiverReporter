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
