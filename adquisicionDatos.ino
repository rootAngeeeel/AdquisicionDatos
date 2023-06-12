#include<Arduino_LSM9DS1.h>

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  while(!Serial);
  Serial.println("Iniciado");

  if(!IMU.begin()){
    Serial.println("Fallo al iniciar el IMU");
    while(1);
  }
}

void loop() {
  // put your main code here, to run repeatedly:
  float Ax, Ay, Az, Gx, Gy, Gz;

  if(IMU.gyroscopeAvailable() && IMU.accelerationAvailable()){
    IMU.readAcceleration(Ax, Ay, Az);
    IMU.readGyroscope(Gx, Gy, Gz);

    Serial.print(Ax);
    Serial.print(",");
    Serial.print(Ay);
    Serial.print(",");
    Serial.print(Az);
    Serial.print(",");
    Serial.print(Gx);
    Serial.print(",");
    Serial.print(Gy);
    Serial.print(",");
    Serial.print(Gz);
    Serial.println();

    delay(100);
  }
}
