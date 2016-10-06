 Demo from Outlook android.
 Realize the following functions:
 
 1) Calendar View
 
    "The calendar view is a freely scrolling view that allows the user to browse between days of the year. 
     Tapping on a date should update the information displayed in the Agenda view."

 2) Schedule View (just means agenda View in my project)
 
     "The agenda view is a freely scrolling view that allows the user to browse events in chronological order. 
     Moving between dates should update the information displayed in the Calendar view."

 3) Show today& tomorrow 's Weather data info from Yahoo public API 

 I  test my app in xiaomi2s(android4.1) and oppo R9(android5.1), if you have any questions, please contact me .
 All my weather info is ShenZhen's weather data that fetched from Yahoo. About weather day info ,because of Yahoo public API, 
 It have some limits,  I had show weather data as much as possible.for example, I can not get afternoon's cloud sate's image
 but just all day, just One image I can get.
 I can not get tomorrow's average temperature but just top temperature  and low temperature .
 
 In my app, if click Email Button(a blue button in right &bottom) , it will toast weather info about ShenZhen. 
 such as temperature  and cloud state.
 
 About network ,its use Retrofit & Rxjava , in UI , RecycleView be used at most .
 So this project is a demo for excersize these points.
