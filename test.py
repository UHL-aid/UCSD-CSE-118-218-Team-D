import RPi.GPIO as GPIO
import time


mic = 17
vib = 22
GPIO.setmode(GPIO.BCM)
GPIO.setup(mic, GPIO.IN)
GPIO.setup(vib, GPIO.OUT, initial=GPIO.LOW)

def detect(mic):
    print("Sound Detected")
    GPIO.output(vib, GPIO.HIGH)
    time.sleep(2)
    GPIO.output(vib, GPIO.LOW)

GPIO.add_event_detect(mic, GPIO.BOTH, bouncetime=300)
GPIO.add_event_callback(mic, detect)

while True:
    time.sleep(1)
