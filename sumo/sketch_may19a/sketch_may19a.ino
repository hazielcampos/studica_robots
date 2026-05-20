const int CH1 = 32;
const int CH2 = 33;
const int CH3 = 34;
const int CH4 = 35;

const int RPWMA = 18, LPWMA = 19;
const int RPWMB = 21, LPWMB = 22;

void map_motors(int x, int y) {
  // =========================================
  // Convertir joystick a rango -50 a 50
  // =========================================

  int steering = 50 - x;
  int throttle = 50 - y;

  // =========================================
  // Mezcla diferencial
  // =========================================

  int leftMotor = throttle + steering;
  int rightMotor = throttle - steering;

  // Limitar
  leftMotor = constrain(leftMotor, -50, 50);
  rightMotor = constrain(rightMotor, -50, 50);

  // =========================================
  // Convertir a PWM
  // =========================================
  int leftPWM = map(abs(leftMotor), 0, 50, 0, 255);
  int rightPWM = map(abs(rightMotor), 0, 50, 0, 255);

  // =========================================
  // MOTOR IZQUIERDO
  // =========================================
  if(leftMotor > 0) {
    // Adelante
    ledcWrite(RPWMB, leftPWM);
    ledcWrite(LPWMB, 0);
  } else if(leftMotor < 0) {
    // Atrás
    ledcWrite(RPWMB, 0);
    ledcWrite(LPWMB, leftPWM);
  } else {
    // Stop libre
    ledcWrite(RPWMB, 0);
    ledcWrite(LPWMB, 0);
  }

  // =========================================
  // MOTOR DERECHO
  // =========================================
  if(rightMotor > 0) {
    // Adelante
    ledcWrite(RPWMA, rightPWM);
    ledcWrite(LPWMA, 0);
  } else if(rightMotor < 0) {
    // Atrás
    ledcWrite(RPWMA, 0);
    ledcWrite(LPWMA, rightPWM);
  } else {
    // Stop libre
    ledcWrite(RPWMA, 0);
    ledcWrite(LPWMA, 0);
  }
}

void control_motors() {
  int x = 50;
  int y = 50;

  int canales[] = {CH1, CH2, CH3, CH4};
  
  for(int i=0; i<2; i++) {
    int val = pulseIn(canales[i], HIGH, 25000); // Timeout de 25ms
    int porc = map(val, 1000, 2000, 0, 100);
    if(porc < 0) porc = 0; if(porc > 100) porc = 100;
    if(porc < 55 && porc > 45) porc = 50;
    if(i == 0){
      x = porc;
    } else {
      y = porc;
    }
}

void setup() {
  Serial.begin(115200);
  pinMode(CH1, INPUT);
  pinMode(CH2, INPUT);
  pinMode(CH3, INPUT);
  pinMode(CH4, INPUT);

  ledcAttach(RPWMA, 20000, 8); ledcAttach(LPWMA, 20000, 8);
  ledcAttach(RPWMB, 20000, 8); ledcAttach(LPWMB, 20000, 8);
}





void loop() {

control_motors();

}