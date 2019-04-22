#include <Key.h>
#include <Keypad.h>


#include <SoftwareSerial.h>
char data = 0;                //Variable for storing received data
bool initial = true;

const byte ROWS = 4;
const byte COLS = 3;

char hexaKeys[ROWS][COLS] = {
    {'1', '2', '3'},
    {'4', '5', '6'},
    {'7', '8', '9'},
    {'*', '0', '#'}
  };

byte rowPins[ROWS] = {9, 8, 7, 6};
byte colPins[COLS] = {5, 4, 3};
Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);
SoftwareSerial btSerial(10,11);
int index = 0;
String password = "";
char* buf;

void setup()
{
  btSerial.begin(9600);
  Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
  pinMode(13, OUTPUT);        //Sets digital pin 13 as output pin
}


void loop()
{
  char customKey = customKeypad.getKey();
  
  if (customKey){
    if (customKey == '#') {
      buf = (char*) malloc(sizeof(char)*(password.length() + 1));
      password.toCharArray(buf, password.length()+1);
      btSerial.write(buf);
      free(buf);
      Serial.println("Sending: " + password);
      password = "";
    } else {
      password += customKey;
      Serial.println(password);
    }
  }
  if (btSerial.available() > 0) // Send data only when you receive data:
  {
    data = btSerial.read();      //Read the incoming data and store it into variable data
    if (data == '1')           //Checks whether value of data is equal to 1
      digitalWrite(13, HIGH);  //If value is 1 then LED turns ON
    else if (data == '0')      //Checks whether value of data is equal to 0
      digitalWrite(13, LOW);   //If value is 0 then LED turns OFF
  }

}
