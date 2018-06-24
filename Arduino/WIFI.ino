#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <SPI.h>
#include <SD.h>


#define DEBUG true


const int buttonpin=10;
const int ledpin=9;
int buttonCounter=0;
int buttonStatus=0;
int i=0;
int j=0;
int p=0;

File myFile;
TinyGPSPlus gps;

SoftwareSerial esp8266(6,5);

SoftwareSerial serial_connection(2, 3);//GPS //RX=pin 10, TX=pin 11


//This is the GPS object that will pretty much do all the grunt work with the NMEA data

// ----------------------------------------------------------------------

void Read();
void startWifi();
void sendRequest();
void GPS();
void sdSearch(float,float);
void  fetch(double,double,int,int);
// -----------------------------------------------------------------------

String sendData(String command, const int timeout, boolean debug)
{
    String response = "";
    
    esp8266.print(command); // send the read character to the esp8266
    
    long int time = millis();
    
    while( (time+timeout) > millis())
    {
      while(esp8266.available())
      {
        
        // The esp has data so display its output to the serial window 
        char c = esp8266.read(); // read the next character.p
        response+=c;
      }  
    }
    
    if(debug)
    {
      Serial.print(response);
    }
    
    return response;
}

///////////////////////////////

void startWifi(){

  digitalWrite(ledpin,HIGH);
  sendData("AT+RST\r\n",2000,DEBUG); // reset module
  digitalWrite(ledpin,LOW);
  sendData("AT+CWMODE=2\r\n",1000,DEBUG); // configure as access point
  digitalWrite(ledpin,HIGH);
  sendData("AT+CIFSR\r\n",1000,DEBUG); // get ip address
  digitalWrite(ledpin,LOW);
  sendData("AT+CIPMUX=1\r\n",1000,DEBUG); // configure for multiple connections
  digitalWrite(ledpin,LOW);
  sendData("AT+CIPSERVER=1,80\r\n",1000,DEBUG); // turn on server on port 80
  digitalWrite(ledpin,HIGH);
  //  sendData("AT+GMR\r\n",1000,DEBUG); // turn on server on port 80
//  sendData("AT+CIOBAUD=9600\r\n",1000,DEBUG); // Change Baud Rate
// AT+CIOBAUD=9600
  
  
}

////////////////////////////////////

void(*resetFunc)(void)=0; 

//=================================================
int flag1=0;

////////////////////////////////////////////////////////

void setup()
{
  Serial.begin(9600);
  pinMode(buttonpin,INPUT);
  pinMode(ledpin,OUTPUT);
  
  if (!SD.begin(4)) { 
    Serial.println("initialization failed!");
    return;
  }
  
  Serial.println("SD done.");
//  serial_connection.begin(9600);
   
  esp8266.begin(9600); // your esp's baud rate might be different

//  startWifi();

  Serial.println("initialization done.");
  Serial.println(i);
//  sdSearch(1,1);
}
///////////////////////////////////////

void loop()
{  
      delay(5000);
      
      buttonStatus=digitalRead(buttonpin);
        
        if(buttonStatus==HIGH){
          startWifi();
          Read();
        }
        else{
          digitalWrite(ledpin,LOW);
        }
        digitalWrite(ledpin,LOW);
      GPS();
    
//  if(esp8266.available())
//  {
//        buttonStatus=digitalRead(buttonpin);
//        Serial.println(buttonStatus);
//        if(buttonStatus==HIGH){
//          digitalWrite(ledpin,HIGH);
//        }
//        else{
//          digitalWrite(ledpin,LOW);
//        }
    
//    
//    char c=esp8266.read();
//    
//    
//    if(c=='s'){
//      Serial.println("start Wifi");
//      Read();
//    }
//    else if(c=='w'){
//      Serial.println("W is start");  
//      GPS();
//    }
//    
//  }
//      sdSearch();
}
void sdSearch(float latf,float logf){
  
  
  String temp="";
  int a,b;
  Serial.println("in sd");
  File myFile1 = SD.open("test.txt");
  if (myFile1) {
    Serial.println("test.txt:");
    
    char lat[10]={'\0'};
    
    while (myFile1.available()) {
      char c=myFile1.read();
          if(c!='\n'){
            if(j>=4 && j<=13 ){
              temp+=c;
              j++;
            }
            else{
              j++;
            }
          }
          else
          {
            temp.toCharArray(lat,10);
//            Serial.println(temp);
//            Serial.println(atof(lat)*10000);
            if(atof(lat)>=latf){
              j=0;
//              Serial.println("in sd "+i);
              return ;              
            }
            i++;
            temp="";
            j=0;
          }
      
      }
       
  }else{
    Serial.println("error opening test.txt");
  }
  myFile1.close();
}


