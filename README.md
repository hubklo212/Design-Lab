# **Design Laboratory - group 5/team 4**

## *Authors:* 
Hubert Kloc,

Adrian Cich

## *Project Topic*: 
Sunset/Fixed Time Controlled Lamp (WiFi communication with an android application)

## *Description*: 
We intend to design lamp that connects to a smartphone via WiFi. The smartphone hosts an application fetching data from the https://sunrisesunset.io/ API and sends the extracted relevant data to the ESP8266 module. The connection occurs periodically to update the data (maybe some day). The lamp will turn on after sunset. There also is a possibility to enter a fixed time in the app at whitch it will turn on. There's potential for expanding the application with additional functions like brightness control, color changes, etc. as we used FastLED to control the LED tape.

## *Equipment Used*:

### NodeMCU v3 - ESP8266 development kit: 
https://www.espressif.com/sites/default/files/documentation/0a-esp8266ex_datasheet_en.pdf 
https://www.espressif.com/sites/default/files/documentation/esp8266-technical_reference_en.pdf

NodeMCU Pinout

![NodeMCU-ESP8266-Pinout](https://github.com/hubklo212/Design-Lab/assets/94645329/9a01b7e8-d913-4a53-9bd5-96420af91eca)

### LED Tape
WS2812B - classic addressable LED strip powered with DC5V. We used 1m of it (60LEDs/m). It came in with the connector soldered to it.

## *Software used*:
Android Studio - application for android (the code i wrote is in app/src/main/java/com/hubklo212/sunsetled)

Visual Studio Code with PlatformIO - ESP8266 application (code in different repository: "ESP8266sunsetLED" (https://github.com/hubklo212/ESP8266sunsetLED))

## *Quick setup instruction*:
1. Flash the ESP8266 with the program. Specify the WiFi network parameters of yours. Remember the IP address of the board (shown in serial or you can check it in the router/hotspot settings) (you can also change the duration of LEDs being turned on).
2. Connect the board to the LED strip using pin D4 for data and in our case Vin and GND for power supply; powering the board with micro-USB cable is the easiest, however for longer LED strips it may be not enough to power each LED properly.
3. Install the app on your phone using .apk (app/release/sunsedLED.apk) or download whole project.
4. Enter IP address of the board and press "Update manually" to get the sunset time or type in fixed time in the text field at the top of the screen.
5. Click "Send".
6. The board will calculate the time between now("now" as for the moment it receives the data from app) and the time parsed by app. After this time the LEDs should turn on.
7. When the board is on and connected to your WiFi you can repeat the process how many times you want by sending the sunset time or fixed time value.
Enjoy!

## *Work Breakdown by Weeks*:

19.10-02.11 - Procuring suitable equipment/understanding the microcontroller's operation and structure.

02.10-09.11 - Searching for materials to learn from and playing with example projects/waiting for ordered components; hardware documentation for the project.

09.11-23.11 - Learning Kotlin for Android app disign (Hubert) + Learning to control basic functions of the acquired equipment (both of us).

23.11-21.12 - Writing the essential part of the required application code.

21.12-02.01 - Holiday break.

04.01-18.01 - Writing code for the chip and estabilishing a connection between the app and the lamp.

18.01 - Project submission.
