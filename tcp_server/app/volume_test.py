import sounddevice as sd
import numpy as np
from tuning import Tuning
import usb.core
import usb.util
import time

dev = usb.core.find(idVendor=0x2886, idProduct=0x0018)

def volume(indata, outdata, frames, time, status):
	volume = np.linalg.norm(indata)*10
	print(volume)

if dev:
	Mic_tuning = Tuning(dev)
	with sd.Stream(callback=volume, samplerate=16000):
		while True:
			print(Mic_tuning.direction)
			time.sleep(1)