void sendRequest(){
  esp8266.println("AT+CIPMUX=1");
  delay(1000);
  esp8266.println("AT+CIPSTART=4,\"TCP\",\"192.168.1.19\",2640");
  delay(1000);
  String cmd = "GET /test.html HTTP/1.1";
  esp8266.println("AT+CIPSEND=4," + String(cmd.length() + 4));
  delay(1000);
  esp8266.println (cmd);
  delay(1000);   
}


int flag=0;


/////////////////////////////////////////////////////////////////////

double a=0;
double b=0;
int flagg=0;
int counter=0;


void GPS(){

    double latg=0,latg1=0;
    
    double logg=0,logg1=0;
    
    serial_connection.begin(9600);
  
  while(1) {
//      sdSearch(70.7681603,234);
//      Serial.println(serial_connection.available());
//      delay(1000);
      while(serial_connection.available())//While there are characters to come from the GPS
      {
          gps.encode(serial_connection.read());//This feeds the serial NMEA data into the library one char at a time   
      }
      if(gps.satellites.value()!=0)
      {
          
          latg1=gps.location.lat();
          logg1=gps.location.lng();
          if(latg1==latg && logg1==logg)
          {
//            Serial.println("123456");
//            continue;
          
          } 
                   
//          Serial.println(latg1, 15);
      
        if(flagg==0){
          
          a=latg1;
          flagg=1;
          sdSearch(latg1,logg1);
          continue;
        }
        else{
          b=latg1; 
        }
      
      if(a<b)
      {
//          fetch(b+(gps.speed.mph()/1000000),gps.location.lng(),1);
          fetch(b,gps.location.lng(),1,23);
      }
      else{
//          fetch(b+(gps.speed.mph()/1000000),gps.location.lng(),0);
            fetch(b,gps.location.lng(),0,23);
      }
      
//      if(counter!=0)
//      {
//        digitalWrite(ledpin,HIGH);
//        counter=0;
//      }
//      else{
//           digitalWrite(ledpin,LOW);
//      }
           
//          Serial.println("Longitude:");
//          Serial.println(gps.location.lng(), 10);
//          Serial.println("Speed MPH:");
//          Serial.println(gps.speed.mph());
//          Serial.println("Altitude Feet:");
//          Serial.println(gps.altitude.feet());
//          Serial.println("");
          
//          delay(1000);
        
      
        latg=latg1;
        logg=logg1;

        a=b;
      }
      
    } 
    
}

