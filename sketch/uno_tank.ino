/*

	pin2 - esp8266 TX
	pin3 - esp8266 RX
	
	8,10 - H-bridge right motor
	9,11 - H-bridge left motor
	
*/


#include<SoftwareSerial.h>


int L1 = 8;
int L2 = 9;
int R1 = 10;
int R2 = 11;

volatile unsigned long t, timeout;

struct cmmd
{                 
   char type;
   int x;
   int y;
};
cmmd* cmd = new cmmd;

SoftwareSerial esp(2,3);

void setup()
{
   pinMode(L1,1);
   pinMode(L2,1);
   pinMode(R1,1);
   pinMode(R2,1);
   stopRunning();
   
   Serial.begin(9600);
   esp.begin(9600);
   esp.setTimeout(150);
   setup_client();   

   TCCR2B |= 1 << CS22;
   TIMSK2 |= 1 << OCIE2A;
   TCNT2 = 0x00;
   OCR2A = 100;
   sei();
}

void loop()
{
  
  if(search_cmdd(cmd, 250)>0)
  { 
    cli(); t = 0; sei();   
    moove(cmd);    
    Serial.println( cmd->type);
  }  
}




void setup_client()
{
  esp.write("AT+CIPCLOSE\r\n"); cup(500);
  
  esp.write("AT+CWMODE=2\r\n"); cup(500);
    
  esp.write("AT+RST\r\n"); cup(500);
  delay(500);
  
  esp.write("AT+CIPMODE=0\r\n"); cup(500);

  esp.write("AT+CIPMUX=0\r\n"); cup(500);
  
  esp.write("AT+CIPSTART=\"UDP\",\"0\",0,8888\r\n"); cup(2500); 
}


void cup( long millesec )
{
  long t = millis();
  while ( (millis() - t) < millesec )
    while(esp.available())esp.read();
}

int search_cmdd(struct cmmd *cmd, long timeout)
{
  char c1;
  c1='\0';
  long t = millis();
  //ответ сервера +IPD,len:string
  while( (c1!='+')&& ((millis()- t) < timeout ))
  {
   c1 = esp.read(); 
  }
  
  if(c1!='+') return -1;

  while(  (c1 != ':') && ( (millis()- t)<timeout)   )c1 =  esp.read();
  
  if( c1!= ':') return -1;
  
  while(!esp.available()&&((millis()- t)<timeout) );
  delay(100);
  cmd->type = esp.read();
  cmd->x = esp.parseInt();
  cmd->y = esp.parseInt();

  return 1;
}

// controlling
void moove(cmmd* cmd)
{
  char t = cmd->type;
  int x = cmd->x; 
  
  switch(t)
  {
    case 'f':
      analogWrite(L1,x);
      digitalWrite(L2,1);
      
      analogWrite(R1,x);
      digitalWrite(R2,1);  
    break;
    
    case 'b':
      analogWrite(L1,x);
      digitalWrite(L2,LOW);
      
      analogWrite(R1,x);
      digitalWrite(R2,LOW);
    break;
    
    case 'l':
      analogWrite(L1,x);
      digitalWrite(L2,1);
    
      analogWrite(R1,x);
      digitalWrite(R2,0);
    break;
    
    case 'r':
      analogWrite(L1,x);
      digitalWrite(L2,0);
    
      analogWrite(R1,x);
      digitalWrite(R2,1);   
    break;
  }
  cli();
  timeout = cmd->y;
  //запуск таймера
  TCCR2B |= 1 << CS22;
  TIMSK2 |= 1 << OCIE2A;
  sei();
}

void stopRunning()
{
  digitalWrite(L1,0);
  digitalWrite(L2,0);

  digitalWrite(R1,0);
  digitalWrite(R2,0);
}

ISR(TIMER2_COMPA_vect)
{
  if ( ++t >= timeout) 
  {
    stopRunning();
    //выключаем таймер
    TCCR2B &= ~(1 << CS22);
    TIMSK2 &= ~(1 << OCIE2A);
  }
}


