#include <SoftwareSerial.h> // TX RX software library for bluetooth
#include <Servo.h> // servo library
#include <EEPROM.h>
#define led 13

    SoftwareSerial SerialBLE(10, 11); // RX, TX

    char servopos;
    String v;
    String s;
    
    #define RELAY_PIN_1       2   //D2
    #define RELAY_PIN_2       3   //D3
    #define RELAY_PIN_3       4   //D4
    #define RELAY_PIN_4       5   //D5 
    Servo regulator; 
    
    volatile int relay1State;
    volatile int relay2State;
    volatile int relay3State;
    volatile int relay4State;
    unsigned int regulatorPos;

void setup() {
  // put your setup code here, to run once:
    regulator.attach(9); // attach servo signal wire to pin 9
    pinMode(RELAY_PIN_1, OUTPUT);
    pinMode(RELAY_PIN_2, OUTPUT);
    pinMode(RELAY_PIN_3, OUTPUT);
    pinMode(RELAY_PIN_4, OUTPUT);
    pinMode(led, OUTPUT);
    
    relay1State = EEPROM.read(0);
    relay2State = EEPROM.read(1);
    relay3State = EEPROM.read(2);
    relay4State = EEPROM.read(3);
    EEPROM.get(4, regulatorPos);
   
    digitalWrite(RELAY_PIN_1, relay1State);
    digitalWrite(RELAY_PIN_2, relay2State);
    digitalWrite(RELAY_PIN_3, relay3State);
    digitalWrite(RELAY_PIN_4, relay4State);
    regulator.write(regulatorPos);

    SerialBLE.begin(38400);   
    Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:

 if(SerialBLE.available()> 0 ) // receive number from bluetooth
  {
     servopos = ((byte)SerialBLE.read());
     
     if(servopos == '1'){
     s = SerialBLE.readString();
    
     if(s == "F"){
     relay1State=1;
     digitalWrite(RELAY_PIN_1, relay1State);
     }
    
     if(s == "O"){
     relay1State=0; 
     digitalWrite(RELAY_PIN_1, relay1State);
     }
    
     if(s == "A"){ 
     relay2State=0;
     digitalWrite(RELAY_PIN_2, relay2State);
     }
     
     if(s == "B"){
     relay2State=1;
     digitalWrite(RELAY_PIN_2, relay2State);
     }
     
     if(s == "C"){
     relay3State=0;
     digitalWrite(RELAY_PIN_3, relay3State);
     }
     
     if(s == "E"){
     relay3State=1;
     digitalWrite(RELAY_PIN_3, relay3State);
     }
    
    if(s == "P"){
    relay4State=0;
    digitalWrite(RELAY_PIN_4, relay4State);
    }
    
    if(s == "D"){
    relay4State=1;
    digitalWrite(RELAY_PIN_4, relay4State);
    } 
  }
   
    if(servopos =='2'){
    v = SerialBLE.readString();
    regulatorPos = map(v.toInt(), 0, 100, 180, 0);
    regulator.write(regulatorPos);
    }
  }

    if (analogRead(A3)<900){
    EEPROM.write(0,relay1State);
    EEPROM.write(1,relay2State);
    EEPROM.write(2,relay3State);
    EEPROM.write(3,relay4State);
    EEPROM.put(4,regulatorPos);
    digitalWrite(led,HIGH);
    }
    else digitalWrite(led,LOW); 
    }