void  fetch(double latt,double logg,int up,int sp){

  
    j=0;
    int k=0;
    
    File myFile1 = SD.open("test.txt");
    String temp="";
    
        
        char lat[13]={'\0'};

//        myFile1.seek((k*38)); 

        while (myFile1.available()) {
        
        char c=' ';

          
          while(c!='\n'){
            c=myFile1.read();
            temp+=c;
           }
           
            temp.substring(4,16).toCharArray(lat,13);
            
//            Serial.println(fabs((latt-atof(lat))),20);

//            Serial.println(lat);
            
            if((myFile1.size()/38<=k)){
                return;
            }
            float l=atof(lat);
            temp.substring(18,30).toCharArray(lat,13);
            
            float o=atof(lat);

            Serial.println(calc_dist(l,o,latt,logg));

//            if(calc_dist(latt,logg,l,o)<15){
//                                    Serial.println(temp.substring(0,3));    
//              }
             
//            if(fabs((latt-atof(lat)))<0.00003){
////              temp.substring(4,13).toCharArray(lat,10);
//              
//              temp.substring(14,25).toCharArray(lat,10);
////              o=atof(lat);
//              Serial.println(fabs((logg-atof(lat))),20);
////                
//                if(fabs((logg-atof(lat)))<0.00010){
//                
//                    Serial.println(temp.substring(0,3));    
//                }
//            }
//            else{
//              Serial.println("dsd");
//              }
//            

            temp="";
            k++;
            
            continue;
            
            Serial.println(latt,20);
            if(atof(lat)<0){return ;}

            
            
            if(latt>=atof(lat)){
              Serial.println("found");             
              counter++;
    //               
    //          if(up==1){
    //              k--;
    //            Serial.println("up");  
    //          }
    //          else if(up==0){
    //              k++;
    //              Serial.println();
    //          }  
                return;          
                temp="";   
            }
            else{
              Serial.println("not   found");
              
            }     
      }
    
    myFile1.close();
    
}
///////////////////////////*************
float calc_dist(float flat1, float flon1, float flat2, float flon2)
{

float dist_calc=0;
float dist_calc2=0;
float diflat=0;
float diflon=0;

//I've to spplit all the calculation in several steps. If i try to do it in a single line the arduino will explode.
diflat=radians(flat2-flat1);
flat1=radians(flat1);
flat2=radians(flat2);
diflon=radians((flon2)-(flon1));

dist_calc = (sin(diflat/2.0)*sin(diflat/2.0));
dist_calc2= cos(flat1);
dist_calc2*=cos(flat2);
dist_calc2*=sin(diflon/2.0);
dist_calc2*=sin(diflon/2.0);
dist_calc +=dist_calc2;

dist_calc=(2*atan2(sqrt(dist_calc),sqrt(1.0-dist_calc)));

  dist_calc*=6371000.0; //Converting to meters
//Serial.println(dist_calc);
return dist_calc;



}
////////////////////////////////////////////////*******************
int convert(float lat1,float log1,float lat2,float log2){

       double nRadius = 6371; // Earth's radius in Kilometers
    // Get the difference between our two points
    // then convert the difference into radians
 
    double nDLat = (lat2 - lat1);
    double nDLon = (log2 - log1)* 3.14 / 180;
 
    // Here is the new line
    double    nLat1 =  lat1 * 3.14 / 180;
    double nLat2 =  lat2 * 3.14 / 180;
 
    double nA = pow ( sin(nDLat/2), 2 ) + cos(nLat1) * cos(nLat2) * pow ( sin(nDLon/2), 2 );
 
    double nC = 2 * atan2( sqrt(nA), sqrt( 1 - nA ));
    double nD = nRadius * nC;
 
    return nD; // Return our calculated distance
  
}
void Read(){

   SD.remove("test.txt");
  Serial.println("tee");
  while(digitalRead(buttonpin)==HIGH){
    
  char buff[100]={'\1'};
  String str="";
  
 while(esp8266.available()) // check if the esp is sending a message 
 {
      Serial.println("===============");
      delay(1000);
//      while(esp8266.available()){
//        char c=esp8266.read();
//        Serial.print(c);
//        delay(10);  
//      }
      
      esp8266.readBytes(buff,esp8266.available());
      digitalWrite(ledpin,LOW);
      
      if(flag1!=0){
        
            str=String(buff);
          
              
          if(str.indexOf('q')>-1){
              return ;
              
            }
            else{
              
               str=str.substring(str.lastIndexOf(':')+1,str.lastIndexOf('@')-1);   
               Serial.println(str.substring(0));
               str.toCharArray(buff,str.length()-1);
    
    // open the file. note that only one file can be open at a time,
   // so you have to close this one before opening another.
    myFile = SD.open("test.txt", FILE_WRITE);
    // if the file opened okay, write to it:
          if (myFile) {
            Serial.print("Writing to test.txt...");
            myFile.println(str);
            digitalWrite(ledpin,HIGH);
            // close the file:
            myFile.close();
            Serial.println("done.");
          }
          else{
            Serial.println("error in sd");
          } 
//      myFile.write(buff,str.length()-1);          
               
      }
         Serial.println("===============");
    }
    else{
        Serial.println("++++++++");
        flag1=1;
    }
  
}
  }
}
