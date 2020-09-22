# pushup_reminder

An open source flutter app, that reminds the user 10 times in a day to do push ups.

## Getting Started

This is an open source project and will be uploaded to the play store once it is good for deployment.


The app works like this: 
    On even days of the week namely, tuesday, thursday and saturday.
    You wake up and press the "start today" button. (the only button).
    then the app calcualtes the time from when it is pressed upto 11pm (or 2300 for europeans).
    and then divides it into 10 equal ranges. then an alarm is set for every range.
    each time the old alarm rings a new alarm is set. and this cycle continues until 10 alarms have been set and rung.
    the part which is buggy is the alarm section. which is very complicated.
    
 there is a dummy counter on the main screen which is just used to remember how many push ups you have to do that day.
    
 on odd days the button is greyed out and not pressable.

A complete structure will be made soon by me.
The front end is built in Flutter.
The background services are built in native android using java. plugins are only used for shared preferences.
iOS version has not been developed yet.
