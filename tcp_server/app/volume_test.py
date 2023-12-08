import sounddevice as sd
import numpy as np
from tuning import Tuning
import usb.core
import usb.util
import time

past_sounds = np.zeros(1000)

dev = usb.core.find(idVendor=0x2886, idProduct=0x0018)

def volume(indata, outdata, frames, time, status):
	volume = np.linalg.norm(indata)*10
	global past_sounds
	if volume > (np.average(past_sounds) + np.std(past_sounds)):
		print(volume)
	past_sounds = np.append(past_sounds, volume)
	past_sounds = np.delete(past_sounds, 0)
	

if dev:
	Mic_tuning = Tuning(dev)
	with sd.Stream(callback=volume, samplerate=16000):
		while True:
			print(Mic_tuning.direction)
			time.sleep(1)
